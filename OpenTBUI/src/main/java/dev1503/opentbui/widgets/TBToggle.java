package dev1503.opentbui.widgets;

import static dev1503.opentbui.FeaturesAdapter.dp2px;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import dev1503.opentbui.FeaturesAdapter;
import dev1503.opentbui.R;

public class TBToggle extends TBWidget{
    TextView textView;
    SwitchCompat switchCompat;

    public TBToggle(Context context, String name) {
        super(context, name);
        view = (LinearLayout) LinearLayout.inflate(context, R.layout.list_toggle, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        ));
        textView = view.findViewWithTag("binding_1");
        switchCompat = view.findViewWithTag("binding_2");
        textView.setText(name);
    }
}
