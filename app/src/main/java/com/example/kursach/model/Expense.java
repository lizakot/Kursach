package com.example.kursach.model;

public class Expense {

    private String id;

    private String categoryId;

    private double amount;
    private String date;


    public Expense(String id, String categoryId, double amount, String date) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
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
}

