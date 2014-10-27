package ua.maker.sinopticua;

import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.models.ItemWeather;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class WeatherDetailActivity extends FragmentActivity {
	
	private static final String TAG = "WeatherDetailActivity";
	
	private ItemWeather weather;
	
	private TextView tvFullDate;
	private TextView tvDayName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_weather_layout);
		weather = (ItemWeather) getIntent().getExtras().get(App.SAVE_ITEM_WEATHER);
		
		tvFullDate = (TextView)findViewById(R.id.tv_date_full);
		tvDayName = (TextView)findViewById(R.id.tv_day_name);
        TextView tvDetailUrl = (TextView) findViewById(R.id.tv_url_detail);

        tvFullDate.setText(String.valueOf(weather.day)+weather.month);
		tvDayName.setText(weather.dayName);
        tvDetailUrl.setText(weather.urlDetail);
	}
}
