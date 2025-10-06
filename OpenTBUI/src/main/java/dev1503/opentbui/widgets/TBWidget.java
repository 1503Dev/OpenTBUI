package dev1503.opentbui.widgets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TBWidget {
    String name;
    ViewGroup view;

    public TBWidget(Context context, String name) {
        this.name = name;
        view = new LinearLayout(context);
    }

    public View getView() {
        return view;
    }
}
