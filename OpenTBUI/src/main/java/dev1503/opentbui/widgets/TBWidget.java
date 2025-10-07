package dev1503.opentbui.widgets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import dev1503.opentbui.OpenTBUI;

public class TBWidget {
    public String name;
    public ViewGroup view;
    public OpenTBUI openTBUI;
    public Context context;

    public TBWidget(OpenTBUI _openTBUI, String _name) {
        this.name = _name;
        this.openTBUI = _openTBUI;
        this.context = openTBUI.getActivity();
        context = openTBUI.getActivity();
        name = _name;
        openTBUI = _openTBUI;
        this.view = new LinearLayout(context);
    }

    public View getView() {
        return view;
    }
}
