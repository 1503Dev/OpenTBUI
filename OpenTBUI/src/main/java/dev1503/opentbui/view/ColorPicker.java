package dev1503.opentbui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ColorPicker extends View {
    private float hue;
    private float saturation;
    private float value;
    private Paint colorPaint;
    private RectF drawRect;
    private LinearGradient saturationGradient;
    private Paint saturationPaint;
    private LinearGradient valueGradient;
    private Paint valuePaint;
    private float cornerRadius;
    private Paint handlePaint;
    private Paint handleBorderPaint;
    private float handleRadius;
    private boolean isDragging;
    private float[] hsv = new float[3];
    private ColorHuePicker huePicker;
    private List<OnColorChangeListener> listeners;
    private int _w;

    public interface OnColorChangeListener {
        void onColorChanged(int color);
    }

    public ColorPicker(Context context) {
        this(context, null);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        hue = 0.0f;
        saturation = 1.0f;
        value = 1.0f;
        drawRect = new RectF();
        listeners = new ArrayList<>();

        colorPaint = new Paint();

        saturationPaint = new Paint();
        saturationPaint.setDither(true);

        valuePaint = new Paint();
        valuePaint.setDither(true);

        handlePaint = new Paint();

        handleBorderPaint = new Paint();
        handleBorderPaint.setStyle(Paint.Style.STROKE);
        handleBorderPaint.setStrokeWidth(dpToPx(3));

        handleRadius = dpToPx(8);
        cornerRadius = dpToPx(2);
    }

    public void setHuePicker(ColorHuePicker huePicker) {
        this.huePicker = huePicker;
        setHue(huePicker.getValue());
        huePicker.addOnValueChangeListener(new ColorHuePicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(float value) {
                setHue(value);
            }
        });
    }

    public void addOnColorChangeListener(OnColorChangeListener listener) {
        listeners.add(listener);
    }

    private float getHandleX() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) * saturation + getPaddingLeft();
    }

    private float getHandleY() {
        return (1.0f - value) * (getHeight() - getPaddingTop() - getPaddingBottom()) + getPaddingTop();
    }

    public int getColor() {
        hsv[0] = hue;
        hsv[1] = saturation;
        hsv[2] = value;
        return Color.HSVToColor(hsv);
    }

    public void setColor(int color) {
        Color.colorToHSV(color, hsv);
        saturation = hsv[1];
        value = hsv[2];

        if (huePicker != null) {
            huePicker.setValue(hsv[0]);
        } else {
            setHue(hsv[0]);
        }
    }

    public void setHue(float _hue) {
        hue = _hue;
        invalidate();
        notifyColorChanged();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Saturation gradient (left to right)
        _w = w;
        saturationGradient = new LinearGradient(
                getPaddingLeft(), 0,
                w - getPaddingRight(), 0,
                Color.WHITE, Color.HSVToColor(new float[]{hue, 1.0f, 1.0f}),
                Shader.TileMode.CLAMP
        );
        saturationPaint.setShader(saturationGradient);

        // Value gradient (top to bottom)
        valueGradient = new LinearGradient(
                0, getPaddingTop(),
                0, h - getPaddingBottom(),
                Color.TRANSPARENT, Color.BLACK,
                Shader.TileMode.CLAMP
        );
        valuePaint.setShader(valueGradient);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Update HSV array for background color
        hsv[0] = hue;
        hsv[1] = saturation;
        hsv[2] = value;
        colorPaint.setColor(Color.HSVToColor(hsv));
        Paint huePaint = new Paint();
        huePaint.setColor(Color.HSVToColor(new float[]{hsv[0], 255f, 255f}));

        saturationGradient = new LinearGradient(
                getPaddingLeft(), 0,
                _w - getPaddingRight(), 0,
                Color.WHITE, Color.HSVToColor(new float[]{hue, 1.0f, 1.0f}),
                Shader.TileMode.CLAMP
        );
        saturationPaint.setShader(saturationGradient);


        // Draw background
        drawRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        canvas.drawRoundRect(drawRect, cornerRadius, cornerRadius, huePaint);

        // Draw saturation gradient
        canvas.drawRoundRect(drawRect, cornerRadius, cornerRadius, saturationPaint);

        // Draw value gradient
        canvas.drawRoundRect(drawRect, cornerRadius, cornerRadius, valuePaint);

        // Draw handle
        float handleX = getHandleX();
        float handleY = getHandleY();
        int color = getColor();
        handlePaint.setColor(color);

        // Determine handle border color based on brightness
        if (Color.red(color) <= 140 || Color.green(color) <= 140 || Color.blue(color) <= 140) {
            handleBorderPaint.setColor(Color.WHITE);
        } else {
            handleBorderPaint.setColor(Color.BLACK);
        }

        canvas.drawCircle(handleX, handleY, handleRadius, handlePaint);
        canvas.drawCircle(handleX, handleY, handleRadius, handleBorderPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDragging = true;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                if (isDragging) {
                    float width = getWidth() - getPaddingLeft() - getPaddingRight();
                    float height = getHeight() - getPaddingTop() - getPaddingBottom();

                    saturation = (event.getX() - getPaddingLeft()) / width;
                    value = 1.0f - (event.getY() - getPaddingTop()) / height;

                    // Clamp values
                    saturation = Math.max(0.0f, Math.min(1.0f, saturation));
                    value = Math.max(0.0f, Math.min(1.0f, value));

                    invalidate();
                    notifyColorChanged();
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isDragging = false;
                }
                break;
        }

        return true;
    }

    private void notifyColorChanged() {
        int color = getColor();
        for (OnColorChangeListener listener : listeners) {
            listener.onColorChanged(color);
        }
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}