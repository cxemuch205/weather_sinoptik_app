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
import android.widget.CheckBox;
import android.widget.EditText;

import ua.maker.sinopticua.Views.ColorPickerView;
import ua.maker.sinopticua.constants.App;
import ua.maker.sinopticua.widget.WidgetThermometerAppProvider;


public class WidgetConfigActivity extends ActionBarActivity {

    private ColorPickerView cpvTextColor;
    private SharedPreferences prefs;
    private Button btnApply;
    private EditText etMaxValue, etMinValue;
    private CheckBox cbEnableSubPoint;

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
        etMaxValue = (EditText) findViewById(R.id.et_max_value);
        etMinValue = (EditText) findViewById(R.id.et_min_value);
        cbEnableSubPoint = (CheckBox) findViewById(R.id.cb_enable_sub_point);

        cpvTextColor.setAlphaSliderVisible(true);

        prefs = getSharedPreferences(App.PREF_APP, Context.MODE_PRIVATE);

        cpvTextColor.setColor(prefs.getInt(App.PREF_TEXT_WIDGET_COLOR, Color.BLACK));
        etMaxValue.setText(String.valueOf(prefs.getInt(App.PREF_MAX_VALUE_THERMOMETER, (int) App.Thermometer.MAX)));
        etMinValue.setText(String.valueOf(prefs.getInt(App.PREF_MIN_VALUE_THERMOMETER, (int)App.Thermometer.MIN)));
        cbEnableSubPoint.setChecked(prefs.getBoolean(App.PREF_ENABLE_SUB_POINT, true));

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(
                        App.PREF_TEXT_WIDGET_COLOR, cpvTextColor.getColor())
                        .putBoolean(App.PREF_ENABLE_SUB_POINT, cbEnableSubPoint.isChecked())
                        .apply();

                if (etMaxValue.getText().length() > 0) {
                    editor.putInt(App.PREF_MAX_VALUE_THERMOMETER,
                            Integer.parseInt(etMaxValue.getText().toString())).apply();
                }

                if (etMinValue.getText().length() > 0) {
                    editor.putInt(App.PREF_MIN_VALUE_THERMOMETER,
                            Integer.parseInt(etMinValue.getText().toString())).apply();
                }

                resultValue.setAction(WidgetThermometerAppProvider.ACTION_UPDATE);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }
}
