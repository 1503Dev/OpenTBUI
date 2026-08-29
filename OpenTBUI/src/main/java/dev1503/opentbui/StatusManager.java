package dev1503.opentbui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev1503.opentbui.widgets.TBWidget;

public class StatusManager {
    private final List<TBWidget> widgets = new ArrayList<>();
    private Listener listener;
    private final Map<String, Double> sdKv = new ConcurrentHashMap<>();

    public StatusManager() {}
    public StatusManager(Listener listener) {
        this.listener = listener;
    }
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public synchronized void addWidget(TBWidget widget) {
        widgets.add(widget);
    }
    public synchronized void setValue(TBWidget _widget, String path, double value) {
        if (path == null) {
            return;
        }
        setValueOnly(path, value);
        for (TBWidget widget : widgets) {
            if (widget.equals(_widget) || widget.getPath() == null || !widget.getPath().equals(path)) {
                continue;
            }
            widget.syncValue(value);
        }
        if (listener != null) {
            listener.onValueChange(path, value);
        }
    }
    public void setValue(String path, double value) {
        setValue(null, path, value);
    }
    public synchronized void trigger(String path) {
        if (listener != null && path != null) {
            listener.onActionTrigger(path);
        }
    }

    public void setValueOnly(String path, double value) {
        if (path == null) {
            return;
        }
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
