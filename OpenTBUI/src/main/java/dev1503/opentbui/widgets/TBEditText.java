package dev1503.opentbui.widgets;

import static com.warkiz.widget.SizeUtils.dp2px;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;
import dev1503.opentbui.picker.TextInputting;

public class TBEditText extends TBWidget{
    AppCompatEditText editText;
    OnTextInputFinishListener onTextInputFinishListener;

    public TBEditText(OpenTBUI openTBUI, String name, CharSequence defaultText, OnTextInputFinishListener _onTextInputFinishListener) {
        super(openTBUI, name);
        view = new LinearLayout(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        this.onTextInputFinishListener = _onTextInputFinishListener;
        view.setMinimumHeight(dp2px(context, 40));
        view.setPadding(dp2px(context, 12), 0, dp2px(context, 12), 0);
        editText = (AppCompatEditText) View.inflate(context, R.layout.list_text_edit, null);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        view.addView(editText);
        editText.setHint(name);
        editText.setText(defaultText);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                new TextInputting(context, openTBUI.getTheme(), Objects.requireNonNull(editText.getText()).toString(), text -> {
                    editText.setText(text);
                    if (onTextInputFinishListener != null) {
                        onTextInputFinishListener.onTextInputFinish(text);
                    }
                }).setHint(name);
            }
        });
        editText.setOnClickListener(v -> {
            if (editText.hasFocus()) {
                new TextInputting(context, openTBUI.getTheme(), Objects.requireNonNull(editText.getText()).toString(), text -> {
                    editText.setText(text);
                    if (onTextInputFinishListener != null) {
                        onTextInputFinishListener.onTextInputFinish(text);
                    }
                }).setHint(name);
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
    public TBEditText(OpenTBUI openTBUI, String name, OnTextInputFinishListener onTextInputFinishListener) {
        this(openTBUI, name, "", onTextInputFinishListener);
    }
    public TBEditText(OpenTBUI openTBUI, OnTextInputFinishListener onTextInputFinishListener) {
        this(openTBUI, "", "", onTextInputFinishListener);
    }

    public TBEditText setOnTextInputFinishListener(OnTextInputFinishListener onTextInputFinishListener) {
        this.onTextInputFinishListener = onTextInputFinishListener;
        return this;
    }

    public interface OnTextInputFinishListener {
        void onTextInputFinish(String text);
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
    public AppCompatEditText getEditText() {
        return editText;
    }
}
