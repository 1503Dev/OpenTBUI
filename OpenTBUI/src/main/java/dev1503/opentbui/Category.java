package dev1503.opentbui;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev1503.opentbui.picker.ColorPicker;
import dev1503.opentbui.widgets.TBAction;
import dev1503.opentbui.widgets.TBBlockList;
import dev1503.opentbui.widgets.TBColor;
import dev1503.opentbui.widgets.TBDivider;
import dev1503.opentbui.widgets.TBDropDown;
import dev1503.opentbui.widgets.TBEditText;
import dev1503.opentbui.widgets.TBLabel;
import dev1503.opentbui.widgets.TBRangeSlider;
import dev1503.opentbui.widgets.TBSlider;
import dev1503.opentbui.widgets.TBToggle;
import dev1503.opentbui.widgets.TBWidget;

public class Category {
    String name;
    int iconId;

    List<TBWidget> widgets = new ArrayList<>();
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

    public String getName() {
        return name;
    }

    public Category addWidget(TBWidget widget, String path) {
        widget.setPath(path);
        widgets.add(widget);
//        if (featuresAdapter != null) {
//            featuresAdapter.addFeature(widget);
//        }
        viewContainer.addView(widget.getView());
        if (openTBUI.getStatusManager() != null) {
            openTBUI.getStatusManager().addWidget(widget);
        }
        return this;
    }

    public Category addWidget(TBWidget widget) {
        return addWidget(widget, null);
    }

    public TBToggle addToggle(String name, String path, TBToggle.OnCheckedChangeListener onCheckedChangeListener) {
        TBToggle toggle = new TBToggle(openTBUI, name, onCheckedChangeListener);
        addWidget(toggle, path);
        return toggle;
    }

