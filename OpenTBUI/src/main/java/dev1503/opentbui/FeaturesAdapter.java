package dev1503.opentbui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev1503.opentbui.widgets.TBWidget;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder> {

    private List<TBWidget> featureViews;
    private Context context;

    @Override
    public FeatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout container = new FrameLayout(parent.getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new FeatureViewHolder(container);
    }

    @Override
    public void onBindViewHolder(FeatureViewHolder holder, int position) {
        TBWidget feature = featureViews.get(position);
        holder.bind(feature, position);
    }

    @Override
    public int getItemCount() {
        return featureViews != null ? featureViews.size() : 0;
    }

    @Override
    public void onViewRecycled(FeatureViewHolder holder) {
        holder.unbind();
        super.onViewRecycled(holder);
    }

    public class FeatureViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout layout;
        private TBWidget currentFeature;

        public FeatureViewHolder(FrameLayout itemView) {
            super(itemView);
            layout = itemView;
        }

        public void bind(TBWidget feature, int position) {
            unbind();
            currentFeature = feature;

            if (feature != null && layout != null) {
                try {
                    View featureView = feature.getView();
                    if (featureView != null && featureView.getParent() == null) {
                        layout.addView(featureView);
                    } else if (featureView != null && featureView.getParent() != layout) {
                        if (featureView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) featureView.getParent()).removeView(featureView);
                        }
                        layout.addView(featureView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void unbind() {
            if (layout != null) {
                layout.removeAllViews();
            }
            currentFeature = null;
        }
    }

    public FeaturesAdapter(Context context, List<TBWidget> featureViews) {
        this.context = context != null ? context : null;
        this.featureViews = featureViews != null ? featureViews : new java.util.ArrayList<>();
    }

    public void addFeature(TBWidget feature) {
        if (feature != null && featureViews != null) {
            int position = featureViews.size();
            featureViews.add(feature);
            notifyItemInserted(position);
        }
    }
    public TBWidget getFeature(int position) {
        if (featureViews != null && position >= 0 && position < featureViews.size()) {
            return featureViews.get(position);
        }
        return null;
    }
    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}