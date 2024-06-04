package com.example.kursach.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.kursach.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddOperationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operation);
        ImageView firstImageBtn = findViewById(R.id.firstImageBtn);

        firstImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запускаем активность ExpenseManager при нажатии на кнопку "Расход"
                Intent intent = new Intent(AddOperationActivity.this, IncomeActivity.class);
                startActivity(intent);
            }
        });

        ImageView secondImageBtn = findViewById(R.id.secondImageBtn);

        secondImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Запускаем активность ExpenseManager при нажатии на кнопку "Расход"
                Intent intent = new Intent(AddOperationActivity.this, ExpenseManager.class);
                startActivity(intent);
            }
        });

        FloatingActionButton closeBtn = findViewById(R.id.close);

        // Устанавливаем обработчик нажатия на кнопку "Закрыть"
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ваш код для операции "Отмена"
                finish(); // Закрыть текущую активность
            }
        });
    }
}