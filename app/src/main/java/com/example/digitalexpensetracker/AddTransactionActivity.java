package com.example.digitalexpensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddTransactionActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private ArrayAdapter<CharSequence> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        getSupportActionBar().setTitle("Add Transaction");

        // Get spinner by id
        spinnerCategory = findViewById(R.id.spinner_category);

        // Populate arrayAdapter using string array
        categoryAdapter = ArrayAdapter.createFromResource(AddTransactionActivity.this, R.array.array_categories, R.layout.spinner_layout);

        // Specify the layout to use when the list of choice appear
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to spinner to populate the category spinner
        spinnerCategory.setAdapter(categoryAdapter);
    }
}