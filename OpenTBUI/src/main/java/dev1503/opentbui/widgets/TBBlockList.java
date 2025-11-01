package dev1503.opentbui.widgets;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev1503.opentbui.OpenTBUI;
import dev1503.opentbui.R;
import dev1503.opentbui.Utils;
import dev1503.opentbui.view.CircleSwitch;
import dev1503.opentbui.view.Cube3DView;

import com.google.android.flexbox.FlexboxLayout;

public class TBBlockList extends TBWidget{
    OnSelectedItemChangeListener onSelectedItemChangeListener;
    Map<String, CircleSwitch> circleSwitches = new HashMap<>();

    public TBBlockList(OpenTBUI openTBUI, String path, OnSelectedItemChangeListener listener) {
        super(openTBUI, "", path);
        onSelectedItemChangeListener = listener;
        view = (FlexboxLayout) LinearLayout.inflate(context, R.layout.list_block_list, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
    }
    public TBBlockList(OpenTBUI openTBUI, String path) {
        this(openTBUI, path, null);
    }
    public TBBlockList(OpenTBUI openTBUI, OnSelectedItemChangeListener listener) {
        this(openTBUI, null, listener);
    }
    public TBBlockList(OpenTBUI openTBUI) {
        this(openTBUI, null, null);
    }

    public TBBlockList addItem(String id, Bitmap[] textures){
        CircleSwitch circleSwitch = new CircleSwitch(context);
        Cube3DView cube3DView = new Cube3DView(context);
        cube3DView.setTextures(textures);
        circleSwitch.setContentView(cube3DView);
//        circleSwitch.setLayoutParams(new ViewGroup.LayoutParams(
//                Utils.dpToPx(context, 40),
//                Utils.dpToPx(context, 40)
//        ));
        circleSwitch.setSwitchListener(isOn -> {
            if (onSelectedItemChangeListener != null) {
                List<String> selectedIds = new ArrayList<>();
                for (Map.Entry<String, CircleSwitch> entry : circleSwitches.entrySet()) {
                    if (entry.getValue().isOn()) {
                        selectedIds.add(entry.getKey());
                    }
                }
                onSelectedItemChangeListener.onSelectedItemChange(selectedIds.toArray(new String[0]));
            }
        });
        view.addView(circleSwitch);
        circleSwitches.put(id, circleSwitch);
        return this;
    }
    public TBBlockList addItem(String id, Bitmap textureTop, Bitmap textureLeft, Bitmap textureRight){
        return addItem(id, new Bitmap[]{textureTop, textureLeft, textureRight});
    }
    public TBBlockList addItem(String id, Bitmap textureTop, Bitmap textureSide){
        return addItem(id, new Bitmap[]{textureTop, textureSide, textureSide});
    }
     public TBBlockList addItem(String id, Bitmap texture){
        return addItem(id, new Bitmap[]{texture, texture, texture});
    }

    public TBBlockList setOnSelectedItemChangeListener(OnSelectedItemChangeListener listener) {
        this.onSelectedItemChangeListener = listener;
        return this;
    }

    public interface OnSelectedItemChangeListener {
        void onSelectedItemChange(String[] selectedIds);
    }

    public CircleSwitch getCircleSwitch(String id){
        return circleSwitches.get(id);
    }
    public CircleSwitch[] getCircleSwitches(){
        return circleSwitches.values().toArray(new CircleSwitch[0]);
    }
    public TBBlockList setItemSelectedWithoutNotify(String id, boolean isSelected){
        CircleSwitch circleSwitch = getCircleSwitch(id);
        if (circleSwitch != null) {
            circleSwitch.setOn(isSelected);
        }
        return this;
    }
    public TBBlockList setItemSelected(String id, boolean isSelected){
        setItemSelectedWithoutNotify(id, isSelected);
        if (onSelectedItemChangeListener != null) {
            onSelectedItemChangeListener.onSelectedItemChange(new String[]{id});
        }
        return this;
    }
}
