package dev1503.opentbui;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev1503.opentbui.widgets.TBToggle;
import dev1503.opentbui.widgets.TBWidget;

public class OpenTBUI {
    public static final int WINDOW_TYPE_POPUP = 0;
    public static final int WINDOW_TYPE_GLOBAL = 1;

    Activity activity;
    Context context;

    PopupWindow popupWindow;
    WindowManager windowManager;
    int windowType;
    View rootView;
    View contentView;
    WindowManager.LayoutParams params;

    boolean isInit = false;
    boolean isShown = false;
    boolean isFirstShow = true;

    List<Category> categories = new ArrayList<>();
    RecyclerView categoriesView;
    CategoriesAdapter categoriesAdapter;
    RecyclerView featuresView;

    Runnable onHideListener;
    TBTheme theme;

    public OpenTBUI(Activity activity, int windowType, View rootView) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.windowType = windowType;
        this.rootView = rootView;

        contentView = LinearLayout.inflate(activity, R.layout.toolbox_overlay, null);
        categoriesView = contentView.findViewById(R.id.categories);
        categoriesView.setLayoutManager(new LinearLayoutManager(context));
        categoriesAdapter = new CategoriesAdapter(context, categories);
        categoriesView.setAdapter(categoriesAdapter);
        categoriesAdapter.setOnCategoryClickListener((category, pos) -> {
            categoriesAdapter.setSelectedCategory(category);
            onCategoryClick(category);
        });

        featuresView = contentView.findViewById(R.id.options);
        featuresView.setLayoutManager(new LinearLayoutManager(context));
        featuresView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, 0); // 不添加间距
            }
        });

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
        } else if (windowType == WINDOW_TYPE_GLOBAL) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    android.graphics.PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP | Gravity.LEFT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            contentView.setFocusable(true);
        }

        categoriesAdapter.setOnTextViewCreatedListener((textView, pos, isFirst) -> {
            if (isFirst) {
                textView.setBackgroundTintList(new ColorStateList(
                        theme.getCategoryBackgroundStates(),
                        theme.getCategoryBackgroundColors()
                ));
            }
        });
    }

    public static OpenTBUI fromPopup(Activity activity, View rootView) {
        return new OpenTBUI(activity, WINDOW_TYPE_POPUP, rootView);
    }

    public static OpenTBUI fromPopup(Activity activity) {
        return fromPopup(activity, activity.getWindow().getDecorView());
    }

    public static OpenTBUI fromGlobal(Activity activity) {
        return new OpenTBUI(activity, WINDOW_TYPE_GLOBAL, null);
    }
    public void show() {
        contentView.setVisibility(View.VISIBLE);
        if (windowType == WINDOW_TYPE_POPUP) {
//            hideSystemUI();
            if (!isShown) {
                popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            }
        } else if (windowType == WINDOW_TYPE_GLOBAL) {
            if (!isShown) {
                windowManager.addView(contentView, params);
                isShown = true;
            }
        }
        if (isFirstShow) {
            isFirstShow = false;
            if (!categories.isEmpty()) {
                featuresView.setAdapter(categories.get(0).getFeaturesAdapter());
            }
        }
    }
    public void hide() {
        if (isShown) {
            if (onHideListener != null) {
                onHideListener.run();
            }
        }
        contentView.setVisibility(View.GONE);
        if (windowType == WINDOW_TYPE_POPUP) {
            popupWindow.dismiss();
        } else if (windowType == WINDOW_TYPE_GLOBAL) {
            if (isShown) {
                windowManager.removeView(contentView);
                isShown = false;
            }
        }
    }
    public void selectCategory(Category category) {
        categoriesAdapter.setSelectedCategory(category);
    }
    public void selectCategory(int pos) {
        categoriesAdapter.setSelectedCategory(pos);
    }
    void hideSystemUI() {
        View decorView = rootView;
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
    public Category addCategory(String name, int iconId){
        Category category = new Category(activity, name, iconId);
        categories.add(category);
//        categoriesAdapter.addCategory(category);
        return category;
    }
    void onCategoryClick(Category category) {
        featuresView.setAdapter(category.getFeaturesAdapter());
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
                for (TBWidget widget : category.getAllWidgets()) {
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
                        switchCompat.setBackground(theme.getRippleDrawable());
                    }
                }
            }
        }
        return this;
    }
}
