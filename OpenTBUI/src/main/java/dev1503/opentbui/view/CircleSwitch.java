package dev1503.opentbui.view;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class CircleSwitch extends FrameLayout {
    private static final int DEFAULT_SIZE_DP = 40;
    private static final int ANIMATION_DURATION = 200;

    private Paint circlePaint;
    private boolean isOn = false;
    private int onColor = Color.argb(0x20, 0, 0, 0);
    private View contentView;
    private OnSwitchListener switchListener;
    private ValueAnimator currentAnimator;
    private int currentColor = Color.TRANSPARENT;

    public interface OnSwitchListener {
        void onSwitchChanged(boolean isOn);
    }

    public CircleSwitch(Context context) {
        super(context);
        init();
    }

    public CircleSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(isOn ? onColor : Color.TRANSPARENT);
        currentColor = circlePaint.getColor();

        // 设置点击监听
        setOnClickListener(v -> {
            toggle();
            if (switchListener != null) {
                switchListener.onSwitchChanged(isOn);
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // 先绘制圆形背景
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // 然后绘制子View
        super.dispatchDraw(canvas);
    }

    public void setOn(boolean on) {
        if (isOn != on) {
            isOn = on;
            updateCircleColorWithAnimation();
        }
    }

    public boolean isOn() {
        return isOn;
    }

    public void toggle() {
        setOn(!isOn);
    }

    public void setOnColor(int color) {
        this.onColor = color;
        if (isOn) {
            updateCircleColorWithAnimation();
        }
    }

    public int getOnColor() {
        return onColor;
    }

    public void setContentView(View view) {
        if (contentView != null) {
            removeView(contentView);
        }
        contentView = view;
        if (contentView != null) {
            // 确保内容View居中且不超过圆形范围
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = android.view.Gravity.CENTER;
            addView(contentView, params);
        }
    }

    public View getContentView() {
        return contentView;
    }

    public void setSwitchListener(OnSwitchListener listener) {
        this.switchListener = listener;
    }

    private void updateCircleColorWithAnimation() {
        // 取消之前的动画
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        int startColor = currentColor;
        int endColor = isOn ? onColor : Color.TRANSPARENT;

        currentAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        currentAnimator.setDuration(ANIMATION_DURATION);
        currentAnimator.addUpdateListener(animator -> {
            currentColor = (int) animator.getAnimatedValue();
            circlePaint.setColor(currentColor);
            invalidate();
        });
        currentAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 将默认尺寸从dp转换为px
        int defaultSizePx = (int) (DEFAULT_SIZE_DP * getResources().getDisplayMetrics().density + 0.5f);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int size;
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            // 两个尺寸都是精确的，取较小值保持正方形
            size = Math.min(widthSize, heightSize);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            // 只有宽度是精确的
            size = widthSize;
        } else if (heightMode == MeasureSpec.EXACTLY) {
            // 只有高度是精确的
            size = heightSize;
        } else {
            // 都不是精确的，使用默认尺寸
            size = defaultSizePx;
        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        );
    }
}