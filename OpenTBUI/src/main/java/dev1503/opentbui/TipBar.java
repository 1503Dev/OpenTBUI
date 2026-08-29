package dev1503.opentbui;

import static dev1503.opentbui.Utils.dp2px;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

/**
 * 底部提示栏，用于显示版权信息、提示文本或带图标的消息。
 */
public class TipBar {
    private final TextView textView;
    private final Context context;
    private int iconSize;

    public TipBar(TextView textView, Context context) {
        this.textView = textView;
        this.context = context;
        this.iconSize = dp2px(context, 16);
    }

    public TipBar setText(CharSequence text) {
        textView.setText(text);
        return this;
    }

    public TipBar show() {
        textView.setVisibility(View.VISIBLE);
        return this;
    }

    public TipBar hide() {
        textView.setVisibility(View.GONE);
        return this;
    }

    public TipBar setIcon(Icon icon) {
        Drawable drawable = icon != null ? icon.resolve(context) : null;
        if (drawable != null) {
            drawable.setBounds(0, 0, iconSize, iconSize);
        }
        textView.setCompoundDrawablesRelative(null, null, drawable, null);
        return this;
    }

    public TipBar setIconSize(int size) {
        this.iconSize = size;
        Drawable drawable = textView.getCompoundDrawablesRelative()[2];
        if (drawable != null) {
            drawable.setBounds(0, 0, size, size);
        }
        textView.setCompoundDrawablesRelative(null, null, drawable, null);
        return this;
    }

    public TipBar setIconSizeDp(int sizeDp) {
        setIconSize(dp2px(context, sizeDp));
        return this;
    }

    public TipBar removeIcon() {
        textView.setCompoundDrawablesRelative(null, null, null, null);
        return this;
    }

    public TipBar setIconPadding(int padding) {
        textView.setCompoundDrawablePadding(padding);
        return this;
    }

    public TextView getTextView() {
        return textView;
    }

    public TipBar setOnClickListener(View.OnClickListener onClickListener) {
        textView.setOnClickListener(onClickListener);
        return this;
    }
}
