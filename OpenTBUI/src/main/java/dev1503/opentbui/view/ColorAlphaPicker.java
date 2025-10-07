package dev1503.opentbui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

public class ColorAlphaPicker extends BaseSliderView {
    private Bitmap checkerboardBitmap;
    private Paint checkerboardPaint;
    private RectF drawRect;
    private float checkerSize;
    private LinearGradient alphaGradient;
    private Paint alphaPaint;
    private int currentColor;

    public ColorAlphaPicker(Context context) {
        this(context, null);
    }

    public ColorAlphaPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorAlphaPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawRect = new RectF();
        checkerboardPaint = new Paint();
        checkerSize = dpToPx(4);

        // Create checkerboard pattern
        int size = (int) (checkerSize * 2);
        checkerboardBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(checkerboardBitmap);

        Paint paint = new Paint();
        paint.setColor(0xFFDDDDDD); // Light gray
        canvas.drawRect(0, 0, checkerSize, checkerSize, paint);
        canvas.drawRect(checkerSize, checkerSize, size, size, paint);

        paint.setColor(0xFFAAAAAA); // Dark gray
        canvas.drawRect(checkerSize, 0, size, checkerSize, paint);
        canvas.drawRect(0, checkerSize, checkerSize, size, paint);

        Shader shader = new BitmapShader(
                checkerboardBitmap,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT
        );
        checkerboardPaint.setShader(shader);

        alphaPaint = new Paint();
        alphaPaint.setDither(true);
        setColor(Color.WHITE);
    }

    @Override
    protected void drawBackground(Canvas canvas) {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        // Draw checkerboard
        drawRect.set(0, 0, width, height);
        canvas.drawRoundRect(drawRect, cornerRadius, cornerRadius, checkerboardPaint);

        // Draw alpha gradient
        alphaPaint.setColor(Color.RED); // Color will be modified by color filter
        canvas.drawRoundRect(drawRect, cornerRadius, cornerRadius, alphaPaint);

        canvas.restore();
    }

    @Override
    protected int getHandleFillColor() {
        return currentColor;
    }

    @Override
    protected float getMaxValue() {
        return 0.0f;
    }

    @Override
    protected float getMinValue() {
        return 1.0f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        alphaGradient = new LinearGradient(
                0, getPaddingTop(),
                0, h - getPaddingBottom(),
                Color.TRANSPARENT, Color.BLACK,
                Shader.TileMode.CLAMP
        );
        alphaPaint.setShader(alphaGradient);
    }

    public void setColor(int color) {
        currentColor = color | 0xFF000000; // Ensure opaque
        alphaPaint.setColorFilter(new PorterDuffColorFilter(
                currentColor,
                PorterDuff.Mode.MULTIPLY
        ));
        invalidate();
    }
}