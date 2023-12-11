package com.example.kursach.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kursach.R;
import com.example.kursach.model.Expense;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import androidx.core.content.ContextCompat;


public class ReportsFragment extends Fragment {

    private PieChart pieChart;
    private DatabaseReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        String userId;
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", "");
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        pieChart = view.findViewById(R.id.pieChart);

        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        fetchExpenseDataForPieChart();

        setupPieChart(entries, colors);

        return view;
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
}

