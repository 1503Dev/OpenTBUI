package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import dev1503.opentbui.picker.ColorPicker;
import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.UIFactory;

public class TBColor extends TBWidget{
    TextView textView;
    ImageView imageView;
    @ColorInt int color;
    ColorPicker.OnColorPickListener onColorPickListener;

    public TBColor(OpenTBUI openTBUI, String name, String path, @ColorInt int defaultColor, ColorPicker.OnColorPickListener onColorPickListener) {
        super(openTBUI, name, path);
        this.onColorPickListener = onColorPickListener;
        view = UIFactory.buildColorLayout(context);
        imageView = (ImageView) view.findViewWithTag(UIFactory.TAG_BINDING_2);
        setColor(defaultColor);

        textView = (TextView) view.findViewWithTag(UIFactory.TAG_BINDING_1);
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
                if (openTBUI.getStatusManager() != null) {
                    openTBUI.getStatusManager().setValue(TBColor.this, getPath(), color);
                }
            });
        });
    }
    public TBColor(OpenTBUI openTBUI, String name) {
        this(openTBUI, name, null, Color.BLACK, null);
    }
    public TBColor(OpenTBUI openTBUI, String name, @ColorInt int defaultColor) {
        this(openTBUI, name, null, defaultColor, null);
    }
    public TBColor(OpenTBUI openTBUI, String name, String path) {
        this(openTBUI, name, path, Color.BLACK, null);
    }
    public TBColor(OpenTBUI openTBUI, String name, String path, @ColorInt int defaultColor) {
        this(openTBUI, name, path, defaultColor, null);
    }

    public TBColor setColorWithoutNotify(int color) {
        this.color = color;
        imageView.setImageTintList(new ColorStateList(new int[][]{
                new int[]{}
        }, new int[]{
                color
        }));
        return this;
    }
    public TBColor setColor(int color) {
        setColorWithoutNotify(color);
        if (onColorPickListener != null) {
            onColorPickListener.onColorPick(color);
        }
        return this;
    }

    public @ColorInt int getColor() {
        return color;
    }

    @Override
    public void syncValue(double value) {
        if (getColor() != (int) value) {
            setColorWithoutNotify((int) value);
        }
    }

    public String getName() {
        return name;
    }
    public TBColor setName(String name) {
        this.name = name;
        textView.setText(name);
        return this;
    }
}
