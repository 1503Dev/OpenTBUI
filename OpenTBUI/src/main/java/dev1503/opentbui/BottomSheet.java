package dev1503.opentbui;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomSheet {
    protected Context context;
    protected TBTheme theme;
    protected BottomSheetDialog sheet;
    protected LinearLayout contentView;

    @SuppressLint("SetTextI18n")
    public BottomSheet(Context context, TBTheme theme, LinearLayout contentView){
        this.context = context;
        this.theme = theme;
        this.contentView = contentView;
        this.sheet = new BottomSheetDialog(context, com.google.android.material.R.style.Theme_MaterialComponents_BottomSheetDialog);

        Window window = sheet.getWindow();
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false);

            WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, window.getDecorView());
            insetsController.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }

        sheet.setContentView(contentView);
        sheet.setCanceledOnTouchOutside(false);
        sheet.getBehavior().setMaxHeight(getScreenHeight(context));
        sheet.getBehavior().setState(STATE_EXPANDED);
    }

    public void show(){
        sheet.show();
    }

    public void dismiss(){
        sheet.dismiss();
    }

    public void cancel(){
        sheet.cancel();
    }

    private static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                android.view.WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
                android.graphics.Rect bounds = windowMetrics.getBounds();
                return bounds.height();
            } else {
                Display display = windowManager.getDefaultDisplay();
                if (display != null) {
                    Point size = new Point();
                    display.getSize(size);
                    return size.y;
                }
            }
        }
        return 0;
    }
}
