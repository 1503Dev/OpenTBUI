package dev1503.opentbui.picker;

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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import dev1503.opentbui.R;
import dev1503.opentbui.TBTheme;
import dev1503.opentbui.Utils;
import dev1503.opentbui.view.ColorAlphaPicker;
import dev1503.opentbui.view.ColorHuePicker;

public class ColorPicker extends ExternalPicker {
    boolean isUserInput = false;
    dev1503.opentbui.view.ColorPicker colorPicker;
    ColorHuePicker colorHuePicker;
    ColorAlphaPicker colorAlphaPicker;

    TextInputEditText valueHex;
    TextInputEditText valueR;
    TextInputEditText valueG;
    TextInputEditText valueB;
    TextInputEditText valueA;

    AtomicInteger finalColor = new AtomicInteger();
    AtomicInteger finalAlpha = new AtomicInteger(255);


    ImageButton btnBack;
    Button btnDone;

    @SuppressLint("SetTextI18n")
    public ColorPicker(Context context, TBTheme theme, @ColorInt int defaultColor, OnColorPickListener onColorPickListener){
        super(context, theme, (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_color_picker, null));

        colorPicker = contentView.findViewById(R.id.picker);
        colorHuePicker = contentView.findViewById(R.id.hue);
        colorAlphaPicker = contentView.findViewById(R.id.alpha);
        valueHex = contentView.findViewById(R.id.value_hex);
        valueR = contentView.findViewById(R.id.value_r);
        valueG = contentView.findViewById(R.id.value_g);
        valueB = contentView.findViewById(R.id.value_b);
        valueA = contentView.findViewById(R.id.value_a);
        btnBack = contentView.findViewWithTag("binding_1");
        btnDone = contentView.findViewWithTag("binding_2");

        colorPicker.setHuePicker(colorHuePicker);
        colorPicker.addOnColorChangeListener(color -> {
            colorAlphaPicker.setColor(color);
            finalColor.set(applyAlphaToColor(color, finalAlpha.get()));
            updateViews();
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
            updateViews();
        });
        colorAlphaPicker.setValue(Color.alpha(defaultColor) / 255f);


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

        show();
    }

    @SuppressLint("SetTextI18n")
    public void updateViews() {
        int color = finalColor.get();
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