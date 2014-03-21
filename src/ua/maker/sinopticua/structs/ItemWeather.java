package ua.maker.sinopticua.structs;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemWeather implements Parcelable {
	private int day = 1;
	private String dayName = "";
	private String month = "";
	private String minTemp = "";
	private String maxTemp = "";
	private String urlImage = "";
	private String weatherName = "";
	private String urlDetail = "";
	private String dateFull = "";
	private int idDB = 0;
	private boolean isFreeDay = false;

	public ItemWeather() {
	};

	public ItemWeather(Parcel in) {
		day = in.readInt();
		dayName = in.readString();
		month = in.readString();
		minTemp = in.readString();
		maxTemp = in.readString();
		urlImage = in.readString();
		urlDetail = in.readString();
		weatherName = in.readString();
		isFreeDay = in.readInt() == 1 ? true : false;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(String minTemp) {
		this.minTemp = minTemp;
	}

	public String getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(String maxTemp) {
		this.maxTemp = maxTemp;
	}

	public String getTemperature() {
		String weather = "";
		weather += dayName;
		weather += "\r\n";
		weather += String.valueOf(day);
		weather += " ";
		weather += String.valueOf(month);
		weather += "\r\n";
		weather += minTemp;
		weather += "\r\n";
		weather += maxTemp;
		return weather;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	public boolean isFreeDay() {
		return isFreeDay;
	}

	public void setFreeDay(boolean isFreeDay) {
		this.isFreeDay = isFreeDay;
	}

	public static final Parcelable.Creator<ItemWeather> CREATOR = new Parcelable.Creator<ItemWeather>() {
		@Override
		public ItemWeather createFromParcel(Parcel source) {
			return new ItemWeather(source);
		}

		@Override
		public ItemWeather[] newArray(int size) {
			return new ItemWeather[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(day);
		dest.writeString(dayName);
		dest.writeString(month);
		dest.writeString(minTemp);
		dest.writeString(maxTemp);
		dest.writeString(urlImage);
		dest.writeString(urlDetail);
		dest.writeString(weatherName);
		dest.writeInt(isFreeDay ? 1 : 0);
	}

	public String getWeatherName() {
		return weatherName;
	}

	public void setWeatherName(String weatherName) {
		this.weatherName = weatherName;
	}

	public String getUrlDetail() {
		return urlDetail;
	}

	public void setUrlDetail(String urlDetail) {
		this.urlDetail = urlDetail;
	}

	public String getDateFull() {
		return dateFull;
	}

	public void setDateFull(String dateFull) {
		this.dateFull = dateFull;
	}

	public int getIdDB() {
		return idDB;
	}

	public void setIdDB(int idDB) {
		this.idDB = idDB;
	}
}