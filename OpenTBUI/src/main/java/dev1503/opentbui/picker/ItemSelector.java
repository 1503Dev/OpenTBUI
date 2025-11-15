package dev1503.opentbui.picker;

import static dev1503.opentbui.Utils.dp2px;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

import dev1503.opentbui.BottomSheet;
import dev1503.opentbui.R;
import dev1503.opentbui.TBTheme;
import dev1503.opentbui.Utils;

public class ItemSelector extends BottomSheet {
    ImageButton btnBack;
    LinearLayout list;

    @SuppressLint("SetTextI18n")
    public ItemSelector(Context context, TBTheme theme, String[] items, OnItemSelectListener onItemSelectListener){
        super(context, theme, (LinearLayout) LinearLayout.inflate(context, R.layout.dialog_item_selector, null));
        btnBack = contentView.findViewWithTag("back");
        btnBack.setOnClickListener(view -> {
            sheet.cancel();
        });
        list = contentView.findViewWithTag("list");

        for(int i = 0; i < items.length; i++){
            String item = items[i];
            LinearLayout layout = new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp2px(context, 40)
            );
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            layout.setLayoutParams(layoutParams);
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray typedArray = context.obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            typedArray.recycle();
            layout.setBackgroundResource(backgroundResource);
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textViewParams.gravity = Gravity.CENTER_VERTICAL;
            textViewParams.setMargins(dp2px(context, 16), 0, dp2px(context, 24), 0);
            TextView textView = new TextView(context);
            textView.setTextAppearance(context, androidx.appcompat.R.style.TextAppearance_AppCompat_Body1);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setLayoutParams(textViewParams);
            layout.addView(textView);

            textView.setText(item);
            int finalI = i;
            layout.setOnClickListener(view -> {
                sheet.dismiss();
                onItemSelectListener.onItemSelect(finalI, items);
            });

            list.addView(layout);
        }
        show();
    }

    public interface OnItemSelectListener{
        void onItemSelect(int index, String[] items);
    }
}