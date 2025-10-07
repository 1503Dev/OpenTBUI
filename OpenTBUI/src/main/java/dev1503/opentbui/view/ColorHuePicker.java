package dev1503.opentbui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

public class ColorHuePicker extends BaseSliderView {
    private Bitmap hueBitmap;
    private Paint huePaint;
    private float[] hsv = new float[3];
    private Rect srcRect;
    private RectF dstRect;

    public ColorHuePicker(Context context) {
        this(context, null);
    }

    public ColorHuePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorHuePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        huePaint = new Paint();
        srcRect = new Rect();
        dstRect = new RectF();
    }

    @Override
    protected void drawBackground(Canvas canvas) {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        if (hueBitmap == null || hueBitmap.getHeight() != height) {
            createHueBitmap(height);
        }

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        dstRect.set(0, 0, width, height);
        canvas.drawRoundRect(dstRect, cornerRadius, cornerRadius, huePaint);

        canvas.restore();
    }

    private void createHueBitmap(int height) {
        if (hueBitmap != null) {
            hueBitmap.recycle();
        }

        hueBitmap = Bitmap.createBitmap(1, height, Bitmap.Config.ARGB_8888);
        hsv[1] = 1.0f;
        hsv[2] = 1.0f;

        for (int y = 0; y < height; y++) {
            hsv[0] = (y / (float) (height - 1)) * 360.0f;
            hueBitmap.setPixel(0, y, Color.HSVToColor(hsv));
        }

        Shader shader = new BitmapShader(hueBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        huePaint.setShader(shader);
        srcRect.set(0, 0, 1, height);
    }

    public int getColorValue() {
        hsv[0] = getValue();
        hsv[1] = 1.0f;
        hsv[2] = 1.0f;
        return Color.HSVToColor(hsv);
    }

    @Override
    protected int getHandleFillColor() {
        return getColorValue();
    }

    @Override
    protected float getMaxValue() {
        return 360.0f;
    }
}