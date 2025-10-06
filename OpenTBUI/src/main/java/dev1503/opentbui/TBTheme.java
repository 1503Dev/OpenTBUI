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
                setColorAlpha(color1, 127),
                Color.parseColor("#88ECECEC")
        };
        ColorStateList rippleColorStateList = ColorStateList.valueOf(setColorAlpha(color1, 64));
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
    public int getColor1() {
        return color1;
    }
    public int getColor2() {
        return color2;
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



    int setColorAlpha(int _color, int alpha) {
        return Color.argb(alpha, Color.red(_color), Color.green(_color), Color.blue(_color));
    }
}
