package com.example.kursach.model;

public class CategoryInfo {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String categoryName;
    private String categoryDescription;

    private int categoryColor;

    private int categoryIcon;

    public CategoryInfo() {
        // Конструктор без параметров (для Firebase)
    }

    public CategoryInfo(String id ,String categoryName, String categoryDescription, int categoryColor, int categoryIcon) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryColor = categoryColor;
        this.categoryIcon = categoryIcon;
        this.id = id;

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public int getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }
}
