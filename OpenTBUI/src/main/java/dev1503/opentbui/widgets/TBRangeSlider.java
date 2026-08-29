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
import dev1503.opentbui.UIFactory;

public class TBRangeSlider extends TBWidget{
    private Context context;

    private TextView textView;
    private IndicatorSeekBar seekBar;

    private OnValueChangeListener onValueChangeListener;
    private boolean isSlideByUser = true;

    public TBRangeSlider(OpenTBUI openTBUI, String name, String path, float min, float max, int decimalScale, OnValueChangeListener seekChangeListener) {
        super(openTBUI, name, path);
        view = UIFactory.buildSliderLayout(openTBUI.getContext());
        textView = (TextView) view.findViewWithTag(UIFactory.TAG_BINDING_1);
        seekBar = (IndicatorSeekBar) view.findViewWithTag(UIFactory.TAG_SEEKBAR);
        textView.setText(name);
        this.onValueChangeListener = seekChangeListener;
        seekBar.setMin(min);
        seekBar.setMax(max);
        seekBar.setDecimalScale(decimalScale);
        seekBar.setTickCount(0);
        seekBar.setShowTickText(false);
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if (seekParams.fromUser) {
                    if (onValueChangeListener != null) {
                        onValueChangeListener.onValueChange(TBRangeSlider.this, seekParams.progressFloat);
                    }
                    if (openTBUI.getStatusManager() != null && isSlideByUser) {
                        openTBUI.getStatusManager().setValue(TBRangeSlider.this, getPath(), seekParams.progressFloat);
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
        seekBar.setProgress(value);
        isSlideByUser = true;
        return this;
    }
    public TBRangeSlider setValue(float value) {
        value = Math.max(value, seekBar.getMin());
        value = Math.min(value, seekBar.getMax());
        seekBar.setProgress(value);
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChange(TBRangeSlider.this, seekBar.getProgressFloat());
        }
        return this;
    }
    public float getValue() {
        return seekBar.getProgressFloat();
    }

    @Override
    public void syncValue(double value) {
        if (Double.compare(getValue(), value) != 0) {
            setValueWithoutNotify((float) value);
        }
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
