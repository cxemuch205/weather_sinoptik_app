package ua.maker.sinopticua.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.maker.sinopticua.R;
import ua.maker.sinopticua.models.ItemDetail;
import ua.maker.sinopticua.utils.ImageCache;

/**
 * Created by Daniil on 02.02.2015.
 */
public class WeatherItemDetailPagerAdapter extends PagerAdapter {

    private ArrayList<ItemDetail> data;
    private Context context;
    private LayoutInflater inflater;

    public WeatherItemDetailPagerAdapter(Context context, ArrayList<ItemDetail> data) {
        super();
        inflater = ((Activity) context).getLayoutInflater();
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_detail_weather, null);

        initViewWeather(view, position);

        container.addView(view);

        return view;
    }

    private void initViewWeather(View view, int position) {
        ViewHolder holder = new ViewHolder();
        holder.tvStageDay = (TextView) view.findViewById(R.id.tv_stage_day);
        holder.tv1TimeDay = (TextView) view.findViewById(R.id.tv_1_time_day);
        holder.tv2TimeDay = (TextView) view.findViewById(R.id.tv_2_time_day);
        holder.tv1Temp = (TextView) view.findViewById(R.id.tv_1_temp);
        holder.tv2Temp = (TextView) view.findViewById(R.id.tv_2_temp);
        holder.tv1FellTemp = (TextView) view.findViewById(R.id.tv_1_fell_temp);
        holder.tv2FellTemp = (TextView) view.findViewById(R.id.tv_2_fell_temp);
        holder.tv1Pressure = (TextView) view.findViewById(R.id.tv_1_pressure);
        holder.tv2Pressure = (TextView) view.findViewById(R.id.tv_2_pressure);
        holder.tv1Humidity = (TextView) view.findViewById(R.id.tv_1_humidity);
        holder.tv2Humidity = (TextView) view.findViewById(R.id.tv_2_humidity);
        holder.tv1SpeedWind = (TextView) view.findViewById(R.id.tv_1_speed_weather);
        holder.tv2SpeedWind = (TextView) view.findViewById(R.id.tv_2_speed_weather);
        holder.tv1ChanceOfPrecipitation = (TextView) view.findViewById(R.id.tv_1_chance_of_precipitation);
        holder.tv2ChanceOfPrecipitation = (TextView) view.findViewById(R.id.tv_2_chance_of_precipitation);
        holder.iv1Weather = (ImageView) view.findViewById(R.id.iv_1_image_weather);
        holder.iv2Weather = (ImageView) view.findViewById(R.id.iv_2_image_weather);
        holder.iv1WindDirection = (ImageView) view.findViewById(R.id.iv_1_direction);
        holder.iv2WindDirection = (ImageView) view.findViewById(R.id.iv_2_direction);

        setInfoAdapter(holder, position);
    }

    private void setInfoAdapter(ViewHolder holder, int position) {
        ItemDetail detail = data.get(position);
        holder.tvStageDay.setText(detail.dayStage);
        holder.tv1TimeDay.setText(detail.dayTime.get(0));
        holder.tv2TimeDay.setText(detail.dayTime.get(1));
        holder.tv1Temp.setText(detail.temperature.get(0));
        holder.tv2Temp.setText(detail.temperature.get(1));
        holder.tv1FellTemp.setText(detail.temperatureFell.get(0));
        holder.tv2FellTemp.setText(detail.temperatureFell.get(1));
        holder.tv1Pressure.setText(detail.pressure.get(0));
        holder.tv2Pressure.setText(detail.pressure.get(1));
        holder.tv1SpeedWind.setText(detail.winds.get(0).speed);
        holder.tv2SpeedWind.setText(detail.winds.get(1).speed);
        holder.tv1Humidity.setText(detail.humidity.get(0));
        holder.tv2Humidity.setText(detail.humidity.get(1));
        holder.tv1ChanceOfPrecipitation.setText(detail.chanceOfPrecipitation.get(0));
        holder.tv2ChanceOfPrecipitation.setText(detail.chanceOfPrecipitation.get(1));
        ImageCache.download(detail.imageWeather.get(0), holder.iv1Weather);
        ImageCache.download(detail.imageWeather.get(1), holder.iv2Weather);
        ImageCache.download(detail.winds.get(0).imgDirection, holder.iv1WindDirection);
        ImageCache.download(detail.winds.get(1).imgDirection, holder.iv2WindDirection);
    }

    private static class ViewHolder {
        TextView tvStageDay, tv1TimeDay, tv2TimeDay,
                tv1Temp, tv2Temp, tv1FellTemp, tv2FellTemp,
                tv1Pressure, tv2Pressure, tv1Humidity, tv2Humidity, tv1SpeedWind, tv2SpeedWind,
                tv1ChanceOfPrecipitation, tv2ChanceOfPrecipitation;
        ImageView iv1Weather, iv2Weather, iv1WindDirection, iv2WindDirection;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
