package dev1503.opentbui.widgets;

import static dev1503.opentbui.FeaturesAdapter.dp2px;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import dev1503.opentbui.FeaturesAdapter;
import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;

public class TBEditText extends TBWidget{
    EditText editText;
    OnTextChangeListener onTextChangeListener;

    public TBEditText(OpenTBUI openTBUI, String name, CharSequence defaultText, OnTextChangeListener onTextChangeListener) {
        super(openTBUI, name);
        view = new LinearLayout(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        view.setMinimumHeight(dp2px(context, 40));
        view.setPadding(dp2px(context, 16), 0, dp2px(context, 16), 0);
        editText = (EditText) View.inflate(context, R.layout.list_text_edit, null);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        editText.setFocusable(true);
        view.addView(editText);
        editText.setHint(name);
        editText.setText(defaultText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (onTextChangeListener != null) {
                    onTextChangeListener.onTextChanged(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public TBEditText(OpenTBUI openTBUI, String name) {
        this(openTBUI, name, "", null);
    }
    public TBEditText(OpenTBUI openTBUI) {
        this(openTBUI, "", "", null);
    }
    public TBEditText(OpenTBUI openTBUI, String name, CharSequence defaultText) {
        this(openTBUI, name, defaultText, null);
    }
    public TBEditText(OpenTBUI openTBUI, String name, OnTextChangeListener onTextChangeListener) {
        this(openTBUI, name, "", onTextChangeListener);
    }
    public TBEditText(OpenTBUI openTBUI, OnTextChangeListener onTextChangeListener) {
        this(openTBUI, "", "", onTextChangeListener);
    }

    public TBEditText setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
        return this;
    }

    public interface OnTextChangeListener {
        void onTextChanged(String text);
    }
    public String getText() {
        return editText.getText().toString();
    }
    public TBEditText setText(String text) {
        editText.setText(text);
        return this;
    }
    public TBEditText setMaxLines(int maxLines) {
        editText.setMaxLines(maxLines);
        return this;
    }
    public EditText getEditText() {
        return editText;
    }
}
