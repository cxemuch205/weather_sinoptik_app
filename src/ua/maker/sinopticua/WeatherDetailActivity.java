package ua.maker.sinopticua;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ua.maker.sinopticua.adapters.WeatherItemDetailPagerAdapter;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.models.ItemDetail;
import ua.maker.sinopticua.models.ItemWeather;
import ua.maker.sinopticua.utils.DataParser;
import ua.maker.sinopticua.utils.Tools;
import ua.setcom.widgets.view.SlidingTabLayout;

public class WeatherDetailActivity extends ActionBarActivity {
	
	private static final String TAG = "WeatherDetailActivity";
    private static final int SECOND_TO_RESTART = 5;
	
	private ItemWeather weather;
    private ViewPager viewPager;
    private WeatherItemDetailPagerAdapter weatherPagerAdapter;
    private SlidingTabLayout tabSlidingTitle;
    private ProgressBar pb;
    private TextView tvMessage;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_weather_layout);
		weather = (ItemWeather) getIntent().getExtras().get(App.SAVE_ITEM_WEATHER);
        setTitleActionBar(Uri.parse(weather.urlDetail).getLastPathSegment());
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabSlidingTitle = (SlidingTabLayout) findViewById(R.id.title_tab);
        pb = (ProgressBar) findViewById(R.id.pb_load);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        if(weather != null && putHttp(weather))
            prepareLoadData();
    }

    private void setTitleActionBar(String date) {
        setTitle(getString(R.string.weather) + " " + date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean putHttp(ItemWeather weather) {
        try {
            if (!Patterns.WEB_URL.matcher(weather.urlDetail).matches()) {
                weather.urlDetail = "https:" + weather.urlDetail;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void prepareLoadData() {
        enablePb(true);
        enableMsg(false, getString(R.string.err_downloading_detail_info), SECOND_TO_RESTART);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String page = Tools.getWebPage(weather.urlDetail);
                        Document document = Jsoup.parse(page);
                        ArrayList<ItemDetail> listItemsDetail = DataParser.parseDetailInfo(document);
                        setupData(listItemsDetail);
                        enablePb(false);
                    } catch (Exception e) {
                        Log.e(TAG, "CRASH #### " + e.getLocalizedMessage());
                        enableMsg(true, getString(R.string.err_downloading_detail_info), SECOND_TO_RESTART);
                        enablePb(false);
                    }
                }
            }).start();
        } catch (Exception e) {
            enablePb(false);
        }
    }

    private void enableMsg(final boolean enable, final String msg, final int secondsForRestart) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (msg != null) {
                    if (enable) {
                        tvMessage.setVisibility(TextView.VISIBLE);
                        if (secondsForRestart == -1) {
                            tvMessage.setText(msg);
                        } else {
                            new Timer().schedule(new TimerTask() {
                                int currentSecond = secondsForRestart;
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvMessage.setText(String.format(msg, currentSecond));
                                        }
                                    });
                                    currentSecond--;
                                    if (currentSecond == -1) {
                                        prepareLoadData();
                                        cancel();
                                    }
                                }
                            }, 0, 1000);
                        }
                    } else {
                        tvMessage.setVisibility(TextView.GONE);
                    }
                }
            }
        });
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
