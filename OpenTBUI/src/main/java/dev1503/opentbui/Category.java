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

    public Category (Activity context, String name, int iconId) {
        this.context = context;
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
        TBToggle toggle = new TBToggle(context, name, onCheckedChangeListener);
        addWidget(toggle);
        return toggle;
    }
    public TBToggle addToggle(String name) {
        TBToggle toggle = new TBToggle(context, name);
        addWidget(toggle);
        return toggle;
    }
    public TBAction addAction(String name, View.OnClickListener onClickListener) {
        TBAction action = new TBAction(context, name, onClickListener);
        addWidget(action);
        return action;
    }
    public TBAction addAction(String name) {
        TBAction action = new TBAction(context, name);
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
        TBSlider slider = new TBSlider(context, name);
        addWidget(slider);
        return slider;
    }
}
