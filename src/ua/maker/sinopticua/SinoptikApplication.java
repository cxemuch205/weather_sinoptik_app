package ua.maker.sinopticua;

import android.app.Application;

import ua.maker.sinopticua.utils.ImageCache;

/**
 * Created by daniil on 10/28/14.
 */
public class SinoptikApplication extends Application {

    private static SinoptikApplication instance;

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
    }
}
