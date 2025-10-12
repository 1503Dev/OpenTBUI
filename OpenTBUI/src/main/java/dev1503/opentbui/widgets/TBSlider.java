package dev1503.opentbui.widgets;

import static dev1503.opentbui.Utils.dp2px;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;
import java.util.List;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;

public class TBSlider extends TBWidget{
    Context context;
    TBSlider self = this;

    TextView textView;
    IndicatorSeekBar seekBar;

    private float[] values = {};

    OnValueChangeListener onValueChangeListener;

    public TBSlider(OpenTBUI openTBUI, String name, OnValueChangeListener seekChangeListener, float[] values) {
        super(openTBUI, name);
        context = openTBUI.getContext();
        view = (LinearLayout) LinearLayout.inflate(context, R.layout.list_slider, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp2px(context, 40)
        ));
        textView = view.findViewWithTag("binding_1");
        seekBar = view.findViewById(R.id.seekbar);
        setValues(values);
        textView.setText(name);
        this.onValueChangeListener = seekChangeListener;
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                if (seekParams.fromUser) {
                    if (onValueChangeListener != null) {
                        onValueChangeListener.onValueChange(self, self.values[(int)seekBar.getProgress()], (int)seekBar.getProgress());
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
    public TBSlider(OpenTBUI openTBUI, String name) {
        this(openTBUI, name, null, new float[0]);
    }
    public TBSlider(OpenTBUI openTBUI, String name, float[] values) {
        this(openTBUI, name, null, values);
    }
    public TBSlider(OpenTBUI openTBUI, String name, OnValueChangeListener seekChangeListener) {
        this(openTBUI, name, seekChangeListener, new float[0]);
    }
    public TBSlider(OpenTBUI openTBUI, String name, float[] values, OnValueChangeListener seekChangeListener) {
        this(openTBUI, name, seekChangeListener, values);
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
        List<Float> list = new ArrayList<>();
        for (float v : values) {
            list.add(v);
        }
        if (list.contains(value)) {
            seekBar.setProgress(list.indexOf(value));
        }
        return this;
    }
    public TBSlider setIndex(int index) {
        if (index >= 0 && index < values.length) {
            seekBar.setProgress(index);
        }
        return this;
    }
    public float getValue() {
        return values[seekBar.getProgress()];
    }
    public int getIndex() {
        return seekBar.getProgress();
    }
}
