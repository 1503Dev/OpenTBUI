package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;

public class TBLabel extends TBWidget{
    TextView textView;

    public TBLabel(OpenTBUI openTBUI, String name) {
        super(openTBUI, name, null);
        view = (LinearLayout) LinearLayout.inflate(context, R.layout.list_label, null);
        textView = view.findViewWithTag("text");
        textView.setText(name);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        );
        view.setLayoutParams(layoutParams);
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
