package com.example.kursach.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursach.R;
import com.example.kursach.adapters.ExpenseAdapter;
import com.example.kursach.adapters.IncomeAdapter;
import com.example.kursach.model.Expense;
import com.example.kursach.model.Income;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference userRef;
    private List<Expense> expenseList;
    private List<Income> incomeList;
    private ExpenseAdapter expenseAdapter;
    private IncomeAdapter incomeAdapter;
    private RecyclerView recyclerView;
    private TextView balanceTextView;
    private float currentBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerViewExpenses = findViewById(R.id.recyclerView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                return true;
            } else if (itemId == R.id.reports) {
                startActivity(new Intent(HomeActivity.this, ReportsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (itemId == R.id.category) {
                startActivity(new Intent(HomeActivity.this, CategoryActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseList = new ArrayList<>();
        incomeList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(this, expenseList);
        incomeAdapter = new IncomeAdapter(this, incomeList);

        recyclerView.setAdapter(expenseAdapter); // По умолчанию показываем расходы

        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", "");
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AddOperationActivity.class);
            startActivity(intent);
        });

        balanceTextView = findViewById(R.id.balanceTextView);
        EditText newBalanceEditText = findViewById(R.id.newBalanceEditText);
        Button saveBalanceButton = findViewById(R.id.saveBalance);
        saveBalanceButton.setOnClickListener(v -> {
            String newBalanceStr = newBalanceEditText.getText().toString();
            if (!newBalanceStr.isEmpty()) {
                currentBalance = Float.parseFloat(newBalanceStr);
                updateBalanceInFirebase();
                newBalanceEditText.setText("");
                Toast.makeText(HomeActivity.this, "Баланс успешно сохранен", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "Введите новый баланс", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton changeCurrencyButton = findViewById(R.id.changeCurrencyButton);

        changeCurrencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCurrencyMenu(view);
            }
        });


        TextView expensesTextView = findViewById(R.id.expensesTextView);
        TextView incomeTextView = findViewById(R.id.incomeTextView);

        expensesTextView.setOnClickListener(v -> {
            recyclerView.setAdapter(expenseAdapter);
            loadExpenses();
        });

        incomeTextView.setOnClickListener(v -> {
            recyclerView.setAdapter(incomeAdapter);
            loadIncomes();
        });

        loadBalance();
        loadExpenses();
        loadIncomes();



        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                String expenseId = expenseAdapter.getExpenseId(position);

                String userId = preferences.getString("userId", "");
                DatabaseReference expenseRefToRemove = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("users")
                        .child(userId)
                        .child("expenses")
                        .child(expenseId);

                expenseRefToRemove.removeValue();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewExpenses);

    }

    private void showCurrencyMenu(View view) {
        PopupMenu popup = new PopupMenu(HomeActivity.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.currency_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showAlert(item.getTitle().toString());
                return true;
            }
        });
        popup.show();
    }

    private void showAlert(final String currency) {
        new AlertDialog.Builder(this)
                .setTitle("Внимание")
                .setMessage("Обозначение валют всех транзакций изменится, при этом суммы по курсу пересчитаны не будут.")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> updateCurrency(currency))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void updateCurrency(String currency) {
        // Сохраняем выбранную валюту в SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit();
        editor.putString("currency", currency);
        editor.apply();

        // Обновляем текстовое поле с балансом
        String currentBalanceText = balanceTextView.getText().toString();
        String newBalanceText = currentBalanceText.replaceAll("BYN|USD|EUR", currency);
        balanceTextView.setText(newBalanceText);
        Toast.makeText(this, "Валюта изменена на " + currency, Toast.LENGTH_SHORT).show();
    }

    private void loadExpenses() {
        userRef.child("expenses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenseList.clear();
                double totalExpenses = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    if (expense != null) {
                        expenseList.add(expense);
                        totalExpenses += expense.getAmount();
                    }
                }

                expenseAdapter.notifyDataSetChanged();
                updateBalanceDisplay();
                updateBalanceInFirebase();
                Log.d("FirebaseData", "Number of expenses: " + expenseList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Ошибка чтения расходов", databaseError.toException());
            }
        });
    }

    private void loadIncomes() {
        userRef.child("incomes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                incomeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Income income = snapshot.getValue(Income.class);
                    if (income != null) {
                        incomeList.add(income);
                    }
                }
                incomeAdapter.notifyDataSetChanged();
                updateBalanceDisplay();
                updateBalanceInFirebase();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Ошибка чтения доходов", databaseError.toException());
            }
        });
    }

    private void loadBalance() {
        userRef.child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentBalance = dataSnapshot.getValue(Float.class);
                    updateBalanceDisplay();
                } else {
                    currentBalance = 0.0f;
                    balanceTextView.setText("Текущий баланс: 0 BYN");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Ошибка чтения баланса", databaseError.toException());
            }
        });

        // Загрузка сохраненной валюты из SharedPreferences и обновление текстового поля с балансом
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String currency = preferences.getString("currency", "BYN");
        String currentBalanceText = balanceTextView.getText().toString();
        String newBalanceText = currentBalanceText.replaceAll("BYN|USD|EUR", currency);
        balanceTextView.setText(newBalanceText);
    }

    private void updateBalanceDisplay() {
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String currency = preferences.getString("currency", "BYN"); // Получаем текущую валюту

        double totalExpenses = calculateTotalExpenses();
        double totalIncomes = calculateTotalIncomes();
        double updatedBalance = currentBalance - totalExpenses + totalIncomes;
        balanceTextView.setText("Текущий баланс: " + updatedBalance + " " + currency); // Используем текущую валюту
    }


    private double calculateTotalExpenses() {
        double totalExpenses = 0;
        for (Expense expense : expenseList) {
            totalExpenses += expense.getAmount();
        }
        return totalExpenses;
    }

    private double calculateTotalIncomes() {
        double totalIncomes = 0;
        for (Income income : incomeList) {
            totalIncomes += income.getAmount();
        }
        return totalIncomes;
    }

    private void updateBalanceInFirebase() {
        userRef.child("balance").setValue(currentBalance)
                .addOnSuccessListener(aVoid -> {
                    updateBalanceDisplay();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка обновления баланса", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Ошибка обновления баланса", e);
                });
    }
}