package ua.maker.sinopticua.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ua.maker.sinopticua.HomeActivity;
import ua.maker.sinopticua.R;
import ua.maker.sinopticua.SinoptikApplication;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.models.PageHTML;
import ua.maker.sinopticua.models.WeatherStruct;
import ua.maker.sinopticua.utils.DataParser;
import ua.maker.sinopticua.utils.Tools;
import ua.maker.sinopticua.utils.UserDB;
import ua.setcom.widgets.view.ThermometerView;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetThermometerAppProvider extends AppWidgetProvider {

    public static final String TAG = "WidgetThermometer";
    public static final String ACTION_UPDATE = "action_update";

    private HttpTask task;
    private RemoteViews views;
    private ComponentName thisWidget;
    private AppWidgetManager appWidgetManager;
    private UserDB db;
    private Context mContext;
    private int appWidgetId = 0;
    private SharedPreferences pref;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat
            dateFullFirmat = new SimpleDateFormat("dd/MMM/yyyy kk:mm");

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i(TAG, "onDeleted");
        super.onDeleted(context, appWidgetIds);
        SharedPreferences prefs = context.getSharedPreferences(App.PREF_APP, 0);

        prefs.edit().remove(App.PREF_TEXT_WIDGET_COLOR).apply();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "onUpdate [" + String.valueOf(appWidgetIds) + "]");
        // There may be multiple widgets active, so update all of them
        this.appWidgetManager = appWidgetManager;
        mContext = context;
        db = new UserDB(context);
        pref = context.getSharedPreferences(App.PREF_APP, 0);
        views = new RemoteViews(context.getPackageName(),
                R.layout.widget_thermometer_app_provider);

        thisWidget = new ComponentName(context, WidgetThermometerAppProvider.class);

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            this.appWidgetId = appWidgetId;
            //updateAppWidget(appWidgetIds[i]);
            Intent intent = new Intent(context, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.rl_root, pendingIntent);
            views.setOnClickPendingIntent(R.id.iv_thermometer, getPendingSelfIntent(context, ACTION_UPDATE));
            views.setFloat(R.id.tv_now_weather, "setTextSize", SinoptikApplication.getScaledSize(18f));
            views.setInt(R.id.tv_now_weather, "setTextColor", pref.getInt(App.PREF_TEXT_WIDGET_COLOR, Color.WHITE));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        refreshWeather(getWeatherUrl(context));
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive [" + (intent!=null?intent.getExtras().toString():"") + "]");
        super.onReceive(context, intent);
        mContext = context;
        this.appWidgetManager = AppWidgetManager.getInstance(context);
        views = new RemoteViews(context.getPackageName(),
                R.layout.widget_thermometer_app_provider);

        thisWidget = new ComponentName(context, WidgetThermometerAppProvider.class);

        if (ACTION_UPDATE.equals(intent.getAction())) {
            Log.i(TAG, "CLICK UPDATE - START");
            refreshWeather(getWeatherUrl(context));
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.i(TAG, "onAppWidgetOptionsChanged[" + appWidgetId + "]");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        this.mContext = context;
        this.appWidgetId = appWidgetId;
        if (newOptions != null) {
            int newHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
            int newWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            Log.i(TAG, "" + String.format("H: %d | W: %d", newHeight, newWidth));

            HEIGHT = newHeight * 2 + newHeight / 2;
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "onEnabled");
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "onDisabled");
        // Enter relevant functionality for when the last widget is disabled
    }

    private String getWeatherUrl(Context context) {
        String defUrl, url;
        SharedPreferences pref = context.getSharedPreferences(App.PREF_APP, Context.MODE_PRIVATE);
        if (Locale.getDefault().getLanguage().equals(App.LANG_UA)) {
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
            try {
                views.setViewVisibility(R.id.pb_load, ProgressBar.VISIBLE);
                appWidgetManager.updateAppWidget(thisWidget, views);
            } catch (Exception e) {
            }
        }

        @Override
        protected WeatherStruct doInBackground(String... urls) {
            Log.i(TAG, "START TASK - " + Uri.decode(urls[0]));
            String response;
            boolean isGet = false;
            do {
                response = Tools.getWebPage(urls[0]);
                if (response != null) {
                    if (response.length() != 0) {
                        isGet = true;
                    }
                }
            } while (isGet == false);
            Log.i(TAG, "Response length: " + response.length());
            DataParser parser = DataParser.getInstance();
            if (db == null) {
                db = new UserDB(mContext);
            }
            db.insertHTML(new PageHTML(response));
            return parser.parserHTML(response);
        }

        @Override
        protected void onPostExecute(WeatherStruct response) {
            if (response != null) {
                Log.i(TAG, "onPostExecute()");
                if (pref == null) {
                    pref = mContext.getSharedPreferences(App.PREF_APP, 0);
                }
                pref.edit().putString(App.PREF_LAST_DATE_UPDATE_FULL,
                        dateFullFirmat.format(new Date())).apply();

                if (response.getWeatherToday() != null
                        && Html.fromHtml(response.getWeatherToday()).length() > 0) {
                    views.setTextViewText(R.id.tv_now_weather, Html.fromHtml(response.getWeatherToday()));
                    views.setFloat(R.id.tv_now_weather, "setTextSize", SinoptikApplication.getScaledSize(18f));
                    views.setInt(R.id.tv_now_weather, "setTextColor", pref.getInt(App.PREF_TEXT_WIDGET_COLOR, Color.WHITE));
                }
                Bitmap image = getBitmapThermometer(response);
                if (image != null) {
                    views.setImageViewBitmap(R.id.iv_thermometer, image);
                }

                views.setViewVisibility(R.id.pb_load, ProgressBar.GONE);

                appWidgetManager.updateAppWidget(thisWidget, views);
            }
        }
    }

    public static int WIDTH = 125, HEIGHT = 550;

    private Bitmap getBitmapThermometer(WeatherStruct response) {

        int newHeight = appWidgetManager.getAppWidgetOptions(appWidgetId)
                .getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        if (HEIGHT < newHeight)
            HEIGHT = newHeight * 2 + newHeight / 2;

        ThermometerView thermometer = new ThermometerView(mContext);
        thermometer.setTextSize(SinoptikApplication.getScaledSize(22f));
        thermometer.setColorText(pref.getInt(App.PREF_TEXT_WIDGET_COLOR, Color.WHITE));
        thermometer.setShowSubPoint(true);
        thermometer.measure(WIDTH, HEIGHT);
        thermometer.layout(0, 0, WIDTH, HEIGHT);
        thermometer.setDrawingCacheEnabled(true);
        thermometer.updateTemperature(getDataForThermometer(response));
        return thermometer.getDrawingCache();
    }

    private Intent getDataForThermometer(WeatherStruct response) {
        Intent data = new Intent();

        try {
            int cursorDegrees = response.getWeatherToday().indexOf("&");
            String textDegrees = response.getWeatherToday().substring(0, cursorDegrees).replace("+", "");
            int degreesNow = Integer.parseInt(textDegrees);

            data.putExtra(ThermometerView.Key.CURRENT_TEMP, (float) degreesNow);
            data.putExtra(ThermometerView.Key.MAX_TEMP, App.Thermometer.MAX);
            data.putExtra(ThermometerView.Key.MIN_TEMP, App.Thermometer.MIN);
        } catch (Exception e) {
        }

        return data;
    }

}


