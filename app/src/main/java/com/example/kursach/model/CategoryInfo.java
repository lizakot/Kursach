package com.example.kursach.model;

public class CategoryInfo {

    private String categoryName;

    private String categoryIconUrl;
    private String categoryColor;

    // Конструктор без параметров (для Firebase)
    public CategoryInfo() {}

    public CategoryInfo(String categoryName, String categoryIconUrl, String categoryColor) {
        this.categoryName = categoryName;
        this.categoryIconUrl = categoryIconUrl;
        this.categoryColor = categoryColor;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIconUrl() {
        return categoryIconUrl;
    }

    public void setCategoryIconUrl(String categoryIconUrl) {
        this.categoryIconUrl = categoryIconUrl;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }
}

