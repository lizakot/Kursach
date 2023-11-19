package com.example.kursach.model;

public class CategoryInfo {

    private String categoryName;
    private String categoryDescription;

    public CategoryInfo() {
        // Конструктор без параметров (для Firebase)
    }

    public CategoryInfo(String categoryName, String categoryDescription) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
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
}
