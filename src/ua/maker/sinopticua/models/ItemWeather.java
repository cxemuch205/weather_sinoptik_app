package ua.maker.sinopticua.models;

import java.io.Serializable;

public class ItemWeather implements Serializable {
	public int day;
    public String dayName;
    public String month;
    public String minTemp;
    public String maxTemp;
    public String urlImage;
    public String weatherName;
    public String urlDetail;
    public String dateFull;
    public int idDB;
    public boolean isFreeDay = false;
}