    public TBToggle addToggle(String name, TBToggle.OnCheckedChangeListener onCheckedChangeListener) {
        return addToggle(name, null, onCheckedChangeListener);
    }
    public TBToggle addToggle(String name, String path) {
        TBToggle toggle = new TBToggle(openTBUI, name);
        addWidget(toggle, path);
        return toggle;
    }
    public TBToggle addToggle(String name) {
        return addToggle(name, (String) null);
    }
    public TBToggle addToggle(String name, String path, boolean isChecked) {
        TBToggle toggle = new TBToggle(openTBUI, name, isChecked);
        addWidget(toggle, path);
        return toggle;
    }
    public TBToggle addToggle(String name, boolean isChecked) {
        return addToggle(name, null, isChecked);
    }
    public TBAction addAction(String name, String path, View.OnClickListener onClickListener) {
        TBAction action = new TBAction(openTBUI, name, onClickListener);
        addWidget(action, path);
        return action;
    }
    public TBAction addAction(String name, View.OnClickListener onClickListener) {
        return addAction(name, null, onClickListener);
    }
    public TBAction addAction(String name, String path) {
        TBAction action = new TBAction(openTBUI, name);
        addWidget(action, path);
        return action;
    }
    public TBAction addAction(String name) {
        return addAction(name, (String) null);
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
    public TBSlider addSlider(String name, String path) {
        TBSlider slider = new TBSlider(openTBUI, name);
        addWidget(slider, path);
        return slider;
    }
    public TBSlider addSlider(String name) {
        return addSlider(name, (String) null);
    }
    public TBSlider addSlider(String name, String path, float[] values, TBSlider.OnValueChangeListener onValueChangeListener) {
        TBSlider slider = new TBSlider(openTBUI, name, values, onValueChangeListener);
        addWidget(slider, path);
        return slider;
    }
    public TBSlider addSlider(String name, float[] values, TBSlider.OnValueChangeListener onValueChangeListener) {
        return addSlider(name, null, values, onValueChangeListener);
    }
    public TBSlider addSlider(String name, String path, float[] values) {
        TBSlider slider = new TBSlider(openTBUI, name, values);
        addWidget(slider, path);
        return slider;
    }
    public TBSlider addSlider(String name, float[] values) {
        return addSlider(name, null, values);
    }
    public TBRangeSlider addRangeSlider(String name, String path, float min, float max) {
        TBRangeSlider rangeSlider = new TBRangeSlider(openTBUI, name, path, min, max);
        addWidget(rangeSlider, path);
        return rangeSlider;
    }
    public TBRangeSlider addRangeSlider(String name, float min, float max) {
        return addRangeSlider(name, null, min, max);
    }
    public TBRangeSlider addRangeSlider(String name, String path, float min, float max, TBRangeSlider.OnValueChangeListener onValueChangeListener) {
        TBRangeSlider rangeSlider = new TBRangeSlider(openTBUI, name, min, max, onValueChangeListener);
        addWidget(rangeSlider, path);
        return rangeSlider;
    }
    public TBRangeSlider addRangeSlider(String name, float min, float max, TBRangeSlider.OnValueChangeListener onValueChangeListener) {
        return addRangeSlider(name, null, min, max, onValueChangeListener);
    }
    public TBColor addColor(String name, String path) {
        TBColor tbColor = new TBColor(openTBUI, name);
        addWidget(tbColor, path);
        return tbColor;
    }
    public TBColor addColor(String name) {
        return addColor(name, (String) null);
    }
    public TBColor addColor(String name, String path, int defaultColor) {
        TBColor tbColor = new TBColor(openTBUI, name, defaultColor);
        addWidget(tbColor, path);
        return tbColor;
    }
    public TBColor addColor(String name, int defaultColor) {
        return addColor(name, null, defaultColor);
    }
    public TBColor addColor(String name, String path, int defaultColor, ColorPicker.OnColorPickListener onColorPickListener) {
        TBColor tbColor = new TBColor(openTBUI, name, path, defaultColor, onColorPickListener);
        addWidget(tbColor, path);
        return tbColor;
    }
    public TBColor addColor(String name, int defaultColor, ColorPicker.OnColorPickListener onColorPickListener) {
        return addColor(name, null, defaultColor, onColorPickListener);
    }
    public TBEditText addEditText() {
        return addEditText((String) null);
    }
    public TBEditText addEditTextWithPath(String name, String path) {
        TBEditText tbEditText = new TBEditText(openTBUI, name);
        addWidget(tbEditText, path);
        return tbEditText;
    }
    public TBEditText addEditText(String name) {
        return addEditText(name, (String) null);
    }
    public TBEditText addEditText(String name, String path, String defaultText) {
        TBEditText tbEditText = new TBEditText(openTBUI, name, defaultText);
        addWidget(tbEditText, path);
        return tbEditText;
    }
    public TBEditText addEditText(String name, String defaultText) {
        return addEditText(name, null, defaultText);
    }
    public TBEditText addEditText(String name, String path, String defaultText, TBEditText.OnTextInputFinishListener onTextChangeListener) {
        TBEditText tbEditText = new TBEditText(openTBUI, name, defaultText, onTextChangeListener);
        addWidget(tbEditText, path);
        return tbEditText;
    }
    public TBEditText addEditText(String name, String defaultText, TBEditText.OnTextInputFinishListener onTextChangeListener) {
        return addEditText(name, null, defaultText, onTextChangeListener);
    }
    public TBBlockList addBlockList(String path) {
        TBBlockList tbBlockList = new TBBlockList(openTBUI);
        addWidget(tbBlockList, path);
        return tbBlockList;
    }
    public TBBlockList addBlockList() {
        return addBlockList((String) null);
    }
    public TBBlockList addBlockList(String path, TBBlockList.OnSelectedItemChangeListener listener) {
        TBBlockList tbBlockList = new TBBlockList(openTBUI, listener);
        addWidget(tbBlockList, path);
        return tbBlockList;
    }
    public TBBlockList addBlockList(TBBlockList.OnSelectedItemChangeListener listener) {
        return addBlockList(null, listener);
    }

    public TBDropDown addDropDown(String name, String path, String[] items, int defaultPosition, TBDropDown.OnItemSelectedListener listener) {
        TBDropDown tbDropDown = new TBDropDown(openTBUI, name, path, items, defaultPosition, listener);
        addWidget(tbDropDown, path);
        return tbDropDown;
    }
    public TBDropDown addDropDown(String name, String path, String[] items, int defaultPosition) {
        return addDropDown(name, path, items, defaultPosition, null);
    }
    public TBDropDown addDropDown(String name, String path, String[] items, TBDropDown.OnItemSelectedListener listener) {
        return addDropDown(name, path, items, 0, listener);
    }
    public TBDropDown addDropDown(String name, String path, String[] items) {
        return addDropDown(name, path, items, 0, null);
    }
    public TBDropDown addDropDown(String name, String[] items, int defaultPosition, TBDropDown.OnItemSelectedListener listener) {
        return addDropDown(name, null, items, defaultPosition, listener);
    }
    public TBDropDown addDropDown(String name, String[] items, int defaultPosition) {
        return addDropDown(name, null, items, defaultPosition, null);
    }
    public TBDropDown addDropDown(String name, String[] items, TBDropDown.OnItemSelectedListener listener) {
        return addDropDown(name, null, items, 0, listener);
    }
    public TBDropDown addDropDown(String name, String[] items) {
        return addDropDown(name, null, items, 0, null);
    }

    public TBDivider addDivider(String name) {
        TBDivider tbDivider = new TBDivider(openTBUI, name);
        addWidget(tbDivider, null);
        return tbDivider;
    }
    public TBDivider addDivider() {
        return addDivider(null);
    }

    public TBLabel addLabel(String name) {
        TBLabel tbLabel = new TBLabel(openTBUI, name);
        addWidget(tbLabel, null);
        return tbLabel;
    }
    public TBLabel addLabel() {
        return addLabel(null);
    }
}
