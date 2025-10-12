package dev1503.opentbui.widgets;

import static dev1503.opentbui.FeaturesAdapter.dp2px;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;

public class TBAction extends TBWidget{
    TextView textView;

    public TBAction(OpenTBUI openTBUI, String name, View.OnClickListener onClickListener) {
        super(openTBUI, name);
        view = (LinearLayout) LinearLayout.inflate(context, R.layout.list_action, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        ));
        textView = view.findViewWithTag("binding_1");
        textView.setText(name);
        view.setOnClickListener(onClickListener);
    }
    public TBAction(OpenTBUI openTBUI, String name) {
        this(openTBUI, name, null);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }
}
