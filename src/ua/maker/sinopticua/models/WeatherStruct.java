package ua.maker.sinopticua.models;

import java.util.ArrayList;
import java.util.List;

public class WeatherStruct {

	private ItemWeather weatherMondey = new ItemWeather();
	private ItemWeather weatherTuesday = new ItemWeather();
	private ItemWeather weatherWednesday = new ItemWeather();
	private ItemWeather weatherThursday = new ItemWeather();
	private ItemWeather weatherFriday = new ItemWeather();
	private ItemWeather weatherSaturday = new ItemWeather();
	private ItemWeather weatherSundey = new ItemWeather();
	private String weatherToday = "";
	private String townName = "";
	private boolean werningWind = false;
	private String windDescription = "";

	public WeatherStruct() {
	};

	public ItemWeather getWeatherMondey() {
		return weatherMondey;
	}

	public void setWeatherMondey(ItemWeather weatherMondey) {
		this.weatherMondey = weatherMondey;
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

	public ItemWeather getWeatherSundey() {
		return weatherSundey;
	}

	public void setWeatherSundey(ItemWeather weatherSundey) {
		this.weatherSundey = weatherSundey;
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

	public boolean getWerningWind() {
		return werningWind;
	}

	public void setWerningWind(boolean werningWind) {
		this.werningWind = werningWind;
	}

	public String getWindDescription() {
		return windDescription;
	}

	public void setWindDescription(String windDescription) {
		this.windDescription = windDescription;
	};
	
	public List<ItemWeather> getAllWeathers(){
		List<ItemWeather> listWeather = new ArrayList<ItemWeather>();
		listWeather.add(getWeatherMondey());
		listWeather.add(getWeatherTuesday());
		listWeather.add(getWeatherWednesday());
		listWeather.add(getWeatherThursday());
		listWeather.add(getWeatherFriday());
		listWeather.add(getWeatherSaturday());
		listWeather.add(getWeatherSundey());
		return listWeather;
	}
	
	public void setAllWeathers(List<ItemWeather> data){
		if(data.size() > 0){
			setWeatherMondey(data.get(0));
			setWeatherTuesday(data.get(1));
			setWeatherWednesday(data.get(2));
			setWeatherThursday(data.get(3));
			setWeatherFriday(data.get(4));
			setWeatherSaturday(data.get(5));
			setWeatherSundey(data.get(6));
		}		
	}
}
