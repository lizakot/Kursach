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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kursach.R;
import com.example.kursach.fragments.CategoryFragment;
import com.example.kursach.fragments.HomeFragment;
import com.example.kursach.model.CategoryInfo;
import com.example.kursach.model.Expense;
import com.example.kursach.model.HelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseManager extends AppCompatActivity {
    TextView cancelTextView;
    Button saveButton;
    EditText summ;

    Button datePickerButton;
    Calendar calendar;
    Spinner categorySpinner;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_manager);
        cancelTextView = findViewById(R.id.cancelTextView);
        saveButton = findViewById(R.id.saveButton);
        datePickerButton = findViewById(R.id.datePickerButton);
        calendar = Calendar.getInstance();
        summ = findViewById(R.id.summ);

        categorySpinner = findViewById(R.id.categorySpinner);

        fetchCategoriesFromFirebase();

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();

                Intent intent = new Intent(ExpenseManager.this, HomeFragment.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void fetchCategoriesFromFirebase() {
        String userId;
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", "");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HelperClass user = dataSnapshot.getValue(HelperClass.class);
                if (user != null) {
                    List<String> categoryIds = user.getCategoryIds();

                    DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");

                    categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<String> categoryNames = new ArrayList<>(); // Список для хранения названий категорий

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                CategoryInfo categoryInfo = snapshot.getValue(CategoryInfo.class);
                                if (categoryInfo != null && categoryIds.contains(categoryInfo.getId())) {
                                    categoryNames.add(categoryInfo.getCategoryName()); // Добавление названий категорий в список
                                }
                            }

                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(ExpenseManager.this, android.R.layout.simple_spinner_item, categoryNames);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categorySpinner.setAdapter(spinnerAdapter); // Установка адаптера для Spinner

                            Log.d("FirebaseData1", "Category Names: " + categoryNames);
                            Log.d("FirebaseData", "DataSnapshot count: " + dataSnapshot.getChildrenCount());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }




    @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Обработка выбранной даты
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                // установить выбранную дату в TextView
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
        String selectedCategory = categorySpinner.getSelectedItem().toString(); // Получаем выбранную категорию

        String userId;
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", "");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Генерируем уникальный ID для нового расхода
        String expenseId = userRef.child("expenses").push().getKey();

        if (expenseId != null) {
            String amountStr = summ.getText().toString(); // Получаем введенную пользователем сумму

            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);; // Преобразуем строку в число

                // Получаем дату из кнопки
                String date = datePickerButton.getText().toString();

                // Создаем объект Expense с полученными данными
                Expense expense = new Expense(expenseId, selectedCategory, amount, date);

                // Сохраняем расход в базе данных Firebase
                userRef.child("expenses").child(expenseId).setValue(expense)
                        .addOnSuccessListener(aVoid -> {

                        })
                        .addOnFailureListener(e -> {

                        });
            }
        }

    }
}