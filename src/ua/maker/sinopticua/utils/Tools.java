package ua.maker.sinopticua.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import ua.maker.sinopticua.interfaces.OnLoadPageAdapter;
import ua.maker.sinopticua.interfaces.OnLoadPageListener;
import ua.maker.sinopticua.structs.WeatherStruct;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class Tools {
	
	private static final String TAG = "Tools";
	
	public static void logToFile(String textData, String nameFile){
		File file = new File("/sdcard/", nameFile+".txt");
		file.getParentFile().mkdirs();
		Writer out = null;
		try {
			Log.e("MyLogs", "Start write");
			
			out  = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(file), "UTF-8"));
			out.write(textData);
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.e("MyLogs", "Start write - ERROR");
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getWebPage(String url, OnLoadPageAdapter loadPageListener) {
        if(loadPageListener != null)
            loadPageListener.onStartLoad();
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet();

	    InputStream inputStream = null;

	    String response = null;

	    try {

	        URI uri = new URI(url);
	        httpGet.setURI(uri);

	        HttpResponse httpResponse = httpClient.execute(httpGet);
	        int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpResponse.getEntity().getContent();
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                int max = 16544;

                int line;
                StringBuffer stringBuffer = new StringBuffer();

                while ((line = reader.read()) != -1) {
                    stringBuffer.append((char)line);
                    if(loadPageListener != null)
                        loadPageListener.onProgress(stringBuffer.length(), max);
                }

                response = stringBuffer.toString();
            }
	    } catch (ClientProtocolException e) {
	        Log.e(TAG, "HttpActivity.getPage() ClientProtocolException error", e);
	    } catch (IOException e) {
	        Log.e(TAG, "HttpActivity.getPage() IOException error", e);
	    } catch (URISyntaxException e) {
	        Log.e(TAG, "HttpActivity.getPage() URISyntaxException error", e);
	    } finally {
	        try {
	            if (inputStream != null)
	                inputStream.close();

	        } catch (IOException e) {
	            Log.e(TAG, "HttpActivity.getPage() IOException error lors de la fermeture des flux", e);
	        }
	    }
        if(loadPageListener != null)
            loadPageListener.onEndLoad();
	    return response;
	}

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }
	
	public static ImageFetcher getImageFetcher(FragmentActivity activity) {
        ImageFetcher fetcher = new ImageFetcher(activity);
        fetcher.addImageCache(activity);
        return fetcher;
    }
	
	public static void hideKeyboard(Activity activity) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			
	    	if (activity.getCurrentFocus() != null) {
	    		imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	    	}
		}
	}
	
	public static void showKeyboard(Activity activity) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
		}
	}

	public static boolean isCorrectDataWeather(WeatherStruct info) {
		if(info.getWeatherMondey().getMaxTemp().length() == 0
				| info.getWeatherMondey().getMinTemp().length() == 0
				| info.getWeatherMondey().getMonth().length() == 0
				| info.getWeatherMondey().getWeatherName().length() == 0){
			return false;
		}
		if(info.getWeatherTuesday().getMaxTemp().length() == 0
				| info.getWeatherTuesday().getMinTemp().length() == 0
				| info.getWeatherTuesday().getMonth().length() == 0
				| info.getWeatherTuesday().getWeatherName().length() == 0){
			return false;
		}
		if(info.getWeatherWednesday().getMaxTemp().length() == 0
				| info.getWeatherWednesday().getMinTemp().length() == 0
				| info.getWeatherWednesday().getMonth().length() == 0
				| info.getWeatherWednesday().getWeatherName().length() == 0){
			return false;
		}
		if(info.getWeatherThursday().getMaxTemp().length() == 0
				| info.getWeatherThursday().getMinTemp().length() == 0
				| info.getWeatherThursday().getMonth().length() == 0
				| info.getWeatherThursday().getWeatherName().length() == 0){
			return false;
		}
		if(info.getWeatherFriday().getMaxTemp().length() == 0
				| info.getWeatherFriday().getMinTemp().length() == 0
				| info.getWeatherFriday().getMonth().length() == 0
				| info.getWeatherFriday().getWeatherName().length() == 0){
			return false;
		}
		if(info.getWeatherSaturday().getMaxTemp().length() == 0
				| info.getWeatherSaturday().getMinTemp().length() == 0
				| info.getWeatherSaturday().getMonth().length() == 0
				| info.getWeatherSaturday().getWeatherName().length() == 0){
			return false;
		}
		if(info.getWeatherSundey().getMaxTemp().length() == 0
				| info.getWeatherSundey().getMinTemp().length() == 0
				| info.getWeatherSundey().getMonth().length() == 0
				| info.getWeatherSundey().getWeatherName().length() == 0){
			return false;
		}
		return true;
	}

}
