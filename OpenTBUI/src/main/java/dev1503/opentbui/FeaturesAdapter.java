package dev1503.opentbui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import java.util.List;

import dev1503.opentbui.widgets.TBWidget;

public class FeaturesAdapter extends BaseAdapter {

    private List<TBWidget> featureViews;
    private Context context;

    public FeaturesAdapter(Context context, List<TBWidget> featureViews) {
        this.context = context;
        this.featureViews = featureViews != null ? featureViews : new java.util.ArrayList<>();
    }

    @Override
    public int getCount() {
        return featureViews != null ? featureViews.size() : 0;
    }

    @Override
    public TBWidget getItem(int position) {
        return featureViews != null && position >= 0 && position < featureViews.size() ?
                featureViews.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout container;
        TBWidget feature = getItem(position);

        if (convertView == null) {
            // 创建新的容器
            container = new FrameLayout(parent.getContext());
            container.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
        } else {
            // 复用现有容器
            container = (FrameLayout) convertView;
            // 先清除旧视图
            container.removeAllViews();
        }

        // 绑定新视图
        if (feature != null) {
            try {
                View featureView = feature.getView();
                if (featureView != null) {
                    // 如果视图已有父容器，先移除
                    if (featureView.getParent() != null) {
                        ((ViewGroup) featureView.getParent()).removeView(featureView);
                    }
                    container.addView(featureView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return container;
    }

    public void addFeature(TBWidget feature) {
        if (feature != null && featureViews != null) {
            featureViews.add(feature);
            notifyDataSetChanged();
        }
    }

    public TBWidget getFeature(int position) {
        return getItem(position);
    }

    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}