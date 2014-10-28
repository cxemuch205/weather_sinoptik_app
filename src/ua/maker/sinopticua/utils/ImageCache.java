package ua.maker.sinopticua.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import ua.maker.sinopticua.HomeActivity;
import ua.maker.sinopticua.R;
import ua.maker.sinopticua.SinoptikApplication;

/**
 * Created by highelf on 04.05.14.
 */
public class ImageCache {
    private static ImageCache _this;
    private static final String LOG_TAG = "ImageCache";
    private static final String CACHE_DIRECTORY = "mediaCloud/photos";
    public ImageCache()
    {
        _this = this;
    }

    public static File getExternalFile(String file) {
        return new File(Environment.getExternalStorageDirectory(), file);
    }
    public static File getCacheDirectory() {
        return getExternalFile(CACHE_DIRECTORY);
    }
    private static File ensureCache() throws IOException {
        File cacheDirectory = getCacheDirectory();
        if (!cacheDirectory.exists()) {
            cacheDirectory.mkdirs();
            new File(cacheDirectory, ".nomedia").createNewFile();
        }
        return cacheDirectory;
    }

    static void closeStream( Closeable stream )
    {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Could not close stream", e);
            }
        }
    }

    public static String getTempFile( String name )
    {
        File cacheDirectory;
        try {
            cacheDirectory = ensureCache();
        } catch (IOException e) {
            return null;
        }
        return cacheDirectory.getAbsolutePath() + "/" + name;
    }

    public static boolean addToCache( String id, Bitmap bitmap ) {
        File cacheDirectory;
        try {
            cacheDirectory = ensureCache();
        } catch (IOException e) {
            return false;
        }

        File coverFile = new File(cacheDirectory, id );
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(coverFile);
            bitmap.compress( Bitmap.CompressFormat.PNG, 100, out );
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            closeStream( out );
        }

        return true;
    }

    private static Bitmap loadFromCache( String id ) {
        final File file = new File( getCacheDirectory(), id );
        if (file.exists()) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(file);
                return BitmapFactory.decodeStream(stream, null, null);
            } catch (FileNotFoundException e) {
            } finally {
                closeStream( stream );
            }
        }
        return null;
    }

    static public void download(String url, ImageView imageView) {
        ImageCache.resetPurgeTimer();
        Bitmap bitmap = ImageCache.getBitmapFromCache(url);

        if (bitmap == null) {
            ImageCache.forceDownload(url, imageView);
        } else {
            ImageCache.cancelPotentialDownload(url, imageView);
            imageView.setImageBitmap(bitmap);
        }
    }

    private static void forceDownload(String url, ImageView imageView) {
        // State sanity: url is guaranteed to never be null in DownloadedDrawable and cache keys.
        if ( url == null || url.length() < 10 ) {
            imageView.setImageResource(R.drawable.white_cube);
            return;
        }

        if (ImageCache.cancelPotentialDownload(url, imageView)) {
            try {
                BitmapDownloaderTask task = _this.new BitmapDownloaderTask( imageView );
                DownloadedDrawable downloadedDrawable = _this.new DownloadedDrawable(task);
                imageView.setImageDrawable( downloadedDrawable );
                task.execute( url );
            } catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns true if the current download has been canceled or if there was no download in
     * progress on this image view.
     * Returns false if the download in progress deals with the same url. The download is not
     * stopped in that case.
     */
    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = ImageCache.getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    public interface OnErrorLoadImageListener{
        void onError(int errorCode, String url);
    }

    private static OnErrorLoadImageListener errorLoadImgListener;

    public static void setOnErrorLoadImageListener(OnErrorLoadImageListener listener){
        errorLoadImgListener = listener;
    }

    static byte[] downloadBitmap( String url ) {
        final int IO_BUFFER_SIZE = 4 * 1024;

        // AndroidHttpClient is not allowed to be used from the main thread
        final HttpClient client = new DefaultHttpClient();
        //(mode == Mode.NO_ASYNC_TASK) ? new DefaultHttpClient() : AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                if(errorLoadImgListener != null)
                    errorLoadImgListener.onError(statusCode, url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    int buf_size = (int) entity.getContentLength();
                    int rd = 0;
                    int res;
                    byte buf[] = new byte[ buf_size ];
                    while( rd < buf_size )
                    {
                        res = inputStream.read( buf, rd, buf_size - rd );
                        if( res == -1 ) break;
                        rd += res;
                    }
                    Log.d( "downloadBitmap", "Result : " + rd + " Buf size : " + buf_size + " Url : " + url );
                    return buf;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            getRequest.abort();
            Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            getRequest.abort();
            Log.w(LOG_TAG, "Incorrect URL: " + url);
        } catch (Exception e) {
            getRequest.abort();
            Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
        } finally {
//            if ((client instanceof AndroidHttpClient)) {((AndroidHttpClient) client).close();}
        }
        return null;
    }

    /**
     * The actual AsyncTask that will asynchronously download the image.
     */
    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        public boolean found_in_db = false;
        private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Actual download method.
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            byte[] img = null;
            Bitmap b = null;
            try {
                int s_pos = url.lastIndexOf('/');//, url.lastIndexOf( '.', s_pos )
                int fs_pos = url.indexOf('/');
                final String file_id = url.substring( s_pos+1, url.indexOf('.', s_pos ) );
                Log.d(">>>>>>>>>> FILE NAME <<<<<<<<<<<", url.substring( fs_pos, url.indexOf( '/', fs_pos+2 ) ) + " : " + file_id );
                b = loadFromCache( file_id );
                if( b != null ) return b;
                if( img == null ) img = downloadBitmap(url);
                if( img != null )
                {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray( img, 0, img.length, options );
                    int nw,nh,ns=1;
                    if( options.outHeight > options.outWidth )
                    {
                        nh = 250;
                        nw = options.outWidth * ( 180 / options.outHeight );
                        ns = options.outHeight / 180;
                    }
                    else
                    {
                        nw = 180;
                        nh = options.outHeight * ( 250 / options.outWidth );
                        ns = options.outWidth / 250;
                    }
                    options.inJustDecodeBounds = false;
                    options.inDither = true;
                    options.inTempStorage = new byte[0x10000];
                    options.inSampleSize = ns;
                    b = BitmapFactory.decodeByteArray( img, 0, img.length, options );

                    addToCache( file_id, b );
                    return b;
                }
            } catch ( Exception e )
            {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Once the image is downloaded, associates it to the imageView
         */
        @Override
        protected void onPostExecute( Bitmap bitmap ) {
            if ( isCancelled() ) {
                bitmap = null;
                return;
            }

            addBitmapToCache(url, bitmap);

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with it
                // Or if we don't use any bitmap to task association (NO_DOWNLOADED_DRAWABLE mode)
                if( this == bitmapDownloaderTask ) {
                    if( bitmap == null ) return;
                    imageView.setImageBitmap( bitmap );
                    //imageView.postInvalidate();
                }
            }
        }
    }


    /**
     * A fake Drawable that will be attached to the imageView while the download is in progress.
     *
     * <p>Contains a reference to the actual download task, so that a download task can be stopped
     * if a new binding is required, and makes sure that only the last started download process can
     * bind its result, independently of the download finish order.</p>
     */
    class DownloadedDrawable extends bDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super();
            bitmapDownloaderTaskReference =
                    new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }

    private static final int HARD_CACHE_CAPACITY = 10 * 1024;
    private static final int DELAY_BEFORE_PURGE = 60 * 60 * 1000; // in milliseconds

    // Hard cache, with a fixed maximum capacity and a life duration
    private final static HashMap<String, Bitmap> sHardBitmapCache =
            new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
                private static final long serialVersionUID = -8366035217806132583L;

                @Override
                protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
                    if (size() > HARD_CACHE_CAPACITY) {
                        // Entries push-out of hard reference cache are transferred to soft reference cache
                        sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                        return true;
                    } else
                        return false;
                }
            };

    // Soft cache for bitmaps kicked out of hard cache
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache =
            new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

    private final static Handler purgeHandler = new Handler();

    private final static Runnable purger = new Runnable() {
        public void run() {
            clearCache();
        }
    };

    /**
     * Adds this bitmap to the cache.
     * @param bitmap The newly downloaded bitmap.
     */
    private static void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (sHardBitmapCache) {
                sHardBitmapCache.put(url, bitmap);
            }
        }
    }

    /**
     * @param url The URL of the image that will be retrieved from the cache.
     * @return The cached bitmap or null if it was not found.
     */
    private static Bitmap getBitmapFromCache(String url) {
        // First try the hard reference cache
        synchronized (sHardBitmapCache) {
            final Bitmap bitmap = sHardBitmapCache.get(url);
            if (bitmap != null) {
                // Bitmap found in hard cache
                // Move element to first position, so that it is removed last
                sHardBitmapCache.remove(url);
                sHardBitmapCache.put(url, bitmap);
                return bitmap;
            }
        }

        // Then try the soft reference cache
        SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
        if (bitmapReference != null) {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null) {
                // Bitmap found in soft cache
                return bitmap;
            } else {
                // Soft reference has been Garbage Collected
                sSoftBitmapCache.remove(url);
            }
        }

        return null;
    }

    /**
     * Clears the image cache used internally to improve performance. Note that for memory
     * efficiency reasons, the cache will automatically be cleared after a certain inactivity delay.
     */
    public static void clearCache() {
        sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
    }

    /**
     * Allow a new delay before the automatic cache clear is done.
     */
    private static void resetPurgeTimer() {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }

    public Bitmap b_default = null;

    private class bDrawable extends Drawable {
        private final Bitmap mBitmap;

        public bDrawable(Bitmap b) {
            mBitmap = b;
        }

        public bDrawable()
        {
            if( b_default == null )
            {
                b_default = BitmapFactory.decodeResource(SinoptikApplication.getInstance().getResources(), R.drawable.white_cube);
            }
            mBitmap = b_default;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0.0f, 0.0f, null);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
        }

        @Override
        public int getIntrinsicWidth() {
            return mBitmap.getWidth();
        }

        @Override
        public int getIntrinsicHeight() {
            return mBitmap.getHeight();
        }

        @Override
        public int getMinimumWidth() {
            return mBitmap.getWidth();
        }

        @Override
        public int getMinimumHeight() {
            return mBitmap.getHeight();
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }
    }
}
