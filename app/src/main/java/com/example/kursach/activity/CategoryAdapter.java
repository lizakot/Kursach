package com.example.kursach.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursach.R;
import com.example.kursach.model.CategoryInfo;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryInfo> categoryList;

    public CategoryAdapter(List<CategoryInfo> categoryList) {
        this.categoryList = categoryList;
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

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            categoryDescriptionTextView = itemView.findViewById(R.id.categoryDescriptionTextView);
        }
    }
}


