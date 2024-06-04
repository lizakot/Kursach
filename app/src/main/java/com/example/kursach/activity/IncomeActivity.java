package com.example.kursach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursach.R;
import com.example.kursach.model.Income;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class IncomeActivity extends AppCompatActivity {
    TextView cancelTextView;
    Button saveButton;
    EditText summ;
    EditText description;
    Button datePickerButton;
    Calendar calendar;

    DatabaseReference incomeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        cancelTextView = findViewById(R.id.cancelTextView);
        saveButton = findViewById(R.id.saveButton);
        datePickerButton = findViewById(R.id.datePickerButton);
        calendar = Calendar.getInstance();
        summ = findViewById(R.id.summ);
        description = findViewById(R.id.description);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        incomeRef = database.getReference().child("users"); // Изменение пути

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
                Intent intent = new Intent(IncomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                datePickerButton.setText(selectedDate);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void onSaveClicked() {
        double amount = Double.parseDouble(summ.getText().toString());
        String date = datePickerButton.getText().toString();
        String descriptionText = description.getText().toString();

        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", "");

        String incomeId = UUID.randomUUID().toString();
        Income income = new Income(incomeId, amount, date, descriptionText);

        DatabaseReference userIncomeRef = incomeRef.child(userId).child("incomes").child(incomeId);
        userIncomeRef.setValue(income)
                .addOnSuccessListener(aVoid -> {
                    updateUserIncomes(userId,incomeId);
                    Toast.makeText(IncomeActivity.this, "Доход успешно сохранен", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(IncomeActivity.this, "Ошибка сохранения дохода", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserIncomes(String userId, String incomeId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("incomeIds");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> incomeIds = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String existingIncomeId = snapshot.getValue(String.class);
                        incomeIds.add(existingIncomeId);
                    }
                }

                incomeIds.add(incomeId);

                userRef.setValue(incomeIds)
                        .addOnSuccessListener(aVoid -> {
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(IncomeActivity.this, "Ошибка обновления списка доходов", Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(IncomeActivity.this, "Ошибка доступа к данным пользователя", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
