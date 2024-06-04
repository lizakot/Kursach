package com.example.kursach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.kursach.R;
import com.example.kursach.model.Expense;
import com.example.kursach.model.Income;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ReportsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private DatabaseReference userRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(ReportsActivity.this, HomeActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.reports) {
                    // Переход на ReportsActivity при выборе "Графики"
                    return true;
                } else if (itemId == R.id.category) {
                    // Переход на CategoryActivity при выборе "Категории"
                    startActivity(new Intent(ReportsActivity.this, CategoryActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.profile) {
                    // Переход на ProfileActivity при выборе "Профиль"
                    startActivity(new Intent(ReportsActivity.this, ProfileActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                }
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.reports);

        String userId;
        SharedPreferences preferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);


        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        fetchExpenseDataForPieChart();
        fetchIncomeDataForBarChart();
        setupPieChart(entries, colors);

    }


    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private void fetchExpenseDataForPieChart() {
        DatabaseReference expensesRef = userRef.child("expenses");

        expensesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Float> categoryExpenses = new HashMap<>();

                // Обход данных о расходах
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    if (expense != null) {
                        String category = expense.getCategoryId();
                        float amount = (float) expense.getAmount();

                        // Если категория уже есть в списке, добавляем сумму к уже имеющейся
                        if (categoryExpenses.containsKey(category)) {
                            float currentAmount = categoryExpenses.get(category);
                            categoryExpenses.put(category, currentAmount + amount);
                        } else {
                            categoryExpenses.put(category, amount);
                        }
                    }
                }

                // Создание PieEntries на основе сумм расходов по категориям
                List<PieEntry> entries = new ArrayList<>();
                List<Integer> colors = new ArrayList<>();

                for (Map.Entry<String, Float> entry : categoryExpenses.entrySet()) {
                    entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                    colors.add(getRandomColor());
                }

                // Постройте график на основе собранных данных
                setupPieChart(entries, colors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setupPieChart(List<PieEntry> entries, List<Integer> colors) {
        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setDrawValues(true);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        // Форматтер для значений графика
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value) + " BYN";
            }
        });

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.getDescription().setEnabled(false);
    }

    private void fetchIncomeDataForBarChart() {
        DatabaseReference incomeRef= userRef.child("incomes");

        incomeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Float> monthlyIncome = new HashMap<>();
                List<BarEntry> entries = new ArrayList<>();
                List<String> monthLabels = new ArrayList<>();
                List<Integer> barColors = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Income income= snapshot.getValue(Income.class);
                    if (income != null) {
                        String month= income.getDate();
                        if (monthlyIncome.containsKey(month)) {
                            float currentAmount= monthlyIncome.get(month);
                            monthlyIncome.put(month, currentAmount + (float) income.getAmount());
                        } else {
                            monthlyIncome.put(month, (float) income.getAmount());
                        }
                    }
                 }
                int index=0;
                for (Map.Entry<String, Float> entry : monthlyIncome.entrySet()) {
                    entries.add(new BarEntry(index + 1, entry.getValue()));
                    monthLabels.add(entry.getKey());
                    barColors.add(getRandomColor());
                    index++;
                }
                setupBarChart(entries, monthLabels, barColors);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setupBarChart(List<BarEntry> entries, List<String> monthLabels, List<Integer> barColors) {
        BarDataSet dataSet=new BarDataSet(entries, "Доходы по месяцам");
        dataSet.setColors(barColors);
        BarData data=new BarData(dataSet);
        data.setBarWidth(0.9f);
        barChart.setData(data);
        XAxis xAxis= barChart.getXAxis();
        xAxis.setTextSize(10f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthLabels));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(monthLabels.size());
        YAxis yAxisLeft= barChart.getAxisLeft();
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
            return value + " BYN";
            }
        });
        YAxis yAxisRight= barChart.getAxisRight();
        yAxisRight.setEnabled(false);
        Legend legend= barChart.getLegend();
        legend.setEnabled(false);
        barChart.invalidate();
        barChart.getDescription().setEnabled(false);
    }
}
