package ua.setcom.testapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import ua.setcom.widgets.view.ThermometerView;


public class TestMainActivity extends ActionBarActivity {

    private ThermometerView thermometerView, thermometerView2, thermometerView3;
    private SeekBar sbTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        thermometerView = (ThermometerView) findViewById(R.id.v_termometer);
        thermometerView.updateTemperature(0, 40, 30);
        thermometerView2 = (ThermometerView) findViewById(R.id.v_thermometer2);
        thermometerView2.updateTemperature(0, 40, 40);
        thermometerView3 = (ThermometerView) findViewById(R.id.v_thermometer3);
        thermometerView3.updateTemperature(0, 20, 40);

        sbTemp = (SeekBar) findViewById(R.id.sb_temp);
        final TextView tvProgress = (TextView) findViewById(R.id.tv_progress);
        sbTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int p = progress;
                if (progress == 80) {
                    progress = 40;
                } else
                if(progress>40)
                    progress = progress%40;
                else
                    progress = progress-40;
                tvProgress.setText(String.valueOf(p)+ " | " + progress);
                thermometerView.updateTemperature((float)progress, thermometerView.getMaxTemp(), thermometerView.getMinTemp());
                thermometerView2.updateTemperature((float)progress, thermometerView2.getMaxTemp(), thermometerView2.getMinTemp());
                thermometerView3.updateTemperature((float)progress, thermometerView3.getMaxTemp(), thermometerView3.getMinTemp());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
