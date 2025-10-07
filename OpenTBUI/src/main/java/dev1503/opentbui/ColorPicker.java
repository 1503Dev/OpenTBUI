package dev1503.opentbui;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import dev1503.opentbui.view.ColorAlphaPicker;
import dev1503.opentbui.view.ColorHuePicker;

public class ColorPicker {
    static boolean isUserInput = false;

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
            String alphaStr = finalAlpha.get() + "";
            isUserInput = false;
            if (!Objects.requireNonNull(valueA.getText()).toString().equals(alphaStr)) {
                valueA.setText(alphaStr);
            }
            isUserInput = true;
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

        valueA.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0 || !isUserInput) {
                    return;
                }
                try {
                    int num = max255(Integer.parseInt(charSequence.toString()));
                    colorAlphaPicker.setValue(num / 255f);
                } catch (NumberFormatException e) {
                }
            }
        });
        valueR.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0 || !isUserInput) {
                    return;
                }
                try {
                    int num = max255(Integer.parseInt(charSequence.toString()));
                    colorPicker.setColor(applyRedToColor(colorPicker.getColor(), num));
                } catch (NumberFormatException e) {
                }
            }
        });
        valueG.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0 || !isUserInput) {
                    return;
                }
                try {
                    int num = max255(Integer.parseInt(charSequence.toString()));
                    colorPicker.setColor(applyGreenToColor(colorPicker.getColor(), num));
                } catch (NumberFormatException e) {
                }
            }
        });
        valueB.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0 || !isUserInput) {
                    return;
                }
                try {
                    int num = max255(Integer.parseInt(charSequence.toString()));
                    colorPicker.setColor(applyBlueToColor(colorPicker.getColor(), num));
                } catch (NumberFormatException e) {
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public static void updateViews(int color, TextView valueHex, TextView valueR, TextView valueG, TextView valueB) {
        String redText = Color.red(color) + "";
        String greenText = Color.green(color) + "";
        String blueText = Color.blue(color) + "";
        isUserInput = false;
        if (!valueR.getText().toString().equals(redText)) {
            valueR.setText(redText);
        }
        if (!valueG.getText().toString().equals(greenText)) {
            valueG.setText(greenText);
        }
        if (!valueB.getText().toString().equals(blueText)) {
            valueB.setText(blueText);
        }
        valueHex.setText(toHexString(color));
        isUserInput = true;
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
        alpha = max255(alpha);
        int rgb = 0x00FFFFFF & rgbColor;
        return (alpha << 24) | rgb;
    }
    public static String toHexString(int color) {
        return String.format("#%08X", color);
    }
    static int max255(int value) {
        return Math.max(0, Math.min(255, value));
    }
    static int applyRedToColor(int rgbColor, int red) {
        red = max255(red);
        int green = Color.green(rgbColor);
        int blue = Color.blue(rgbColor);
        int alpha = Color.alpha(rgbColor);
        return applyAlphaToColor(Color.rgb(red, green, blue), alpha);
    }
    static int applyGreenToColor(int rgbColor, int green) {
        green = max255(green);
        int red = Color.red(rgbColor);
        int blue = Color.blue(rgbColor);
        int alpha = Color.alpha(rgbColor);
        return applyAlphaToColor(Color.rgb(red, green, blue), alpha);
    }
    static int applyBlueToColor(int rgbColor, int blue) {
        blue = max255(blue);
        int red = Color.red(rgbColor);
        int green = Color.green(rgbColor);
        int alpha = Color.alpha(rgbColor);
        return applyAlphaToColor(Color.rgb(red, green, blue), alpha);
    }
}