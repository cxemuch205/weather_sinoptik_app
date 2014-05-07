package ua.maker.sinopticua;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ua.maker.sinopticua.adapters.TownAdapter;
import ua.maker.sinopticua.adapters.TownAdapter.onClearItemListener;
import ua.maker.sinopticua.adapters.TownCompliteAdapter;
import ua.maker.sinopticua.adapters.WeatherAdapter;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.structs.ItemTown;
import ua.maker.sinopticua.structs.ItemWeather;
import ua.maker.sinopticua.structs.WeatherStruct;
import ua.maker.sinopticua.utils.GPSTracker;
import ua.maker.sinopticua.utils.OnDialogClickListener;
import ua.maker.sinopticua.utils.Tools;
import ua.maker.sinopticua.utils.UserDB;
import ua.maker.sinopticua.utils.DataParser;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class HomeActivity extends FragmentActivity{

	private TextView tvNow, tvTown, tvWind, tvLastSelectInfo, tvLastDateUpdate;
	private LinearLayout llWerningWind, llGetLocation;
	private ListView lvWeathers, lvTown;
	private AutoCompleteTextView etUrl;
	private static final String TAG = "HomeActivity";
	public String URL;
	public String cityName = "краматорск";
	
	private List<ItemTown> listAutoCompliteTown;
	private WeakReference<LoadTownsTask> asyncTaskLoadTownWeakRef;
	private TownCompliteAdapter compliteAdapter;
	
	private ArrayList<ItemWeather> listItemsWeather;
	private WeatherStruct currentWeather;
	private WeatherAdapter adapter;
	private TownAdapter adapterTown;
	private List<ItemTown> listTown;
	private UserDB db;
	
	private SharedPreferences pref;
	private HttpTask task;
	private LoadTownsTask loadTownTask;
	private ProgressDialog pd;
	private ProgressBar pbLoadLocation;
	private AlertDialog.Builder settingDialogBuilder;
	private AlertDialog settingDialog;
	private Button btnOkSettingDialog;
	private boolean isFirst = true;
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat dateFormat = new SimpleDateFormat("D"),
							 dateFullFirmat = new SimpleDateFormat("dd/MMM/yyyy kk:mm");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_white_drawable));
		getActionBar().setIcon(R.drawable.sinoptic_logo);
		getActionBar().setTitle("");
		setContentView(R.layout.activity_home);
		Log.i(TAG, "onCreate()");
		if(Locale.getDefault().getLanguage().equals(App.LANG_UA)){
			URL = App.DEFAULT_URL_UA;
		} else {
			URL = App.DEFAULT_URL_RU;
		}
		lvWeathers = (ListView)findViewById(R.id.lv_weathers);
		tvNow = (TextView)findViewById(R.id.tv_now_temp);
		tvTown = (TextView)findViewById(R.id.tv_name_sity);
		tvLastDateUpdate = (TextView)findViewById(R.id.tv_last_update);
		llWerningWind = (LinearLayout)findViewById(R.id.ll_werning_wind);
		tvWind = (TextView)findViewById(R.id.tv_werning_wind);
		pref = getSharedPreferences(App.PREF_APP, 0);
		
		listItemsWeather = new ArrayList<ItemWeather>();
		listAutoCompliteTown = new ArrayList<ItemTown>();
		
		compliteAdapter = new TownCompliteAdapter(this, listAutoCompliteTown);
		adapter = new WeatherAdapter(this, listItemsWeather, Tools.getImageFetcher(HomeActivity.this));
		lvWeathers.setAdapter(adapter);
		lvWeathers.setOnItemClickListener(itemWeatherClickListener);
		
		db = new UserDB(HomeActivity.this);
		listTown = new ArrayList<ItemTown>();
		listTown = db.getListTowns();
		adapterTown = new TownAdapter(HomeActivity.this, listTown);
		adapterTown.setClearItemListener(clearItemListener);
		
		settingDialogBuilder = new AlertDialog.Builder(this);
		settingDialogBuilder.setTitle(R.string.dialog_title_setting);
		
		View viewDialog = getLayoutInflater().inflate(R.layout.setting_dialog_layout, null);
		etUrl = (AutoCompleteTextView)viewDialog.findViewById(R.id.autoCompleteTextView_city);
		llGetLocation = (LinearLayout)viewDialog.findViewById(R.id.ll_start_get_location);
		pbLoadLocation = (ProgressBar)viewDialog.findViewById(R.id.pb_load_location);
		tvLastSelectInfo = (TextView)viewDialog.findViewById(R.id.tv_info_last_towns);
		lvTown = (ListView)viewDialog.findViewById(R.id.lv_list_last_towns);
		lvTown.setAdapter(adapterTown);
		lvTown.setOnItemClickListener(itemTownClickListener);
		
		llGetLocation.setOnClickListener(clickGetLocationListener);
		
		updateInfosTvTown();
		
		settingDialogBuilder.setView(viewDialog);
		settingDialogBuilder.setPositiveButton(R.string.apply, clickBtnListener);
		settingDialogBuilder.setNegativeButton(R.string.cancel, new OnDialogClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(loadTownTask != null){
					loadTownTask.cancel(true);
				}
				dialog.cancel();
				if(isFirst){
					finish();
				}
			}
		});
		
		settingDialog = settingDialogBuilder.create();
		if(pref.contains(App.PREF_SITY_URL)){
			URL = pref.getString(App.PREF_SITY_URL, URL);
		}
		
		if(pref.contains(App.PREF_SITY_NAME)){
			cityName = pref.getString(App.PREF_SITY_NAME, "");
		}
		
		etUrl.addTextChangedListener(textChangeListener);
		etUrl.setOnItemClickListener(itemComplitListener);
		etUrl.setAdapter(compliteAdapter);
		
		Log.i(TAG, "isTaskPendingOrRunning: " + isTaskPendingOrRunning());
	}
	
	private OnClickListener clickGetLocationListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			GPSTracker tracker = new GPSTracker(HomeActivity.this);
			if(tracker.canGetLocation()){
				Log.d(TAG, "#### canGetLocation()");
				double lat = 0, lon = 0;
				lat = tracker.getLatitude();
				lon = tracker.getLongitude();
				new LoadCurrentLocationTask().execute(new Double[]{lat, lon});
			}else{
				Toast.makeText(HomeActivity.this, getString(R.string.cant_get_current_location), Toast.LENGTH_LONG).show();
			}
		}
	};
	
	class LoadCurrentLocationTask extends AsyncTask<Double, Integer, ItemTown>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pbLoadLocation.setVisibility(ProgressBar.VISIBLE);
			llGetLocation.setVisibility(LinearLayout.GONE);
		}

		@Override
		protected ItemTown doInBackground(Double... params) {
			double lat = params[0];
			double lon = params[1];
			Uri.Builder builderUrl = new Uri.Builder();
			builderUrl.scheme("http")
				.authority("maps.googleapis.com")
				.appendPath("maps")
				.appendPath("api")
				.appendPath("geocode")
				.appendPath("json")
				.appendQueryParameter("latlng", String.valueOf(lat)+","+String.valueOf(lon))
				.appendQueryParameter("sensor", String.valueOf(true));
			if(Locale.getDefault().getLanguage().equals(App.LANG_UA)){
				builderUrl.appendQueryParameter("language", App.LANG_UA);
			} else {
				builderUrl.appendQueryParameter("language", App.LANG_RU);
			}
			
			Log.i(TAG, "LoadLocation URL: " + builderUrl.build().toString());
			String response = Tools.getWebPage(builderUrl.build().toString());
			DataParser parser = DataParser.getInstance();
			return parser.parserGetLocation(response);
		}
		
		@Override
		protected void onPostExecute(ItemTown result) {
			super.onPostExecute(result);
			pbLoadLocation.setVisibility(ProgressBar.GONE);
			llGetLocation.setVisibility(LinearLayout.VISIBLE);
			etUrl.setText(String.valueOf(result.getNameTown()));
			cityName = result.getNameTown().substring(0, 3);
			updateListAuto(cityName);
			etUrl.setSelection(etUrl.getText().toString().length()-2, etUrl.getText().toString().length());
		}
	}
	
	private OnItemClickListener itemComplitListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			settingDialog.dismiss();
			Uri.Builder urlBuild = new Uri.Builder();
			urlBuild.scheme("http")
				.appendPath(listAutoCompliteTown.get(position).getUrlEndTown());
			if(Locale.getDefault().getLanguage().equals(App.LANG_UA)){
				urlBuild.authority(App.SITE_AUTHORITY_UA);
			} else {
				urlBuild.authority(App.SITE_AUTHORITY_RU);
			}
			
			String newURL = urlBuild.build().toString();//App.SITE_URL_RU+Uri.encode(listAutoCompliteTown.get(position).getUrlEndTown());
			
			Log.i(TAG, "URL: " + newURL);
			if(!URL.equals(newURL)){
				updateListLastTown(tvTown.getText().toString());
				URL = newURL;
				refreshWeather(URL);
				etUrl.setText("");
				updateInfosTvTown();
			}
		}
	};
	
	private void updateInfosTvTown() {
		if(listTown.size() > 0 | adapterTown.getCount() > 0){
			tvLastSelectInfo.setVisibility(TextView.VISIBLE);
		} else {
			tvLastSelectInfo.setVisibility(TextView.GONE);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		if(listItemsWeather.size() > 0){
			outState.putParcelableArrayList(App.SAVE_LIST_WEATHER, listItemsWeather);
			outState.putString(App.SAVE_NOW_WEATHER, tvNow.getText().toString());
			outState.putString(App.SAVE_CITY_NAME, currentWeather.getTownName());
			if(llWerningWind.getVisibility() == LinearLayout.VISIBLE)
				outState.putString(App.SAVE_WERNING_WIND, tvWind.getText().toString());
		}
		Log.i(TAG, "onSaveInstanceState()"); 
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		
		super.onRestoreInstanceState(savedInstanceState);
		if(!savedInstanceState.isEmpty()){
			listItemsWeather = savedInstanceState.getParcelableArrayList(App.SAVE_LIST_WEATHER);
			if(adapter.isEmpty()){
				adapter.addAll(listItemsWeather);
				adapter.notifyDataSetChanged();
			}
			tvNow.setText(Html.fromHtml(savedInstanceState.getString(App.SAVE_NOW_WEATHER)));
			tvTown.setText(Html.fromHtml(savedInstanceState.getString(App.SAVE_CITY_NAME)));
			if(savedInstanceState.containsKey(App.SAVE_WERNING_WIND)){
				llWerningWind.setVisibility(LinearLayout.VISIBLE);
				tvWind.setText(savedInstanceState.getString(App.SAVE_WERNING_WIND));
			}else{
				llWerningWind.setVisibility(LinearLayout.GONE);
			}
		}
		Log.i(TAG, "onRestoreInstanceState()");
	}
	
	private boolean isTaskPendingOrRunning(){
		return this.asyncTaskLoadTownWeakRef != null &&
				this.asyncTaskLoadTownWeakRef.get() != null &&
				!this.asyncTaskLoadTownWeakRef.get().getStatus().equals(Status.FINISHED);
	}
	
	private void updateListAuto(String nameTown) {
		Log.i(TAG, "isTaskPendingOrRunning: " + isTaskPendingOrRunning());
		cityName = nameTown;
		if(!isTaskPendingOrRunning()){
			LoadTownsTask townLoadTask = new LoadTownsTask(this);
			asyncTaskLoadTownWeakRef = new WeakReference<HomeActivity.LoadTownsTask>(townLoadTask);
			townLoadTask.execute(cityName);
		}
	}
	
	static class LoadTownsTask extends AsyncTask<String, Integer, List<ItemTown>>{
		
		private WeakReference<HomeActivity> activityWeakRef;
		
		public LoadTownsTask(HomeActivity activity){
			this.activityWeakRef = new WeakReference<HomeActivity>(activity);
		}

		@Override
		protected List<ItemTown> doInBackground(String... params) {
			String city = params[0];
			Uri.Builder builderRequest = new Uri.Builder();
			
			builderRequest.scheme("http")
					.appendPath("search.php")
					.appendQueryParameter("q", city)
					.appendQueryParameter("limit", "5");
			if(Locale.getDefault().getLanguage().equals(App.LANG_UA)){
				builderRequest.authority(App.SITE_AUTHORITY_UA);
			} else {
				builderRequest.authority(App.SITE_AUTHORITY_RU);
			}
			Log.i(TAG, "URL request for city list: " + builderRequest.build().toString());
			String response = Tools.getWebPage(builderRequest.build().toString());
			
			if(BuildConfig.DEBUG)
				Tools.logToFile(response, "log_load_town");
			
			DataParser parser = DataParser.getInstance();
			return parser.parserTowns(response);
		}
		
		@Override
		protected void onPostExecute(List<ItemTown> result) {
			super.onPostExecute(result);
			if(this.activityWeakRef.get() != null){
				Log.i(TAG, "LoadTownsTask - onPostExecute()");
				activityWeakRef.get().listAutoCompliteTown = result;
				activityWeakRef.get().compliteAdapter.clear();
				activityWeakRef.get().compliteAdapter.addAll(result);
				activityWeakRef.get().compliteAdapter.notifyDataSetChanged();
			}
		}		
	}
	
	private onClearItemListener clearItemListener = new onClearItemListener() {

		@Override
		public void onClearingStart(int position) {
			db.deleteItemTown(listTown.get(position).getIdInDB());
		}

		@Override
		public void onClearingEnd(int position) {
			updateInfosTvTown();
		}		
	};
	
	private OnItemClickListener itemWeatherClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
//			Intent startDetail = new Intent(HomeActivity.this, WeatherDetailActivity.class);
//			ItemWeather weather = listItemsWeather.get(position);
//			startDetail.putExtra(App.SAVE_ITEM_WEATHER, new String[]{
//					weather.getUrlDetail(), 
//					String.valueOf(weather.getDay()),
//					weather.getDayName(),
//					weather.getMonth()});
//			startActivity(startDetail);
//			overridePendingTransition(R.anim.open_item_weather_anim, android.R.anim.fade_out);
		}
	};
	
	private OnItemClickListener itemTownClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			settingDialog.dismiss();
			ItemTown town = listTown.get(position);
			updateListLastTown(tvTown.getText().toString());
			if(!URL.equals(town.getUrlTown())){
				URL = town.getUrlTown();
				refreshWeather(URL);
				etUrl.setText("");
			}
			updateInfosTvTown();
		}
	};

	private TextWatcher textChangeListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(count > 0 & settingDialog.isShowing()){
				cityName = etUrl.getText().toString();
				updateListAuto(cityName);
			}
			if(btnOkSettingDialog == null){
				btnOkSettingDialog = settingDialog.getButton(DialogInterface.BUTTON_POSITIVE);
			}
			if(count > 0){
				btnOkSettingDialog.setVisibility(Button.VISIBLE);
			}else{
				btnOkSettingDialog.setVisibility(Button.GONE);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {}
	};
	
	private OnDialogClickListener clickBtnListener = new OnDialogClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int index) {
			if(etUrl.length() > 0){
				if(checkConnection(HomeActivity.this))
				{
					Tools.hideKeyboard(getParent());
					dialog.cancel();
					String text = etUrl.getText().toString();
					text = text.toLowerCase();
					updateListLastTown(tvTown.getText().toString());
					
					Uri.Builder urlBuild = new Uri.Builder();
					urlBuild.scheme("http")
						.appendPath("погода-"+text);
					if(Locale.getDefault().getLanguage().equals(App.LANG_UA)){
						urlBuild.authority(App.SITE_AUTHORITY_UA);
					} else {
						urlBuild.authority(App.SITE_AUTHORITY_RU);
					}
					Log.i(TAG, "GET URL: "+urlBuild.build().toString());
					String newUrl = urlBuild.build().toString();//"http://sinoptik.ua/"+Uri.encode("погода-")+Uri.encode(text);
					if(!URL.equals(newUrl)){
						URL = newUrl;
						refreshWeather(URL);
						etUrl.setText("");
						updateInfosTvTown();
					}
				}
				else
				{
					Toast.makeText(HomeActivity.this, "No connetions", Toast.LENGTH_SHORT).show();
				}
			}
		}

	};
	
	private boolean updateListLastTown(String townName){
		if(townName.length() > 0){
			ItemTown lastTown = new ItemTown(townName, URL);
			db.insertTown(lastTown);	
			adapterTown.clear();
			adapterTown.addAll(db.getListTowns());
			adapterTown.notifyDataSetChanged();
			return true;
		}
		else{
			//Toast.makeText(HomeActivity.this, getString(R.string.err_incorrect_city_name), Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		if(task !=null)
			task.unlink();
		return task;
	}

	private void refreshWeather(String url){
		Log.d(TAG, "refreshWeather");
		task = (HttpTask) getLastCustomNonConfigurationInstance();
		if(task == null){
			task = new HttpTask();
			task.link(HomeActivity.this);
			task.execute(url);
		}
	}
	
	public static boolean checkConnection(Context ctx) {	
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if ((netInfo != null) && (netInfo.isConnectedOrConnecting())) {
			return true;
		}
		
		return false;
	}

	static class HttpTask extends AsyncTask<String, Integer, WeatherStruct> {
		
		HomeActivity activity;
		
		public void link(HomeActivity act){
			activity = act;
		}
		
		public void unlink(){
			activity = null;
		}

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        activity.pd = new ProgressDialog(activity);
	        activity.pd.setMessage(activity.getString(R.string.dialog_downld_page_msg));
	        activity.pd.show();
	        if(activity.etUrl.isShown()){
	        	activity.etUrl.dismissDropDown();
	        }
	    }

	    @Override
	    protected WeatherStruct doInBackground(String... urls) {
	    	Log.i(TAG, "START TASK - " + Uri.decode(urls[0]));
	        String response = Tools.getWebPage(urls[0]);
	        if(BuildConfig.DEBUG)
	        	Tools.logToFile(response, "log_http_task");	        
	        DataParser parser = DataParser.getInstance();
	        return parser.parserHTML(response);
	    }

	    @Override
	    protected void onPostExecute(WeatherStruct response) {
	        try {
	        	activity.pref.edit().putString(App.PREF_LAST_DATE_UPDATE_FULL,
	        			activity.dateFullFirmat.format(new Date())).commit();
	        	if(activity.pd != null) activity.pd.dismiss();
	        	activity.pref.edit().putString(App.PREF_SITY_URL, activity.URL).commit();
	        	activity.pref.edit().putString(App.PREF_SITY_NAME, activity.cityName).commit();
	        	activity.setInfoWeather(response);
	        	if(activity.isFirst){
	        		activity.pref.edit().putBoolean(App.PREF_is_FIRST_START, false).commit();
	        		activity.isFirst = false;
	        	}
			} catch (Exception e) {}
	    }

	    @Override
	    protected void onProgressUpdate(Integer... values) {
	        super.onProgressUpdate(values);
	    }

	}
	
	public void setInfoWeather(WeatherStruct info){
		if(Tools.isCorrectDataWeather(info)){
			currentWeather = info;
			if(info.getWeatherMondey().getDateFull().length() > 0){
				tvLastDateUpdate.setText(getString(R.string.last_update_date)+" "+info.getWeatherMondey().getDateFull());
			} else {
				String date = pref.getString(App.PREF_LAST_DATE_UPDATE_FULL, dateFullFirmat.format(new Date()));
				tvLastDateUpdate.setText(getString(R.string.last_update_date)+" "+date);
			}
			tvNow.setText(getString(R.string.now_temp_on_street)+ " " + Html.fromHtml(info.getWeatherToday()));
			tvTown.setText(Html.fromHtml(info.getTownName()));
			if(info.getWerningWind()){
				llWerningWind.setVisibility(LinearLayout.VISIBLE);
				tvWind.setText(info.getWindDescription());
			}else{
				llWerningWind.setVisibility(LinearLayout.GONE);
			}
			
			listItemsWeather.clear();
			listItemsWeather.add(info.getWeatherMondey());
			listItemsWeather.add(info.getWeatherTuesday());
			listItemsWeather.add(info.getWeatherWednesday());
			listItemsWeather.add(info.getWeatherThursday());
			listItemsWeather.add(info.getWeatherFriday());
			listItemsWeather.add(info.getWeatherSaturday());
			listItemsWeather.add(info.getWeatherSundey());
			adapter.notifyDataSetChanged();
			db.insertDataCache(info);
			pref.edit().putString(App.PREF_LAST_DATE_UPDATE, dateFormat.format(new Date())).commit();
		} else {
			settingDialog.show();
			Toast.makeText(HomeActivity.this, getString(R.string.err_incorrect_city_name), Toast.LENGTH_SHORT).show();
		}		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(pref.contains(App.PREF_SITY_URL)){
			URL = pref.getString(App.PREF_SITY_URL, URL);
		}
		isFirst = pref.getBoolean(App.PREF_is_FIRST_START, true);
		if(isFirst == false){
			String nowDate = dateFormat.format(new Date());
			String prefDate = pref.getString(App.PREF_LAST_DATE_UPDATE, "");
			
			if(checkConnection(this) & (!nowDate.equals(prefDate) | prefDate.length() == 0))
			{
				if(listItemsWeather != null & listItemsWeather.size() == 0)
					refreshWeather(URL);
			}
			else
			{
				setInfoWeather(db.getCacheWeather());
				if(!checkConnection(this))
					Toast.makeText(this, getString(R.string.no_connections), Toast.LENGTH_SHORT).show();
			}
		}else{
			settingDialog.show();
			btnOkSettingDialog = settingDialog.getButton(DialogInterface.BUTTON_POSITIVE);
			btnOkSettingDialog.setVisibility(Button.GONE);
		}
		Log.i(TAG, "onStart()");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume()");
		if(adapter.isEmpty()){
			Log.i(TAG, "onResume - setAdapter");
			adapter = new WeatherAdapter(HomeActivity.this, listItemsWeather, Tools.getImageFetcher(HomeActivity.this));
			lvWeathers.setAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			if(checkConnection(HomeActivity.this))
			{
				refreshWeather(URL);
			}
			else
			{
				Toast.makeText(this, getString(R.string.no_connections), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.action_setting:
			settingDialog.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private Toast toast;
	private long lastBackPressTime = 0;
	private static final int TIME_TO_BACK = 1500;
	
	@Override
	public void onBackPressed() {
		if (this.lastBackPressTime < System.currentTimeMillis() - TIME_TO_BACK) {
			toast = Toast.makeText(this,  R.string.repeat_click_on_back, TIME_TO_BACK);
		    toast.show();
		    this.lastBackPressTime = System.currentTimeMillis();
		} else {
			if (toast != null) {
				toast.cancel();
			}
			super.onBackPressed();
		}
	}
}
