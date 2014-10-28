package ua.maker.sinopticua.adapters;

import java.util.List;

import ua.maker.sinopticua.R;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.models.ItemWeather;
import ua.maker.sinopticua.utils.ImageCache;
import ua.maker.sinopticua.utils.Tools;

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

	public WeatherAdapter(Context context, List<ItemWeather> data) {
		super(context, R.layout.item_weather_layout, data);
		this.mContext = context;
		this.data = data;
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

            initTypefaces(holder);

            view.setTag(holder);
		} else {
			holder = (ViewHolder)view.getTag();
		}

        view.setBackgroundResource(R.drawable.back_item_weather);
		
		ItemWeather item = data.get(position);
		
		holder.tvDay.setText(Html.fromHtml(String.valueOf(item.day)));
		holder.tvNameDay.setText(String.valueOf(item.dayName));
		holder.tvMonth.setText(Html.fromHtml(item.month));
		holder.tvTempMin.setText(Html.fromHtml(item.minTemp));
		holder.tvTempMax.setText(Html.fromHtml(item.maxTemp));
		holder.tvNameWeather.setText(Html.fromHtml(item.weatherName));
		if(item.isFreeDay){
			holder.tvDay.setTextColor(Color.RED);
			holder.tvMonth.setTextColor(Color.RED);
		}else{
			holder.tvDay.setTextColor(Color.BLACK);
			holder.tvMonth.setTextColor(Color.BLACK);
		}
		String url = item.urlImage;
        ImageCache.download(url, holder.ivWeatherImage);

		return view;
	}

    private void initTypefaces(ViewHolder holder) {
        holder.tvDay.setTypeface(Tools.getFont(mContext, App.MTypeface.ROBOTO_MEDIUM));
        holder.tvMonth.setTypeface(Tools.getFont(mContext, App.MTypeface.ROBOTO_MEDIUM));
        holder.tvNameDay.setTypeface(Tools.getFont(mContext, App.MTypeface.ROBOTO_LIGHT));
        holder.tvTempMin.setTypeface(Tools.getFont(mContext, App.MTypeface.ROBOTO_LIGHT));
        holder.tvTempMax.setTypeface(Tools.getFont(mContext, App.MTypeface.ROBOTO_LIGHT));
        holder.tvNameWeather.setTypeface(Tools.getFont(mContext, App.MTypeface.ROBOTO_THIN));
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
