package ua.maker.sinopticua.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Daniil on 02.02.2015.
 */
public class ItemDetail implements Serializable {

    public String dayStage;
    public ArrayList<String> dayTime = new ArrayList<String>();
    public ArrayList<String> imageWeather = new ArrayList<String>();
    public ArrayList<String> temperature = new ArrayList<String>();
    public ArrayList<String> temperatureFell = new ArrayList<String>();
    public ArrayList<String> pressure = new ArrayList<String>();
    public ArrayList<String> humidity = new ArrayList<String>();
    public ArrayList<Wind> winds = new ArrayList<Wind>();
    public ArrayList<String> chanceOfPrecipitation = new ArrayList<String>();
}
