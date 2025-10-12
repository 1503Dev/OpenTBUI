package dev1503.opentbui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.widget.Switch;

public class TBTheme {
    int[][] switchStates;
    int[] switchThumbColors;
    int[] switchTrackColors;
    RippleDrawable rippleDrawable;
    int[][] categoryBackgroundStates;
    int[] categoryBackgroundColors;
    int seekBarThumbColor;
    int seekBarTrackColorInactive;
    int seekBarTrackColorActive;
    int seekBarTickColor;
    int seekBarIndicatorColor;
    int buttonTextColor;
    int editTextUnderlineColor;
    int editTextCursorColor;
    int circleSwitchBackgroundColor;

    int color1;
    int color2;

    public TBTheme(int color1, int color2) {
        this.switchStates = new int[][] {
                new int[] { android.R.attr.state_checked },
                new int[] { -android.R.attr.state_checked }
        };
        this.color1 = color1;
        this.color2 = color2;
        this.switchThumbColors = new int[] {
                color1,
                Color.parseColor("#FFECECEC")
        };
        this.switchTrackColors = new int[] {
                Utils.colorApplyMultiplyAlpha(color1, 0.5f),
                Color.parseColor("#88ECECEC")
        };
        ColorStateList rippleColorStateList = ColorStateList.valueOf(Utils.colorApplyMultiplyAlpha(color1, 64f / 255f));
        rippleDrawable = new RippleDrawable(
                rippleColorStateList,
                null,
                null
        );

        this.categoryBackgroundStates = new int[][] {
                new int[] { android.R.attr.state_selected },
                new int[] { android.R.attr.state_pressed },
                new int[] {}
        };
        this.categoryBackgroundColors = new int[] {
                color2,
                Color.parseColor("#20FFFFFF"),
                Color.TRANSPARENT
        };

        this.seekBarThumbColor = color1;
        this.seekBarTrackColorInactive = setColorAlpha(color1, 64);
        this.seekBarTrackColorActive = color1;
        this.seekBarTickColor = color1;
        this.seekBarIndicatorColor = color1;
        this.buttonTextColor = color1;
        this.editTextUnderlineColor = color1;
        this.editTextCursorColor = color1;
        this.circleSwitchBackgroundColor = Utils.colorApplyMultiplyAlpha(color1, (float) 0x30 / 0xFF);
    }

    public int[][] getSwitchStates() {
        return switchStates;
    }
    public int[] getSwitchThumbColors() {
        return switchThumbColors;
    }
    public int[] getSwitchTrackColors() {
        return switchTrackColors;
    }
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }
    public int[][] getCategoryBackgroundStates() {
        return categoryBackgroundStates;
    }
    public int[] getCategoryBackgroundColors() {
        return categoryBackgroundColors;
    }
    public int getSeekBarThumbColor() {
        return seekBarThumbColor;
    }
    public int getSeekBarTrackColorInactive() {
        return seekBarTrackColorInactive;
    }
    public int getSeekBarTrackColorActive() {
        return seekBarTrackColorActive;
    }
    public int getSeekBarTickColor() {
        return seekBarTickColor;
    }
    public int getColor1() {
        return color1;
    }
    public int getColor2() {
        return color2;
    }
    public int getSeekBarIndicatorColor() {
        return seekBarIndicatorColor;
    }
    public int getButtonTextColor() {
        return buttonTextColor;
    }
    public int getEditTextUnderlineColor() {
        return editTextUnderlineColor;
    }
    public int getEditTextCursorColor() {
        return editTextCursorColor;
    }
    public int getCircleSwitchBackgroundColor() {
        return circleSwitchBackgroundColor;
    }


    public TBTheme setSwitchStates(int[][] switchStates) {
        this.switchStates = switchStates;
        return this;
    }
    public TBTheme setSwitchThumbColors(int[] switchThumbColors) {
        this.switchThumbColors = switchThumbColors;
        return this;
    }
    public TBTheme setSwitchTrackColors(int[] switchTrackColors) {
        this.switchTrackColors = switchTrackColors;
        return this;
    }
    public TBTheme setRippleDrawable(RippleDrawable rippleDrawable) {
        this.rippleDrawable = rippleDrawable;
        return this;
    }
    public TBTheme setCategoryBackgroundStates(int[][] categoryBackgroundStates) {
        this.categoryBackgroundStates = categoryBackgroundStates;
        return this;
    }
    public TBTheme setCategoryBackgroundColors(int[] categoryBackgroundColors) {
        this.categoryBackgroundColors = categoryBackgroundColors;
        return this;
    }
    public TBTheme setSeekBarThumbColor(int seekBarThumbColor) {
        this.seekBarThumbColor = seekBarThumbColor;
        return this;
    }
    public TBTheme setSeekBarTrackColorInactive(int seekBarTrackColorInactive) {
        this.seekBarTrackColorInactive = seekBarTrackColorInactive;
        return this;
    }
    public TBTheme setSeekBarTrackColorActive(int seekBarTrackColorActive) {
        this.seekBarTrackColorActive = seekBarTrackColorActive;
        return this;
    }
    public TBTheme setSeekBarTickColor(int seekBarTickColor) {
        this.seekBarTickColor = seekBarTickColor;
        return this;
    }
    public TBTheme setSeekBarIndicatorColor(int seekBarIndicatorColor) {
        this.seekBarIndicatorColor = seekBarIndicatorColor;
        return this;
    }
    public TBTheme setButtonTextColor(int buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
        return this;
    }
    public TBTheme setEditTextUnderlineColor(int editTextUnderlineColor) {
        this.editTextUnderlineColor = editTextUnderlineColor;
        return this;
    }
    public TBTheme setEditTextCursorColor(int editTextCursorColor) {
        this.editTextCursorColor = editTextCursorColor;
        return this;
    }
    public TBTheme setCircleSwitchBackgroundColor(int circleSwitchBackgroundColor) {
        this.circleSwitchBackgroundColor = circleSwitchBackgroundColor;
        return this;
    }



    int setColorAlpha(int _color, int alpha) {
        return Color.argb(alpha, Color.red(_color), Color.green(_color), Color.blue(_color));
    }
}
