package dev1503.opentbui;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import dev1503.opentbui.widgets.TBToggle;
import dev1503.opentbui.widgets.TBWidget;

public class Category {
    String name;
    int iconId;

    List<TBWidget> widgets = new ArrayList<>();
    FeaturesAdapter featuresAdapter;

    Activity context;

    public Category (Activity context, String name, int iconId) {
        this.context = context;
        this.name = name;
        this.iconId = iconId;
        featuresAdapter = new FeaturesAdapter(context, widgets);
    }

    public Category addWidget(TBWidget widget) {
        widgets.add(widget);
        if (featuresAdapter != null) {
            featuresAdapter.addFeature(widget);
        }
        return this;
    }
    public FeaturesAdapter getFeaturesAdapter() {
        return featuresAdapter;
    }

    public Category addToggle(String name) {
        addWidget(new TBToggle(context, name));
        return this;
    }
}
