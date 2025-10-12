package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev1503.opentbui.picker.ColorPicker;
import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;

public class TBToggle extends TBWidget {
    private static final long STATUS_VIEW_ANIM_TICK = 200;

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


    public TBToggle(OpenTBUI openTBUI, String name, boolean isChecked, SwitchCompat.OnCheckedChangeListener onCheckedChangeListener) {
        super(openTBUI, name);
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

        isStatusViewVisible = !isChecked;
        switchCompat.setChecked(isChecked);

        switchCompat.setOnCheckedChangeListener((buttonView, _isChecked) -> {
            if (_isChecked) {
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
                onCheckedChangeListener.onCheckedChanged(buttonView, _isChecked);
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
        if (!isChecked) {
            statusView.setVisibility(View.INVISIBLE);
        }

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

    public TBToggle(OpenTBUI openTBUI, String name) {
        this(openTBUI, name, false, null);
    }
    public TBToggle(OpenTBUI openTBUI, String name, boolean isChecked) {
        this(openTBUI, name, isChecked, null);
    }
    public TBToggle(OpenTBUI openTBUI, String name, SwitchCompat.OnCheckedChangeListener onCheckedChangeListener) {
        this(openTBUI, name, false, onCheckedChangeListener);
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
        if (items != null) {
            initItemsContainer(false);
            items.add(item);
            itemsContainer.addView(item.getView());
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
            if (isChecked()) {
                fadeInItemsContainer();
            }
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
                itemsContainer.setAlpha(1f);
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

    public TBToggle addToggle(String name) {
        TBToggle toggle = new TBToggle(openTBUI, name);
        addItem(toggle);
        return toggle;
    }
    public TBToggle addToggle(String name, SwitchCompat.OnCheckedChangeListener onCheckedChangeListener) {
        TBToggle toggle = new TBToggle(openTBUI, name, onCheckedChangeListener);
        addItem(toggle);
        return toggle;
    }
    public TBToggle addToggle(String name, boolean isChecked) {
        TBToggle toggle = new TBToggle(openTBUI, name, isChecked);
        addItem(toggle);
        return toggle;
    }
    public TBToggle addToggle(String name, boolean isChecked, SwitchCompat.OnCheckedChangeListener onCheckedChangeListener) {
        TBToggle toggle = new TBToggle(openTBUI, name, isChecked, onCheckedChangeListener);
        addItem(toggle);
        return toggle;
    }

    public TBAction addAction(String name) {
        TBAction action = new TBAction(openTBUI, name);
        addItem(action);
        return action;
    }
    public TBAction addAction(String name, View.OnClickListener onClickListener) {
        TBAction action = new TBAction(openTBUI, name, onClickListener);
        addItem(action);
        return action;
    }

    public TBSlider addSlider(String name) {
        TBSlider slider = new TBSlider(openTBUI, name);
        addItem(slider);
        return slider;
    }
    public TBSlider addSlider(String name, TBSlider.OnValueChangeListener onValueChangeListener) {
        TBSlider slider = new TBSlider(openTBUI, name, onValueChangeListener);
        addItem(slider);
        return slider;
    }
    public TBSlider addSlider(String name, float[] values) {
        TBSlider slider = new TBSlider(openTBUI, name, values);
        addItem(slider);
        return slider;
    }
    public TBSlider addSlider(String name, float[] values, TBSlider.OnValueChangeListener onValueChangeListener) {
        TBSlider slider = new TBSlider(openTBUI, name, values, onValueChangeListener);
        addItem(slider);
        return slider;
    }

    public TBRangeSlider addRangeSlider(String name, float min, float max) {
        TBRangeSlider slider = new TBRangeSlider(openTBUI, name, min, max);
        addItem(slider);
        return slider;
    }
    public TBRangeSlider addRangeSlider(String name, float min, float max, TBRangeSlider.OnValueChangeListener onValueChangeListener) {
        TBRangeSlider slider = new TBRangeSlider(openTBUI, name, min, max, onValueChangeListener);
        addItem(slider);
        return slider;
    }

    public TBColor addColor(String name) {
        TBColor tbColor = new TBColor(openTBUI, name);
        addItem(tbColor);
        return tbColor;
    }
    public TBColor addColor(String name, @ColorInt int color) {
        TBColor tbColor = new TBColor(openTBUI, name, color);
        addItem(tbColor);
        return tbColor;
    }
    public TBColor addColor(String name, @ColorInt int color, ColorPicker.OnColorPickListener onColorPickListener) {
        TBColor tbColor = new TBColor(openTBUI, name, color, onColorPickListener);
        addItem(tbColor);
        return tbColor;
    }

    public TBEditText addEditText() {
        TBEditText tbEditText = new TBEditText(openTBUI);
        addItem(tbEditText);
        return tbEditText;
    }
    public TBEditText addEditText(String name) {
        TBEditText tbEditText = new TBEditText(openTBUI, name);
        addItem(tbEditText);
        return tbEditText;
    }
    public TBEditText addEditText(String name, CharSequence defaultText) {
        TBEditText tbEditText = new TBEditText(openTBUI, name, defaultText);
        addItem(tbEditText);
        return tbEditText;
    }
    public TBEditText addEditText(String name, CharSequence defaultText, TBEditText.OnTextInputFinishListener onTextChangeListener) {
        TBEditText tbEditText = new TBEditText(openTBUI, name, defaultText, onTextChangeListener);
        addItem(tbEditText);
        return tbEditText;
    }
    public TBEditText addEditText(String name, TBEditText.OnTextInputFinishListener onTextChangeListener) {
        TBEditText tbEditText = new TBEditText(openTBUI, name, onTextChangeListener);
        addItem(tbEditText);
        return tbEditText;
    }

    public TBBlockList addBlockList() {
        TBBlockList tbBlockList = new TBBlockList(openTBUI);
        addItem(tbBlockList);
        return tbBlockList;
    }
    public TBBlockList addBlockList(TBBlockList.OnSelectedItemChangeListener listener) {
        TBBlockList tbBlockList = new TBBlockList(openTBUI, listener);
        addItem(tbBlockList);
        return tbBlockList;
    }
}