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

    public static final String TAG = "ThermometerView";

    private float maxTemp = 40f;
    private float minTemp = 25f;
    private float curTemp = 0f;

    private int STROKE_WIDTH = 4;

    private static final int MIN_WIDTH = 150;
    private static final int MIN_HEIGHT = 300;

    private Paint mPaintBar, mPaintCircle;
    private boolean showSubPoint = false;
    private int colorMercury = Color.RED;
    private int colorBackThermometer = Color.WHITE;
    private int colorLinesPoint = Color.BLACK;
    private float mTextSize = 15;
    private int colorText = Color.BLACK;
    private int posYMax, posYMin, posYZero;

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

    private void drawLineValue(Canvas canvas) {
        Paint paintLine = new Paint();
        paintLine.setStrokeWidth(2);
        paintLine.setColor(colorLinesPoint);

        Paint paintText = new Paint();
        paintText.setStrokeWidth(1);
        paintText.setColor(colorText);
        paintText.setTextSize(mTextSize);
        Paint paintSubText = new Paint(paintText);
        paintSubText.setTextSize(mTextSize-2);

        int width = getWidth();
        int height = getHeight();
        int center;

        if (width > height) {
            center = height / 2;
        } else {
            center = width / 2;
        }

        int startX = width - center;
        int stopX = width - Math.round((float) center / 1.8f);
        int stopXsub = width - Math.round((float) center / 1.4f);

        //Max temp line
        canvas.drawLine(startX, posYMax, stopX, posYMax, paintLine);
        canvas.drawText(String.valueOf(Math.round(maxTemp)), stopX, posYMax, paintText);
        if (showSubPoint) {
            int middlePosMax = posYZero - posYMax;
            middlePosMax = posYMax + (middlePosMax / 2);
            canvas.drawLine(startX, middlePosMax, stopXsub, middlePosMax, paintLine);
            canvas.drawText(String.valueOf(Math.round(maxTemp/2)), stopXsub, middlePosMax, paintSubText);
        }
        //Min temp line
        canvas.drawLine(startX, posYMin, stopX, posYMin, paintLine);
        canvas.drawText("-"+String.valueOf(Math.round(minTemp)), stopX, posYMin, paintText);
        if (showSubPoint) {
            int middlePosMin = posYMin - posYZero;
            middlePosMin = posYMin - (middlePosMin / 2);
            canvas.drawLine(startX, middlePosMin, stopXsub, middlePosMin, paintLine);
            canvas.drawText("-"+String.valueOf(Math.round(minTemp/2)), stopXsub, middlePosMin, paintSubText);
        }
        //Zero temp line
        canvas.drawLine(startX, posYZero, stopX, posYZero, paintLine);
        canvas.drawText(String.valueOf(0), stopX, posYZero, paintText);
    }

    private void drawTemperatureValue(Canvas canvas) {
        mPaintBar.setColor(colorMercury);
        int stroke = STROKE_WIDTH*2;
        mPaintBar.setStrokeWidth(stroke);

        int width = getWidth();
        int height = getHeight();
        int center;

        if (width > height) {
            center = height / 2;
        } else {
            center = width / 2;
        }

        int stopX = width - center;
        int stopY = height - center;
        int startX = stopX;
        int h = height - (int)(((float)height / 100f) * 10f);
        h = height - h;
        posYMax = h;
        int startY = calculateTemperature(height - center - h);
        startY += h;

        canvas.drawLine(startX, startY, stopX, stopY, mPaintBar);
        mPaintCircle.setColor(Color.RED);
        canvas.drawCircle(startX, startY, (stroke / 2), mPaintCircle);
    }

    private int calculateTemperature(int height) {
        posYMin = height;
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

        posYZero = h2 + posYMax;

        if(curTemp < (minTemp * (-1))) {
            Log.e(TAG, String.format("CURRENT TEMPERATURE[%d] < MIN TEMPERATURE[%d]", curTemp, minTemp));
            return height - posYMax;
        }
        if (curTemp > maxTemp) {
            Log.e(TAG, String.format("CURRENT TEMPERATURE[%d] > MAX TEMPERATURE[%d]", curTemp, maxTemp));
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
                    v = (((float)posYMin - posYZero) / Math.abs(minTemp));
                }

                int v2 = Math.round(curTemp * v);
                h2 = h2 - v2;
            } else {
                float v = ((float) (h2 - posYMax) / minTemp);
                int v2 = Math.round(curTemp * v);
                h2 = h2 - v2;
            }
        }
        return h2;
    }

    private void drawBackBarThermometer(Canvas canvas) {
        mPaintBar.setColor(colorBackThermometer);
        mPaintCircle.setColor(colorBackThermometer);
        int stroke = STROKE_WIDTH*6;
        mPaintBar.setStrokeWidth(stroke);

        int width = getWidth();
        int height = getHeight();
        int center;

        if (width > height) {
            center = height / 2;
        } else {
            center = width / 2;
        }

        int stopX = width - center;
        int stopY = height - center;
        int startX = stopX;
        int startY = height - (height - (int)(((float)height / 100f) * 10f));

        canvas.drawCircle(stopX, startY, (stroke / 2), mPaintCircle);
        canvas.drawLine(startX, startY, stopX, stopY, mPaintBar);
    }

    private void drawBaseThermometerCircle(Canvas canvas) {
        mPaintCircle.setColor(colorMercury);
        int width = getWidth();
        int height = getHeight();
        int radius, radiusBack, center;

        if (width > height) {
            radius = height / 7;
            radiusBack = height / 5;
            center = height / 2;
        } else {
            radius = width / 7;
            radiusBack = width / 5;
            center = width / 2;
        }
        mPaintCircle.setAntiAlias(true);
        Paint paintC = new Paint(mPaintCircle);
        paintC.setColor(colorBackThermometer);
        canvas.drawCircle(width - (center), height - (center), radiusBack, paintC);
        canvas.drawCircle(width - (center), height - (center), radius, mPaintCircle);
    }

    public void setShowSubPoint(boolean show) {
        this.showSubPoint = show;
    }

    public boolean isShowSubPoint() {
        return showSubPoint;
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

    public int getColorMercury() {
        return colorMercury;
    }

    public void setColorMercury(int colorMercury) {
        this.colorMercury = colorMercury;
    }

    public int getColorBackThermometer() {
        return colorBackThermometer;
    }

    public void setColorBackThermometer(int colorBackThermometer) {
        this.colorBackThermometer = colorBackThermometer;
    }

    public int getColorLinesPoint() {
        return colorLinesPoint;
    }

    public void setColorLinesPoint(int colorLinesPoint) {
        this.colorLinesPoint = colorLinesPoint;
    }

    public int getColorText() {
        return colorText;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }
}
