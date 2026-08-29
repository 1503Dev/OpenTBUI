package dev1503.opentbui;

import static dev1503.opentbui.Utils.dp2px;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;
import java.util.List;

import dev1503.opentbui.view.CircleSwitch;
import dev1503.opentbui.widgets.TBBlockList;
import dev1503.opentbui.widgets.TBEditText;
import dev1503.opentbui.widgets.TBRangeSlider;
import dev1503.opentbui.widgets.TBSlider;
import dev1503.opentbui.widgets.TBToggle;
import dev1503.opentbui.widgets.TBWidget;

public class OpenTBUI {
    public static final String VERSION_NAME = "v1.0.0.7";

    public static final int WINDOW_TYPE_POPUP = 0;
    public static final int WINDOW_TYPE_GLOBAL = 1;
    public static final int WINDOW_TYPE_APPLICATION = 2;

    protected Activity activity;
    protected Context context;
    protected StatusManager statusManager;

    PopupWindow popupWindow;
    WindowManager windowManager;
    PopupWindow popupWindowShortcut;

    int windowType;
    View rootView;
    View contentView;
    FrameLayout shortcutLayout;
    WindowManager.LayoutParams params;
    WindowManager.LayoutParams paramsShortcut;

    boolean isInit = false;
    boolean isShown = false;
    boolean isFirstShow = true;

    List<Category> categories = new ArrayList<>();

    RecyclerView categoriesView;
    CategoriesAdapter categoriesAdapter;
    LinearLayout featuresView;
    TextView remainingTimeText;
    ViewGroup extraButtonsLayout;

    Runnable onHideListener;
    TBTheme theme = TBTheme.DEFAULT;

    List<TextView> categoryTextViews = new ArrayList<>();

    public OpenTBUI(Activity activity, StatusManager statusManager, int windowType, View rootView, ViewGroup overlayLayout) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.windowType = windowType;
        this.rootView = rootView;
        this.statusManager = statusManager;

        this.shortcutLayout = new FrameLayout(context);
        contentView = overlayLayout;
        categoriesView = contentView.findViewWithTag(UIFactory.TAG_CATEGORIES);
        categoriesView.setLayoutManager(new LinearLayoutManager(context));
        categoriesAdapter = new CategoriesAdapter(context, categories);
        categoriesView.setAdapter(categoriesAdapter);
        categoriesAdapter.setOnCategoryClickListener((category, pos) -> {
            categoriesAdapter.setSelectedCategory(category);
            onCategoryClick(category);
        });

        featuresView = contentView.findViewWithTag(UIFactory.TAG_OPTIONS);

