package ua.maker.sinopticua.interfaces;

/**
 * Created by Daniil on 27.02.2015.
 */
public interface LocationGetListener {

    void onCanGetLocation(double lat, double lon);

    void onNoCanGetLocation();
}
