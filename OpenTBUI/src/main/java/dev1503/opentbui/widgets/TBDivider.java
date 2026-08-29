package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.UIFactory;

public class TBDivider extends TBWidget{
    TextView textView;

    public TBDivider(OpenTBUI openTBUI, String name) {
        super(openTBUI, name, null);
        view = UIFactory.buildDividerLayout(context);
        textView = (TextView) view.findViewWithTag(UIFactory.TAG_TEXT);
        textView.setText(name);
        if (name == null || name.isEmpty()) {
            textView.setVisibility(View.GONE);
        }

    }
    public TBDivider(OpenTBUI openTBUI) {
        this(openTBUI, null);
    }

    public String getName() {
        return name;
    }
    public TBDivider setName(String name) {
        this.name = name;
        textView.setText(name);
        return this;
    }
}
