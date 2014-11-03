package ua.maker.sinopticua.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import ua.maker.sinopticua.R;

/**
 * Created by daniil on 11/3/14.
 */
public class ThermometerView extends View {

    private double maxTemp = 40;
    private double minTemp = 25;
    private double curTemp = 0;

    private int mColor = Color.DEFAULT_RED;
    private int STROKE_WIDTH = 4;

    private static final int MIN_WIDTH = 100;
    private static final int MIN_HEIGHT = 300;

    private Paint mPaintBar, mPaintCircle;

    public interface Color {
        public static final int DEFAULT_RED = R.color.red;
        public static final int BLUE = R.color.blue;
    }

    public ThermometerView(Context context) {
        super(context);
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

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getCurrentTemp() {
        return curTemp;
    }

    public void setCurrentTemp(double curTemp) {
        this.curTemp = curTemp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaintBar.setColor(mColor);
        mPaintBar.setStrokeWidth(STROKE_WIDTH);

        mPaintCircle.setColor(mColor);
        int width = getWidth();
        int height = getHeight();
        int radius;

        if (width > height) {
            radius = height / 2;
        } else {
            radius = width / 2;
        }
        mPaintCircle.setAntiAlias(true);
        canvas.drawCircle(width, height, radius, mPaintCircle);
    }
}
