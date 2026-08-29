package dev1503.opentbui;

import static dev1503.opentbui.Utils.dp2px;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

public final class UIFactory {

    public static final String TAG_CATEGORIES = "_tb_categories";
    public static final String TAG_OPTIONS = "_tb_options";
    public static final String TAG_OPTIONS_CONTAINER = "_tb_options_container";
    public static final String TAG_REMAINING_TIME_TEXT = "_tb_remaining_time";
    public static final String TAG_EXTRA_BUTTONS = "_tb_extra_buttons";
    public static final String TAG_SEEKBAR = "_tb_seekbar";
    public static final String TAG_TOGGLE = "_tb_toggle";
    public static final String TAG_EDIT_TEXT = "_tb_edit_text";
    public static final String TAG_BACK = "_tb_back";
    public static final String TAG_DONE = "_tb_done";
    public static final String TAG_HEADER = "_tb_header";
    public static final String TAG_COLOR_PICKER_ROOT = "_tb_color_picker_root";
    public static final String TAG_PICKER = "_tb_picker";
    public static final String TAG_HUE = "_tb_hue";
    public static final String TAG_ALPHA = "_tb_alpha";
    public static final String TAG_VALUE_HEX = "_tb_value_hex";
    public static final String TAG_VALUE_R = "_tb_value_r";
    public static final String TAG_VALUE_G = "_tb_value_g";
    public static final String TAG_VALUE_B = "_tb_value_b";
    public static final String TAG_VALUE_A = "_tb_value_a";

    public static final String TAG_BINDING_1 = "binding_1";
    public static final String TAG_BINDING_2 = "binding_2";
    public static final String TAG_NAME = "name";
    public static final String TAG_ICON = "icon";
    public static final String TAG_VALUE = "value";
    public static final String TAG_TEXT = "text";

    private UIFactory() {}

    private static int resolveThemeColor(Context ctx, int attr) {
        TypedValue tv = new TypedValue();
        ctx.getTheme().resolveAttribute(attr, tv, true);
        if (tv.resourceId != 0) {
            return ContextCompat.getColor(ctx, tv.resourceId);
        }
        return tv.data;
    }

    private static void setSelectableItemBackground(Context ctx, View view) {
        TypedValue outValue = new TypedValue();
        if (ctx.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
                && outValue.resourceId != 0) {
            view.setBackgroundResource(outValue.resourceId);
        }
    }

    private static void setIcon(ImageView imageView, Icon icon) {
        if (icon == null) {
            return;
        }
        imageView.setImageDrawable(icon.resolve(imageView.getContext()));
    }


