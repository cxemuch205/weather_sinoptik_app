package ua.maker.sinopticua.interfaces;

/**
 * Created by daniil on 10/27/14.
 */
public interface OnLoadPageListener {
    void onStartLoad();

    void onProgress(int progress, int count);

    void onEndLoad();
}
