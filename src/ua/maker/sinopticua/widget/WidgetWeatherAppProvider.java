package ua.maker.sinopticua.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ua.maker.sinopticua.HomeActivity;
import ua.maker.sinopticua.R;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.models.ItemWeather;
import ua.maker.sinopticua.models.WeatherStruct;
import ua.maker.sinopticua.utils.DataParser;
import ua.maker.sinopticua.utils.Tools;

public class WidgetWeatherAppProvider extends AppWidgetProvider {

	private static final String TAG = "WidgetWeatherAppProvider";
	private static final String ACTION_UPDATE = "action_update";
	private static final int PERIOD_UPDATE = 120000;
	
	private HttpTask task;
	private RemoteViews view;
	private ComponentName thisWidget;
	private AppWidgetManager appWidgetManager;
	private SharedPreferences pref;
	private String url = "";
	private Context mContext;
	private Timer timerWidget = new Timer();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyy kk:mm");

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i(TAG, "onUpdate()");
		this.appWidgetManager = appWidgetManager;
		this.mContext = context;

        view = new RemoteViews(context.getPackageName(),
                R.layout.widget_3x2_layout);

        thisWidget = new ComponentName(context, WidgetWeatherAppProvider.class);
		pref = context.getSharedPreferences(App.PREF_APP, Context.MODE_PRIVATE);
		
		timerWidget.schedule(new TimerTask() {
			
			@Override
			public void run() {
                refreshWeather(getWeatherUrl(mContext));
			}
		}, 0, PERIOD_UPDATE);	
		for (int i = 0; i < appWidgetIds.length; i++) {
	        int appWidgetId = appWidgetIds[i];

	        Intent intent = new Intent(context, HomeActivity.class);
	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	        view.setOnClickPendingIntent(R.id.ll_widget, pendingIntent);
	        view.setOnClickPendingIntent(R.id.iv_update, getPendingSelfIntent(context, ACTION_UPDATE));
	        appWidgetManager.updateAppWidget(appWidgetId, view);
	    }
	}

    @Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		pref = context.getSharedPreferences(App.PREF_APP, Context.MODE_PRIVATE);
		this.appWidgetManager = AppWidgetManager.getInstance(context);
		this.mContext = context;
		view = new RemoteViews(context.getPackageName(),
				R.layout.widget_3x2_layout);
		thisWidget = new ComponentName(context, WidgetWeatherAppProvider.class);

        refreshWeather(getWeatherUrl(context));
	}

    private String getWeatherUrl(Context context) {
        String defUrl, url;
        SharedPreferences pref = context.getSharedPreferences(App.PREF_APP, Context.MODE_PRIVATE);
        if(Locale.getDefault().getLanguage().equals(App.LANG_UA)){
            defUrl = App.DEFAULT_URL_UA;
        } else {
            defUrl = App.DEFAULT_URL_RU;
        }
        if (pref.contains(App.PREF_SITY_URL)) {
            url = pref.getString(App.PREF_SITY_URL, defUrl);
        } else {
            url = defUrl;
        }

        return url;
    }

	protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

	private void refreshWeather(String url) {
		Log.i(TAG, "refreshWeather");
		if (task == null) {
			task = new HttpTask();
			task.execute(url);
		}
	}

	class HttpTask extends AsyncTask<String, Integer, WeatherStruct> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.i(TAG, "onPreExecute()");
			view.setViewVisibility(R.id.pb_widget, ProgressBar.VISIBLE);
			view.setViewVisibility(R.id.iv_update, ImageView.GONE);
			appWidgetManager.updateAppWidget(thisWidget, view);
		}

		@Override
		protected WeatherStruct doInBackground(String... urls) {
			Log.i(TAG, "START TASK - " + Uri.decode(urls[0]));
			String response;
			boolean isGet = false;
			do {
				response = Tools.getWebPage(urls[0]);
				if(response != null){
					if(response.length() != 0){
						isGet = true;
					}
				}
			} while (isGet == false);
			Log.i(TAG, "Response length: " + response.length());
			DataParser parser = DataParser.getInstance();
			return parser.parserHTML(response);
		}

		@Override
		protected void onPostExecute(WeatherStruct response) {
			if(response != null){
				ItemWeather weather = response.getWeatherMondey();
				Log.i(TAG, "onPostExecute()");
				String dateUpdate = dateFormat.format(new Date());
				pref.edit().putString(App.PREF_LAST_WIDGET_UPDATE, dateUpdate).commit();

                view.setViewVisibility(R.id.pb_widget, ProgressBar.GONE);
				view.setViewVisibility(R.id.iv_update, ImageView.VISIBLE);
                if(response.getTownName() != null)
				    view.setTextViewText(R.id.tv_town_name, Html.fromHtml(response.getTownName()));
				view.setTextViewText(R.id.tv_info_last_update, dateUpdate);
				view.setTextViewText(R.id.tv_day, String.valueOf(weather.day));
                if(weather.month != null)
				    view.setTextViewText(R.id.tv_month, weather.month);
                if(weather.dayName != null)
				    view.setTextViewText(R.id.tv_name_day, weather.dayName);
                if(response.getWeatherToday() != null)
                    view.setTextViewText(R.id.tv_now_weather, Html.fromHtml(response.getWeatherToday()));
                if(weather.minTemp != null)
				    view.setTextViewText(R.id.tv_temp_min,
						Html.fromHtml(weather.minTemp));
                if(weather.maxTemp != null)
				    view.setTextViewText(R.id.tv_temp_max,
						Html.fromHtml(weather.maxTemp));
                if(weather.weatherName != null)
                    view.setTextViewText(R.id.tv_name_weather, weather.weatherName);

				String urlImage = weather.urlImage;
                if (urlImage != null) {
                    if (!Patterns.WEB_URL.matcher(urlImage).matches()) {
                        urlImage = "https:" + urlImage;
                    } else {
                        urlImage = weather.urlImage;
                    }
                    URL imageURL;
                    try {
                        imageURL = new URL(urlImage);
                        new LoadImage().execute(imageURL);
                    } catch (Exception e) {}
                }
				
				if (response.getWerningWind()) {
					view.setViewVisibility(R.id.ll_werning_wind,
							LinearLayout.VISIBLE);
					view.setTextViewText(R.id.tv_werning_wind,
							response.getWindDescription());
				} else {
					view.setViewVisibility(R.id.ll_werning_wind, LinearLayout.GONE);
				}

				appWidgetManager.updateAppWidget(thisWidget, view);
			} else {
				view.setViewVisibility(R.id.pb_widget, ProgressBar.GONE);
				view.setViewVisibility(R.id.iv_update, ImageView.VISIBLE);
				appWidgetManager.updateAppWidget(thisWidget, view);
			}
		}
	}
	
	private class LoadImage extends AsyncTask<URL, Void, Bitmap> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Bitmap doInBackground(URL... urls) {
			Bitmap networkBitmap = null;

			URL networkUrl = urls[0];
			try {
				networkBitmap = BitmapFactory.decodeStream(networkUrl
						.openConnection().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return networkBitmap;
		}

		protected void onPostExecute(Bitmap result) {
			view.setImageViewBitmap(R.id.iv_weather, result);
			appWidgetManager.updateAppWidget(thisWidget, view);
		}
	}
}
