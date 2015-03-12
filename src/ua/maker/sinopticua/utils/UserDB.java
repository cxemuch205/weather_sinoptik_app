package ua.maker.sinopticua.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.maker.sinopticua.models.ItemTown;
import ua.maker.sinopticua.models.ItemWeather;
import ua.maker.sinopticua.models.PageHTML;
import ua.maker.sinopticua.models.WeatherStruct;

public class UserDB extends SQLiteOpenHelper {
	
	private static final String TAG = "UserDB";
	
	private static final String DB_NAME = "user_db.db";
	private static final int DB_VERSION = 1;
	
	public static final String TABLE_TOWN = "town_select";
	public static final String TABLE_CACHE_WEATHER = "weather_cache";
	public static final String TABLE_HTML_CACHE = "html_cache";

	public static final String FIELD_ID = "_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_DATA = "data_html";
	public static final String FIELD_DATE = "date_long";
	public static final String FIELD_URL = "url";
	public static final String FIELD_DATE_FULL = "date_full";
	public static final String FIELD_DATE_DAY = "date_day";
	public static final String FIELD_DATE_MONTH = "date_month";
	public static final String FIELD_DATE_DAY_NAME = "date_day_name";
	public static final String FIELD_TEMP_MIN = "temp_min";
	public static final String FIELD_TEMP_MAX = "temp_max";
	public static final String FIELD_WEATHER_DESCRIPTIONS = "weather_description";
	public static final String FIELD_WEATHER_DESCRIPTIONS_URL = "weather_description_url";
	public static final String FIELD_WEATHER_IMG_URL = "img_weather_url";
	public static final String FIELD_NOW_WEATHER = "now_weather";
	public static final String FIELD_TOWN = "town_name";
	public static final String FIELD_WARNING_WIND = "warning_wind";
	public static final String FIELD_WIND_DESCRIPTION = "wind_descr";
	public static final String FIELD_TODAY_URL = "today_img_url";

	private static final String SQL_CREATE_TABLE_TOWN = "CREATE TABLE " + TABLE_TOWN
			+ " (" + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
			+	FIELD_NAME + " TEXT, "
			+	FIELD_URL + " TEXT);";
	
	private static final String SQL_CREATE_TABLE_CACHE_WEATHER = "CREATE TABLE " + TABLE_CACHE_WEATHER
			+ " (" + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
			+	FIELD_DATE_FULL + " TEXT,                                            "
			+	FIELD_DATE_DAY + " INTEGER,                                          "
			+	FIELD_DATE_MONTH + " TEXT,                                           "
			+	FIELD_DATE_DAY_NAME + " TEXT,                                        "
			+	FIELD_TEMP_MIN + " TEXT,                                             "
			+	FIELD_TEMP_MAX + " TEXT,                                             "
			+	FIELD_NOW_WEATHER + " TEXT,                                          "
			+	FIELD_TODAY_URL + " TEXT,                                            "
			+	FIELD_TOWN + " TEXT,                                                 "
			+	FIELD_WEATHER_DESCRIPTIONS + " TEXT,                                 "
			+	FIELD_WEATHER_DESCRIPTIONS_URL + " TEXT,                             "
			+ FIELD_WARNING_WIND + " TEXT,                                           "
			+	FIELD_WIND_DESCRIPTION + " TEXT,                                     "
			+	FIELD_WEATHER_IMG_URL + " TEXT);                                     ";

    //TODO: make table for caching html site, for all interfaces get info
    private static final String SQL_CREATE_TABLE_CACHE_HTML = "CREATE TABLE " + TABLE_HTML_CACHE + " ("
            + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
            + FIELD_DATA + " TEXT,                                            "
            + FIELD_DATE + " TEXT);                                           ";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy kk:mm");
	
	
	private Context mContext;
	private SQLiteDatabase db;

