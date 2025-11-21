package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;

public class TBAction extends TBWidget{
    TextView textView;
    View.OnClickListener listener;

    public TBAction(OpenTBUI openTBUI, String name, String path, View.OnClickListener onClickListener) {
        super(openTBUI, name, path);
        view = (LinearLayout) LinearLayout.inflate(context, R.layout.list_action, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        ));
        textView = view.findViewWithTag("binding_1");
        textView.setText(name);
        this.listener = onClickListener;
        view.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(view);
            }
            if  (openTBUI.getStatusManager() != null) {
                openTBUI.getStatusManager().trigger(getPath());
            }
        });
    }
    public TBAction(OpenTBUI openTBUI, String name, View.OnClickListener onClickListener) {
        this(openTBUI, name, null, onClickListener);
    }
    public TBAction(OpenTBUI openTBUI, String name, String path) {
        this(openTBUI, name, path, null);
    }
    public TBAction(OpenTBUI openTBUI, String name) {
        this(openTBUI, name, null, null);
    }

    public TBAction setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public String getName() {
        return name;
    }
    public TBAction setName(String name) {
        this.name = name;
        textView.setText(name);
        return this;
    }
}
