package ua.maker.sinopticua;

import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.structs.ItemWeather;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherDetailActivity extends FragmentActivity {
	
	private static final String TAG = "WeatherDetailActivity";
	
	private ItemWeather weather;
	
	private TextView tvFullDate;
	private TextView tvDayName;
	
	private String urlDetail = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_weather_layout);
		weather = new ItemWeather();
		String[] data = getIntent().getExtras().getStringArray(App.SAVE_ITEM_WEATHER);
		
		weather.setDay(Integer.parseInt(data[1]));
		weather.setDayName(data[2]);
		weather.setMonth(data[3]);
		urlDetail = data[0];
		weather.setUrlDetail(urlDetail);
		
		tvFullDate = (TextView)findViewById(R.id.tv_date_full);
		tvDayName = (TextView)findViewById(R.id.tv_day_name);
		
		tvFullDate.setText(String.valueOf(weather.getDay())+weather.getMonth());
		tvDayName.setText(weather.getDayName());
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(WeatherDetailActivity.this, "Url detail: " + weather.getUrlDetail(), Toast.LENGTH_SHORT).show();
			}
		}, 3000);
	}
}
