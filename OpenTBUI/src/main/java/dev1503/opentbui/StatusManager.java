package dev1503.opentbui;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dev1503.opentbui.widgets.TBColor;
import dev1503.opentbui.widgets.TBRangeSlider;
import dev1503.opentbui.widgets.TBSlider;
import dev1503.opentbui.widgets.TBToggle;
import dev1503.opentbui.widgets.TBWidget;

public class StatusManager {
    List<TBWidget> widgets = new ArrayList<>();
    Listener listener;

    public StatusManager() {}
    public StatusManager(Listener listener) {
        this.listener = listener;
    }
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void addWidget(TBWidget widget) {
        widgets.add(widget);
    }
    public void setValue(TBWidget _widget, String path, double value) {
        if (path == null) {
            return;
        }
        for (TBWidget widget : widgets) {
            if (widget.equals(_widget) || widget.getPath() == null || !widget.getPath().equals(path)) {
                continue;
            }
            if (widget instanceof TBToggle) {
                TBToggle toggle = (TBToggle) widget;
                if (toggle.getPath().equals(path) && toggle.isChecked() != value >= 1) {
                    toggle.setCheckedWithoutNotify(value >= 1);
                }
            } else if (widget instanceof TBSlider) {
                TBSlider slider = (TBSlider) widget;
                if (slider.getPath().equals(path) && slider.getValue() != value) {
                    slider.setValueWithoutNotify((float) value);
                }
            } else if (widget instanceof TBRangeSlider) {
                TBRangeSlider rangeSlider = (TBRangeSlider) widget;
                if (rangeSlider.getPath().equals(path) && rangeSlider.getValue() != value) {
                    rangeSlider.setValueWithoutNotify((float) value);
                }
            } else if (widget instanceof TBColor) {
                TBColor color = (TBColor) widget;
                if (color.getPath().equals(path) && color.getColor() != (int) value) {
                    color.setColorWithoutNotify((int) value);
                }
            }
        }
        if (listener != null) {
            listener.onValueChange(path, value);
        }
    }
    public void setValue(String path, double value) {
        setValue(null, path, value);
    }
    public void trigger(String path) {
        if (listener != null && path != null) {
            listener.onActionTrigger(path);
        }
    }

    public interface Listener {
        void onValueChange(String path, double value);
        void onActionTrigger(String path);
    }
}