        contentView.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                hide();
                return true;
            }
            return false;
        });
        contentView.setOnClickListener(v -> {
            hide();
        });

        if (windowType == WINDOW_TYPE_POPUP) {
            popupWindow = new PopupWindow(activity);
            popupWindow.setContentView(contentView);
            popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setBackgroundDrawable(null);
            popupWindow.setClippingEnabled(false);
            popupWindow.setOnDismissListener(() -> {
                isShown = false;
                if (onHideListener != null) {
                    onHideListener.run();
                }
            });

            popupWindowShortcut = new PopupWindow(activity);
            popupWindowShortcut.setContentView(shortcutLayout);
            popupWindowShortcut.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindowShortcut.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindowShortcut.setFocusable(false);
            popupWindowShortcut.setOutsideTouchable(true);
            popupWindowShortcut.setBackgroundDrawable(null);
            popupWindowShortcut.setClippingEnabled(false);
        } else if (windowType == WINDOW_TYPE_GLOBAL || windowType == WINDOW_TYPE_APPLICATION) {
            windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            if (windowType == WINDOW_TYPE_GLOBAL) {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        android.graphics.PixelFormat.TRANSLUCENT);
            } else {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        android.graphics.PixelFormat.TRANSLUCENT);
            }
            params.gravity = Gravity.TOP | Gravity.LEFT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            contentView.setFocusable(true);
        }

        categoriesAdapter.setOnTextViewCreatedListener((textView, pos, isFirst) -> {
            if (isFirst) {
                categoryTextViews.add(textView);
                textView.setBackgroundTintList(new ColorStateList(
                        theme.getCategoryBackgroundStates(),
                        theme.getCategoryBackgroundColors()
                ));
            }
        });
        remainingTimeText = contentView.findViewWithTag(UIFactory.TAG_REMAINING_TIME_TEXT);
        extraButtonsLayout = contentView.findViewWithTag(UIFactory.TAG_EXTRA_BUTTONS);
        remainingTimeText.setText("Powered by 1503Dev/OpenTBUI " + VERSION_NAME);

    }
    public OpenTBUI(Activity activity, StatusManager statusManager, int windowType, View rootView) {
        this(activity, statusManager, windowType, rootView, UIFactory.buildToolboxOverlay(activity));
    }
    public OpenTBUI(Activity activity, int windowType, View rootView) {
        this(activity, new StatusManager(), windowType, rootView);
    }

    public static OpenTBUI fromPopup(Activity activity, StatusManager statusManager, View rootView, ViewGroup overlayLayout) {
        return new OpenTBUI(activity, statusManager, WINDOW_TYPE_POPUP, rootView, overlayLayout);
    }
    public static OpenTBUI fromPopup(Activity activity, StatusManager statusManager, View rootView, int overlayLayoutResId) {
        return fromPopup(activity, statusManager, rootView, (ViewGroup) ViewGroup.inflate(activity, overlayLayoutResId, null));
    }
    public static OpenTBUI fromPopup(Activity activity, StatusManager statusManager, View rootView) {
        return new OpenTBUI(activity, statusManager, WINDOW_TYPE_POPUP, rootView);
    }
    public static OpenTBUI fromPopup(Activity activity, View rootView) {
        return new OpenTBUI(activity, WINDOW_TYPE_POPUP, rootView);
    }

    public static OpenTBUI fromPopup(Activity activity, StatusManager statusManager, int overlayLayoutResId) {
        return fromPopup(activity, statusManager, activity.getWindow().getDecorView(), overlayLayoutResId);
    }
    public static OpenTBUI fromPopup(Activity activity, StatusManager statusManager) {
        return fromPopup(activity, statusManager, activity.getWindow().getDecorView());
    }
    public static OpenTBUI fromPopup(Activity activity) {
        return fromPopup(activity, activity.getWindow().getDecorView());
    }

    public static OpenTBUI fromGlobal(Activity activity, StatusManager statusManager) {
        return new OpenTBUI(activity, statusManager, WINDOW_TYPE_GLOBAL, null);
    }
    public static OpenTBUI fromGlobal(Activity activity) {
        return new OpenTBUI(activity, WINDOW_TYPE_GLOBAL, null);
    }
    public static OpenTBUI fromGlobal(Activity activity, StatusManager statusManager, View rootView) {
        return new OpenTBUI(activity, statusManager, WINDOW_TYPE_GLOBAL, rootView);
    }
    public static OpenTBUI fromGlobal(Activity activity, StatusManager statusManager, int overlayLayoutResId) {
        return fromGlobal(activity, statusManager, activity.getWindow().getDecorView(), overlayLayoutResId);
    }
    public static OpenTBUI fromGlobal(Activity activity, StatusManager statusManager, View rootView, int overlayLayoutResId) {
        return fromGlobal(activity, statusManager, rootView, (ViewGroup) ViewGroup.inflate(activity, overlayLayoutResId, null));
    }
    public static OpenTBUI fromGlobal(Activity activity, StatusManager statusManager, View rootView, ViewGroup overlayLayout) {
        return new OpenTBUI(activity, statusManager, WINDOW_TYPE_GLOBAL, rootView, overlayLayout);
    }

    public static OpenTBUI fromApplication(Activity activity, StatusManager statusManager) {
        return new OpenTBUI(activity, statusManager, WINDOW_TYPE_APPLICATION, null);
    }
    public static OpenTBUI fromApplication(Activity activity) {
        return new OpenTBUI(activity, WINDOW_TYPE_APPLICATION, null);
    }
    public static OpenTBUI fromApplication(Activity activity, StatusManager statusManager, View rootView) {
        return new OpenTBUI(activity, statusManager, WINDOW_TYPE_APPLICATION, rootView);
    }
    public static OpenTBUI fromApplication(Activity activity, StatusManager statusManager, int overlayLayoutResId) {
        return fromApplication(activity, statusManager, activity.getWindow().getDecorView(), overlayLayoutResId);
    }
    public static OpenTBUI fromApplication(Activity activity, StatusManager statusManager, View rootView, int overlayLayoutResId) {
        return fromApplication(activity, statusManager, rootView, (ViewGroup) ViewGroup.inflate(activity, overlayLayoutResId, null));
    }
    public static OpenTBUI fromApplication(Activity activity, StatusManager statusManager, View rootView, ViewGroup overlayLayout) {
        return new OpenTBUI(activity, statusManager, WINDOW_TYPE_APPLICATION, rootView, overlayLayout);
    }
    public void show() {
        if (isFirstShow) {
            isFirstShow = false;
            if (!categories.isEmpty()) {
                selectCategory(0);
            }
            refreshTheme();
        }
        contentView.setVisibility(View.VISIBLE);
        if (windowType == WINDOW_TYPE_POPUP) {
//            hideSystemUI();
            if (!isShown) {
                try {
                    popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                    isShown = true;
                } catch (Exception ignored) {
                }
            }
        } else if (windowType == WINDOW_TYPE_GLOBAL || windowType == WINDOW_TYPE_APPLICATION) {
            if (!isShown) {
                windowManager.addView(contentView, params);
                isShown = true;
            }
        }
    }
    public void hide() {
        if (isShown && windowType != WINDOW_TYPE_POPUP) {
            if (onHideListener != null) {
                onHideListener.run();
            }
        }
        contentView.setVisibility(View.GONE);
        if (windowType == WINDOW_TYPE_POPUP) {
            popupWindow.dismiss();
        } else if (windowType == WINDOW_TYPE_GLOBAL || windowType == WINDOW_TYPE_APPLICATION) {
            if (isShown) {
                windowManager.removeView(contentView);
                isShown = false;
            }
        }
    }
    public void showShortcut() {
        if (isFirstShow) {
            isFirstShow = false;
            if (!categories.isEmpty()) {
                selectCategory(0);
            }
            refreshTheme();
        }
        contentView.setVisibility(View.VISIBLE);
        if (windowType == WINDOW_TYPE_POPUP) {
//            hideSystemUI();
            if (!isShown) {
                try {
                    popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                    isShown = true;
                } catch (Exception ignored) {
                }
            }
        } else if (windowType == WINDOW_TYPE_GLOBAL || windowType == WINDOW_TYPE_APPLICATION) {
            if (!isShown) {
                windowManager.addView(contentView, params);
                isShown = true;
            }
        }
    }
    public void selectCategory(Category category) {
        categoriesAdapter.setSelectedCategory(category);
        onCategoryClick(category);
    }
    public void selectCategory(int pos) {
        if (pos < 0 || pos >= categories.size()) {
            return;
        }
        categoriesAdapter.setSelectedCategory(pos);
        onCategoryClick(categories.get(pos));
    }
    public void hideSystemUI() {
        if (activity == null || activity.isFinishing()) return;
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(
                activity.getWindow(), activity.getWindow().getDecorView());
        if (controller != null) {
            controller.hide(WindowInsetsCompat.Type.systemBars());
            controller.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
    }
    public Category addCategory(String name, Icon icon){
        Category category = new Category(this, name, icon);
        categoriesAdapter.addCategory(category);
        return category;
    }
    void onCategoryClick(Category category) {
        featuresView.removeAllViews();
        featuresView.addView(category.getViewContainer());
    }
    public boolean isShowing() {
        return isShown;
    }
    public OpenTBUI setOnHideListener(Runnable onHideListener) {
        this.onHideListener = onHideListener;
        return this;
    }
    public OpenTBUI setTheme(TBTheme theme) {
        this.theme = theme;
        refreshTheme();
        return this;
    }

    public OpenTBUI refreshTheme() {
        if (theme != null) {
            for (Category category : categories) {
                for (TBWidget widget : category.getAllWidgetsAndSubWidgets()) {
                    if (widget instanceof TBToggle) {
                        SwitchCompat switchCompat = ((TBToggle) widget).getSwitch();
                        switchCompat.setThumbTintList(new ColorStateList(
                                theme.getSwitchStates(),
                                theme.getSwitchThumbColors()
                        ));
                        switchCompat.setTrackTintList(new ColorStateList(
                                theme.getSwitchStates(),
                                theme.getSwitchTrackColors()
                        ));
                        // 只在首次设置 ripple background，避免重置正在进行的动画
                        if (!(switchCompat.getBackground() instanceof android.graphics.drawable.RippleDrawable)) {
                            switchCompat.setBackground(theme.getRippleDrawable());
                        }

                        View statusView = ((TBToggle) widget).getStatusView();
                        statusView.setBackgroundColor(theme.getColor1());
                    } else if (widget instanceof TBSlider) {
                        IndicatorSeekBar seekBar = ((TBSlider) widget).getSeekBar();
                        seekBar.setTrackColorInactive(theme.getSeekBarTrackColorInactive());
                        seekBar.setTrackColorActive(theme.getSeekBarTrackColorActive());
                        seekBar.tickMarksColor(theme.getSeekBarTickColor());
                        seekBar.setThumbColor(theme.getSeekBarThumbColor());
                        seekBar.setIndicatorColor(theme.getSeekBarIndicatorColor());
                    } else if (widget instanceof TBRangeSlider) {
                        IndicatorSeekBar seekBar = ((TBRangeSlider) widget).getSeekBar();
                        seekBar.setTrackColorInactive(theme.getSeekBarTrackColorInactive());
                        seekBar.setTrackColorActive(theme.getSeekBarTrackColorActive());
                        seekBar.tickMarksColor(theme.getSeekBarTickColor());
                        seekBar.setThumbColor(theme.getSeekBarThumbColor());
                        seekBar.setIndicatorColor(theme.getSeekBarIndicatorColor());
                    } else if (widget instanceof TBEditText) {
                        AppCompatEditText editText = ((TBEditText) widget).getEditText();
                        Utils.setEditTextUnderlineColorAndCursorColor(editText, theme.getEditTextUnderlineColor());
                    } else if (widget instanceof TBBlockList) {
                        CircleSwitch[] circleSwitches = ((TBBlockList) widget).getCircleSwitches();
                        for (CircleSwitch circleSwitch : circleSwitches) {
                            circleSwitch.setOnColor(theme.getCircleSwitchBackgroundColor());
                        }
                    }
                }
            }
            for (TextView textView: categoryTextViews.toArray(new TextView[0])) {
                if (textView != null) {
                    textView.setBackgroundTintList(new ColorStateList(
                            theme.getCategoryBackgroundStates(),
                            theme.getCategoryBackgroundColors()
                    ));
                }
            }
        }
        return this;
    }

    public Context getContext() {
        return this.context;
    }
    public Activity getActivity() {
        return this.activity;
    }
    public TBTheme getTheme() {
        return this.theme;
    }

    public OpenTBUI setFeaturesViewWidth(int width) {
        ViewGroup.LayoutParams layoutParams = featuresView.getLayoutParams();
        layoutParams.width = width;
        featuresView.setLayoutParams(layoutParams);
        return this;
    }
    public OpenTBUI setCategoriesViewWidth(int width) {
        ViewGroup.LayoutParams layoutParams = categoriesView.getLayoutParams();
        layoutParams.width = width;
        categoriesView.setLayoutParams(layoutParams);
        return this;
    }
    public StatusManager getStatusManager(){
        return statusManager;
    }
    public OpenTBUI setStatusManager(StatusManager statusManager) {
        this.statusManager = statusManager;
        return this;
    }
    public ImageView addExtraButton(Icon icon, View.OnClickListener onClickListener) {
        ImageView imageView = new ImageView(context);
        if (icon != null) {
            imageView.setImageDrawable(icon.resolve(context));
        }
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                dp2px(context, 48),
                dp2px(context, 48)
        );
        imageView.setLayoutParams(layoutParams);
        imageView.setBackground(UIFactory.buildSettingsIconBackground(context));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageTintList(ColorStateList.valueOf(0xFFFFFFFF));
        imageView.setOnClickListener(onClickListener);
        extraButtonsLayout.addView(imageView);
        return imageView;
    }

    public TipBar getTipBar() {
        return new TipBar(remainingTimeText, context);
    }
}
