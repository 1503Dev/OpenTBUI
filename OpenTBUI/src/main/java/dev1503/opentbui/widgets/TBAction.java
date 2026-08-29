package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev1503.opentbui.Icon;
import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.UIFactory;

public class TBAction extends TBWidget{
    TextView textView;
    View.OnClickListener listener;
    ImageView iconView;
    View.OnLongClickListener longClickListener;

    public TBAction(OpenTBUI openTBUI, String name, String path, View.OnClickListener onClickListener) {
        super(openTBUI, name, path);
        view = UIFactory.buildActionLayout(context);
        textView = (TextView) view.findViewWithTag(UIFactory.TAG_NAME);
        textView.setText(name);
        iconView = (ImageView) view.findViewWithTag(UIFactory.TAG_ICON);
        this.listener = onClickListener;
        view.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(view);
            }
            if  (openTBUI.getStatusManager() != null) {
                openTBUI.getStatusManager().trigger(getPath());
            }
        });
        view.setOnLongClickListener(view -> {
            if (longClickListener != null) {
                return longClickListener.onLongClick(view);
            }
            return false;
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
    public TBAction setOnLongClickListener(View.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
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

    public TBAction setIcon(Icon icon) {
        if (icon != null) {
            iconView.setImageDrawable(icon.resolve(iconView.getContext()));
        }
        return this;
    }
}