    public static LinearLayout createHorizontalRow(Context ctx, int heightDp) {
        LinearLayout row = new LinearLayout(ctx);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp2px(ctx, heightDp)));
        return row;
    }

    public static TextView createLabel(Context ctx, String tag) {
        TextView tv = new TextView(ctx);
        tv.setTag(tag);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        lp.gravity = Gravity.CENTER_VERTICAL;
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding(dp2px(ctx, 16), 0, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Body1);
        }
        tv.setTextColor(Color.WHITE);
        return tv;
    }

    /** list_toggle.xml */
    public static ViewGroup buildToggleLayout(Context ctx) {
        LinearLayout row = createHorizontalRow(ctx, 40);
        TextView label = createLabel(ctx, TAG_BINDING_1);
        SwitchCompat toggle = new SwitchCompat(ctx);
        toggle.setTag(TAG_TOGGLE);
        toggle.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        toggle.setPadding(dp2px(ctx, 16), 0, dp2px(ctx, 16), 0);
        row.addView(label);
        row.addView(toggle);
        return row;
    }

    /** list_slider.xml */
    public static ViewGroup buildSliderLayout(Context ctx) {
        LinearLayout row = createHorizontalRow(ctx, 40);
        TextView label = createLabel(ctx, TAG_BINDING_1);
        com.warkiz.widget.IndicatorSeekBar seekBar = new com.warkiz.widget.IndicatorSeekBar(ctx);
        seekBar.setTag(TAG_SEEKBAR);
        LinearLayout.LayoutParams sbParams = new LinearLayout.LayoutParams(
                dp2px(ctx, 120), ViewGroup.LayoutParams.WRAP_CONTENT);
        sbParams.gravity = Gravity.CENTER_VERTICAL;
        seekBar.setLayoutParams(sbParams);
        seekBar.setPadding(dp2px(ctx, 16), dp2px(ctx, 10), dp2px(ctx, 24), dp2px(ctx, 10));
        row.addView(label);
        row.addView(seekBar);
        return row;
    }

    /** list_action.xml */
    public static ViewGroup buildActionLayout(Context ctx) {
        LinearLayout row = createHorizontalRow(ctx, 40);
        setSelectableItemBackground(ctx, row);
        TextView label = createLabel(ctx, TAG_NAME);
        ImageView icon = new ImageView(ctx);
        icon.setTag(TAG_ICON);
        icon.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        icon.setPadding(dp2px(ctx, 16), 0, dp2px(ctx, 28), 0);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setIcon(icon, Icon.ICON_ARROW_FORWARD);
        icon.setImageTintList(android.content.res.ColorStateList.valueOf(0xFFFFFFFF));
        row.addView(label);
        row.addView(icon);
        return row;
    }

    /** list_color.xml */
    public static ViewGroup buildColorLayout(Context ctx) {
        LinearLayout row = createHorizontalRow(ctx, 40);
        setSelectableItemBackground(ctx, row);
        TextView label = createLabel(ctx, TAG_BINDING_1);
        ImageView colorIcon = new ImageView(ctx);
        colorIcon.setTag(TAG_BINDING_2);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconParams.gravity = Gravity.CENTER_VERTICAL;
        iconParams.setMargins(dp2px(ctx, 16), 0, dp2px(ctx, 16), 0);
        colorIcon.setLayoutParams(iconParams);
        colorIcon.setImageDrawable(buildColorCircleDrawable(ctx));
        row.addView(label);
        row.addView(colorIcon);
        return row;
    }

    /** list_drop_down.xml */
    public static ViewGroup buildDropDownLayout(Context ctx) {
        LinearLayout row = createHorizontalRow(ctx, 40);
        setSelectableItemBackground(ctx, row);
        TextView label = createLabel(ctx, TAG_BINDING_1);
        TextView value = new TextView(ctx);
        value.setTag(TAG_VALUE);
        LinearLayout.LayoutParams valueParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        valueParams.gravity = Gravity.CENTER_VERTICAL;
        value.setLayoutParams(valueParams);
        value.setGravity(Gravity.END);
        value.setPadding(0, 0, dp2px(ctx, 16), 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            value.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Body1);
        }
        value.setTextColor(Color.WHITE);
        ImageView arrow = new ImageView(ctx);
        arrow.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        arrow.setPadding(0, 0, dp2px(ctx, 28), 0);
        setIcon(arrow, Icon.ICON_ARROW_DROP_DOWN);
        arrow.setImageTintList(android.content.res.ColorStateList.valueOf(0xFFFFFFFF));
        row.addView(label);
        row.addView(value);
        row.addView(arrow);
        return row;
    }

    /** list_label.xml */
    public static ViewGroup buildLabelLayout(Context ctx) {
        LinearLayout row = createHorizontalRow(ctx, 40);
        TextView label = createLabel(ctx, TAG_TEXT);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        labelParams.gravity = Gravity.CENTER_VERTICAL;
        label.setLayoutParams(labelParams);
        label.setPadding(dp2px(ctx, 16), 0, dp2px(ctx, 24), 0);
        row.addView(label);
        return row;
    }

    /** list_divider.xml */
    public static ViewGroup buildDividerLayout(Context ctx) {
        LinearLayout row = createHorizontalRow(ctx, 16);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp2px(ctx, 16), 0, dp2px(ctx, 24), 0);

        View line1 = new View(ctx);
        line1.setLayoutParams(new LinearLayout.LayoutParams(dp2px(ctx, 8), dp2px(ctx, 1)));
        line1.setBackgroundColor(0x6CFFFFFF);
        row.addView(line1);

        TextView text = new TextView(ctx);
        text.setTag(TAG_TEXT);
        text.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        text.setTextColor(0x6CFFFFFF);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        text.setPadding(dp2px(ctx, 4), 0, dp2px(ctx, 4), 0);
        row.addView(text);

        View line2 = new View(ctx);
        line2.setLayoutParams(new LinearLayout.LayoutParams(0, dp2px(ctx, 1), 1f));
        line2.setBackgroundColor(0x6CFFFFFF);
        row.addView(line2);

        return row;
    }

    /** list_text_edit.xml */
    public static View buildEditTextLayout(Context ctx) {
        AppCompatEditText editText = new AppCompatEditText(ctx);
        editText.setTag("layout/list_text_edit_0");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(dp2px(ctx, 16), 0, dp2px(ctx, 16), 0);
        editText.setLayoutParams(lp);
        editText.setTextColor(Color.WHITE);
        return editText;
    }

    /** list_block_list.xml */
    public static ViewGroup buildBlockListLayout(Context ctx) {
        FlexboxLayout flex = new FlexboxLayout(ctx);
        flex.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        flex.setPadding(dp2px(ctx, 16), 0, dp2px(ctx, 16), 0);
        flex.setFlexWrap(com.google.android.flexbox.FlexWrap.WRAP);
        flex.setFlexDirection(com.google.android.flexbox.FlexDirection.ROW);
        return flex;
    }

    // ==================== toolbox_overlay.xml ====================

    public static ViewGroup buildToolboxOverlay(Context ctx) {
        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Left: Categories RecyclerView
        RecyclerView categoriesView = new RecyclerView(ctx);
        categoriesView.setTag(TAG_CATEGORIES);
        categoriesView.setBackgroundColor(0x80000000);
        categoriesView.setPadding(0, dp2px(ctx, 8), 0, dp2px(ctx, 8));
        categoriesView.setClipToPadding(false);
        categoriesView.setLayoutParams(new LinearLayout.LayoutParams(
                dp2px(ctx, 186), ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(categoriesView);

        // Center: FrameLayout (tip bar + extra buttons)
        FrameLayout centerArea = new FrameLayout(ctx);
        centerArea.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        root.addView(centerArea);

        TextView remainingTimeText = new TextView(ctx);
        remainingTimeText.setTag(TAG_REMAINING_TIME_TEXT);
        remainingTimeText.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams tipParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tipParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        tipParams.topMargin = dp2px(ctx, 16);
        remainingTimeText.setLayoutParams(tipParams);
        remainingTimeText.setBackground(buildSettingsIconBackground(ctx));
        remainingTimeText.setPadding(dp2px(ctx, 16), dp2px(ctx, 12), dp2px(ctx, 16), dp2px(ctx, 12));
        remainingTimeText.setCompoundDrawablePadding(dp2px(ctx, 8));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            remainingTimeText.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Body1);
        }
        centerArea.addView(remainingTimeText);

        FlexboxLayout extraButtons = new FlexboxLayout(ctx);
        extraButtons.setTag(TAG_EXTRA_BUTTONS);
        FrameLayout.LayoutParams extraParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        extraParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        extraButtons.setLayoutParams(extraParams);
        extraButtons.setPadding(0, 0, 0, dp2px(ctx, 16));
        extraButtons.setFlexWrap(com.google.android.flexbox.FlexWrap.WRAP);
        extraButtons.setFlexDirection(com.google.android.flexbox.FlexDirection.ROW);
        centerArea.addView(extraButtons);

        ScrollView scrollView = new ScrollView(ctx);
        scrollView.setTag(TAG_OPTIONS_CONTAINER);
        scrollView.setBackgroundColor(0x80000000);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(scrollView);

        LinearLayout optionsView = new LinearLayout(ctx);
        optionsView.setTag(TAG_OPTIONS);
        optionsView.setOrientation(LinearLayout.VERTICAL);
        optionsView.setLayoutParams(new LinearLayout.LayoutParams(
                dp2px(ctx, 240), ViewGroup.LayoutParams.WRAP_CONTENT));
        optionsView.setPadding(0, dp2px(ctx, 8), 0, dp2px(ctx, 8));
        optionsView.setClipToPadding(false);
        scrollView.addView(optionsView);

        return root;
    }

    // ==================== category_list_item.xml ====================

    @SuppressLint("UseCompatTextViewDrawableApis")
    public static TextView buildCategoryItem(Context ctx) {
        TextView tv = new TextView(ctx);
        tv.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setCompoundDrawablePadding(dp2px(ctx, 16));
        tv.setClickable(true);
        tv.setFocusable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Subhead);
        }
        int textColorDefault = resolveThemeColor(ctx, android.R.attr.textColorPrimary);
        tv.setTextColor(new android.content.res.ColorStateList(
                new int[][]{ new int[]{android.R.attr.state_selected}, new int[]{} },
                new int[]{ Color.WHITE, textColorDefault }
        ));
        int iconColorDefault = resolveThemeColor(ctx, android.R.attr.textColorSecondary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.setCompoundDrawableTintList(new ColorStateList(
                    new int[][]{ new int[]{android.R.attr.state_selected}, new int[]{} },
                    new int[]{ Color.WHITE, iconColorDefault }
            ));
        }
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_selected}, createCategoryActiveBg(ctx));
        bg.addState(new int[]{android.R.attr.state_pressed}, createCategoryActiveBg(ctx));
        bg.addState(new int[]{}, new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
        tv.setBackground(bg);
        tv.setPadding(dp2px(ctx, 16), dp2px(ctx, 12), dp2px(ctx, 16), dp2px(ctx, 12));
        return tv;
    }

    private static InsetDrawable createCategoryActiveBg(Context ctx) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.WHITE);
        shape.setCornerRadii(new float[]{
                0, 0, dp2px(ctx, 64), dp2px(ctx, 64),
                dp2px(ctx, 64), dp2px(ctx, 64), 0, 0
        });
        return new InsetDrawable(shape, 0, dp2px(ctx, 4), dp2px(ctx, 8), dp2px(ctx, 4));
    }

    // ==================== Drawable ====================

    /** settings_icon_background.xml */
    public static InsetDrawable buildSettingsIconBackground(Context ctx) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(dp2px(ctx, 128));
        shape.setColor(0x80000000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            shape.setPadding(dp2px(ctx, 8), dp2px(ctx, 8), dp2px(ctx, 8), dp2px(ctx, 8));
        }
        return new InsetDrawable(shape, dp2px(ctx, 4), dp2px(ctx, 4), dp2px(ctx, 4), dp2px(ctx, 4));
    }

    /** user_color_circle.xml */
    public static GradientDrawable buildColorCircleDrawable(Context ctx) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setSize(dp2px(ctx, 28), dp2px(ctx, 28));
        shape.setStroke(dp2px(ctx, 2), 0xFFCCCCCC);
        shape.setColor(Color.WHITE);
        return shape;
    }

    /** list_block_list_background.xml */
    public static StateListDrawable buildBlockListBackground() {
        StateListDrawable selector = new StateListDrawable();
        GradientDrawable selected = new GradientDrawable();
        selected.setShape(GradientDrawable.RECTANGLE);
        selected.setCornerRadius(400);
        selected.setColor(0x4066FFA6);
        selector.addState(new int[]{android.R.attr.state_selected}, selected);
        return selector;
    }


    /** dialog_color_picker.xml */
    public static View buildColorPickerDialog(Context ctx) {
        LinearLayout root = new LinearLayout(ctx);
        root.setTag(TAG_COLOR_PICKER_ROOT);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF222222);
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Header
        LinearLayout header = buildDialogHeader(ctx);
        root.addView(header);

        LinearLayout inputRow = new LinearLayout(ctx);
        inputRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams inputRowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inputRowParams.setMargins(dp2px(ctx, 12), dp2px(ctx, 8), dp2px(ctx, 12), 0);
        inputRow.setLayoutParams(inputRowParams);

        String[] hints = {"Hex", "R", "G", "B", "A"};
        String[] tags = {TAG_VALUE_HEX, TAG_VALUE_R, TAG_VALUE_G, TAG_VALUE_B, TAG_VALUE_A};
        int[] weights = {2, 1, 1, 1, 1};
        int[] maxLengths = {9, 3, 3, 3, 3};
        boolean[] isNumber = {false, true, true, true, true};
        for (int i = 0; i < hints.length; i++) {
            com.google.android.material.textfield.TextInputLayout til = new com.google.android.material.textfield.TextInputLayout(ctx);
            til.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, weights[i]));
            til.setHint(hints[i]);
            til.setDefaultHintTextColor(android.content.res.ColorStateList.valueOf(0x88FFFFFF));
            com.google.android.material.textfield.TextInputEditText et = new com.google.android.material.textfield.TextInputEditText(ctx);
            et.setTag(tags[i]);
            et.setTextColor(Color.WHITE);
            if (isNumber[i]) et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            et.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(maxLengths[i])});
            til.addView(et);
            inputRow.addView(til);
        }
        root.addView(inputRow);

        LinearLayout pickerRow = new LinearLayout(ctx);
        pickerRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams pickerRowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp2px(ctx, 200));
        pickerRowParams.setMargins(dp2px(ctx, 6), dp2px(ctx, 16), dp2px(ctx, 6), 0);
        pickerRow.setLayoutParams(pickerRowParams);

        dev1503.opentbui.view.ColorPicker picker = new dev1503.opentbui.view.ColorPicker(ctx);
        picker.setTag(TAG_PICKER);
        picker.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        picker.setPadding(dp2px(ctx, 10), dp2px(ctx, 10), dp2px(ctx, 10), dp2px(ctx, 10));
        pickerRow.addView(picker);

        dev1503.opentbui.view.ColorHuePicker hue = new dev1503.opentbui.view.ColorHuePicker(ctx);
        hue.setTag(TAG_HUE);
        hue.setLayoutParams(new LinearLayout.LayoutParams(dp2px(ctx, 42), ViewGroup.LayoutParams.MATCH_PARENT));
        hue.setPadding(dp2px(ctx, 10), dp2px(ctx, 10), dp2px(ctx, 10), dp2px(ctx, 10));
        pickerRow.addView(hue);

        dev1503.opentbui.view.ColorAlphaPicker alpha = new dev1503.opentbui.view.ColorAlphaPicker(ctx);
        alpha.setTag(TAG_ALPHA);
        alpha.setLayoutParams(new LinearLayout.LayoutParams(dp2px(ctx, 42), ViewGroup.LayoutParams.MATCH_PARENT));
        alpha.setPadding(dp2px(ctx, 10), dp2px(ctx, 10), dp2px(ctx, 10), dp2px(ctx, 10));
        pickerRow.addView(alpha);

        root.addView(pickerRow);
        return root;
    }

    /** dialog_text_inputting.xml */
    public static View buildTextInputDialog(Context ctx) {
        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF222222);
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout header = buildDialogHeader(ctx);
        root.addView(header);

        LinearLayout editRow = new LinearLayout(ctx);
        LinearLayout.LayoutParams editRowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editRowParams.setMargins(dp2px(ctx, 12), dp2px(ctx, 8), dp2px(ctx, 12), 0);
        editRow.setLayoutParams(editRowParams);

        androidx.appcompat.widget.AppCompatEditText editText = new androidx.appcompat.widget.AppCompatEditText(ctx);
        editText.setTag(TAG_EDIT_TEXT);
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        editText.setMaxLines(1);
        editText.setTextColor(Color.WHITE);
        editText.setHintTextColor(0x80FFFFFF);
        editText.setPadding(dp2px(ctx, 16), dp2px(ctx, 16), dp2px(ctx, 16), dp2px(ctx, 16));
        editRow.addView(editText);
        root.addView(editRow);
        return root;
    }

    /** dialog_item_selector.xml */
    public static View buildItemSelectorDialog(Context ctx) {
        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF222222);
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout header = buildDialogHeader(ctx, false);
        root.addView(header);

        androidx.core.widget.NestedScrollView scroll = new androidx.core.widget.NestedScrollView(ctx);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout list = new LinearLayout(ctx);
        list.setTag("list");
        list.setOrientation(LinearLayout.VERTICAL);
        list.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scroll.addView(list);
        root.addView(scroll);
        return root;
    }

    private static LinearLayout buildDialogHeader(Context ctx, boolean withDone) {
        LinearLayout header = new LinearLayout(ctx);
        header.setTag(TAG_HEADER);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setBackgroundColor(0x20000000);
        header.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageButton back = new ImageButton(ctx);
        back.setTag(TAG_BACK);
        back.setBackgroundColor(Color.TRANSPARENT);
        back.setLayoutParams(new LinearLayout.LayoutParams(dp2px(ctx, 52), dp2px(ctx, 52)));
        setIcon(back, Icon.ICON_ARROW_BACK);
        back.setContentDescription("Back");
        back.setImageTintList(android.content.res.ColorStateList.valueOf(0xFFFFFFFF));
        header.addView(back);

        View spacer = new View(ctx);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1f));
        header.addView(spacer);

        if (withDone) {
            Button done = new Button(ctx, null, androidx.appcompat.R.attr.buttonStyle);
            done.setTag(TAG_DONE);
            done.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            done.setText("Done");
            done.setBackgroundColor(Color.TRANSPARENT);
            header.addView(done);
        }

        return header;
    }

    private static LinearLayout buildDialogHeader(Context ctx) {
        return buildDialogHeader(ctx, true);
    }
}
