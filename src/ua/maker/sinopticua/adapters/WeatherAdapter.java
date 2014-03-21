package ua.maker.sinopticua.adapters;

import java.util.List;

import ua.maker.sinopticua.R;
import ua.maker.sinopticua.structs.ItemWeather;
import ua.maker.sinopticua.utils.ImageFetcher;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherAdapter extends ArrayAdapter<ItemWeather> {

	private Context mContext;
	private List<ItemWeather> data;
	private ImageFetcher imageFether;

	public WeatherAdapter(Context context, List<ItemWeather> data, ImageFetcher imageFether) {
		super(context, R.layout.item_weather_layout, data);
		this.mContext = context;
		this.data = data;
		this.imageFether = imageFether;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;

		if (view == null) {
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			view = inflater.inflate(R.layout.item_weather_layout, null);
			
			holder = new ViewHolder();
			holder.tvDay = (TextView)view.findViewById(R.id.tv_day);
			holder.tvNameDay = (TextView)view.findViewById(R.id.tv_name_day);
			holder.tvMonth = (TextView)view.findViewById(R.id.tv_month);
			holder.tvTempMin = (TextView)view.findViewById(R.id.tv_temp_min);
			holder.tvTempMax = (TextView)view.findViewById(R.id.tv_temp_max);
			holder.tvNameWeather = (TextView)view.findViewById(R.id.tv_name_weather);
			holder.ivWeatherImage = (ImageView)view.findViewById(R.id.iv_weather);
			
			view.setTag(holder);			
		} else {
			holder = (ViewHolder)view.getTag();
		}
		
		ItemWeather item = data.get(position);
		
		holder.tvDay.setText(Html.fromHtml(String.valueOf(item.getDay())));
		holder.tvNameDay.setText(String.valueOf(item.getDayName()));
		holder.tvMonth.setText(Html.fromHtml(item.getMonth()));
		holder.tvTempMin.setText(Html.fromHtml(item.getMinTemp()));
		holder.tvTempMax.setText(Html.fromHtml(item.getMaxTemp()));
		holder.tvNameWeather.setText(Html.fromHtml(item.getWeatherName()));
		if(item.isFreeDay()){
			holder.tvDay.setTextColor(Color.RED);
			holder.tvMonth.setTextColor(Color.RED);
		}else{
			holder.tvDay.setTextColor(Color.BLACK);
			holder.tvMonth.setTextColor(Color.BLACK);
		}
		String url = item.getUrlImage();
         
        if((url == null) || (!Patterns.WEB_URL.matcher(url).matches()))
        {
        	url = "";
        }
        
        imageFether.loadThumbnailImage(url, holder.ivWeatherImage, R.drawable.white_cube);

		return view;
	}

	static class ViewHolder {
		TextView tvDay;
		TextView tvNameDay;
		TextView tvMonth;
		TextView tvTempMin;
		TextView tvTempMax;
		TextView tvNameWeather;
		ImageView ivWeatherImage;
	}

}
