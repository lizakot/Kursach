package com.example.kursach.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kursach.model.Expense;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Expense>> expenseList;
    private MutableLiveData<Integer> balance = new MutableLiveData<>();

    public LiveData<Integer> getBalance() {
        return balance;
    }

    // Setter для установки нового значения баланса
    public void setBalance(int newBalance) {
        balance.setValue(newBalance);
    }

    public LiveData<List<Expense>> getExpenseList() {
        if (expenseList == null) {
            expenseList = new MutableLiveData<>();
            loadExpenses();
        }
        return expenseList;
    }

    private void loadExpenses() {

        List<Expense> expenses = new ArrayList<>();


        Expense expense1 = new Expense("Машина", 30, "02.01.2023");

        expenses.add(expense1);

        expenseList.setValue(expenses);
    }
}
