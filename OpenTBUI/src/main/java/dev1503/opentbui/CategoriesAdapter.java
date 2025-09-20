package dev1503.opentbui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private Context context;
    private Category selectedCategory;
    private OnCategoryClickListener listener;

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;

        public CategoryViewHolder(TextView itemView) {
            super(itemView);
            textView = itemView;
            textView.setOnClickListener(this);
        }

        public void bind(Category category) {
            if (textView != null && category != null) {
                textView.setText(category.name);

                if (category.iconId != 0) {
                    @SuppressLint("UseCompatLoadingForDrawables") Drawable icon = context.getResources().getDrawable(category.iconId);
                    textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                }
                boolean isSelected = category.equals(selectedCategory);
                textView.setSelected(isSelected);
                float elevation = isSelected ?
                        context.getResources().getDimension(R.dimen.category_list_item_active_elevation) : 0.0f;
                textView.setElevation(elevation);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                Category category = categories.get(position);
                listener.onCategoryClick(category, position);
            }
        }
    }

    public CategoriesAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories != null ? categories : new java.util.ArrayList<>();
    }

    public void addCategory(Category category) {
        if (category != null && categories != null) {
            categories.add(category);
            notifyItemInserted(categories.size() - 1);
        }
    }

    public void setSelectedCategory(Category category) {
        this.selectedCategory = category;
        notifyDataSetChanged();
    }
    public void setSelectedCategory(int pos) {
        if (pos >= 0 && pos < categories.size()) {
            setSelectedCategory(categories.get(pos));
        }
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }
}