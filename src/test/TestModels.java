package test;

import android.test.AndroidTestCase;

import ua.maker.sinopticua.models.ItemDetail;
import ua.maker.sinopticua.models.WeatherStruct;

/**
 * Created by Daniil on 25.02.2015.
 */
public class TestModels extends AndroidTestCase{

    public static final String TAG = "TestModels";

    public void testWeatherStruct() {
        WeatherStruct weathers = new WeatherStruct();

        assertNotNull(weathers.getWeatherMonday());
        assertNotNull(weathers.getWeatherTuesday());
        assertNotNull(weathers.getWeatherWednesday());
        assertNotNull(weathers.getWeatherThursday());
        assertNotNull(weathers.getWeatherFriday());
        assertNotNull(weathers.getWeatherSaturday());
        assertNotNull(weathers.getWeatherSunday());
    }

    public void testWeatherDetail() {
        ItemDetail itemDetail = new ItemDetail();

        assertNotNull(itemDetail.chanceOfPrecipitation);
        assertNotNull(itemDetail.dayTime);
        assertNotNull(itemDetail.humidity);
        assertNotNull(itemDetail.imageWeather);
        assertNotNull(itemDetail.pressure);
        assertNotNull(itemDetail.temperature);
        assertNotNull(itemDetail.winds);
        assertNotNull(itemDetail.imageWeather);
    }
}
