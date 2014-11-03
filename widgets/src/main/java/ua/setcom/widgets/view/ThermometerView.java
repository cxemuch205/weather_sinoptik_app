package ua.setcom.widgets.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by daniil on 11/3/14.
 */
public class ThermometerView extends View {

    private float maxTemp = 40f;
    private float minTemp = 25f;
    private float curTemp = 0f;

    private int STROKE_WIDTH = 4;

    private static final int MIN_WIDTH = 100;
    private static final int MIN_HEIGHT = 300;

    private Paint mPaintBar, mPaintCircle;

    public ThermometerView(Context context) {
        super(context);
        init();
    }

    public ThermometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThermometerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setMinimumWidth(MIN_WIDTH);
        setMinimumHeight(MIN_HEIGHT);
        mPaintBar = new Paint();
        mPaintBar.setStyle(Paint.Style.STROKE);
        mPaintCircle = new Paint();
        mPaintCircle.setStyle(Paint.Style.FILL);
    }

    public void setStrokeThermometerWidth(int width) {
        this.STROKE_WIDTH = width;
    }

    public int getStrokeThermometerWidth() {
        return STROKE_WIDTH;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getCurrentTemp() {
        return curTemp;
    }

    public void setCurrentTemp(float curTemp) {
        this.curTemp = curTemp;
    }

    public void updateTemperature(float currentTemp, float maxTemp, float minTemp) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.curTemp = currentTemp;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackBarThermometer(canvas);
        drawBaseThermometerCircle(canvas);
        drawTemperatureValue(canvas);
        drawLineValue(canvas);
    }

    private int posXMax, posXMin, posXZero;

    private void drawLineValue(Canvas canvas) {
        Paint paintLine = new Paint();
        paintLine.setStrokeWidth(2);
        paintLine.setColor(Color.BLACK);

        Paint paintText = new Paint();
        paintText.setStrokeWidth(1);
        paintText.setColor(Color.BLACK);

        int width = getWidth();
        int height = getHeight();
        int radius;

        if (width > height) {
            radius = height / 3;
        } else {
            radius = width / 3;
        }

        int startX = width - radius;
        int stopX = width - (radius / 2);

        //Max temp line
        canvas.drawLine(startX, posXMax, stopX, posXMax, paintLine);
        canvas.drawText(String.valueOf(maxTemp), stopX, posXMax, paintText);
        //Min temp line
        canvas.drawLine(startX, posXMin, stopX, posXMin, paintLine);
        canvas.drawText(String.valueOf(minTemp), stopX, posXMin, paintText);
        //Zero temp line
        canvas.drawLine(startX, posXZero, stopX, posXZero, paintLine);
        canvas.drawText(String.valueOf(0), stopX, posXZero, paintText);
    }

    private void drawTemperatureValue(Canvas canvas) {
        mPaintBar.setColor(Color.RED);
        int stroke = STROKE_WIDTH*2;
        mPaintBar.setStrokeWidth(stroke);

        int width = getWidth();
        int height = getHeight();
        int radius;

        if (width > height) {
            radius = height / 3;
        } else {
            radius = width / 3;
        }

        int stopX = width - radius;//width - radius;//(int)(((float)width / 100f) * 20f);
        int stopY = height - radius;//(int)(((float)height / 100f) * 10f);
        int startX = stopX;
        int h = height - (int)(((float)height / 100f) * 10f);
        h = height - h;
        posXMax = h;
        int startY = calculateTemperature(height - radius - h);//height - stopY;
        startY += h;

        canvas.drawLine(startX, startY, stopX, stopY, mPaintBar);
        mPaintCircle.setColor(Color.RED);
        canvas.drawCircle(startX, startY, (stroke / 2), mPaintCircle);
    }

    private int calculateTemperature(int height) {
        posXMin = height;
        float different;
        if(maxTemp > minTemp)
            different = maxTemp / minTemp;
        else
            different = minTemp / maxTemp;

        int h2 = height / 2;
        if (different > 1f) {
            if(maxTemp > minTemp)
                h2 = height - Math.round(h2 / different); // is zero temp
            else
                h2 = Math.round((float)height / different) - Math.round(h2 / different); // is zero temp
        }

        posXZero = h2 + posXMax;

        if(curTemp < (minTemp * (-1))){
            return height - posXMax;
        }
        if (curTemp > maxTemp) {
            return 0;
        }

        if (curTemp >= 0) {
            h2 = h2 - Math.round((curTemp * ((float)h2 / maxTemp)));
        } else {
            if (different > 1f) {
                float v;
                if(maxTemp > minTemp)
                    v = ((float)h2 / Math.abs(minTemp+maxTemp));
                else {
                    v = (((float)posXMin - posXZero) / Math.abs(minTemp));
                    Log.i("RES", "v: " + v + " h2: " + h2 + " height: " + height);
                }

                int v2 = Math.round(curTemp * v);
                h2 = h2 - v2;
            } else {
                float v = ((float) (h2 - posXMax) / minTemp);
                int v2 = Math.round(curTemp * v);
                h2 = h2 - v2;
            }
        }
        return h2;
    }

    private void drawBackBarThermometer(Canvas canvas) {
        mPaintBar.setColor(Color.WHITE);
        mPaintCircle.setColor(Color.WHITE);
        int stroke = STROKE_WIDTH*6;
        mPaintBar.setStrokeWidth(stroke);

        int width = getWidth();
        int height = getHeight();
        int radius;

        if (width > height) {
            radius = height / 3;
        } else {
            radius = width / 3;
        }

        int stopX = width - radius;
        int stopY = height - (int)(((float)height / 100f) * 10f);
        int startX = stopX;
        int startY = height - stopY;

        canvas.drawCircle(stopX, startY, (stroke / 2), mPaintCircle);
        canvas.drawLine(startX, startY, stopX, stopY, mPaintBar);
    }

    private void drawBaseThermometerCircle(Canvas canvas) {
        mPaintCircle.setColor(Color.RED);
        int width = getWidth();
        int height = getHeight();
        int radius, radiusBack;

        if (width > height) {
            radius = height / 5;
            radiusBack = height / 3;
        } else {
            radius = width / 5;
            radiusBack = width / 3;
        }
        mPaintCircle.setAntiAlias(true);
        Paint paintC = new Paint(mPaintCircle);
        paintC.setColor(Color.WHITE);
        canvas.drawCircle(width - (radiusBack), height - (radiusBack), radiusBack, paintC);
        canvas.drawCircle(width - (radiusBack), height - (radiusBack), radius, mPaintCircle);
    }
}
