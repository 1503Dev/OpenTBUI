package dev1503.opentbui.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

import dev1503.opentbui.BottomSheet;
import dev1503.opentbui.R;
import dev1503.opentbui.TBTheme;
import dev1503.opentbui.Utils;

public class TextInputting extends BottomSheet {
    ImageButton btnBack;
    Button btnDone;

    AppCompatEditText editText;

    @SuppressLint("SetTextI18n")
    public TextInputting(Context context, TBTheme theme, String defaultText, OnTextInputFinishListener onTextInputFinishListener){
        super(context, theme, (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_text_inputting, null));
        btnBack = contentView.findViewWithTag("binding_1");
        btnDone = contentView.findViewWithTag("binding_2");
        btnBack.setOnClickListener(view -> {
            sheet.cancel();
        });
        btnDone.setOnClickListener(view -> {
            onTextInputFinishListener.onTextInputFinish(Objects.requireNonNull(editText.getText()).toString());
            sheet.cancel();
        });
        btnDone.setTextColor(theme.getButtonTextColor());

        editText = contentView.findViewById(R.id.edit_text);
        editText.setText(defaultText);
        Utils.setEditTextUnderlineColor(editText, theme.getEditTextUnderlineColor());
        Utils.setEditTextCursorColor(editText, theme.getEditTextCursorColor());

        show();
    }

    public interface OnTextInputFinishListener{
        void onTextInputFinish(String text);
    }

    public TextInputting setHint(String hint){
        editText.setHint(hint);
        return this;
    }

    public TextInputting setText(String text){
        editText.setText(text);
        return this;
    }

    public TextInputting setMaxLines(int maxLines){
        editText.setMaxLines(maxLines);
        return this;
    }
}