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
import ua.setcom.widgets.view.SlidingTabLayout;

public class WeatherDetailActivity extends ActionBarActivity {
	
	private static final String TAG = "WeatherDetailActivity";
	
	private ItemWeather weather;
    private ViewPager viewPager;
    private WeatherItemDetailPagerAdapter weatherPagerAdapter;
    private SlidingTabLayout tabSlidingTitle;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_weather_layout);
		weather = (ItemWeather) getIntent().getExtras().get(App.SAVE_ITEM_WEATHER);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabSlidingTitle = (SlidingTabLayout) findViewById(R.id.title_tab);
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
                        try {
                            String data = element.text();
                            listItemsDetail.get(count).dayTime.add(data);
                            if (listItemsDetail.get(count).dayTime.size() == 2) {
                                count++;
                            }
                        } catch (Exception e) {}
                    }

                    if (iterator == 1) {
                        try {
                            String data = element.child(0).getElementsByClass("weatherImg").attr("src");
                            data = "https:" + data;
                            listItemsDetail.get(count).imageWeather.add(data);
                            if (listItemsDetail.get(count).imageWeather.size() == 2) {
                                count++;
                            }
                        } catch (Exception e) {}
                    }

                    if (iterator == 2) {
                        try {
                            String data = element.text();
                            listItemsDetail.get(count).temperature.add(data);
                            if (listItemsDetail.get(count).temperature.size() == 2) {
                                count++;
                            }
                        } catch (Exception e) {}
                    }

                    if (iterator == 3) {
                        try {
                            String data = element.text();
                            listItemsDetail.get(count).temperatureFell.add(data);
                            if (listItemsDetail.get(count).temperatureFell.size() == 2) {
                                count++;
                            }
                        } catch (Exception e) {}
                    }

                    if (iterator == 4) {
                        try {
                            String data = element.text();
                            listItemsDetail.get(count).pressure.add(data);
                            if (listItemsDetail.get(count).pressure.size() == 2) {
                                count++;
                            }
                        } catch (Exception e) {}
                    }

                    if (iterator == 5) {
                        try {
                            String data = element.text();
                            listItemsDetail.get(count).humidity.add(data);
                            if (listItemsDetail.get(count).humidity.size() == 2) {
                                count++;
                            }
                        } catch (Exception e) {}
                    }

                    if (iterator == 6) {
                        try {
                            String data = element.text();
                            String className = element.child(0).attr("data-tooltip");
                            listItemsDetail.get(count).winds.add(new Wind(null,
                                    data,
                                    className));
                            if (listItemsDetail.get(count).winds.size() == 2) {
                                count++;
                            }
                        } catch (Exception e) {}
                    }

                    if (iterator == 7) {
                        try {
                            String data = element.text();
                            listItemsDetail.get(count).chanceOfPrecipitation.add(data);
                            if (listItemsDetail.get(count).chanceOfPrecipitation.size() == 2) {
                                count++;
                            }
                        } catch (Exception e) {}
                    }
                    if (count == 4) {
                        count = 0;
                        iterator++;
                    }
                }

                setupData(listItemsDetail);
            }
        }).start();
    }

    private void setupData(final ArrayList<ItemDetail> list) {
        if (list != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    weatherPagerAdapter = new WeatherItemDetailPagerAdapter(WeatherDetailActivity.this, list);
                    viewPager.setAdapter(weatherPagerAdapter);
                    tabSlidingTitle.setViewPager(viewPager);
                }
            });
        }
    }
}
