package dev1503.opentbui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSliderView extends View {
    private RectF handleRect;
    protected float currentValue;
    protected float cornerRadius;
    private Paint handleBorderPaint;
    private Paint handleFillPaint;
    private float handleHeight;
    private float handleCornerRadius;
    private boolean isSliding;
    private boolean isClick;
    private float touchStartX, touchStartY;
    private long touchStartTime;
    private float lastTouchY;
    private float touchSlop;
    private List<OnValueChangeListener> listeners;

    public interface OnValueChangeListener {
        void onValueChanged(float value);
    }

    public BaseSliderView(Context context) {
        this(context, null);
    }

    public BaseSliderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleRect = new RectF();
        currentValue = 0.0f;
        listeners = new ArrayList<>();

        // Initialize paints
        handleBorderPaint = new Paint();
        handleBorderPaint.setColor(Color.WHITE);
        handleBorderPaint.setStyle(Paint.Style.STROKE);
        handleBorderPaint.setStrokeWidth(dpToPx(3));

        handleFillPaint = new Paint();

        handleHeight = dpToPx(8);
        touchSlop = dpToPx(16);
        cornerRadius = dpToPx(2);
        handleCornerRadius = dpToPx(2);
    }

    protected abstract void drawBackground(Canvas canvas);

    protected int getHandleFillColor() {
        return Color.WHITE;
    }

    protected float getMinValue() {
        return 0.0f;
    }

    protected float getMaxValue() {
        return 1.0f;
    }

    public float getValue() {
        return currentValue;
    }

    public void setValue(float value) {
        float min = Math.min(getMinValue(), getMaxValue());
        float max = Math.max(getMinValue(), getMaxValue());
        currentValue = Math.min(Math.max(value, min), max);
        invalidate();

        for (OnValueChangeListener listener : listeners) {
            listener.onValueChanged(currentValue);
        }
    }

    public void addOnValueChangeListener(OnValueChangeListener listener) {
        listeners.add(listener);
    }

    private float getHandleY() {
        float availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        return getPaddingTop() + (currentValue - getMinValue()) * availableHeight / (getMaxValue() - getMinValue());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);

        float handleY = getHandleY();
        handleRect.set(
                getPaddingLeft(),
                handleY - handleHeight / 2,
                getWidth() - getPaddingRight(),
                handleY + handleHeight / 2
        );

        handleFillPaint.setColor(getHandleFillColor());
        canvas.drawRoundRect(handleRect, handleCornerRadius, handleCornerRadius, handleFillPaint);
        canvas.drawRoundRect(handleRect, handleCornerRadius, handleCornerRadius, handleBorderPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = event.getX();
                touchStartY = event.getY();
                touchStartTime = System.nanoTime();
                lastTouchY = touchStartY;
                isSliding = true;
                isClick = true;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - touchStartX) >= touchSlop ||
                        Math.abs(event.getY() - touchStartY) >= touchSlop) {
                    isClick = false;
                }

                if (isSliding) {
                    float availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
                    float delta = (event.getY() - lastTouchY) / availableHeight;
                    float range = getMaxValue() - getMinValue();
                    setValue(currentValue + delta * range);
                    lastTouchY = event.getY();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isClick && System.nanoTime() - touchStartTime < 100000000) {
                    float availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
                    float newValue = (event.getY() - getPaddingTop()) / availableHeight;
                    newValue = newValue * (getMaxValue() - getMinValue()) + getMinValue();
                    setValue(newValue);
                }

                getParent().requestDisallowInterceptTouchEvent(false);
                isSliding = false;
                break;
        }

        return true;
    }

    float dpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}