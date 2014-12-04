package ua.maker.sinopticua.constants;

public class App {
	
	//API
	public static final String SITE_AUTHORITY_RU = "sinoptik.ua";
	public static final String SITE_AUTHORITY_UA = "ua.sinoptik.ua";
	
	//Preferences
	public static final String PREF_APP = "app_pref";
	public static final String PREF_is_FIRST_START = "is_furst_start";
	public static final String PREF_SITY_URL = "sity_url";
	public static final String PREF_SITY_NAME = "sity_name";
	public static final String PREF_LAST_DATE_UPDATE = "last_update";
	public static final String PREF_LAST_WIDGET_UPDATE = "last_widget_update";
	public static final String PREF_LAST_DATE_UPDATE_FULL = "last_full_update";
	
	public static final String DEFAULT_URL_RU = "http://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BA%D0%B8%D0%B5%D0%B2";
	public static final String DEFAULT_URL_UA = "http://ua.sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BA%D0%B8%D1%97%D0%B2";
	
	//key for intent...
	public static final String SAVE_LIST_WEATHER = "save_list_weather";
	public static final String SAVE_NOW_WEATHER = "save_now_weather";
	public static final String SAVE_CITY_NAME = "save_city_name";
	public static final String SAVE_ITEM_WEATHER = "save_item_weather";
	public static final String SAVE_WARNING_WIND = "save_warning_wind";
	
	public static final String LANG_RU = "ru";
	public static final String LANG_UA = "uk";

    public interface MTypeface {
        public static final String ROBOTO_LIGHT = "Roboto-Light";
        public static final String ROBOTO_MEDIUM = "Roboto-Medium";
        public static final String ROBOTO_THIN = "Roboto-Thin";
    }

    public interface Thermometer{
        public static final float MAX = 40f;
        public static final float MIN = 30f;
    }
}
