package dev1503.opentbui;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev1503.opentbui.widgets.TBColor;
import dev1503.opentbui.widgets.TBDropDown;
import dev1503.opentbui.widgets.TBRangeSlider;
import dev1503.opentbui.widgets.TBSlider;
import dev1503.opentbui.widgets.TBToggle;
import dev1503.opentbui.widgets.TBWidget;

public class StatusManager {
    List<TBWidget> widgets = new ArrayList<>();
    Listener listener;
    Map<String, Double> sdKv = new HashMap<>();

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
        setValueOnly(path, value);
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
            } else if (widget instanceof TBDropDown) {
                TBDropDown dropDown = (TBDropDown) widget;
                if (dropDown.getPath().equals(path) && dropDown.getPosition() != value) {
                    dropDown.selectItemWithoutNotify((int) value);
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

    public void setValueOnly(String path, double value) {
        sdKv.put(path, value);
    }

    public double getDouble(String path, double defaultValue) {
        if (sdKv.containsKey(path)) {
            try {
                return sdKv.get(path);
            } catch (NullPointerException ignored) {}
        }
        return defaultValue;
    }
    public double getDouble(String path) {
        return getDouble(path, 0.0);
    }
    public int getInt(String path, int defaultValue) {
        return (int) getDouble(path, defaultValue);
    }
    public int getInt(String path) {
        return getInt(path, 0);
    }
    public boolean getBoolean(String path, boolean defaultValue) {
        return getDouble(path, defaultValue ? 1.0 : 0.0) >= 1.0;
    }
    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }
    public float getFloat(String path, float defaultValue) {
        return (float) getDouble(path, defaultValue);
    }
    public float getFloat(String path) {
        return getFloat(path, 0.0f);
    }

    public interface Listener {
        void onValueChange(String path, double value);
        void onActionTrigger(String path);
    }
}
