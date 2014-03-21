package ua.maker.sinopticua.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.maker.sinopticua.structs.ItemTown;
import ua.maker.sinopticua.structs.ItemWeather;
import ua.maker.sinopticua.structs.WeatherStruct;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDB extends SQLiteOpenHelper {
	
	private static final String TAG = "UserDB";
	
	private static final String DB_NAME = "user_db.db";
	private static final int DB_VERSION = 1;
	
	public static final String TABLE_TOWN = "town_select";
	public static final String TABLE_CACHE_WEATHER = "weather_cache";
	
	public static final String FIELD_ID = "_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_URL = "url";
	public static final String FIELD_DATE_FULL = "date_full";
	public static final String FIELD_DATE_DAY = "date_day";
	public static final String FIELD_DATE_MONTH = "date_month";
	public static final String FIELD_DATE_DAY_NAME = "date_day_name";
	public static final String FIELD_TEMP_MIN = "temp_min";
	public static final String FIELD_TEMP_MAX = "temp_max";
	public static final String FIELD_WEATHER_DESCRIPTIONS = "weather_description";
	public static final String FIELD_WEATHER_IMG_URL = "img_weather_url";
	public static final String FIELD_NOW_WEATHER = "now_weather";
	public static final String FIELD_TOWN = "town_name";
	public static final String FIELD_WERNING_WIND = "werning_wind";
	public static final String FIELD_WIND_DESCRIPTION = "wind_descr";
	
	private static final String SQL_CREATE_TABLE_TOWN = "CREATE TABLE " + TABLE_TOWN
			+ " (" + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
			+	FIELD_NAME + " TEXT, "
			+	FIELD_URL + " TEXT);";
	
	private static final String SQL_CREATE_TABLE_CACHE_WEATHER = "CREATE TABLE " + TABLE_CACHE_WEATHER
			+ " (" + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
			+	FIELD_DATE_FULL + " TEXT, "
			+	FIELD_DATE_DAY + " INTEGER, "
			+	FIELD_DATE_MONTH + " TEXT, "
			+	FIELD_DATE_DAY_NAME + " TEXT, "
			+	FIELD_TEMP_MIN + " TEXT, "
			+	FIELD_TEMP_MAX + " TEXT, "
			+	FIELD_NOW_WEATHER + " TEXT, "
			+	FIELD_TOWN + " TEXT, "
			+	FIELD_WEATHER_DESCRIPTIONS + " TEXT, "
			+	FIELD_WERNING_WIND + " TEXT, "
			+	FIELD_WIND_DESCRIPTION + " TEXT, "
			+	FIELD_WEATHER_IMG_URL + " TEXT);";
	
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
				cv.put(FIELD_DATE_DAY, item.getDay());
				cv.put(FIELD_DATE_MONTH, item.getMonth());
				cv.put(FIELD_DATE_DAY_NAME, item.getDayName());
				cv.put(FIELD_TEMP_MIN, item.getMinTemp());
				cv.put(FIELD_TEMP_MAX, item.getMaxTemp());
				cv.put(FIELD_WEATHER_DESCRIPTIONS, item.getWeatherName());
				cv.put(FIELD_WEATHER_IMG_URL, item.getUrlImage());
				cv.put(FIELD_NOW_WEATHER, data.getWeatherToday());
				cv.put(FIELD_TOWN, data.getTownName());
				cv.put(FIELD_WERNING_WIND, String.valueOf(data.getWerningWind()));
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
				int weatherImgUrlIndex = c.getColumnIndex(FIELD_WEATHER_IMG_URL);
				int nowWeatherIndex = c.getColumnIndex(FIELD_NOW_WEATHER);
				int townIndex = c.getColumnIndex(FIELD_TOWN);
				int werningWindIndex = c.getColumnIndex(FIELD_WERNING_WIND);
				int windDescrIndex = c.getColumnIndex(FIELD_WIND_DESCRIPTION);
				do {
					String dateFull = c.getString(dateFullIndex);
					int dateDay = c.getInt(dateDayIndex);
					String dateMonth = c.getString(dateMonthIndex);
					String dateDayName = c.getString(dateDayNameIndex);
					String tempMin = c.getString(tempMinIndex);
					String tempMax = c.getString(tempMaxIndex);
					String weatherDesc = c.getString(weatherDescIndex);
					String weatherImgUrl = c.getString(weatherImgUrlIndex);
					String nowWeather = c.getString(nowWeatherIndex);
					String town = c.getString(townIndex);
					boolean wernWind = Boolean.parseBoolean(c.getString(werningWindIndex));
					String windDEscr = c.getString(windDescrIndex);
					
					ItemWeather item = new ItemWeather();
					item.setDateFull(dateFull);
					item.setDay(dateDay);
					item.setMonth(dateMonth);
					item.setDayName(dateDayName);
					item.setMinTemp(tempMin);
					item.setMaxTemp(tempMax);
					item.setWeatherName(weatherDesc);
					item.setUrlImage(weatherImgUrl);
					
					data.add(item);
					result.setWeatherToday(nowWeather);
					result.setTownName(town);
					result.setWerningWind(wernWind);
					result.setWindDescription(windDEscr);
				} while (c.moveToNext());
			}
			result.setAllWeathers(data);
		}
		return result;
	}
}