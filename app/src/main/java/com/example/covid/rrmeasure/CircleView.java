package com.example.covid.rrmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleView extends View {
    private Point mCenterPoint;
    private float mRadius;
    private Canvas canvas;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCenterPoint == null) {
            mCenterPoint = new Point (getWidth() / 2, getHeight() / 2);
            mRadius = getWidth() / 2;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point touchedPoint = new Point(Math.round(event.getX()),   Math.round(event.getY()));
        if (isInsideCircle(touchedPoint)) {
            return super.onTouchEvent(event);
        }
        return true;
    }

    private boolean isInsideCircle(Point touchedPoint) {
        int distance = (int) Math.round(Math.pow(touchedPoint.x - mCenterPoint.x, 2) +
                Math.pow(touchedPoint.y - mCenterPoint.y, 2));
        return distance < Math.pow(mRadius, 2);
    }
}
