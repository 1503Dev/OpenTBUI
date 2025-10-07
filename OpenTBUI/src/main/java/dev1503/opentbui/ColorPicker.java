package dev1503.opentbui;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.concurrent.atomic.AtomicInteger;

import dev1503.opentbui.view.ColorAlphaPicker;
import dev1503.opentbui.view.ColorHuePicker;

public class ColorPicker {
    @SuppressLint("SetTextI18n")
    public static void open(Context context, TBTheme theme, @ColorInt int defaultColor, OnColorPickListener onColorPickListener){
        @SuppressLint("PrivateResource")
        BottomSheetDialog sheet = new BottomSheetDialog(context, com.google.android.material.R.style.Theme_MaterialComponents_BottomSheetDialog);

        Window window = sheet.getWindow();
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false);

            WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, window.getDecorView());
            if (insetsController != null) {
                insetsController.setSystemBarsBehavior(
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }

            window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
        AtomicInteger finalColor = new AtomicInteger();
        AtomicInteger finalAlpha = new AtomicInteger(255);

        dev1503.opentbui.view.ColorPicker colorPicker;
        ColorHuePicker colorHuePicker;
        ColorAlphaPicker colorAlphaPicker;

        TextInputEditText valueHex;
        TextInputEditText valueR;
        TextInputEditText valueG;
        TextInputEditText valueB;
        TextInputEditText valueA;

        ImageButton btnBack;
        Button btnDone;

        LinearLayout layout = (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_color_picker, null);
        colorPicker = layout.findViewById(R.id.picker);
        colorHuePicker = layout.findViewById(R.id.hue);
        colorAlphaPicker = layout.findViewById(R.id.alpha);
        valueHex = layout.findViewById(R.id.value_hex);
        valueR = layout.findViewById(R.id.value_r);
        valueG = layout.findViewById(R.id.value_g);
        valueB = layout.findViewById(R.id.value_b);
        valueA = layout.findViewById(R.id.value_a);
        btnBack = layout.findViewWithTag("binding_1");
        btnDone = layout.findViewWithTag("binding_2");

        colorPicker.setHuePicker(colorHuePicker);
        colorPicker.addOnColorChangeListener(color -> {
            colorAlphaPicker.setColor(color);
            finalColor.set(applyAlphaToColor(color, finalAlpha.get()));
            updateViews(finalColor.get(), valueHex, valueR, valueG, valueB);
        });
        float[] hsv = {0, 0, 0};
        Color.colorToHSV(defaultColor, hsv);
        colorPicker.setColor(defaultColor);
        colorPicker.setHue(hsv[0]);
        colorHuePicker.addOnValueChangeListener(colorPicker::setHue);
        colorAlphaPicker.addOnValueChangeListener(value -> {
            finalAlpha.set(Math.round(255 * value));
            finalColor.set(applyAlphaToColor(finalColor.get(), finalAlpha.get()));
            valueA.setText(finalAlpha.get() + "");
            updateViews(finalColor.get(), valueHex, valueR, valueG, valueB);
        });
        colorAlphaPicker.setValue(Color.alpha(defaultColor) / 255f);

        sheet.setContentView(layout);
        sheet.show();
        sheet.setCanceledOnTouchOutside(false);
        sheet.getBehavior().setMaxHeight(getScreenHeight(context));
        sheet.getBehavior().setState(STATE_EXPANDED);

        btnBack.setOnClickListener(view -> {
            sheet.cancel();
        });
        btnDone.setOnClickListener(view -> {
            onColorPickListener.onColorPick(finalColor.get());
            sheet.cancel();
        });

        btnDone.setTextColor(theme.getButtonTextColor());
    }

    @SuppressLint("SetTextI18n")
    public static void updateViews(int color, TextView valueHex, TextView valueR, TextView valueG, TextView valueB) {
        valueR.setText(Color.red(color) + "");
        valueG.setText(Color.green(color) + "");
        valueB.setText(Color.blue(color) + "");
        valueHex.setText(toHexString(color));
    }

    public interface OnColorPickListener{
        void onColorPick(int color);
    }

    private static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
        return 0;
    }

    public static int applyAlphaToColor(int rgbColor, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        int rgb = 0x00FFFFFF & rgbColor;
        return (alpha << 24) | rgb;
    }
    public static String toHexString(int color) {
        return String.format("#%08X", color);
    }
}