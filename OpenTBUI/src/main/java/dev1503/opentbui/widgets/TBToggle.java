package dev1503.opentbui.widgets;

import static dev1503.opentbui.FeaturesAdapter.dp2px;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import dev1503.opentbui.FeaturesAdapter;
import dev1503.opentbui.R;

public class TBToggle extends TBWidget {
    private static final long STATUS_VIEW_ANIM_TICK = 200; // 动画持续时间(毫秒)

    TextView textView;
    SwitchCompat switchCompat;
    LinearLayout container;
    View statusView;
    private boolean isStatusViewVisible = true;

    public TBToggle(Context context, String name, SwitchCompat.OnCheckedChangeListener onCheckedChangeListener) {
        super(context, name);
        container = (LinearLayout) LinearLayout.inflate(context, R.layout.list_toggle, null);
        container.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        ));
        view = new FrameLayout(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView = container.findViewWithTag("binding_1");
        switchCompat = container.findViewWithTag("binding_2");
        textView.setText(name);

        // 初始化状态视图为隐藏
        isStatusViewVisible = false;

        // 设置开关监听器，同步动画
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                statusViewSlideIn();
            } else {
                statusViewSlideOut();
            }
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
            }
        });

        statusView = new View(context);
        ViewGroup.MarginLayoutParams statusViewLayoutParams = new ViewGroup.MarginLayoutParams(
                dp2px(context, 3),
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        statusViewLayoutParams.setMargins(
                0,
                dp2px(context, 2.5f),
                0,
                dp2px(context, 2.5f)
        );
        statusView.setLayoutParams(statusViewLayoutParams);
        statusView.setBackgroundColor(Color.BLACK);
        statusView.setVisibility(View.INVISIBLE); // 初始设置为不可见

        view.addView(container);
        view.addView(statusView);
    }

    public TBToggle(Context context, String name) {
        this(context, name, null);
    }

    /**
     * 状态视图滑入动画
     */
    public void statusViewSlideIn() {
        if (isStatusViewVisible) return;

        statusView.clearAnimation();
        statusView.setVisibility(View.VISIBLE);

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1.0f, // 从左侧完全隐藏
                Animation.RELATIVE_TO_SELF, 0.0f,  // 滑动到原始位置
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );

        animation.setDuration(STATUS_VIEW_ANIM_TICK);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isStatusViewVisible = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        statusView.startAnimation(animation);
    }

    /**
     * 状态视图滑出动画
     */
    public void statusViewSlideOut() {
        if (!isStatusViewVisible) return;

        statusView.clearAnimation();

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,  // 从当前位置开始
                Animation.RELATIVE_TO_SELF, -1.0f, // 滑动到左侧隐藏
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );

        animation.setDuration(STATUS_VIEW_ANIM_TICK);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                statusView.setVisibility(View.INVISIBLE);
                isStatusViewVisible = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        statusView.startAnimation(animation);
    }

    public TBToggle setChecked(boolean checked) {
        switchCompat.setChecked(checked);
        // 同步状态视图
        if (checked) {
            statusViewSlideIn();
        } else {
            statusViewSlideOut();
        }
        return this;
    }

    public boolean isChecked() {
        return switchCompat.isChecked();
    }

    public TBToggle setOnCheckedChangeListener(SwitchCompat.OnCheckedChangeListener listener) {
        switchCompat.setOnCheckedChangeListener(listener);
        return this;
    }

    public SwitchCompat getSwitch() {
        return switchCompat;
    }

    public View getStatusView() {
        return statusView;
    }
}