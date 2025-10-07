package dev1503.opentbui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev1503.opentbui.widgets.TBAction;
import dev1503.opentbui.widgets.TBColor;
import dev1503.opentbui.widgets.TBEditText;
import dev1503.opentbui.widgets.TBRangeSlider;
import dev1503.opentbui.widgets.TBSlider;
import dev1503.opentbui.widgets.TBToggle;
import dev1503.opentbui.widgets.TBWidget;

public class Category {
    String name;
    int iconId;

    List<TBWidget> widgets = new ArrayList<>();
    FeaturesAdapter featuresAdapter;
    LinearLayout viewContainer;

    Activity context;
    OpenTBUI openTBUI;

    public Category (OpenTBUI openTBUI, String name, int iconId) {
        this.context = openTBUI.getActivity();
        this.openTBUI = openTBUI;
        this.name = name;
        this.iconId = iconId;
//        featuresAdapter = new FeaturesAdapter(context, widgets);
        viewContainer = new LinearLayout(context);
        viewContainer.setOrientation(LinearLayout.VERTICAL);
        viewContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
    }

    public Category addWidget(TBWidget widget) {
        widgets.add(widget);
//        if (featuresAdapter != null) {
//            featuresAdapter.addFeature(widget);
//        }
        viewContainer.addView(widget.getView());
        return this;
    }
    public FeaturesAdapter getFeaturesAdapter() {
        return featuresAdapter;
    }

    public TBToggle addToggle(String name, SwitchCompat.OnCheckedChangeListener onCheckedChangeListener) {
        TBToggle toggle = new TBToggle(openTBUI, name, onCheckedChangeListener);
        addWidget(toggle);
        return toggle;
    }
    public TBToggle addToggle(String name) {
        TBToggle toggle = new TBToggle(openTBUI, name);
        addWidget(toggle);
        return toggle;
    }
    public TBToggle addToggle(String name, boolean isChecked) {
        TBToggle toggle = new TBToggle(openTBUI, name, isChecked);
        addWidget(toggle);
        return toggle;
    }
    public TBAction addAction(String name, View.OnClickListener onClickListener) {
        TBAction action = new TBAction(openTBUI, name, onClickListener);
        addWidget(action);
        return action;
    }
    public TBAction addAction(String name) {
        TBAction action = new TBAction(openTBUI, name);
        addWidget(action);
        return action;
    }
    public TBWidget[] getAllWidgets() {
        return widgets.toArray(new TBWidget[0]);
    }
    public TBWidget[] getAllWidgetsAndSubWidgets() {
        List<TBWidget> allWidgets = new ArrayList<>();
        for (TBWidget widget : widgets) {
            if (widget instanceof TBToggle) {
                allWidgets.addAll(Arrays.asList(((TBToggle) widget).getAllWidgetsAndSelf()));
            } else {
                allWidgets.add(widget);
            }
        }
        return allWidgets.toArray(new TBWidget[0]);
    }
    public LinearLayout getViewContainer() {
        return viewContainer;
    }
    public TBSlider addSlider(String name) {
        TBSlider slider = new TBSlider(openTBUI, name);
        addWidget(slider);
        return slider;
    }
    public TBSlider addSlider(String name, float[] values, TBSlider.OnValueChangeListener onValueChangeListener) {
        TBSlider slider = new TBSlider(openTBUI, name, values, onValueChangeListener);
        addWidget(slider);
        return slider;
    }
    public TBSlider addSlider(String name, float[] values) {
        TBSlider slider = new TBSlider(openTBUI, name, values);
        addWidget(slider);
        return slider;
    }
    public TBRangeSlider addRangeSlider(String name, float min, float max) {
        TBRangeSlider rangeSlider = new TBRangeSlider(openTBUI, name, min, max);
        addWidget(rangeSlider);
        return rangeSlider;
    }
    public TBRangeSlider addRangeSlider(String name, float min, float max, TBRangeSlider.OnValueChangeListener onValueChangeListener) {
        TBRangeSlider rangeSlider = new TBRangeSlider(openTBUI, name, min, max, onValueChangeListener);
        addWidget(rangeSlider);
        return rangeSlider;
    }
    public TBColor addColor(String name) {
        TBColor tbColor = new TBColor(openTBUI, name);
        addWidget(tbColor);
        return tbColor;
    }
    public TBColor addColor(String name, int defaultColor) {
        TBColor tbColor = new TBColor(openTBUI, name, defaultColor);
        addWidget(tbColor);
        return tbColor;
    }
    public TBColor addColor(String name, int defaultColor, ColorPicker.OnColorPickListener onColorPickListener) {
        TBColor tbColor = new TBColor(openTBUI, name, defaultColor, onColorPickListener);
        addWidget(tbColor);
        return tbColor;
    }
    public TBEditText addEditText() {
        TBEditText tbEditText = new TBEditText(openTBUI);
        addWidget(tbEditText);
        return tbEditText;
    }
    public TBEditText addEditText(String name) {
        TBEditText tbEditText = new TBEditText(openTBUI, name);
        addWidget(tbEditText);
        return tbEditText;
    }
    public TBEditText addEditText(String name, String defaultText) {
        TBEditText tbEditText = new TBEditText(openTBUI, name, defaultText);
        addWidget(tbEditText);
        return tbEditText;
    }
    public TBEditText addEditText(String name, String defaultText, TBEditText.OnTextChangeListener onTextChangeListener) {
        TBEditText tbEditText = new TBEditText(openTBUI, name, defaultText, onTextChangeListener);
        addWidget(tbEditText);
        return tbEditText;
    }
}
