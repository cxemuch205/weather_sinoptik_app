package ua.maker.sinopticua.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.maker.sinopticua.models.ItemTown;
import ua.maker.sinopticua.models.ItemWeather;
import ua.maker.sinopticua.models.WeatherStruct;
import android.util.Log;

public class DataParser {
	
	private static final String TAG = "WeatherParser";
	
	private static DataParser instance;
	
	private DataParser(){};
	
	public static DataParser getInstance(){
		if(instance == null){
			instance = new DataParser();
		}
		
		return instance;
	}
	
	public WeatherStruct parserHTML(String sendHttpRequest) {
        if (sendHttpRequest == null
                || (sendHttpRequest != null && sendHttpRequest.length() == 0)) {
            return null;
        }
        String result = "";
        WeatherStruct wRes = new WeatherStruct();
        result = sendHttpRequest;
        int dayCount = 0,
                monthCount = 0,
                minTCount = 0,
                maxTCount = 0,
                imgUrlCount = 0,
                detailUrlCount = 0;

        for (int i = 0; i < (result.length() - 100); i++) {

            // GET NAME SITY
            if (result.substring(i, (i + 36)).equals("<div class=\"cityName cityNameShort\">")) {
                for (int j = (i + 36); j < (result.length() - 100); j++) {
                    if (result.substring(j, (j + 9)).equals("</strong>")) {
                        String town = result.substring((i + 42), (j + 9));
                        for (int rem = 0; rem < town.length(); rem++) {
                            if (town.substring(rem, rem + 1).contains("П")) {
                                town = town.substring(rem, rem + 6) + " " + town.substring(rem + 11, town.length());
                                break;
                            }
                        }
                        Log.i(TAG, "TOWN || " + town);
                        wRes.setTownName(town);
                        break;
                    }
                }
            }

            //GET ON WEEK WEATHER
            if (result.substring(i, (i + 11)).equals("\" id=\"bd1\">")) {
                boolean D = false, M = false, MI = false, MA = false, imgUrl = false, detailUrl = false;
                ItemWeather itemWeater = new ItemWeather();
                //Log.d(TAG, "process 1");
                for (int j = (i + 11); j < (result.length() - 100); j++) {
                    if (dayCount == 0
                            | monthCount == 0
                            | minTCount == 0
                            | maxTCount == 0
                            | imgUrlCount == 0
                            | detailUrlCount == 0) {
                        if (result.substring(j, (j + 31)).equals("<a class=\"day-link\" data-link=\"")
                                & detailUrlCount == 0) {
                            for (int q = (j + 31); q < (j + 650); q++) {
                                if (result.substring(q, (q + 8)).equals("\" href=\"")
                                        & detailUrlCount == 0) {
                                    for (int qa = q; qa < (q + 200); qa++) {
                                        if (result.substring(qa, (qa + 2)).equals("\">")) {
                                            if (detailUrlCount == 0) {
                                                String urlDetail = result.substring((q + 8), qa);
                                                Log.i(TAG, "URL detail: " + urlDetail);
                                                itemWeater.urlDetail = urlDetail;
                                            }
                                            for (int qb = (qa + 2); qb < (qa + 22); qb++) {
                                                if (result.substring(qb, (qb + 8)).equals("</a></p>")) {
                                                    String nameDay = result.substring((qa + 2), qb);
                                                    Log.i(TAG, "Day name: " + nameDay);
                                                    itemWeater.dayName = nameDay;
                                                    detailUrlCount = 1;
                                                    detailUrl = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if ((result.substring(j, (j + 17)).equals("<p class=\"date \">")
                                & dayCount == 0) |
                                (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")
                                        & dayCount == 0)) {
                            //Log.d(TAG, "process 3");
                            if (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")) {
                                itemWeater.isFreeDay = true;
                            } else {
                                itemWeater.isFreeDay = false;
                            }
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //////Log.d(TAG, "process 4");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String n = result.substring((j + 17), t);
                                    int dd = 0;
                                    try {
                                        dd = Integer.parseInt(n);
                                    } catch (Exception e) {
                                        dd = Integer.parseInt(result.substring((j + 25), t));
                                    }
                                    itemWeater.day = dd;
                                    Log.i(TAG, "MONDAY DAY " + dd);
                                    dayCount = 1;
                                    D = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<p class=\"month\">")
                                & monthCount == 0) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.month = m;
                                    Log.i(TAG, "MONDAY MONTH " + m);
                                    monthCount = 1;
                                    M = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"min\">")
                                & minTCount == 0) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 6");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.minTemp = m;
                                    Log.i(TAG, "MONDAY MINT " + m);
                                    minTCount = 1;
                                    MI = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"max\">")
                                & maxTCount == 0) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 7");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.maxTemp = m;
                                    Log.i(TAG, "MONDAY MAXT " + m);
                                    maxTCount = 1;
                                    MA = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 29)).equals("<img class=\"weatherImg\" src=\"")
                                & imgUrlCount == 0) {
                            for (int t = (j - 120); t < j; t++) {
                                if (result.substring(t, (t + 9)).equals("\" title=\"")) {
                                    for (int p = (t + 9); p < (t + 120); p++) {
                                        if (result.substring(p, (p + 14)).equals("\"><img class=\"")) {
                                            String nameWea = result.substring((t + 9), p);
                                            Log.i(TAG, "Name weather: " + nameWea);
                                            itemWeater.weatherName = nameWea;
                                        }
                                    }
                                }
                            }
                            for (int t = (j + 29); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 8");
                                if (result.substring(t, (t + 5)).equals("\" alt")) {
                                    String urlI = result.substring((j + 29), t);
                                    itemWeater.urlImage = urlI;
                                    Log.i(TAG, "MONDAY urlImage " + urlI);
                                    imgUrlCount = 1;
                                    imgUrl = true;
                                    break;
                                }
                            }
                        }
                        if (D & M & MI & MA & imgUrl & detailUrl
                                & dayCount == 1
                                & monthCount == 1
                                & minTCount == 1
                                & maxTCount == 1
                                & imgUrlCount == 1
                                & detailUrlCount == 1) {
                            Log.d(TAG, "ItemWeather 1 SUCCESS");
                            wRes.setWeatherMondey(itemWeater);
                            D = false;
                            M = false;
                            MI = false;
                            MA = false;
                            imgUrl = false;
                            detailUrl = false;
                            itemWeater = new ItemWeather();
                        }
                    } else if (dayCount == 1
                            | monthCount == 1
                            | minTCount == 1
                            | maxTCount == 1
                            | imgUrlCount == 1
                            | detailUrlCount == 1) {
                        if (result.substring(j, (j + 31)).equals("<a class=\"day-link\" data-link=\"")
                                & detailUrlCount == 1) {
                            for (int q = (j + 31); q < (j + 650); q++) {
                                if (result.substring(q, (q + 8)).equals("\" href=\"")
                                        & detailUrlCount == 1) {
                                    for (int qa = q; qa < (q + 200); qa++) {
                                        if (result.substring(qa, (qa + 2)).equals("\">")) {
                                            if (detailUrlCount == 1) {
                                                String urlDetail = result.substring((q + 8), qa);
                                                Log.i(TAG, "URL detail: " + urlDetail);
                                                itemWeater.urlDetail = urlDetail;
                                            }
                                            for (int qb = (qa + 2); qb < (qa + 22); qb++) {
                                                if (result.substring(qb, (qb + 8)).equals("</a></p>")) {
                                                    String nameDay = result.substring((qa + 2), qb);
                                                    Log.i(TAG, "Day name: " + nameDay);
                                                    itemWeater.dayName = nameDay;
                                                    detailUrlCount = 2;
                                                    detailUrl = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if ((result.substring(j, (j + 17)).equals("<p class=\"date \">")
                                & dayCount == 1) |
                                (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")
                                        & dayCount == 1)) {
                            //Log.d(TAG, "process 3");
                            if (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")) {
                                itemWeater.isFreeDay = true;
                            } else {
                                itemWeater.isFreeDay = false;
                            }
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                ////Log.d(TAG, "process 4");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String n = result.substring((j + 17), t);
                                    int dd = 0;
                                    try {
                                        dd = Integer.parseInt(n);
                                    } catch (Exception e) {
                                        dd = Integer.parseInt(result.substring((j + 25), t));
                                    }
                                    itemWeater.day = dd;
                                    Log.i(TAG, "MONDAY DAY " + dd);
                                    dayCount = 2;
                                    D = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<p class=\"month\">")
                                & monthCount == 1) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                ////Log.d(TAG, "process 5");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.month = m;
                                    Log.i(TAG, "MONDAY MONTH " + m);
                                    monthCount = 2;
                                    M = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"min\">")
                                & minTCount == 1) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 6");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.minTemp = m;
                                    Log.i(TAG, "MONDAY MINT " + m);
                                    minTCount = 2;
                                    MI = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"max\">")
                                & maxTCount == 1) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 7");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.maxTemp = m;
                                    Log.i(TAG, "MONDAY MAXT " + m);
                                    maxTCount = 2;
                                    MA = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 29)).equals("<img class=\"weatherImg\" src=\"")
                                & imgUrlCount == 1) {
                            for (int t = (j - 120); t < j; t++) {
                                if (result.substring(t, (t + 9)).equals("\" title=\"")) {
                                    for (int p = (t + 9); p < (t + 120); p++) {
                                        if (result.substring(p, (p + 14)).equals("\"><img class=\"")) {
                                            String nameWea = result.substring((t + 9), p);
                                            Log.i(TAG, "Name weather: " + nameWea);
                                            itemWeater.weatherName = nameWea;
                                        }
                                    }
                                }
                            }
                            for (int t = (j + 29); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 8");
                                if (result.substring(t, (t + 5)).equals("\" alt")) {
                                    String urlI = result.substring((j + 29), t);
                                    itemWeater.urlImage = urlI;
                                    Log.i(TAG, "MONDAY urlImage " + urlI);
                                    imgUrlCount = 2;
                                    imgUrl = true;
                                    break;
                                }
                            }
                        }
                        if (D & M & MI & MA & imgUrl & detailUrl
                                & dayCount == 2
                                & monthCount == 2
                                & minTCount == 2
                                & maxTCount == 2
                                & imgUrlCount == 2
                                & detailUrlCount == 2) {
                            Log.d(TAG, "ItemWeather 2 SUCCESS");
                            wRes.setWeatherTuesday(itemWeater);
                            D = false;
                            M = false;
                            MI = false;
                            MA = false;
                            imgUrl = false;
                            detailUrl = false;
                            itemWeater = new ItemWeather();
                        }
                    } else if (dayCount == 2
                            | monthCount == 2
                            | minTCount == 2
                            | maxTCount == 2
                            | imgUrlCount == 2
                            | detailUrlCount == 2) {
                        if (result.substring(j, (j + 31)).equals("<a class=\"day-link\" data-link=\"")
                                & detailUrlCount == 2) {
                            for (int q = (j + 31); q < (j + 650); q++) {
                                if (result.substring(q, (q + 8)).equals("\" href=\"")
                                        & detailUrlCount == 2) {
                                    for (int qa = q; qa < (q + 200); qa++) {
                                        if (result.substring(qa, (qa + 2)).equals("\">")) {
                                            if (detailUrlCount == 2) {
                                                String urlDetail = result.substring((q + 8), qa);
                                                Log.i(TAG, "URL detail: " + urlDetail);
                                                itemWeater.urlDetail = urlDetail;
                                            }
                                            for (int qb = (qa + 2); qb < (qa + 22); qb++) {
                                                if (result.substring(qb, (qb + 8)).equals("</a></p>")) {
                                                    String nameDay = result.substring((qa + 2), qb);
                                                    Log.i(TAG, "Day name: " + nameDay);
                                                    itemWeater.dayName = nameDay;
                                                    detailUrlCount = 3;
                                                    detailUrl = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if ((result.substring(j, (j + 17)).equals("<p class=\"date \">")
                                & dayCount == 2) |
                                (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")
                                        & dayCount == 2)) {
                            //Log.d(TAG, "process 3");
                            if (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")) {
                                itemWeater.isFreeDay = true;
                            } else {
                                itemWeater.isFreeDay = false;
                            }
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                ////Log.d(TAG, "process 4");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String n = result.substring((j + 17), t);
                                    int dd = 0;
                                    try {
                                        dd = Integer.parseInt(n);
                                    } catch (Exception e) {
                                        dd = Integer.parseInt(result.substring((j + 25), t));
                                    }
                                    itemWeater.day = dd;
                                    Log.i(TAG, "MONDAY DAY " + dd);
                                    dayCount = 3;
                                    D = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<p class=\"month\">")
                                & monthCount == 2) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 5");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.month = m;
                                    Log.i(TAG, "MONDAY MONTH " + m);
                                    monthCount = 3;
                                    M = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"min\">")
                                & minTCount == 2) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 6");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.minTemp = m;
                                    Log.i(TAG, "MONDAY MINT " + m);
                                    minTCount = 3;
                                    MI = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"max\">")
                                & maxTCount == 2) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 7");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.maxTemp = m;
                                    Log.i(TAG, "MONDAY MAXT " + m);
                                    maxTCount = 3;
                                    MA = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 29)).equals("<img class=\"weatherImg\" src=\"")
                                & imgUrlCount == 2) {
                            for (int t = (j - 120); t < j; t++) {
                                if (result.substring(t, (t + 9)).equals("\" title=\"")) {
                                    for (int p = (t + 9); p < (t + 120); p++) {
                                        if (result.substring(p, (p + 14)).equals("\"><img class=\"")) {
                                            String nameWea = result.substring((t + 9), p);
                                            Log.i(TAG, "Name weather: " + nameWea);
                                            itemWeater.weatherName = nameWea;
                                        }
                                    }
                                }
                            }
                            for (int t = (j + 29); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 8");
                                if (result.substring(t, (t + 5)).equals("\" alt")) {
                                    String urlI = result.substring((j + 29), t);
                                    itemWeater.urlImage = urlI;
                                    Log.i(TAG, "MONDAY urlImage " + urlI);
                                    imgUrlCount = 3;
                                    imgUrl = true;
                                    break;
                                }
                            }
                        }
                        if (D & M & MI & MA & imgUrl & detailUrl
                                & dayCount == 3
                                & monthCount == 3
                                & minTCount == 3
                                & maxTCount == 3
                                & imgUrlCount == 3
                                & detailUrlCount == 3) {
                            Log.d(TAG, "ItemWeather 3 SUCCESS");
                            wRes.setWeatherWednesday(itemWeater);
                            D = false;
                            M = false;
                            MI = false;
                            MA = false;
                            imgUrl = false;
                            detailUrl = false;
                            itemWeater = new ItemWeather();
                        }
                    } else if (dayCount == 3
                            | monthCount == 3
                            | minTCount == 3
                            | maxTCount == 3
                            | imgUrlCount == 3
                            | detailUrlCount == 3) {
                        if (result.substring(j, (j + 31)).equals("<a class=\"day-link\" data-link=\"")
                                & detailUrlCount == 3) {
                            for (int q = (j + 31); q < (j + 650); q++) {
                                if (result.substring(q, (q + 8)).equals("\" href=\"")
                                        & detailUrlCount == 3) {
                                    for (int qa = q; qa < (q + 200); qa++) {
                                        if (result.substring(qa, (qa + 2)).equals("\">")) {
                                            if (detailUrlCount == 3) {
                                                String urlDetail = result.substring((q + 8), qa);
                                                Log.i(TAG, "URL detail: " + urlDetail);
                                                itemWeater.urlDetail = urlDetail;
                                            }
                                            for (int qb = (qa + 2); qb < (qa + 22); qb++) {
                                                if (result.substring(qb, (qb + 8)).equals("</a></p>")) {
                                                    String nameDay = result.substring((qa + 2), qb);
                                                    Log.i(TAG, "Day name: " + nameDay);
                                                    itemWeater.dayName = nameDay;
                                                    detailUrlCount = 4;
                                                    detailUrl = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if ((result.substring(j, (j + 17)).equals("<p class=\"date \">")
                                & dayCount == 3) |
                                (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")
                                        & dayCount == 3)) {
                            //Log.d(TAG, "process 3");
                            if (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")) {
                                itemWeater.isFreeDay = true;
                            } else {
                                itemWeater.isFreeDay = false;
                            }
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                ////Log.d(TAG, "process 4");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String n = result.substring((j + 17), t);
                                    int dd;
                                    try {
                                        dd = Integer.parseInt(n);
                                    } catch (Exception e) {
                                        dd = Integer.parseInt(result.substring((j + 25), t));
                                    }
                                    itemWeater.day = dd;
                                    Log.i(TAG, "MONDAY DAY " + dd);
                                    dayCount = 4;
                                    D = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<p class=\"month\">")
                                & monthCount == 3) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 5");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.month = m;
                                    Log.i(TAG, "MONDAY MONTH " + m);
                                    monthCount = 4;
                                    M = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"min\">")
                                & minTCount == 3) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 6");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.minTemp = m;
                                    Log.i(TAG, "MONDAY MINT " + m);
                                    minTCount = 4;
                                    MI = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"max\">")
                                & maxTCount == 3) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 7");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.maxTemp = m;
                                    Log.i(TAG, "MONDAY MAXT " + m);
                                    maxTCount = 4;
                                    MA = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 29)).equals("<img class=\"weatherImg\" src=\"")
                                & imgUrlCount == 3) {
                            for (int t = (j - 120); t < j; t++) {
                                if (result.substring(t, (t + 9)).equals("\" title=\"")) {
                                    for (int p = (t + 9); p < (t + 120); p++) {
                                        if (result.substring(p, (p + 14)).equals("\"><img class=\"")) {
                                            String nameWea = result.substring((t + 9), p);
                                            Log.i(TAG, "Name weather: " + nameWea);
                                            itemWeater.weatherName = nameWea;
                                        }
                                    }
                                }
                            }
                            for (int t = (j + 29); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 8");
                                if (result.substring(t, (t + 5)).equals("\" alt")) {
                                    String urlI = result.substring((j + 29), t);
                                    itemWeater.urlImage = urlI;
                                    Log.i(TAG, "MONDAY urlImage " + urlI);
                                    imgUrlCount = 4;
                                    imgUrl = true;
                                    break;
                                }
                            }
                        }
                        if (D & M & MI & MA & imgUrl & detailUrl
                                & dayCount == 4
                                & monthCount == 4
                                & minTCount == 4
                                & maxTCount == 4
                                & imgUrlCount == 4
                                & detailUrlCount == 4) {
                            Log.d(TAG, "ItemWeather 4 SUCCESS");
                            wRes.setWeatherThursday(itemWeater);
                            D = false;
                            M = false;
                            MI = false;
                            MA = false;
                            imgUrl = false;
                            detailUrl = false;
                            itemWeater = new ItemWeather();
                        }
                    } else if (dayCount == 4
                            | monthCount == 4
                            | minTCount == 4
                            | maxTCount == 4
                            | imgUrlCount == 4
                            | detailUrlCount == 4) {
                        if (result.substring(j, (j + 31)).equals("<a class=\"day-link\" data-link=\"")
                                & detailUrlCount == 4) {
                            for (int q = (j + 31); q < (j + 650); q++) {
                                if (result.substring(q, (q + 8)).equals("\" href=\"")
                                        & detailUrlCount == 4) {
                                    for (int qa = q; qa < (q + 200); qa++) {
                                        if (result.substring(qa, (qa + 2)).equals("\">")) {
                                            if (detailUrlCount == 4) {
                                                String urlDetail = result.substring((q + 8), qa);
                                                Log.i(TAG, "URL detail: " + urlDetail);
                                                itemWeater.urlDetail = urlDetail;
                                            }
                                            for (int qb = (qa + 2); qb < (qa + 22); qb++) {
                                                if (result.substring(qb, (qb + 8)).equals("</a></p>")) {
                                                    String nameDay = result.substring((qa + 2), qb);
                                                    Log.i(TAG, "Day name: " + nameDay);
                                                    itemWeater.dayName = nameDay;
                                                    detailUrlCount = 5;
                                                    detailUrl = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if ((result.substring(j, (j + 17)).equals("<p class=\"date \">")
                                & dayCount == 4) |
                                (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")
                                        & dayCount == 4)) {
                            //Log.d(TAG, "process 3");
                            if (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")) {
                                itemWeater.isFreeDay = true;
                            } else {
                                itemWeater.isFreeDay = false;
                            }
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                ////Log.d(TAG, "process 4");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String n = result.substring((j + 17), t);
                                    int dd = 0;
                                    try {
                                        dd = Integer.parseInt(n);
                                    } catch (Exception e) {
                                        dd = Integer.parseInt(result.substring((j + 25), t));
                                    }
                                    itemWeater.day = dd;
                                    Log.i(TAG, "MONDAY DAY " + dd);
                                    dayCount = 5;
                                    D = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<p class=\"month\">")
                                & monthCount == 4) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 5");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.month = m;
                                    Log.i(TAG, "MONDAY MONTH " + m);
                                    monthCount = 5;
                                    M = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"min\">")
                                & minTCount == 4) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 6");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.minTemp = m;
                                    Log.i(TAG, "MONDAY MINT " + m);
                                    minTCount = 5;
                                    MI = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"max\">")
                                & maxTCount == 4) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 7");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.maxTemp = m;
                                    Log.i(TAG, "MONDAY MAXT " + m);
                                    maxTCount = 5;
                                    MA = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 29)).equals("<img class=\"weatherImg\" src=\"")
                                & imgUrlCount == 4) {
                            for (int t = (j - 120); t < j; t++) {
                                if (result.substring(t, (t + 9)).equals("\" title=\"")) {
                                    for (int p = (t + 9); p < (t + 120); p++) {
                                        if (result.substring(p, (p + 14)).equals("\"><img class=\"")) {
                                            String nameWea = result.substring((t + 9), p);
                                            Log.i(TAG, "Name weather: " + nameWea);
                                            itemWeater.weatherName = nameWea;
                                        }
                                    }
                                }
                            }
                            for (int t = (j + 29); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 8");
                                if (result.substring(t, (t + 5)).equals("\" alt")) {
                                    String urlI = result.substring((j + 29), t);
                                    itemWeater.urlImage = urlI;
                                    Log.i(TAG, "MONDAY urlImage " + urlI);
                                    imgUrlCount = 5;
                                    imgUrl = true;
                                    break;
                                }
                            }
                        }
                        if (D & M & MI & MA & imgUrl & detailUrl
                                & dayCount == 5
                                & monthCount == 5
                                & minTCount == 5
                                & maxTCount == 5
                                & imgUrlCount == 5
                                & detailUrlCount == 5) {
                            Log.d(TAG, "ItemWeather 5 SUCCESS");
                            wRes.setWeatherFriday(itemWeater);
                            D = false;
                            M = false;
                            MI = false;
                            MA = false;
                            imgUrl = false;
                            detailUrl = false;
                            itemWeater = new ItemWeather();
                        }
                    } else if (dayCount == 5
                            | monthCount == 5
                            | minTCount == 5
                            | maxTCount == 5
                            | imgUrlCount == 5
                            | detailUrlCount == 5) {
                        if (result.substring(j, (j + 31)).equals("<a class=\"day-link\" data-link=\"")
                                & detailUrlCount == 5) {
                            for (int q = (j + 31); q < (j + 650); q++) {
                                if (result.substring(q, (q + 8)).equals("\" href=\"")
                                        & detailUrlCount == 5) {
                                    for (int qa = q; qa < (q + 200); qa++) {
                                        if (result.substring(qa, (qa + 2)).equals("\">")) {
                                            if (detailUrlCount == 5) {
                                                String urlDetail = result.substring((q + 8), qa);
                                                Log.i(TAG, "URL detail: " + urlDetail);
                                                itemWeater.urlDetail = urlDetail;
                                            }
                                            for (int qb = (qa + 2); qb < (qa + 22); qb++) {
                                                if (result.substring(qb, (qb + 8)).equals("</a></p>")) {
                                                    String nameDay = result.substring((qa + 2), qb);
                                                    Log.i(TAG, "Day name: " + nameDay);
                                                    itemWeater.dayName = nameDay;
                                                    detailUrlCount = 6;
                                                    detailUrl = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if ((result.substring(j, (j + 17)).equals("<p class=\"date \">")
                                & dayCount == 5) |
                                (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")
                                        & dayCount == 5)) {
                            //Log.d(TAG, "process 3");
                            if (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")) {
                                itemWeater.isFreeDay = true;
                            } else {
                                itemWeater.isFreeDay = false;
                            }
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                ////Log.d(TAG, "process 4");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String n = result.substring((j + 17), t);
                                    int dd = 0;
                                    try {
                                        dd = Integer.parseInt(n);
                                    } catch (Exception e) {
                                        dd = Integer.parseInt(result.substring((j + 25), t));
                                    }
                                    itemWeater.day = dd;
                                    Log.i(TAG, "MONDAY DAY " + dd);
                                    dayCount = 6;
                                    D = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<p class=\"month\">")
                                & monthCount == 5) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 5");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.month = m;
                                    Log.i(TAG, "MONDAY MONTH " + m);
                                    monthCount = 6;
                                    M = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"min\">")
                                & minTCount == 5) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 6");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.minTemp = m;
                                    Log.i(TAG, "MONDAY MINT " + m);
                                    minTCount = 6;
                                    MI = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"max\">")
                                & maxTCount == 5) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 7");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.maxTemp = m;
                                    Log.i(TAG, "MONDAY MAXT " + m);
                                    maxTCount = 6;
                                    MA = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 29)).equals("<img class=\"weatherImg\" src=\"")
                                & imgUrlCount == 5) {
                            for (int t = (j - 120); t < j; t++) {
                                if (result.substring(t, (t + 9)).equals("\" title=\"")) {
                                    for (int p = (t + 9); p < (t + 120); p++) {
                                        if (result.substring(p, (p + 14)).equals("\"><img class=\"")) {
                                            String nameWea = result.substring((t + 9), p);
                                            Log.i(TAG, "Name weather: " + nameWea);
                                            itemWeater.weatherName = nameWea;
                                        }
                                    }
                                }
                            }
                            for (int t = (j + 29); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 8");
                                if (result.substring(t, (t + 5)).equals("\" alt")) {
                                    String urlI = result.substring((j + 29), t);
                                    itemWeater.urlImage = urlI;
                                    Log.i(TAG, "MONDAY urlImage " + urlI);
                                    imgUrlCount = 6;
                                    imgUrl = true;
                                    break;
                                }
                            }
                        }
                        if (D & M & MI & MA & imgUrl & detailUrl
                                & dayCount == 6
                                & monthCount == 6
                                & minTCount == 6
                                & maxTCount == 6
                                & imgUrlCount == 6
                                & detailUrlCount == 6) {
                            Log.d(TAG, "ItemWeather 6 SUCCESS");
                            wRes.setWeatherSaturday(itemWeater);
                            D = false;
                            M = false;
                            MI = false;
                            MA = false;
                            imgUrl = false;
                            detailUrl = false;
                            itemWeater = new ItemWeather();
                        }
                    } else if (dayCount == 6
                            | monthCount == 6
                            | minTCount == 6
                            | maxTCount == 6
                            | imgUrlCount == 6
                            | detailUrlCount == 6) {
                        if (result.substring(j, (j + 31)).equals("<a class=\"day-link\" data-link=\"")
                                & detailUrlCount == 6) {
                            for (int q = (j + 31); q < (j + 650); q++) {
                                if (result.substring(q, (q + 8)).equals("\" href=\"")
                                        & detailUrlCount == 6) {
                                    for (int qa = q; qa < (q + 200); qa++) {
                                        if (result.substring(qa, (qa + 2)).equals("\">")) {
                                            if (detailUrlCount == 6) {
                                                String urlDetail = result.substring((q + 8), qa);
                                                Log.i(TAG, "URL detail: " + urlDetail);
                                                itemWeater.urlDetail = urlDetail;
                                            }
                                            for (int qb = (qa + 2); qb < (qa + 22); qb++) {
                                                if (result.substring(qb, (qb + 8)).equals("</a></p>")) {
                                                    String nameDay = result.substring((qa + 2), qb);
                                                    Log.i(TAG, "Day name: " + nameDay);
                                                    itemWeater.dayName = nameDay;
                                                    detailUrlCount = 7;
                                                    detailUrl = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if ((result.substring(j, (j + 17)).equals("<p class=\"date \">")
                                & dayCount == 6) |
                                (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")
                                        & dayCount == 6)) {
                            //Log.d(TAG, "process 3");
                            if (result.substring(j, (j + 25)).equals("<p class=\"date dateFree\">")) {
                                itemWeater.isFreeDay = true;
                            } else {
                                itemWeater.isFreeDay = false;
                            }
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                ////Log.d(TAG, "process 4");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String n = result.substring((j + 17), t);
                                    int dd = 0;
                                    try {
                                        dd = Integer.parseInt(n);
                                    } catch (Exception e) {
                                        dd = Integer.parseInt(result.substring((j + 25), t));
                                    }
                                    itemWeater.day = dd;
                                    Log.i(TAG, "MONDAY DAY " + dd);
                                    dayCount = 7;
                                    D = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<p class=\"month\">")
                                & monthCount == 6) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 5");
                                if (result.substring(t, (t + 4)).equals("</p>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.month = m;
                                    Log.i(TAG, "MONDAY MONTH " + m);
                                    monthCount = 7;
                                    M = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"min\">")
                                & minTCount == 6) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 6");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.minTemp = m;
                                    Log.i(TAG, "MONDAY MINT " + m);
                                    minTCount = 7;
                                    MI = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 17)).equals("<div class=\"max\">")
                                & maxTCount == 6) {
                            for (int t = (j + 17); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 7");
                                if (result.substring(t, (t + 6)).equals("</div>")) {
                                    String m = result.substring((j + 17), t);
                                    itemWeater.maxTemp = m;
                                    Log.i(TAG, "MONDAY MAXT " + m);
                                    maxTCount = 7;
                                    MA = true;
                                    break;
                                }
                            }
                        }
                        if (result.substring(j, (j + 29)).equals("<img class=\"weatherImg\" src=\"")
                                & imgUrlCount == 6) {
                            for (int t = (j - 120); t < j; t++) {
                                if (result.substring(t, (t + 9)).equals("\" title=\"")) {
                                    for (int p = (t + 9); p < (t + 120); p++) {
                                        if (result.substring(p, (p + 14)).equals("\"><img class=\"")) {
                                            String nameWea = result.substring((t + 9), p);
                                            Log.i(TAG, "Name weather: " + nameWea);
                                            itemWeater.weatherName = nameWea;
                                        }
                                    }
                                }
                            }
                            for (int t = (j + 29); t < (result.length() - 100); t++) {
                                //Log.d(TAG, "process 8");
                                if (result.substring(t, (t + 5)).equals("\" alt")) {
                                    String urlI = result.substring((j + 29), t);
                                    itemWeater.urlImage = urlI;
                                    Log.i(TAG, "MONDAY urlImage " + urlI);
                                    imgUrlCount = 7;
                                    imgUrl = true;
                                    break;
                                }
                            }
                        }
                        if (D & M & MI & MA & imgUrl & detailUrl
                                & dayCount == 7
                                & monthCount == 7
                                & minTCount == 7
                                & maxTCount == 7
                                & imgUrlCount == 7
                                & detailUrlCount == 7) {
                            Log.d(TAG, "ItemWeather 7 SUCCESS");
                            wRes.setWeatherSundey(itemWeater);
                            D = false;
                            M = false;
                            MI = false;
                            MA = false;
                            imgUrl = false;
                            detailUrl = false;
                            itemWeater = new ItemWeather();
                        }
                    }
                }
            }

            // GET CURRENT TEMP
            if (result.substring(i, (i + 22)).equals("<p class=\"today-temp\">")) {
                for (int j = (i + 22); j < (result.length() - 100); j++) {
                    if (result.substring(j, (j + 4)).equals("</p>")) {
                        wRes.setWeatherToday(result.substring((i + 22), (j)));
                        break;
                    }
                }
            }

            // GET SPEED WIND if !NULL
            if (result.substring(i, (i + 32)).equals("<div class=\"oWarnings clearfix\">")) {
                for (int j = (i + 32); j < (result.length() - 100); j++) {
                    if (result.substring(j, (j + 89)).equals("<div class=\"description ico-stormWarning-3 ico-stormWarning-wide ico-stormWarning-short\">")) {
                        for (int t = (j + 89); t < (result.length() - 100); t++) {
                            if (result.substring(t, (t + 6)).equals("</div>")) {
                                String descrWind = result.substring((j + 89), t);
                                wRes.setWindDescription(descrWind);
                                wRes.setWerningWind(true);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return wRes;
    }

    public List<ItemTown> parserTowns(String data){
		Log.d(TAG, data);
		List<ItemTown> result = new ArrayList<ItemTown>();
		int param = 0;
		int lastP = 0;
		ItemTown item = new ItemTown();
		for(int i = 0; i < (data.length()-2); i++){
			if(data.substring(i, (i+1)).equals("|") | data.substring(i, (i+1)).equals("\n")){
				if(param == 0){
					String town = data.substring(lastP, i);
					item.setNameTown(town);
				}
				if(param == 1){
					String detailLocation = data.substring(lastP, i);
					item.setDetailLocation(detailLocation);
				}
				if(param == 2){
					String urlEnd = data.substring(lastP, i);
					item.setUrlEndTown(urlEnd);
				}
				lastP = (i+1);
				param++;
				if(param == 3){
					param = 0;
					result.add(item);
					item = new ItemTown();
				}
			}
		}
		Log.i(TAG, "##################### END PARSE - length " + result.size());
		return result;
	}
	
	public ItemTown parserGetLocation(String json){
		ItemTown result = new ItemTown();
		try {
			JSONObject objectAll = new JSONObject(json);
			JSONArray data = objectAll.getJSONArray("results");
			JSONObject townJson = data.getJSONObject(1);
			JSONArray addressComp = townJson.getJSONArray("address_components");
			JSONObject townNameJson = addressComp.getJSONObject(2);
			String townName = townNameJson.getString("long_name");
			result.setNameTown(townName);
		} catch (JSONException e) {
			e.printStackTrace();
            return null;
		}
		return result;
	}
}
