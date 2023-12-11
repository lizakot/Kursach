package com.example.kursach.adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursach.R;
import com.example.kursach.model.CategoryInfo;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public List<CategoryInfo> categoryList;

    public CategoryAdapter(List<CategoryInfo> categoryList) {
        this.categoryList = categoryList;
    }

    public void filterList(List<CategoryInfo> filteredList) {
        categoryList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryInfo category = categoryList.get(position);

        holder.categoryNameTextView.setText(category.getCategoryName());
        holder.categoryDescriptionTextView.setText(category.getCategoryDescription());
        holder.categoryIconImageView.setImageResource(category.getCategoryIcon());
        holder.categoryIconImageView.setColorFilter(category.getCategoryColor());

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public void updateCategoryList(List<CategoryInfo> updatedList) {
        categoryList = updatedList;
        notifyDataSetChanged();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;
        TextView categoryDescriptionTextView;

        TextView categoryColorView;
        ImageView categoryIconImageView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            categoryDescriptionTextView = itemView.findViewById(R.id.categoryDescriptionTextView);
            categoryIconImageView = itemView.findViewById(R.id.categoryIconImageView);
        }
    }

    public void filter(String text) {
        List<CategoryInfo> filteredList = new ArrayList<>();
        for (CategoryInfo category : categoryList) {
            if (category.getCategoryName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(category);
            }
        }
        filterList(filteredList);
    }

}


