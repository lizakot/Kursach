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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursach.R;
import com.example.kursach.activity.ExpenseManager;
import com.example.kursach.adapters.ExpenseAdapter;
import com.example.kursach.model.Expense;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DatabaseReference balanceRef;
    private DatabaseReference userRef;
    private List<Expense> expenseList;
    private ExpenseAdapter expenseAdapter;

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

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        DatabaseReference expensesRef = userRef.child("expenses");

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExpenseManager.class);
                startActivity(intent);
            }
        });

        TextView balanceTextView = view.findViewById(R.id.balanceTextView);
        EditText newBalanceEditText = view.findViewById(R.id.newBalanceEditText);
        Button saveBalanceButton = view.findViewById(R.id.saveBalance);

        saveBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newBalanceStr = newBalanceEditText.getText().toString();
                if (!newBalanceStr.isEmpty()) {
                    float newBalance = Float.parseFloat(newBalanceStr);

                    // Сохранение нового баланса в SharedPreferences
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("currentBalance", newBalance);
                    editor.apply();

                    balanceTextView.setText("Текущий баланс: " + newBalance + " BYN");
                    newBalanceEditText.setText("");
                    Toast.makeText(getActivity(), "Баланс успешно сохранен", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Введите новый баланс", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Получение текущего баланса из SharedPreferences
        float currentBalance = preferences.getFloat("currentBalance", 0.0f);
        balanceTextView.setText("Текущий баланс: " + currentBalance + " BYN");

        expensesRef.addValueEventListener(new ValueEventListener() {
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

                updateBalance(currentBalance, totalExpenses, balanceTextView);
                expenseAdapter.notifyDataSetChanged();
                Log.d("FirebaseData", "Number of expenses: " + expenseList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Ошибка чтения расходов", databaseError.toException());
            }
        });
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

        return view;
    }

    private void updateBalance(float currentBalance, double totalExpenses, TextView balanceTextView) {
        balanceRef = userRef.child("balance");
        double updatedBalance = currentBalance - totalExpenses;

        balanceRef.setValue(updatedBalance)
                .addOnSuccessListener(aVoid -> {
                    balanceTextView.setText("Текущий баланс: " + updatedBalance + " BYN");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Ошибка обновления баланса", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Ошибка обновления баланса", e);
                });
    }
}
