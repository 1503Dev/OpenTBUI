package dev1503.opentbui.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import dev1503.opentbui.OpenTBUI;

public class TBWidget {
    protected String name;
    protected String path;
    protected ViewGroup view;
    protected OpenTBUI openTBUI;
    protected Context context;
    protected Activity activity;

    public TBWidget(OpenTBUI _openTBUI, String _name, String _path) {
        this.name = _name;
        this.path = _path;
        this.openTBUI = _openTBUI;
        this.context = _openTBUI.getActivity();
        this.activity = _openTBUI.getActivity();
        this.view = new LinearLayout(context);
    }
    public TBWidget(OpenTBUI _openTBUI, String _name) {
        this(_openTBUI, _name, null);
    }

    public View getView() {
        return view;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 从 StatusManager 同步值到此 widget（不触发回调）。
     * 子类应重写此方法以实现各自的同步逻辑。
     *
     * @param value 要同步的值
     */
    public void syncValue(double value) {
        // 默认不做任何操作，子类按需重写
    }
}
