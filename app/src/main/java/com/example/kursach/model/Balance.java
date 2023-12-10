package com.example.kursach.model;

public class Balance {
    private double currentBalance;

    public Balance() {

    }

    public Balance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
}

