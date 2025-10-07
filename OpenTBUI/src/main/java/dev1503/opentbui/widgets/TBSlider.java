package dev1503.opentbui.widgets;

import static dev1503.opentbui.FeaturesAdapter.dp2px;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import dev1503.opentbui.R;

public class TBSlider extends TBWidget{
    TextView textView;
    IndicatorSeekBar seekBar;

    float[] values;

    OnValueChangeListener onValueChangeListener;

    public TBSlider(Context context, String name, OnValueChangeListener seekChangeListener, float[] values) {
        super(context, name);
        view = (LinearLayout) LinearLayout.inflate(context, R.layout.list_slider, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        ));
        textView = view.findViewWithTag("binding_1");
        seekBar = view.findViewById(R.id.seekbar);
        textView.setText(name);
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if (seekParams.fromUser) {
                    if (onValueChangeListener != null) {
                        onValueChangeListener.onValueChange(seekBar, values[seekBar.getProgress()], seekBar.getProgress());
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
        setValues(values);
    }
    public TBSlider(Context context, String name) {
        this(context, name, null, new float[0]);
    }

    public TBSlider setOnSeekChangeListener(OnValueChangeListener seekChangeListener) {
        this.onValueChangeListener = seekChangeListener;
        return this;
    }

    public IndicatorSeekBar getSeekBar() {
        return seekBar;
    }

    public TBSlider setValues(float[] values) {
        seekBar.setMin(0);
        if (values.length <= 0) {
            seekBar.setMax(0);
            return this;
        }
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
        this.values = values;
        return this;
    }

    public interface OnValueChangeListener {
        void onValueChange(IndicatorSeekBar seekBar, float value, int index);
    }
}
