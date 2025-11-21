package dev1503.opentbui.widgets;
import static dev1503.opentbui.Utils.dp2px;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;

public class TBRangeSlider extends TBWidget{
    Context context;
    TBRangeSlider self = this;

    TextView textView;
    IndicatorSeekBar seekBar;

    OnValueChangeListener onValueChangeListener;
    boolean isSlideByUser = true;

    public TBRangeSlider(OpenTBUI openTBUI, String name, String path, float min, float max, int decimalScale, OnValueChangeListener seekChangeListener) {
        super(openTBUI, name, path);
        view = (LinearLayout) LinearLayout.inflate(openTBUI.getContext(), R.layout.list_slider, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(openTBUI.getContext(), 40)
        ));
        textView = view.findViewWithTag("binding_1");
        seekBar = view.findViewById(R.id.seekbar);
        textView.setText(name);
        this.onValueChangeListener = seekChangeListener;
        seekBar.setMin(min);
        seekBar.setMax(max);
        seekBar.setDecimalScale(decimalScale);
        seekBar.setTickCount(0);
        seekBar.mShowTickText = false;
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if (seekParams.fromUser) {
                    if (onValueChangeListener != null) {
                        onValueChangeListener.onValueChange(self, seekBar.getProgress());
                    }
                    if (openTBUI.getStatusManager() != null && isSlideByUser) {
                        openTBUI.getStatusManager().setValue(self, getPath(), seekBar.getProgress());
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
    }
    public TBRangeSlider(OpenTBUI openTBUI, String name, float min, float max, int decimalScale) {
        this(openTBUI, name, null, min, max, decimalScale, null);
    }
    public TBRangeSlider(OpenTBUI openTBUI, String name, String path, float min, float max) {
        this(openTBUI, name, path, min, max, 0, null);
    }
    public TBRangeSlider(OpenTBUI openTBUI, String name, float min, float max) {
        this(openTBUI, name, null, min, max, 0, null);
    }
    public TBRangeSlider(OpenTBUI openTBUI, String name, String path, float min, float max, OnValueChangeListener onValueChangeListener) {
        this(openTBUI, name, path, min, max, 0, onValueChangeListener);
    }
    public TBRangeSlider(OpenTBUI openTBUI, String name, float min, float max, OnValueChangeListener onValueChangeListener) {
        this(openTBUI, name, null, min, max, 0, onValueChangeListener);
    }

    public TBRangeSlider setOnValueChangeListener(OnValueChangeListener seekChangeListener) {
        this.onValueChangeListener = seekChangeListener;
        return this;
    }

    public IndicatorSeekBar getSeekBar() {
        return seekBar;
    }
    public interface OnValueChangeListener {
        void onValueChange(TBRangeSlider tbSlider, float value);
    }
    public TBRangeSlider setValueWithoutNotify(float value) {
        value = Math.max(value, seekBar.getMin());
        value = Math.min(value, seekBar.getMax());
        isSlideByUser = false;
        seekBar.setProgress((int) value);
        isSlideByUser = true;
        return this;
    }
    public TBRangeSlider setValue(float value) {
        value = Math.max(value, seekBar.getMin());
        value = Math.min(value, seekBar.getMax());
        seekBar.setProgress((int) value);
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChange(self, seekBar.getProgress());
        }
        return this;
    }
    public float getValue() {
        return seekBar.getProgress();
    }
    public TBRangeSlider setMin(float min) {
        seekBar.setMin(min);
        return this;
    }
    public TBRangeSlider setMax(float max) {
        seekBar.setMax(max);
        return this;
    }
    public TBRangeSlider setDecimalScale(int decimalScale) {
        seekBar.setDecimalScale(decimalScale);
        return this;
    }
    public TBRangeSlider setRange(float min, float max) {
        seekBar.setMin(min);
        seekBar.setMax(max);
        return this;
    }

    public String getName() {
        return name;
    }
    public TBRangeSlider setName(String name) {
        this.name = name;
        textView.setText(name);
        return this;
    }
}
