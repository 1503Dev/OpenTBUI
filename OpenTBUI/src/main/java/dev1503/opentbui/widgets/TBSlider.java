package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.UIFactory;

public class TBSlider extends TBWidget{
    private Context context;

    private TextView textView;
    private IndicatorSeekBar seekBar;

    private float[] values = {};

    private OnValueChangeListener onValueChangeListener;
    private boolean isSlideByUser = true;

    public TBSlider(OpenTBUI openTBUI, String name, String path, OnValueChangeListener seekChangeListener, float[] values) {
        super(openTBUI, name, path);
        context = openTBUI.getContext();
        view = UIFactory.buildSliderLayout(context);
        textView = (TextView) view.findViewWithTag(UIFactory.TAG_BINDING_1);
        seekBar = (IndicatorSeekBar) view.findViewWithTag(UIFactory.TAG_SEEKBAR);
        setValues(values);
        textView.setText(name);
        this.onValueChangeListener = seekChangeListener;
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if (seekParams.fromUser) {
                    int progress = (int) seekBar.getProgress();
                    if (progress >= 0 && progress < values.length) {
                        if (onValueChangeListener != null) {
                            onValueChangeListener.onValueChange(TBSlider.this, values[progress], progress);
                        }
                        if (openTBUI.getStatusManager() != null && isSlideByUser) {
                            openTBUI.getStatusManager().setValue(TBSlider.this, getPath(), values[progress]);
                        }
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
    public TBSlider(OpenTBUI openTBUI, String name, String path) {
        this(openTBUI, name, path, null, new float[0]);
    }
    public TBSlider(OpenTBUI openTBUI, String name) {
        this(openTBUI, name, null, null, new float[0]);
    }
    public TBSlider(OpenTBUI openTBUI, String name, String path, float[] values) {
        this(openTBUI, name, path, null, values);
    }
    public TBSlider(OpenTBUI openTBUI, String name, float[] values) {
        this(openTBUI, name, null, values);
    }
    public TBSlider(OpenTBUI openTBUI, String name, OnValueChangeListener seekChangeListener) {
        this(openTBUI, name, null, seekChangeListener, new float[0]);
    }
    public TBSlider(OpenTBUI openTBUI, String name, float[] values, OnValueChangeListener seekChangeListener) {
        this(openTBUI, name, null, seekChangeListener, values);
    }

    public TBSlider setOnValueChangeListener(OnValueChangeListener seekChangeListener) {
        this.onValueChangeListener = seekChangeListener;
        return this;
    }

    public IndicatorSeekBar getSeekBar() {
        return seekBar;
    }

    public TBSlider setValues(float[] values) {
        seekBar.setMin(0);
        if (values.length <= 0) {
            this.values = new float[]{0};
            seekBar.setMax(0);
            return this;
        }
        this.values = values;
        seekBar.setMax(values.length - 1);
        String[] strings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            strings[i] = String.valueOf(values[i]);
            if (strings[i].endsWith(".0")) {
                strings[i] = strings[i].substring(0, strings[i].length() - 2);
            }
        }
        seekBar.customTickTexts(strings);
        seekBar.setIndicatorTextFormat("${TICK_TEXT}");
        seekBar.setTickCount(values.length);
        return this;
    }

    public interface OnValueChangeListener {
        void onValueChange(TBSlider tbSlider, float value, int index);
    }

    public TBSlider setValue(float value) {
        int index = indexOfValue(value);
        if (index >= 0) {
            seekBar.setProgress(index);
        }
        return this;
    }
    public TBSlider setValueWithoutNotify(float value) {
        int index = indexOfValue(value);
        if (index >= 0) {
            isSlideByUser = false;
            seekBar.setProgress(index);
            isSlideByUser = true;
        }
        return this;
    }
    private int indexOfValue(float value) {
        for (int i = 0; i < values.length; i++) {
            if (Float.compare(values[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }
    public TBSlider setIndex(int index) {
        if (index >= 0 && index < values.length) {
            seekBar.setProgress(index);
        }
        return this;
    }
    public float getValue() {
        int index = seekBar.getProgress();
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        return 0f;
    }

    @Override
    public void syncValue(double value) {
        if (Double.compare(getValue(), value) != 0) {
            setValueWithoutNotify((float) value);
        }
    }
    public int getIndex() {
        return seekBar.getProgress();
    }

    public String getName() {
        return name;
    }
    public TBSlider setName(String name) {
        this.name = name;
        textView.setText(name);
        return this;
    }
}
