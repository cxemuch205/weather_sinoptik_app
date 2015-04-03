package ua.maker.sinopticua;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ua.maker.sinopticua.Views.ColorPickerView;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.widget.WidgetThermometerAppProvider;


public class WidgetConfigActivity extends ActionBarActivity {

    private ColorPickerView cpvTextColor;
    private SharedPreferences prefs;
    private Button btnApply;

    private int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent resultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.activity_widget_config);
        cpvTextColor = (ColorPickerView) findViewById(R.id.cpv_color_text);
        btnApply = (Button) findViewById(R.id.btn_apply_text_color);

        cpvTextColor.setAlphaSliderVisible(true);

        prefs = getSharedPreferences(App.PREF_APP, Context.MODE_PRIVATE);

        cpvTextColor.setColor(prefs.getInt(App.PREF_TEXT_WIDGET_COLOR, Color.BLACK));

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putInt(
                        App.PREF_TEXT_WIDGET_COLOR, cpvTextColor.getColor())
                        .apply();
                resultValue.setAction(WidgetThermometerAppProvider.ACTION_UPDATE);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }
}
