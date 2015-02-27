package ua.maker.sinopticua;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ua.maker.sinopticua.adapters.TownAdapter;
import ua.maker.sinopticua.adapters.TownAdapter.onClearItemListener;
import ua.maker.sinopticua.adapters.TownCompleteAdapter;
import ua.maker.sinopticua.adapters.WeatherAdapter;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.interfaces.LocationGetListener;
import ua.maker.sinopticua.models.ItemTown;
import ua.maker.sinopticua.models.ItemWeather;
import ua.maker.sinopticua.models.WeatherStruct;
import ua.maker.sinopticua.utils.DataParser;
import ua.maker.sinopticua.utils.Tools;
import ua.maker.sinopticua.utils.UserDB;
import ua.setcom.widgets.view.ThermometerView;

@SuppressLint("DefaultLocale")
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
	private TownCompleteAdapter compliteAdapter;
	private WeakReference<HttpTask> asyncTaskLoadWeatherData;
	
	private ArrayList<ItemWeather> listItemsWeather;
	private WeatherStruct currentWeather;
	private WeatherAdapter adapter;
	private TownAdapter adapterTown;
	private List<ItemTown> listTown;
	private UserDB db;
	
	private SharedPreferences pref;
	private LoadTownsTask loadTownTask;
	private ProgressDialog pd;
	private ProgressBar pbLoadLocation;
	private AlertDialog.Builder settingDialogBuilder;
	private AlertDialog settingDialog;
	private Button btnOkSettingDialog, btnUpdate;
    private ThermometerView thermometer;
    private ObjectAnimator animLoadBtnUpdate;
	private boolean isFirst = true;
    private ImageView ivBigWeather;
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat dateFormat = new SimpleDateFormat("D"),
							 dateFullFirmat = new SimpleDateFormat("dd/MMM/yyyy kk:mm");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		if(VERSION.SDK_INT >= 11){
			initActionBar();
		}

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
        btnUpdate = (Button) findViewById(R.id.btn_update);
        tvWind = (TextView)findViewById(R.id.tv_werning_wind);
        ivBigWeather = (ImageView) findViewById(R.id.iv_big_weather);
        pref = getSharedPreferences(App.PREF_APP, 0);
        thermometer = (ThermometerView) findViewById(R.id.v_thermometer);
        initThermometer();
        animLoadBtnUpdate = ObjectAnimator.ofFloat(btnUpdate, "rotation", 0f, 360f);
        animLoadBtnUpdate.setDuration(500);
        animLoadBtnUpdate.setRepeatCount(ValueAnimator.INFINITE);

        listItemsWeather = new ArrayList<ItemWeather>();
		listAutoCompliteTown = new ArrayList<ItemTown>();
		
		compliteAdapter = new TownCompleteAdapter(this, listAutoCompliteTown);
		adapter = new WeatherAdapter(this, listItemsWeather);
		lvWeathers.setAdapter(adapter);
		lvWeathers.setOnItemClickListener(itemWeatherClickListener);
        btnUpdate.setOnClickListener(clickUpdateBtnListener);
		
		db = new UserDB(HomeActivity.this);
		listTown = new ArrayList<ItemTown>();
		listTown = db.getListTowns();
		adapterTown = new TownAdapter(HomeActivity.this, listTown);
		adapterTown.setClearItemListener(clearItemListener);

		if(pref.contains(App.PREF_SITY_URL)){
			URL = pref.getString(App.PREF_SITY_URL, URL);
		}
		
		if(pref.contains(App.PREF_SITY_NAME)){
			cityName = pref.getString(App.PREF_SITY_NAME, "");
		}
		
		Log.i(TAG, "isTaskPendingOrRunning: " + isTaskPendingOrRunning());
	}

    private void initThermometer() {
        thermometer.setTextSize(21);
        thermometer.setShowSubPoint(true);
        thermometer.initMaxMin(App.Thermometer.MAX, App.Thermometer.MIN);
    }

    private void initTypefaces() {
        tvLastDateUpdate.setTypeface(Tools.getFont(this, App.MTypeface.ROBOTO_LIGHT));
        tvLastSelectInfo.setTypeface(Tools.getFont(this, App.MTypeface.ROBOTO_LIGHT));
        tvNow.setTypeface(Tools.getFont(this, App.MTypeface.ROBOTO_THIN));
        tvTown.setTypeface(Tools.getFont(this, App.MTypeface.ROBOTO_LIGHT));
        etUrl.setTypeface(Tools.getFont(this, App.MTypeface.ROBOTO_LIGHT));
        tvWind.setTypeface(Tools.getFont(this, App.MTypeface.ROBOTO_MEDIUM));

    }

    @SuppressLint("NewApi")
	private void initActionBar() {
        ActionBar actionBar = getActionBar();
		//actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_white_drawable));
        //actionBar.setIcon(R.drawable.sinoptic_logo);
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setTitle("");
        }
	}

    private OnClickListener clickUpdateBtnListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Tools.checkConnection(HomeActivity.this)) {
                refreshWeather(URL);
                animLoadBtnUpdate.start();
            } else {
                Toast.makeText(HomeActivity.this, getString(R.string.no_connections), Toast.LENGTH_SHORT).show();
            }
        }
    };

	private OnClickListener clickGetLocationListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
            Tools.getLocation(HomeActivity.this, new LocationGetListener() {
                @Override
                public void onCanGetLocation(double lat, double lon) {
                    new LoadCurrentLocationTask().execute(new Double[]{lat, lon});
                }

                @Override
                public void onNoCanGetLocation() {

                }
            });
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
            if(pbLoadLocation != null)
			    pbLoadLocation.setVisibility(ProgressBar.GONE);
            if(llGetLocation != null)
			    llGetLocation.setVisibility(LinearLayout.VISIBLE);
            if (result != null) {
                etUrl.setText(result.getNameTown());
                if (result.getNameTown() != null) {
                    cityName = result.getNameTown().substring(0, 3);
                    updateListAuto(cityName);
                }
                etUrl.setSelection(etUrl.getText().toString().length() - 2, etUrl.getText().toString().length());
            } else {
                Toast.makeText(HomeActivity.this, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
            }
        }
	}
	
	private OnItemClickListener itemCompleteListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Uri.Builder urlBuild = new Uri.Builder();
			urlBuild.scheme("http");
			if(Locale.getDefault().getLanguage().equals(App.LANG_UA)){
				urlBuild.authority(App.SITE_AUTHORITY_UA);
			} else {
				urlBuild.authority(App.SITE_AUTHORITY_RU);
			}

            final ItemTown town = listAutoCompliteTown.get(position);

            urlBuild.appendPath(town.getUrlEndTown());
			
			final String newURL = urlBuild.build().toString();
			
			Log.i(TAG, "URL: " + newURL);
            if(!URL.equals(newURL)
                    || (URL.equals(newURL) && adapter.getCount() == 0)){
                updateListLastTown(tvTown.getText().toString());
                URL = newURL;
                refreshWeather(URL);
                etUrl.setText("");
                updateInfosTvTown();
            }
            settingDialog.dismiss();
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
			outState.putSerializable(App.SAVE_LIST_WEATHER, listItemsWeather);
			outState.putString(App.SAVE_NOW_WEATHER, tvNow.getText().toString());
			outState.putString(App.SAVE_CITY_NAME, currentWeather.getTownName());
			if(llWerningWind.getVisibility() == LinearLayout.VISIBLE)
				outState.putString(App.SAVE_WARNING_WIND, tvWind.getText().toString());
		}
		Log.i(TAG, "onSaveInstanceState()"); 
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		
		super.onRestoreInstanceState(savedInstanceState);
		if(!savedInstanceState.isEmpty() & !isLoadWeatherPendingOrRunning()){
			ArrayList<ItemWeather> restore = (ArrayList<ItemWeather>)savedInstanceState.getSerializable(App.SAVE_LIST_WEATHER);
			if(restore != null){
				listItemsWeather.clear();
				for(ItemWeather item : restore){
					listItemsWeather.add(item);
				}
				if(adapter.isEmpty()){
					adapter.addAll(listItemsWeather);
					adapter.notifyDataSetChanged();
				}
				tvNow.setText(Html.fromHtml(savedInstanceState.getString(App.SAVE_NOW_WEATHER)));
                getActionBar().setTitle(Html.fromHtml(savedInstanceState.getString(App.SAVE_CITY_NAME)));
				tvTown.setText(Html.fromHtml(savedInstanceState.getString(App.SAVE_CITY_NAME)));
				if(savedInstanceState.containsKey(App.SAVE_WARNING_WIND)){
					llWerningWind.setVisibility(LinearLayout.VISIBLE);
					tvWind.setText(savedInstanceState.getString(App.SAVE_WARNING_WIND));
				}else{
					llWerningWind.setVisibility(LinearLayout.GONE);
				}
			}
		}
		Log.i(TAG, "onRestoreInstanceState()");
	}
	
	private boolean isTaskPendingOrRunning(){
		return this.asyncTaskLoadTownWeakRef != null &&
				this.asyncTaskLoadTownWeakRef.get() != null &&
				!this.asyncTaskLoadTownWeakRef.get().getStatus().equals(Status.FINISHED);
	}
	
	private boolean isLoadWeatherPendingOrRunning(){
		return this.asyncTaskLoadWeatherData != null &&
				this.asyncTaskLoadWeatherData.get() != null &&
				!this.asyncTaskLoadWeatherData.get().getStatus().equals(Status.FINISHED);
	}
	
	private void updateListAuto(String nameTown) {
		Log.i(TAG, "isTaskPendingOrRunning: " + isTaskPendingOrRunning());
		cityName = nameTown;
		LoadTownsTask townLoadTask = new LoadTownsTask(this);
		asyncTaskLoadTownWeakRef = new WeakReference<HomeActivity.LoadTownsTask>(townLoadTask);
		townLoadTask.execute(cityName);
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
			
			//if(BuildConfig.DEBUG)
			//	Tools.logToFile(response, "log_load_town");
			
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
			Intent startDetail = new Intent(HomeActivity.this, WeatherDetailActivity.class);
			ItemWeather weather = listItemsWeather.get(position);
            startDetail.putExtra(App.SAVE_ITEM_WEATHER, weather);
            startActivity(startDetail);
			overridePendingTransition(R.anim.open_item_weather_anim, android.R.anim.fade_out);
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
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						cityName = etUrl.getText().toString();
						updateListAuto(cityName);
					}
				}, 100);
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
	
	private DialogInterface.OnClickListener clickBtnListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int index) {
			if(etUrl.getText().toString().length() > 0){
				if(Tools.checkConnection(HomeActivity.this))
				{
					Tools.hideKeyboard(getParent());
					dialog.cancel();
					String text = etUrl.getText().toString();
					text = text.toLowerCase();
					updateListLastTown(tvTown.getText().toString());
					
					Uri.Builder urlBuild = new Uri.Builder();
					if(Locale.getDefault().getLanguage().equals(App.LANG_UA)){
						urlBuild.authority(App.SITE_AUTHORITY_UA);
					} else {
						urlBuild.authority(App.SITE_AUTHORITY_RU);
					}
                    urlBuild.scheme("http")
                            .appendPath("погода-"+text);
					Log.i(TAG, "GET URL: "+urlBuild.build().toString());

					String newUrl = urlBuild.build().toString();
					if(!URL.equals(newUrl)){
						URL = newUrl;
						refreshWeather(URL);
						etUrl.setText("");
						updateInfosTvTown();
					}
				}
				else
				{
					Toast.makeText(HomeActivity.this, getString(R.string.no_connections), Toast.LENGTH_SHORT).show();
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

	private void refreshWeather(String url){
		if(!isLoadWeatherPendingOrRunning()){
			HttpTask task = new HttpTask(HomeActivity.this);
			asyncTaskLoadWeatherData = new WeakReference<HomeActivity.HttpTask>(task);
			task.execute(url);
		}
	}

	static class HttpTask extends AsyncTask<String, Integer, WeatherStruct> {
		
		private WeakReference<HomeActivity> activityWeakRef;
		
		public HttpTask(HomeActivity act){
			activityWeakRef = new WeakReference<HomeActivity>(act);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        activityWeakRef.get().pd.show();
                    } catch (Exception e) {}
                }
            }, 50);
	        if(activityWeakRef.get().etUrl.isShown()){
	        	activityWeakRef.get().etUrl.dismissDropDown();
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
	    	if(activityWeakRef.get() != null && response != null){
	    		try {
		        	activityWeakRef.get().pref.edit().putString(App.PREF_LAST_DATE_UPDATE_FULL,
		        			activityWeakRef.get().dateFullFirmat.format(new Date())).commit();
		        	if(activityWeakRef.get().pd != null) activityWeakRef.get().pd.dismiss();
                    activityWeakRef.get().animLoadBtnUpdate.cancel();
		        	activityWeakRef.get().pref.edit().putString(App.PREF_SITY_URL, activityWeakRef.get().URL).commit();
		        	activityWeakRef.get().pref.edit().putString(App.PREF_SITY_NAME, activityWeakRef.get().cityName).commit();
		        	activityWeakRef.get().setInfoWeather(response);
		        	if(activityWeakRef.get().isFirst){
		        		activityWeakRef.get().pref.edit().putBoolean(App.PREF_is_FIRST_START, false).commit();
		        		activityWeakRef.get().isFirst = false;
		        	}
				} catch (Exception e) {}
	    	} else if (activityWeakRef.get() != null) {
                if(activityWeakRef.get().pd != null) activityWeakRef.get().pd.dismiss();
                Toast toast = Toast.makeText(activityWeakRef.get(), activityWeakRef.get().getString(R.string.no_correct_name_town), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
	}
	
	public void setInfoWeather(WeatherStruct info){
		if(Tools.isCorrectDataWeather(info)){
			currentWeather = info;
			if(info.getWeatherMonday().dateFull != null
                    && info.getWeatherMonday().dateFull.length() > 0){
				tvLastDateUpdate.setText(getString(R.string.last_update_date)+" "+info.getWeatherMonday().dateFull);
			} else {
				String date = pref.getString(App.PREF_LAST_DATE_UPDATE_FULL, dateFullFirmat.format(new Date()));
				tvLastDateUpdate.setText(getString(R.string.last_update_date)+" "+date);
			}
            int cursorDegrees = info.getWeatherToday().indexOf("&");
            String textDegrees = info.getWeatherToday().substring(0, cursorDegrees).replace("+","");
            int degreesNow = Integer.parseInt(textDegrees);
            thermometer.updateTemperature((float)degreesNow);
            tvNow.setText(/*getString(R.string.now_temp_on_street)+ " " + */Html.fromHtml(info.getWeatherToday()));
            getActionBar().setTitle(Html.fromHtml(info.getTownName()));
            tvTown.setText(Html.fromHtml(info.getTownName()));
            //ImageCache.download(info.getWeatherTodayImg(), ivBigWeather);
			if(info.getWarningWind()){
				llWerningWind.setVisibility(LinearLayout.VISIBLE);
				tvWind.setText(info.getWindDescription());
			}else{
				llWerningWind.setVisibility(LinearLayout.GONE);
			}
			
			listItemsWeather.clear();
			listItemsWeather.add(info.getWeatherMonday());
			listItemsWeather.add(info.getWeatherTuesday());
			listItemsWeather.add(info.getWeatherWednesday());
			listItemsWeather.add(info.getWeatherThursday());
			listItemsWeather.add(info.getWeatherFriday());
			listItemsWeather.add(info.getWeatherSaturday());
			listItemsWeather.add(info.getWeatherSunday());
			adapter.notifyDataSetChanged();
			db.insertDataCache(info);
			pref.edit().putString(App.PREF_LAST_DATE_UPDATE, dateFormat.format(new Date())).apply();
		} else {
            if(!settingDialog.isShowing())
                settingDialog.show();
			Toast.makeText(HomeActivity.this, getString(R.string.err_incorrect_city_name), Toast.LENGTH_SHORT).show();
		}		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume()");
        if (pd == null) {
            pd = new ProgressDialog(this);
            pd.setMessage(getString(R.string.dialog_downld_page_msg));
            pd.setCanceledOnTouchOutside(false);
        }

        if (settingDialog == null) {
            View viewDialog = getLayoutInflater().inflate(R.layout.setting_dialog_layout, null);
            etUrl = (AutoCompleteTextView)viewDialog.findViewById(R.id.autoCompleteTextView_city);
            llGetLocation = (LinearLayout)viewDialog.findViewById(R.id.ll_start_get_location);
            pbLoadLocation = (ProgressBar)viewDialog.findViewById(R.id.pb_load_location);
            tvLastSelectInfo = (TextView)viewDialog.findViewById(R.id.tv_info_last_towns);
            lvTown = (ListView)viewDialog.findViewById(R.id.lv_list_last_towns);
            lvTown.setAdapter(adapterTown);
            lvTown.setOnItemClickListener(itemTownClickListener);

            llGetLocation.setOnClickListener(clickGetLocationListener);

            settingDialogBuilder = new AlertDialog.Builder(this);
            settingDialogBuilder.setTitle(R.string.dialog_title_setting);
            settingDialogBuilder.setView(viewDialog);
            settingDialogBuilder.setPositiveButton(R.string.apply, clickBtnListener);
            settingDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

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
            settingDialog.setCanceledOnTouchOutside(false);

            etUrl.addTextChangedListener(textChangeListener);
            etUrl.setOnItemClickListener(itemCompleteListener);
        }

        updateInfosTvTown();

        if(etUrl.getAdapter() == null && compliteAdapter != null)
            etUrl.setAdapter(compliteAdapter);

        initTypefaces();
        if(pref.contains(App.PREF_SITY_URL)){
            URL = pref.getString(App.PREF_SITY_URL, URL);
        }
        isFirst = pref.getBoolean(App.PREF_is_FIRST_START, true);
        if(isFirst == false){
            String nowDate = dateFormat.format(new Date());
            String prefDate = pref.getString(App.PREF_LAST_DATE_UPDATE, "");

            if(Tools.checkConnection(this) & (!nowDate.equals(prefDate) | prefDate.length() == 0))
            {
                if(listItemsWeather != null & listItemsWeather.size() == 0)
                    refreshWeather(URL);
            } else {
                setInfoWeather(db.getCacheWeather());
                if(!Tools.checkConnection(this))
                    Toast.makeText(this, getString(R.string.no_connections), Toast.LENGTH_SHORT).show();
            }
        }else{
            if(settingDialog != null) {
                try {
                    if (!settingDialog.isShowing())
                        settingDialog.show();
                    btnOkSettingDialog = settingDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    btnOkSettingDialog.setVisibility(Button.GONE);
                } catch (Exception e) {}
            }
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
            case android.R.id.home:
            case R.id.action_setting:
                if(!settingDialog.isShowing())
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
			toast = Toast.makeText(this,  R.string.repeat_click_on_back, Toast.LENGTH_SHORT);
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
