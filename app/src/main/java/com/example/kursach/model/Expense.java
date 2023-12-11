package com.example.kursach.model;

public class Expense {

    private String id;

    private String categoryId;

    private double amount;
    private String date;

    private int categoryIcon;

    private int categoryColor;




    public Expense(String id, String categoryId, double amount, String date, int categoryIcon, int categoryColor) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.categoryColor =  categoryColor;
        this.categoryIcon = categoryIcon;
    }

    public Expense() {

    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public int getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }
}

