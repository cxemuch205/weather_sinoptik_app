package ua.maker.sinopticua;

import android.app.Application;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import ua.maker.sinopticua.utils.ImageCache;

/**
 * Created by daniil on 10/28/14.
 */
public class SinoptikApplication extends Application {

    private static SinoptikApplication instance;
    public static float maxWidth = 0;
    private static DisplayMetrics _displayMetrics;

    public static SinoptikApplication getInstance() {
        if (instance == null) {
            instance = new SinoptikApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        new ImageCache();
        measureScreen();
    }

    public void measureScreen() {
        _displayMetrics = Resources.getSystem().getDisplayMetrics();
        SinoptikApplication.maxWidth = _displayMetrics.widthPixels;
        if( _displayMetrics.heightPixels > SinoptikApplication.maxWidth ) {
            SinoptikApplication.maxWidth = _displayMetrics.heightPixels;
        }
    }

    public static int getScaledSize(float size) {
        return Math.round(size * ( maxWidth / 1920 ));
    }
}
