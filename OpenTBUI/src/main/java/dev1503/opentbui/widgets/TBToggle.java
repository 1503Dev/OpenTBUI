package dev1503.opentbui.widgets;

import static dev1503.opentbui.FeaturesAdapter.dp2px;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev1503.opentbui.FeaturesAdapter;
import dev1503.opentbui.R;

public class TBToggle extends TBWidget {
    private static final long STATUS_VIEW_ANIM_TICK = 200;

    Context context;

    TextView textView;
    SwitchCompat switchCompat;
    LinearLayout toggleView;
    LinearLayout container;
    View statusView;
    boolean isStatusViewVisible = true;
    ValueAnimator currentAnimatorStatusView;
    ValueAnimator currentAnimatorItemsContainer;
    LinearLayout itemsContainer;

    List<TBWidget> items = new ArrayList<>();


    public TBToggle(Context context, String name, SwitchCompat.OnCheckedChangeListener onCheckedChangeListener) {
        super(context, name);
        this.context = context;
        toggleView = (LinearLayout) LinearLayout.inflate(context, R.layout.list_toggle, null);
        toggleView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        ));
        view = new FrameLayout(context);
        view.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView = toggleView.findViewWithTag("binding_1");
        switchCompat = toggleView.findViewWithTag("binding_2");
        textView.setText(name);

        isStatusViewVisible = false;

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                statusViewSlideIn();
                if (itemsContainer != null) {
                    fadeInItemsContainer();
                }
            } else {
                statusViewSlideOut();
                if (itemsContainer != null) {
                    fadeOutItemsContainer();
                }
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
        statusView.setVisibility(View.INVISIBLE);

        container = new LinearLayout(context);
        container.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        container.setOrientation(LinearLayout.VERTICAL);
        container.addView(toggleView);

        view.addView(container);
        view.addView(statusView);
    }

    public TBToggle(Context context, String name) {
        this(context, name, null);
    }

    public void statusViewSlideIn() {
        if (statusView == null) return;

        cancelCurrentAnimationStatusView();

        isStatusViewVisible = true;
        statusView.setVisibility(View.VISIBLE);

        currentAnimatorStatusView = ValueAnimator.ofFloat(-statusView.getWidth(), 0f);
        currentAnimatorStatusView.setDuration(STATUS_VIEW_ANIM_TICK);
        currentAnimatorStatusView.setInterpolator(new AccelerateDecelerateInterpolator());
        currentAnimatorStatusView.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            statusView.setTranslationX(value);
        });

        currentAnimatorStatusView.start();
    }

    public void statusViewSlideOut() {
        if (statusView == null) return;
        cancelCurrentAnimationStatusView();

        currentAnimatorStatusView = ValueAnimator.ofFloat(0f, -statusView.getWidth());
        currentAnimatorStatusView.setDuration(STATUS_VIEW_ANIM_TICK);
        currentAnimatorStatusView.setInterpolator(new AccelerateDecelerateInterpolator());
        currentAnimatorStatusView.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            statusView.setTranslationX(value);
        });

        currentAnimatorStatusView.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isStatusViewVisible = false;
                statusView.setVisibility(View.INVISIBLE);
                statusView.setTranslationX(0f);
            }
        });

        currentAnimatorStatusView.start();
    }

    private void cancelCurrentAnimationStatusView() {
        if (currentAnimatorStatusView != null) {
            currentAnimatorStatusView.cancel();
            currentAnimatorStatusView.removeAllUpdateListeners();
            currentAnimatorStatusView.removeAllListeners();
            currentAnimatorStatusView = null;
        }
    }

    private void cancelCurrentAnimationItemsContainer() {
        if (currentAnimatorItemsContainer != null) {
            currentAnimatorItemsContainer.cancel();
            currentAnimatorItemsContainer.removeAllUpdateListeners();
            currentAnimatorItemsContainer.removeAllListeners();
            currentAnimatorItemsContainer = null;
        }
    }

    public TBToggle setChecked(boolean checked) {
        switchCompat.setChecked(checked);
        if (checked) {
            statusViewSlideIn();
            if (itemsContainer != null) {
                fadeInItemsContainer();
            }
        } else {
            statusViewSlideOut();
            if (itemsContainer != null) {
                fadeOutItemsContainer();
            }
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
    public TBToggle setItems(List<TBWidget> items) {
        this.items = items;
        if (items != null && !items.isEmpty()) {
            initItemsContainer(true);
            for (TBWidget item : items) {
                itemsContainer.addView(item.getView());
            }
        }
        return this;
    }
    public TBToggle addItem(TBWidget item) {
        Log.d("TBToggle", "addingItem: " + item.name);
        if (items != null) {
            initItemsContainer(false);
            items.add(item);
            itemsContainer.addView(item.getView());
            Log.d("TBToggle", "addItem: " + item.name);
        }
        return this;
    }
    void initItemsContainer(boolean force) {
        if (itemsContainer == null || force) {
            if (force && itemsContainer != null) {
                itemsContainer.removeAllViews();
                container.removeView(itemsContainer);
            }
            itemsContainer = new LinearLayout(context);
            itemsContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            itemsContainer.setOrientation(LinearLayout.VERTICAL);
            itemsContainer.setVisibility(View.GONE);
            container.addView(itemsContainer);
        }
    }
    void fadeInItemsContainer() {
        if (itemsContainer == null) return;

        cancelCurrentAnimationItemsContainer();

        itemsContainer.setAlpha(0f);
        itemsContainer.setVisibility(View.VISIBLE);

        currentAnimatorItemsContainer = ValueAnimator.ofFloat(0f, 1f);
        currentAnimatorItemsContainer.setDuration(STATUS_VIEW_ANIM_TICK);
        currentAnimatorItemsContainer.setInterpolator(new AccelerateDecelerateInterpolator());
        currentAnimatorItemsContainer.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            itemsContainer.setAlpha(value);
        });

        currentAnimatorItemsContainer.start();
    }

    void fadeOutItemsContainer() {
        if (itemsContainer == null) return;

        cancelCurrentAnimationItemsContainer();

        currentAnimatorItemsContainer = ValueAnimator.ofFloat(1f, 0f);
        currentAnimatorItemsContainer.setDuration(STATUS_VIEW_ANIM_TICK);
        currentAnimatorItemsContainer.setInterpolator(new AccelerateDecelerateInterpolator());
        currentAnimatorItemsContainer.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            itemsContainer.setAlpha(value);
        });

        currentAnimatorItemsContainer.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                itemsContainer.setVisibility(View.GONE);
                itemsContainer.setAlpha(1f); // 重置透明度以便下次动画
            }
        });

        currentAnimatorItemsContainer.start();
    }

    public TBWidget[] getAllWidgetsAndSelf() {
        List<TBWidget> views = new ArrayList<>();
        views.add(this);
        for (TBWidget item : items) {
            if (item instanceof TBToggle) {
                views.addAll(Arrays.asList(((TBToggle) item).getAllWidgetsAndSelf()));
            } else {
                views.add(item);
            }
        }
        return views.toArray(new TBWidget[0]);
    }

    public TBWidget[] getAllTogglesAndSelf() {
        List<TBWidget> views = new ArrayList<>();
        views.add(this);
        for (TBWidget item : items) {
            if (item instanceof TBToggle) {
                views.addAll(Arrays.asList(((TBToggle) item).getAllTogglesAndSelf()));
            }
        }
        return views.toArray(new TBWidget[0]);
    }
}