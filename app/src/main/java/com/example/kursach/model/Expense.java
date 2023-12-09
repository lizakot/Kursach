package com.example.kursach.model;

public class Expense {

    private String id;
    private String iconId;
    private double amount;
    private String date;


    public Expense(String id, String iconId, double amount, String date) {
        this.id = id;
        this.iconId = iconId;
        this.amount = amount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
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

