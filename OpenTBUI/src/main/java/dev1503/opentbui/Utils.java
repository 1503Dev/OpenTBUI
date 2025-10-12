package dev1503.opentbui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.ColorInt;

public class Utils {
    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public static int dp2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public static void setEditTextUnderlineColor(EditText editText, int color) {
        Drawable background = editText.getBackground();
        if (background != null) {
            background.setTintList(new ColorStateList(
                        new int[][]{
                                new int[]{-android.R.attr.state_focused},
                                new int[]{android.R.attr.state_enabled}
                        },
                        new int[]{
                                Color.parseColor("#BDBDBD"),
                                color
                        }
            ));
            editText.setBackground(background);
        }
    }
    public static void setEditTextCursorColor(EditText editText, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Drawable cursorDrawable = editText.getTextCursorDrawable();
            if (cursorDrawable != null) {
                cursorDrawable.setTint(color);
                editText.setTextCursorDrawable(cursorDrawable);
            }
        }
    }
    public static void setEditTextUnderlineColorAndCursorColor(EditText editText, int color) {
        setEditTextUnderlineColor(editText, color);
        setEditTextCursorColor(editText, color);
    }
    public static @ColorInt int colorApplyReduceAlpha(@ColorInt int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alphaInt = Color.alpha(color) - alpha;
        return Color.argb(alphaInt, red, green, blue);
    }
    public static @ColorInt int colorApplyMultiplyAlpha(@ColorInt int color, float alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alphaInt = (int) (Color.alpha(color) * alpha);
        return Color.argb(alphaInt, red, green, blue);
    }
}
