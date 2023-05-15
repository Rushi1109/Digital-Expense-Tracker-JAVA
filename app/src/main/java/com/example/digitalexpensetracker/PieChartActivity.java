package com.example.digitalexpensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        getSupportActionBar().setTitle("PieChart View");

        int sumIncome = getIntent().getIntExtra("Income", 0);
        int sumExpense = getIntent().getIntExtra("Expense", 0);

        setUpGraph(sumIncome, sumExpense);
    }

    private void setUpGraph(int income, int expense) {
        List<PieEntry> pieEntryList = new ArrayList<>();
        List<Integer> colorsList = new ArrayList<>();

        if(income != 0){
            pieEntryList.add(new PieEntry(income, "Income"));
            colorsList.add(this.getColor(R.color.dark_green));
        }
        if(expense != 0){
            pieEntryList.add(new PieEntry(expense, "Expense"));
            colorsList.add(this.getColor(R.color.red));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntryList,"Balance = "+ String.valueOf(income-expense));
        pieDataSet.setColors(colorsList);
        pieDataSet.setValueTextColor(this.getColor(R.color.white));
        PieData pieData = new PieData(pieDataSet);

        pieChart = findViewById(R.id.pie_chart);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }
}