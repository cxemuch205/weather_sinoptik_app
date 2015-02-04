package ua.maker.sinopticua;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import ua.maker.sinopticua.adapters.WeatherItemDetailPagerAdapter;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.models.ItemDetail;
import ua.maker.sinopticua.models.ItemWeather;
import ua.maker.sinopticua.utils.DataParser;
import ua.maker.sinopticua.utils.Tools;
import ua.setcom.widgets.view.SlidingTabLayout;

public class WeatherDetailActivity extends ActionBarActivity {
	
	private static final String TAG = "WeatherDetailActivity";
	
	private ItemWeather weather;
    private ViewPager viewPager;
    private WeatherItemDetailPagerAdapter weatherPagerAdapter;
    private SlidingTabLayout tabSlidingTitle;
    private ProgressBar pb;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_weather_layout);
		weather = (ItemWeather) getIntent().getExtras().get(App.SAVE_ITEM_WEATHER);
        setTitleActionBar(Uri.parse(weather.urlDetail).getLastPathSegment());
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabSlidingTitle = (SlidingTabLayout) findViewById(R.id.title_tab);
        pb = (ProgressBar) findViewById(R.id.pb_load);
        if(weather != null && putHttp(weather))
            prepareLoadData();
    }

    private void setTitleActionBar(String date) {
        setTitle(getString(R.string.weather) + " " + date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        enablePb(true);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Document document = Jsoup.parse(Tools.getWebPage(weather.urlDetail));
                    ArrayList<ItemDetail> listItemsDetail = DataParser.parseDetailInfo(document);
                    setupData(listItemsDetail);
                    enablePb(false);
                }
            }).start();
        } catch (Exception e) {
            enablePb(false);
        }
    }

    private void enablePb(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (enable) {
                    pb.setVisibility(ProgressBar.VISIBLE);
                } else {
                    pb.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }

    private void setupData(final ArrayList<ItemDetail> list) {
        if (list != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    weatherPagerAdapter = new WeatherItemDetailPagerAdapter(WeatherDetailActivity.this, list);
                    viewPager.setAdapter(weatherPagerAdapter);
                    tabSlidingTitle.setCustomTabView(R.layout.item_tab, R.id.title_text);
                    tabSlidingTitle.setSelectedIndicatorColors(new int[]{Color.parseColor("#c3c3c3")});
                    tabSlidingTitle.setViewPager(viewPager);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
