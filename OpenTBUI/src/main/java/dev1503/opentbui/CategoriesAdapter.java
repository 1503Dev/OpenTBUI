package dev1503.opentbui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static dev1503.opentbui.Utils.dp2px;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private Context context;
    private Category selectedCategory;
    private OnCategoryClickListener listener;
    private OnCategoryChangeListener changeListener;
    private List<TextView> allTextViews = new ArrayList<>();
    private OnTextViewCreatedListener textViewCreatedListener;

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = UIFactory.buildCategoryItem(parent.getContext());

        allTextViews.add(view);

        if (textViewCreatedListener != null) {
            textViewCreatedListener.onTextViewCreated(view, allTextViews.size() - 1, true);
        }

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);

        if (textViewCreatedListener != null) {
            textViewCreatedListener.onTextViewCreated(holder.textView, position, false);
        }
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

                Icon icon = category.getIcon();
                if (icon != null) {
                    Drawable iconDrawable = icon.resolve(context);
                    if (iconDrawable != null) {
                        int size = dp2px(context, Icon.DEFAULT_SIZE_DP);
                        iconDrawable.setBounds(0, 0, size, size);
                        textView.setCompoundDrawables(iconDrawable, null, null, null);
                    }
                }
                boolean isSelected = category.equals(selectedCategory);
                textView.setSelected(isSelected);
                float elevation = isSelected ? dp2px(context, 4) : 0.0f;
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
        if (changeListener != null && this.selectedCategory != category) {
            changeListener.onCategoryChange(selectedCategory, categories.indexOf(category));
        }
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

    public void setOnCategoryChangeListener(OnCategoryChangeListener listener) {
        this.changeListener = listener;
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    public interface OnCategoryChangeListener {
        void onCategoryChange(Category category, int position);
    }

    public List<TextView> getAllTextViews() {
        return allTextViews;
    }

    public interface OnTextViewCreatedListener {
        void onTextViewCreated(TextView textView, int position, boolean isInitialCreation);
    }

    @Override
    public void onViewRecycled(@NonNull CategoryViewHolder holder) {
        super.onViewRecycled(holder);
        allTextViews.remove(holder.textView);
    }

    public void setOnTextViewCreatedListener(OnTextViewCreatedListener listener) {
        this.textViewCreatedListener = listener;
    }
}