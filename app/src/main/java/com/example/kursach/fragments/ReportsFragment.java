package com.example.kursach.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kursach.R;
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

import java.util.ArrayList;
import java.util.List;
import androidx.core.content.ContextCompat;


public class ReportsFragment extends Fragment {

    private PieChart pieChart;
    private BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);


        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);


        setupPieChart();


        setupBarChart();

        return view;
    }

    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, ""));
        entries.add(new PieEntry(26.7f, ""));
        entries.add(new PieEntry(24.0f, ""));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(requireContext(), R.color.cornflower_blue));
        colors.add(ContextCompat.getColor(requireContext(), R.color.green));
        colors.add(ContextCompat.getColor(requireContext(), R.color.yellow));

        List<String> labels = new ArrayList<>();
        labels.add("Продукты");
        labels.add("Еда");
        labels.add("Аптека");

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();


        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setDrawInside(false);
        legend.setWordWrapEnabled(true);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);


        LegendEntry[] legendEntries = new LegendEntry[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors.get(i);
            entry.label = labels.get(i) + " (" + entries.get(i).getValue() + "%)"; // Добавляем проценты к метке
            legendEntries[i] = entry;
        }

        legend.setCustom(legendEntries);
        pieChart.getDescription().setEnabled(false);

    }

    private void setupBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 350));
        entries.add(new BarEntry(2, 450));
        entries.add(new BarEntry(3, 600));

        List<String> monthLabels = new ArrayList<>();
        monthLabels.add("Январь");
        monthLabels.add("Февраль");
        monthLabels.add("Март");

        int[] colorsArray = new int[]{
                ContextCompat.getColor(requireContext(), R.color.lavender),
                ContextCompat.getColor(requireContext(), R.color.yellow),
                ContextCompat.getColor(requireContext(), R.color.rose)
        };

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(colorsArray);
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(10f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthLabels));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);


        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value + " BYN";
            }
        });

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        barChart.invalidate();
        barChart.getDescription().setEnabled(false);
    }


}
