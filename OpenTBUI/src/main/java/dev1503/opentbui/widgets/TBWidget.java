package dev1503.opentbui.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import dev1503.opentbui.OpenTBUI;

public class TBWidget {
    protected String name;
    protected ViewGroup view;
    protected OpenTBUI openTBUI;
    protected Context context;
    protected Activity activity;

    public TBWidget(OpenTBUI _openTBUI, String _name) {
        this.name = _name;
        this.openTBUI = _openTBUI;
        this.context = openTBUI.getActivity();
        context = openTBUI.getActivity();
        this.activity = openTBUI.getActivity();
        name = _name;
        openTBUI = _openTBUI;
        this.view = new LinearLayout(context);
    }

    public View getView() {
        return view;
    }
}
