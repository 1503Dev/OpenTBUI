package dev1503.opentbui.widgets;

import static dev1503.opentbui.FeaturesAdapter.dp2px;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import dev1503.opentbui.picker.ColorPicker;
import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;

public class TBColor extends TBWidget{
    TextView textView;
    ImageView imageView;
    @ColorInt int color;

    public TBColor(OpenTBUI openTBUI, String name, @ColorInt int defaultColor, ColorPicker.OnColorPickListener onColorPickListener) {
        super(openTBUI, name);
        view = (LinearLayout) LinearLayout.inflate(context, R.layout.list_color, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        ));
        imageView = view.findViewWithTag("binding_2");
        setColor(defaultColor);

        textView = view.findViewWithTag("binding_1");
        textView.setText(name);
        view.setOnClickListener(view1 -> {
            new ColorPicker(context, openTBUI.getTheme(), color, color1 -> {
                color = color1;
                imageView.setImageTintList(new ColorStateList(new int[][]{
                        new int[]{}
                }, new int[]{
                        color1
                }));
                if (onColorPickListener != null) {
                    onColorPickListener.onColorPick(color);
                }
            });
        });
    }
    public TBColor(OpenTBUI openTBUI, String name) {
        this(openTBUI, name, Color.BLACK, null);
    }
    public TBColor(OpenTBUI openTBUI, String name, @ColorInt int defaultColor) {
        this(openTBUI, name, defaultColor, null);
    }

    public void setColor(int color) {
        this.color = color;
        imageView.setImageTintList(new ColorStateList(new int[][]{
                new int[]{}
        }, new int[]{
                color
        }));
    }

    public @ColorInt int getColor() {
        return color;
    }
}
