package ua.maker.sinopticua;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import ua.maker.sinopticua.adapters.WeatherItemDetailPagerAdapter;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.models.ItemDetail;
import ua.maker.sinopticua.models.ItemWeather;
import ua.maker.sinopticua.models.Wind;
import ua.maker.sinopticua.utils.Tools;

public class WeatherDetailActivity extends ActionBarActivity {
	
	private static final String TAG = "WeatherDetailActivity";
	
	private ItemWeather weather;
    private ViewPager viewPager;
    private WeatherItemDetailPagerAdapter weatherPagerAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_weather_layout);
		weather = (ItemWeather) getIntent().getExtras().get(App.SAVE_ITEM_WEATHER);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        if(weather != null && putHttp(weather))
            prepareLoadData();
    }

    private boolean putHttp(ItemWeather weather) {
        try {
            weather.urlDetail = "http:" + weather.urlDetail;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void prepareLoadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = Jsoup.parse(Tools.getWebPage(weather.urlDetail));
                Elements table = document.getElementsByClass("weatherDetails");
                Elements header = table.select("thead");
                Elements body = table.select("tbody");

                ArrayList<ItemDetail> listItemsDetail = new ArrayList<ItemDetail>();
                for (int i = 0; i < 4; i++) {
                    listItemsDetail.add(new ItemDetail());
                }
                int count = 0;
                Elements tdHeader = header.select("tr td");
                for (Element element : tdHeader) {
                    Log.d(TAG, "header\t" + element.text());
                    listItemsDetail.get(count).dayStage = element.text();
                    count++;
                    if (count == 4) {
                        count = 0;
                        break;
                    }
                }

                int iterator = 0;
                Elements tdBody = body.select("tr td");
                for (Element element : tdBody) {
                    Log.d(TAG, "body\t" + element.text());
                    if (iterator == 0) {
                        listItemsDetail.get(count).dayTime.add(element.text());
                        if (count % 2 == 0) {
                            count++;
                        }
                        if (count == 4) {
                            count = 0;
                            iterator = 1;
                        }
                    }

                    if (iterator == 1) {
                        listItemsDetail.get(count).imageWeather.add(element.getElementsByClass("imgWeather").attr("src"));
                        if (count % 2 == 0) {
                            count++;
                        }
                        if (count == 4) {
                            count = 0;
                            iterator = 2;
                        }
                    }

                    if (iterator == 2) {
                        listItemsDetail.get(count).temperature.add(element.text());
                        if (count % 2 == 0) {
                            count++;
                        }
                        if (count == 4) {
                            count = 0;
                            iterator = 3;
                        }
                    }

                    if (iterator == 3) {
                        listItemsDetail.get(count).temperatureFell.add(element.text());
                        if (count % 2 == 0) {
                            count++;
                        }
                        if (count == 4) {
                            count = 0;
                            iterator = 4;
                        }
                    }

                    if (iterator == 4) {
                        listItemsDetail.get(count).pressure.add(element.text());
                        if (count % 2 == 0) {
                            count++;
                        }
                        if (count == 4) {
                            count = 0;
                            iterator = 5;
                        }
                    }
                }
                Elements huniditys = tdBody.get(iterator).getAllElements();
                if (huniditys != null) {
                    for (Element e : huniditys) {
                        listItemsDetail.get(count).humidity.add(e.text());
                        if (count % 2 == 0) {
                            count++;
                        }
                        if (count == 4) {
                            count = 0;
                        }
                    }
                }
                Elements winds = tdBody.get(iterator).getAllElements();
                if (winds != null) {
                    for (Element e : winds) {
                        listItemsDetail.get(count).winds.add(new Wind(null,
                                e.getAllElements().get(0).text(),
                                e.getAllElements().get(0).className()));
                        if (count % 2 == 0) {
                            count++;
                        }
                        if (count == 4) {
                            count = 0;
                        }
                    }
                }
                iterator++;
                Elements chanceOfPrecipitation = tdBody.get(iterator).children();
                if (chanceOfPrecipitation != null) {
                    for (Element e : chanceOfPrecipitation) {
                        listItemsDetail.get(count).chanceOfPrecipitation.add(e.text());
                        if (count % 2 == 0) {
                            count++;
                        }
                        if (count == 4) {
                            count = 0;
                        }
                    }
                }
            }
        }).start();
    }
}
