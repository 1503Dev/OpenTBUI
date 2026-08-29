package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.UIFactory;

public class TBLabel extends TBWidget{
    TextView textView;

    public TBLabel(OpenTBUI openTBUI, String name) {
        super(openTBUI, name, null);
        view = UIFactory.buildLabelLayout(context);
        textView = (TextView) view.findViewWithTag(UIFactory.TAG_TEXT);
        textView.setText(name);

    }
    public TBLabel(OpenTBUI openTBUI) {
        this(openTBUI, null);
    }

    public void setText(CharSequence text) {
        name = text.toString();
        textView.setText(text);
    }
    public String getText() {
        return name;
    }
}
