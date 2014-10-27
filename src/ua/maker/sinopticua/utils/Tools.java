package ua.maker.sinopticua.utils;

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

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import ua.maker.sinopticua.models.WeatherStruct;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class Tools {
	
	private static final String TAG = "Tools";
	
	public static void logToFile(String textData, String nameFile){
		File file = new File("/sdcard/", nameFile+"_sinoptik_ua.txt");
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
	
	public static String getWebPage(String url) {
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
		if(info.getWeatherMondey().maxTemp.length() == 0
				| info.getWeatherMondey().minTemp.length() == 0
				| info.getWeatherMondey().month.length() == 0
				| info.getWeatherMondey().weatherName.length() == 0){
			return false;
		}
		if(info.getWeatherTuesday().maxTemp.length() == 0
				| info.getWeatherTuesday().minTemp.length() == 0
				| info.getWeatherTuesday().month.length() == 0
				| info.getWeatherTuesday().weatherName.length() == 0){
			return false;
		}
		if(info.getWeatherWednesday().maxTemp.length() == 0
				| info.getWeatherWednesday().minTemp.length() == 0
				| info.getWeatherWednesday().month.length() == 0
				| info.getWeatherWednesday().weatherName.length() == 0){
			return false;
		}
		if(info.getWeatherThursday().maxTemp.length() == 0
				| info.getWeatherThursday().minTemp.length() == 0
				| info.getWeatherThursday().month.length() == 0
				| info.getWeatherThursday().weatherName.length() == 0){
			return false;
		}
		if(info.getWeatherFriday().maxTemp.length() == 0
				| info.getWeatherFriday().minTemp.length() == 0
				| info.getWeatherFriday().month.length() == 0
				| info.getWeatherFriday().weatherName.length() == 0){
			return false;
		}
		if(info.getWeatherSaturday().maxTemp.length() == 0
				| info.getWeatherSaturday().minTemp.length() == 0
				| info.getWeatherSaturday().month.length() == 0
				| info.getWeatherSaturday().weatherName.length() == 0){
			return false;
		}
		if(info.getWeatherSundey().maxTemp.length() == 0
				| info.getWeatherSundey().minTemp.length() == 0
				| info.getWeatherSundey().month.length() == 0
				| info.getWeatherSundey().weatherName.length() == 0){
			return false;
		}
		return true;
	}

}