	public UserDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.mContext = context;
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_TOWN);
		db.execSQL(SQL_CREATE_TABLE_CACHE_WEATHER);
		db.execSQL(SQL_CREATE_TABLE_CACHE_HTML);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
		case 2:
			
		}
	}
	
	public boolean insertTown(ItemTown townData){
		long res = 0;
		if(db.isOpen()){
			if(isUniqTown(townData)){
				ContentValues cv = new ContentValues();
				
				cv.put(FIELD_NAME, townData.getNameTown());
				cv.put(FIELD_URL, townData.getUrlTown());
				
				res = db.insert(TABLE_TOWN, null, cv);
			}
			else
				return false;
		}
		return (Object)res == null?false:true;
	}
	
	private boolean isUniqTown(ItemTown town){
		
		if(db.isOpen()){
			Cursor c = db.rawQuery("SELECT * FROM '"+TABLE_TOWN+"'", null);
			if(c.moveToFirst()){
				int idIndex = c.getColumnIndex(FIELD_ID);
				int idName = c.getColumnIndex(FIELD_NAME);
				int idUrl = c.getColumnIndex(FIELD_URL);
				do {
					int id = c.getInt(idIndex);
					String name = c.getString(idName);
					String url = c.getString(idUrl);
					
					if(url.equals(town.getUrlTown())){
						deleteItemTown(id);
						return true;
					}
					
				} while (c.moveToNext());
			}
		}
		
		return true;
	}
	
	public List<ItemTown> getListTowns(){
		List<ItemTown> listResult = new ArrayList<ItemTown>();
		
		if(db.isOpen()){
			Cursor c = db.rawQuery("SELECT * FROM '"+TABLE_TOWN+"'", null);
			if(c.moveToFirst()){
				int idIndex = c.getColumnIndex(FIELD_ID);
				int idName = c.getColumnIndex(FIELD_NAME);
				int idUrl = c.getColumnIndex(FIELD_URL);
				do {
					int id = c.getInt(idIndex);
					String name = c.getString(idName);
					String url = c.getString(idUrl);
					ItemTown item = new ItemTown(name, url);
					item.setIdInDB(id);
					
					listResult.add(item);
				} while (c.moveToNext());
			}
		}
		
		return listResult;
	}
	
	public boolean deleteItemTown(int id){
		int res = 0;
		if(db.isOpen()){
			res = db.delete(TABLE_TOWN, FIELD_ID + "="+id, null);
		}
		return res == 0?false:true;
	}
	
	public void insertDataCache(WeatherStruct data){
		Log.d(TAG, "insertDataCache()");
		
		cleaningTableWeatherCache();
		
		if(db.isOpen()){
			List<ItemWeather> listWeather = data.getAllWeathers();			
			
			for(int i = 0; i < listWeather.size(); i++){
				ItemWeather item = listWeather.get(i);
				ContentValues cv = new ContentValues();
				
				cv.put(FIELD_DATE_FULL, dateFormat.format(new Date()));
				cv.put(FIELD_DATE_DAY, item.day);
				cv.put(FIELD_DATE_MONTH, item.month);
				cv.put(FIELD_DATE_DAY_NAME, item.dayName);
				cv.put(FIELD_TEMP_MIN, item.minTemp);
				cv.put(FIELD_TEMP_MAX, item.maxTemp);
				cv.put(FIELD_WEATHER_DESCRIPTIONS, item.weatherName);
				cv.put(FIELD_WEATHER_DESCRIPTIONS_URL, item.urlDetail);
				cv.put(FIELD_WEATHER_IMG_URL, item.urlImage);
				cv.put(FIELD_TODAY_URL, data.getWeatherTodayImg());
				cv.put(FIELD_NOW_WEATHER, data.getWeatherToday());
				cv.put(FIELD_TOWN, data.getTownName());
				cv.put(FIELD_WARNING_WIND, String.valueOf(data.getWarningWind()));
				cv.put(FIELD_WIND_DESCRIPTION, data.getWindDescription());
				
				db.insert(TABLE_CACHE_WEATHER, null, cv);				
			}
			
		}
	}
	
	private void cleaningTableWeatherCache(){
		Log.d(TAG, "cleaningTableWeatherCache()");
		if(db.isOpen()){
			db.delete(TABLE_CACHE_WEATHER, null, null);
		}
	}
	
	public WeatherStruct getCacheWeather(){
		Log.d(TAG, "getCacheWeather()");
		WeatherStruct result = new WeatherStruct();
		if(db.isOpen()){
			List<ItemWeather> data = new ArrayList<ItemWeather>();
			Cursor c = db.rawQuery("SELECT * FROM '" + TABLE_CACHE_WEATHER + "'", null);
			if(c.moveToFirst()){
				int dateFullIndex = c.getColumnIndex(FIELD_DATE_FULL);
				int dateDayIndex = c.getColumnIndex(FIELD_DATE_DAY);
				int dateMonthIndex = c.getColumnIndex(FIELD_DATE_MONTH);
				int dateDayNameIndex = c.getColumnIndex(FIELD_DATE_DAY_NAME);
				int tempMinIndex = c.getColumnIndex(FIELD_TEMP_MIN);
				int tempMaxIndex = c.getColumnIndex(FIELD_TEMP_MAX);
				int weatherDescIndex = c.getColumnIndex(FIELD_WEATHER_DESCRIPTIONS);
				int weatherDescUrlIndex = c.getColumnIndex(FIELD_WEATHER_DESCRIPTIONS_URL);
				int weatherImgUrlIndex = c.getColumnIndex(FIELD_WEATHER_IMG_URL);
				int nowWeatherIndex = c.getColumnIndex(FIELD_NOW_WEATHER);
				int townIndex = c.getColumnIndex(FIELD_TOWN);
				int werningWindIndex = c.getColumnIndex(FIELD_WARNING_WIND);
				int windDescrIndex = c.getColumnIndex(FIELD_WIND_DESCRIPTION);
				int weatherTodayImgUrlIndex = c.getColumnIndex(FIELD_TODAY_URL);
				do {
					String dateFull = c.getString(dateFullIndex);
					int dateDay = c.getInt(dateDayIndex);
					String dateMonth = c.getString(dateMonthIndex);
					String dateDayName = c.getString(dateDayNameIndex);
					String tempMin = c.getString(tempMinIndex);
					String tempMax = c.getString(tempMaxIndex);
					String weatherDesc = c.getString(weatherDescIndex);
					String weatherDescUrl = c.getString(weatherDescUrlIndex);
					String weatherImgUrl = c.getString(weatherImgUrlIndex);
					String nowWeather = c.getString(nowWeatherIndex);
					String town = c.getString(townIndex);
					boolean warnWind = Boolean.parseBoolean(c.getString(werningWindIndex));
					String windDEscr = c.getString(windDescrIndex);
					String todayUrlIng = c.getString(weatherTodayImgUrlIndex);

					ItemWeather item = new ItemWeather();
					item.dateFull = dateFull;
					item.day = dateDay;
					item.month = dateMonth;
					item.dayName = dateDayName;
					item.minTemp = tempMin;
					item.maxTemp=tempMax;
					item.weatherName = weatherDesc;
					item.urlDetail = weatherDescUrl;
					item.urlImage=weatherImgUrl;
					
					data.add(item);
					result.setWeatherToday(nowWeather);
					result.setWeatherTodayImg(todayUrlIng);
					result.setTownName(town);
					result.setWarningWind(warnWind);
					result.setWindDescription(windDEscr);
				} while (c.moveToNext());
			}
			result.setAllWeathers(data);
		}
		return result;
	}

    public void insertHTML(PageHTML pageHTML) {
        if (db.isOpen() && pageHTML != null) {
            deleteAllRowsByTable(TABLE_HTML_CACHE);
            ContentValues cv = new ContentValues();

            cv.put(FIELD_DATA, pageHTML.html);
            cv.put(FIELD_DATE, String.valueOf(System.currentTimeMillis()));

            pageHTML.idDB = db.insert(TABLE_HTML_CACHE, null, cv);
        }
    }

    private void deleteAllRowsByTable(String tableName) {
        db.delete(tableName, null, null);
    }

    public PageHTML getHTML() {
        PageHTML result = new PageHTML();
        if (db.isOpen()) {
            Cursor c = db.rawQuery("SELECT * FROM '" + TABLE_HTML_CACHE + "'", null);
            if (c.moveToFirst()) {
                int indexIdDB = c.getColumnIndex(FIELD_ID);
                int indexData = c.getColumnIndex(FIELD_DATA);
                int indexDate = c.getColumnIndex(FIELD_DATE);
                do {
                    long id = c.getLong(indexIdDB);
                    String html = c.getString(indexData);
                    String dateMillis = c.getString(indexDate);

                    result.idDB = id;
                    result.html = html;
                    result.timeInsertMillis = Long.parseLong(dateMillis);

                } while (c.moveToNext());
            }
        }
        return result;
    }
}
