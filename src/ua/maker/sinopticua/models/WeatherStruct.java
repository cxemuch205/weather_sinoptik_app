package ua.maker.sinopticua.models;

import java.util.ArrayList;
import java.util.List;

public class WeatherStruct {

	private ItemWeather weatherMonday = new ItemWeather();
	private ItemWeather weatherTuesday = new ItemWeather();
	private ItemWeather weatherWednesday = new ItemWeather();
	private ItemWeather weatherThursday = new ItemWeather();
	private ItemWeather weatherFriday = new ItemWeather();
	private ItemWeather weatherSaturday = new ItemWeather();
	private ItemWeather weatherSunday = new ItemWeather();
	private String weatherToday = "";
	private String townName = "";
	private boolean warningWind = false;
	private String windDescription = "";
    private String weatherTodayImg = "";

    public WeatherStruct() {
	};

	public ItemWeather getWeatherMonday() {
		return weatherMonday;
	}

	public void setWeatherMonday(ItemWeather weatherMonday) {
		this.weatherMonday = weatherMonday;
	}

	public ItemWeather getWeatherTuesday() {
		return weatherTuesday;
	}

	public void setWeatherTuesday(ItemWeather weatherTuesday) {
		this.weatherTuesday = weatherTuesday;
	}

	public ItemWeather getWeatherWednesday() {
		return weatherWednesday;
	}

	public void setWeatherWednesday(ItemWeather weatherWednesday) {
		this.weatherWednesday = weatherWednesday;
	}

	public ItemWeather getWeatherThursday() {
		return weatherThursday;
	}

	public void setWeatherThursday(ItemWeather weatherThursday) {
		this.weatherThursday = weatherThursday;
	}

	public ItemWeather getWeatherFriday() {
		return weatherFriday;
	}

	public void setWeatherFriday(ItemWeather weatherFriday) {
		this.weatherFriday = weatherFriday;
	}

	public ItemWeather getWeatherSaturday() {
		return weatherSaturday;
	}

	public void setWeatherSaturday(ItemWeather weatherSaturday) {
		this.weatherSaturday = weatherSaturday;
	}

	public ItemWeather getWeatherSunday() {
		return weatherSunday;
	}

	public void setWeatherSunday(ItemWeather weatherSunday) {
		this.weatherSunday = weatherSunday;
	}

	public String getWeatherToday() {
		return weatherToday;
	}

	public void setWeatherToday(String weatherToday) {
		this.weatherToday = weatherToday;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public boolean getWarningWind() {
		return warningWind;
	}

	public void setWarningWind(boolean warningWind) {
		this.warningWind = warningWind;
	}

	public String getWindDescription() {
		return windDescription;
	}

	public void setWindDescription(String windDescription) {
		this.windDescription = windDescription;
	};
	
	public List<ItemWeather> getAllWeathers(){
		List<ItemWeather> listWeather = new ArrayList<ItemWeather>();
		listWeather.add(getWeatherMonday());
		listWeather.add(getWeatherTuesday());
		listWeather.add(getWeatherWednesday());
		listWeather.add(getWeatherThursday());
		listWeather.add(getWeatherFriday());
		listWeather.add(getWeatherSaturday());
		listWeather.add(getWeatherSunday());
		return listWeather;
	}
	
	public void setAllWeathers(List<ItemWeather> data){
		if(data.size() > 6){
			setWeatherMonday(data.get(0));
			setWeatherTuesday(data.get(1));
			setWeatherWednesday(data.get(2));
			setWeatherThursday(data.get(3));
			setWeatherFriday(data.get(4));
			setWeatherSaturday(data.get(5));
			setWeatherSunday(data.get(6));
		}		
	}

    public void setWeatherTodayImg(String weatherTodayImg) {
        this.weatherTodayImg = weatherTodayImg;
    }

    public String getWeatherTodayImg() {
        return weatherTodayImg;
    }
}
