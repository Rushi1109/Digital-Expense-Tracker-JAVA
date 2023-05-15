package com.example.digitalexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTransactionActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private ArrayAdapter<CharSequence> categoryAdapter;
    private EditText editTextAmount, editTextDate, editTextNote;
    private RadioGroup radioGroupExpenseType, radioGroupExpenseMode;
    private RadioButton radioButtonExpenseType, radioButtonExpenseMode;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String expenseType, expenseMode, expenseCategory;
    private DatePickerDialog picker;
    private ImageView imageViewDatePicker;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        getSupportActionBar().setTitle("Add Transaction");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        editTextAmount = findViewById(R.id.editText_add_transaction_amount);
        editTextDate = findViewById(R.id.editText_add_transaction_date);
        editTextNote = findViewById(R.id.editText_add_transaction_note);

        imageViewDatePicker = findViewById(R.id.imageView_add_transaction_date_picker);
        imageViewDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(AddTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editTextDate.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        radioGroupExpenseType = findViewById(R.id.radio_group_expense_type);
        radioGroupExpenseType.clearCheck();
        radioGroupExpenseMode = findViewById(R.id.radio_group_expense_mode);
        radioGroupExpenseMode.clearCheck();


        // Get spinner by id
        spinnerCategory = findViewById(R.id.spinner_category);

        // Populate arrayAdapter using string array
        categoryAdapter = ArrayAdapter.createFromResource(AddTransactionActivity.this, R.array.array_categories, R.layout.spinner_layout);

        // Specify the layout to use when the list of choice appear
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to spinner to populate the category spinner
        spinnerCategory.setAdapter(categoryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_transaction_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.add_to_database){

            int selectedExpenseType = radioGroupExpenseType.getCheckedRadioButtonId();
            radioButtonExpenseType = findViewById(selectedExpenseType);

            int selectedExpenseMode = radioGroupExpenseMode.getCheckedRadioButtonId();
            radioButtonExpenseMode = findViewById(selectedExpenseMode);

            String amount = editTextAmount.getText().toString().trim();
            String date = editTextDate.getText().toString().trim();
            String note = editTextNote.getText().toString().trim();

            if (TextUtils.isEmpty(amount)) {
                Toast.makeText(AddTransactionActivity.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                editTextAmount.setError("Amount is required");
                editTextAmount.requestFocus();
            } else if (TextUtils.isEmpty(date)) {
                Toast.makeText(AddTransactionActivity.this, "Please enter the date", Toast.LENGTH_SHORT).show();
                editTextDate.setError("Date is required");
                editTextDate.requestFocus();
            } else if (radioGroupExpenseType.getCheckedRadioButtonId() == -1) {
                Toast.makeText(AddTransactionActivity.this, "Please select the expense type", Toast.LENGTH_SHORT).show();
                radioButtonExpenseType.setError("Expense type is required");
                radioGroupExpenseType.requestFocus();
            } else if (radioGroupExpenseMode.getCheckedRadioButtonId() == -1) {
                Toast.makeText(AddTransactionActivity.this, "Please select the expense mode", Toast.LENGTH_SHORT).show();
                radioButtonExpenseMode.setError("Expense mode is required");
                radioGroupExpenseMode.requestFocus();
            } else{
                // Code for adding data to database

                expenseType = radioButtonExpenseType.getText().toString();
                expenseMode = radioButtonExpenseMode.getText().toString();
                expenseCategory = spinnerCategory.getSelectedItem().toString();

                String fId = UUID.randomUUID().toString();
                // Creating object to put inside database
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("id", fId);
                transaction.put("amount", amount);
                transaction.put("note", note);
                transaction.put("date", date);
                transaction.put("category", expenseCategory);
                transaction.put("type", expenseType);
                transaction.put("mode", expenseMode);

                firestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(fId)
                        .set(transaction)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddTransactionActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                editTextAmount.setText("");
                                editTextNote.setText("");
                                editTextDate.setText("");
                                radioGroupExpenseType.clearCheck();
                                radioGroupExpenseMode.clearCheck();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddTransactionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

        return super.onOptionsItemSelected(item);
    }
}