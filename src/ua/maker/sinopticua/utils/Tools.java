package ua.maker.sinopticua.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.Hashtable;

import ua.maker.sinopticua.R;
import ua.maker.sinopticua.interfaces.LocationGetListener;
import ua.maker.sinopticua.models.WeatherStruct;

public class Tools {
	
	private static final String TAG = "Tools";
	
	public static void logToFile(String textData, String nameFile) {
        if (textData == null
                || (textData != null && textData.length() == 0)
                || nameFile == null
                || (nameFile != null && nameFile.length() == 0)) {
            return;
        }
        File file = new File("/sdcard/", nameFile + "_sinoptik_ua.txt");
        file.getParentFile().mkdirs();
        Writer out = null;
        try {
            Log.e("MyLogs", "Start write");

            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));
            out.write(textData);
        } catch (IOException e1) {
            e1.printStackTrace();
            Log.e("MyLogs", "Start write - ERROR");
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getWebPage(String url) {
        if (url == null
                || (url != null && url.length() == 0)) {
            return null;
        }
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
                    stringBuffer.append((char) line);
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
        return response;
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
        if (info == null) {
            return false;
        }
        if (info.getWeatherMonday().maxTemp.length() == 0
                | info.getWeatherMonday().minTemp.length() == 0
                | info.getWeatherMonday().month.length() == 0
                | info.getWeatherMonday().weatherName.length() == 0) {
            return false;
        }
        if (info.getWeatherTuesday().maxTemp.length() == 0
                | info.getWeatherTuesday().minTemp.length() == 0
                | info.getWeatherTuesday().month.length() == 0
                | info.getWeatherTuesday().weatherName.length() == 0) {
            return false;
        }
        if (info.getWeatherWednesday().maxTemp.length() == 0
                | info.getWeatherWednesday().minTemp.length() == 0
                | info.getWeatherWednesday().month.length() == 0
                | info.getWeatherWednesday().weatherName.length() == 0) {
            return false;
        }
        if (info.getWeatherThursday().maxTemp.length() == 0
                | info.getWeatherThursday().minTemp.length() == 0
                | info.getWeatherThursday().month.length() == 0
                | info.getWeatherThursday().weatherName.length() == 0) {
            return false;
        }
        if (info.getWeatherFriday().maxTemp.length() == 0
                | info.getWeatherFriday().minTemp.length() == 0
                | info.getWeatherFriday().month.length() == 0
                | info.getWeatherFriday().weatherName.length() == 0) {
            return false;
        }
        if (info.getWeatherSaturday().maxTemp.length() == 0
                | info.getWeatherSaturday().minTemp.length() == 0
                | info.getWeatherSaturday().month.length() == 0
                | info.getWeatherSaturday().weatherName.length() == 0) {
            return false;
        }
        if (info.getWeatherSunday().maxTemp.length() == 0
                | info.getWeatherSunday().minTemp.length() == 0
                | info.getWeatherSunday().month.length() == 0
                | info.getWeatherSunday().weatherName.length() == 0) {
            return false;
        }
        return true;
    }

    private static final Hashtable<String, Typeface> fontsCache = new Hashtable<String, Typeface>();

    public static Typeface getFont(Context ctx, String name) {
        synchronized (fontsCache) {
            if (!fontsCache.containsKey(name)) {
                String path = "fonts/" + name;
                try {
                    Typeface t = Typeface.createFromAsset(ctx.getAssets(),
                            path + ".ttf");
                    fontsCache.put(name, t);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return fontsCache.get(name);
        }
    }

    public static boolean checkConnection(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting())) {
            return true;
        }

        return false;
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static void getLocation(Context context, LocationGetListener listener) {
        GPSTracker tracker = new GPSTracker(context);
        if(tracker.canGetLocation()){
            Log.d(TAG, "#### canGetLocation()");
            double lat = 0, lon = 0;
            lat = tracker.getLatitude();
            lon = tracker.getLongitude();
            if (listener != null) {
                listener.onCanGetLocation(lat, lon);
            }
            //new LoadCurrentLocationTask().execute(new Double[]{lat, lon});
        }else{
            Toast.makeText(context, context.getString(R.string.cant_get_current_location), Toast.LENGTH_LONG).show();
        }
    }
}
