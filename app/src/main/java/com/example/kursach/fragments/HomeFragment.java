package com.example.kursach.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursach.R;
import com.example.kursach.activity.ExpenseManager;
import com.example.kursach.adapters.ExpenseAdapter;
import com.example.kursach.model.Balance;
import com.example.kursach.model.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DatabaseReference balanceRef;

    private List<Expense> expenseList;
    private ExpenseAdapter expenseAdapter;
    private DatabaseReference userRef;

    private DatabaseReference expensesRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerViewExpenses = view.findViewById(R.id.recyclerView);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(getActivity(), expenseList);
        recyclerViewExpenses.setAdapter(expenseAdapter);

        String userId;
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", "");

        // Получаем ссылку на базу данных Firebase для расходов пользователя
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId); // Инициализация userRef

        expensesRef = userRef.child("expenses");

        // Читаем расходы из базы данных Firebase
        expensesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenseList.clear(); // Очищаем список перед обновлением данных

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    if (expense != null) {
                        expenseList.add(expense); // Добавляем расход в список
                    }
                }

                expenseAdapter.notifyDataSetChanged();
                Log.d("FirebaseData", "Number of expenses: " + expenseList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Ошибка чтения расходов", databaseError.toException());
            }
        });
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExpenseManager.class);
                startActivity(intent);
            }
        });


        balanceRef = userRef.child("balance");



        TextView balanceTextView = view.findViewById(R.id.balanceTextView);

        // Читаем текущее значение баланса из базы данных Firebase
        balanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Double currentBalance = dataSnapshot.getValue(Double.class);
                    if (currentBalance != null) {
                        // Отображаем текущий баланс в TextView
                        balanceTextView.setText("Текущий баланс: " + currentBalance + " BYN");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Ошибка чтения баланса", databaseError.toException());
            }
        });

        Button saveBalanceButton = view.findViewById(R.id.saveBalance);
        EditText newBalanceEditText = view.findViewById(R.id.newBalanceEditText);

        saveBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newBalanceStr = newBalanceEditText.getText().toString();
                if (!newBalanceStr.isEmpty()) {
                    double newBalance = Double.parseDouble(newBalanceStr);

                    // Записываем новое значение в базу данных Firebase
                    balanceRef.setValue(newBalance)
                            .addOnSuccessListener(aVoid -> {
                                // Обновляем отображение баланса в TextView
                                balanceTextView.setText("Текущий баланс: " + newBalance + " BYN");
                                newBalanceEditText.setText("");
                                Toast.makeText(getActivity(), "Баланс успешно сохранен", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Ошибка сохранения баланса", Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Ошибка сохранения баланса", e);
                            });
                } else {
                    Toast.makeText(getActivity(), "Введите новый баланс", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
