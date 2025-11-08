package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;

public class TBDropDown extends TBWidget{
    TextView textView;
    TBDropDown.OnItemSelectedListener listener;
    String[] items;
    int position;

    public TBDropDown(OpenTBUI openTBUI, String path, String[] items, int defaultPosition, TBDropDown.OnItemSelectedListener listener) {
        super(openTBUI, null, path);
        view = (LinearLayout) LinearLayout.inflate(context, R.layout.list_drop_down, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        ));
        textView = view.findViewWithTag("binding_1");
        textView.setText("");
        this.items = items;
        this.position = Math.max(Math.min(defaultPosition, items.length - 1), 0);
        this.listener = listener;
        view.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            for (int i = 0; i < items.length; i++) {
                popupMenu.getMenu().add(0, i, i, items[i]);
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                position = item.getOrder();
                textView.setText(item.getTitle().toString());
                if (listener != null) {
                    listener.onItemSelected(TBDropDown.this, position, item.getTitle().toString());
                }
                if (openTBUI.getStatusManager() != null) {
                    openTBUI.getStatusManager().setValue(TBDropDown.this, getPath(), position);
                }
                return true;
            });
            popupMenu.show();
        });
        selectItemWithoutNotify(defaultPosition);
    }
    public TBDropDown (OpenTBUI openTBUI, String path, String[] items) {
        this(openTBUI, path, items, 0, null);
    }
    public TBDropDown (OpenTBUI openTBUI, String[] items) {
        this(openTBUI, null, items, 0, null);
    }

    public void selectItemWithoutNotify(int position) {
        if (position < 0 || position >= items.length) {
            return;
        }
        this.position = position;
        textView.setText(items[position]);
    }
    public void selectItem(int position) {
        selectItemWithoutNotify(position);
        if (listener != null) {
            listener.onItemSelected(TBDropDown.this, position, items[position]);
        }
        if (openTBUI.getStatusManager() != null) {
            openTBUI.getStatusManager().setValue(TBDropDown.this, getPath(), position);
        }
    }
    public int getPosition() {
        return position;
    }
    public String getText() {
        return textView.getText().toString();
    }

    public void setOnItemSelectedListener(TBDropDown.OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(TBDropDown dropDown, int position, String itemName);
    }
}
