package com.example.digitalexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class UpdateTransactionActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextNote, editTextDate;
    private Spinner spinnerCategoryUpdate;
    private ArrayAdapter<CharSequence> categoryAdapter;
    ;
    private RadioGroup radioGroupExpenseType, radioGroupExpenseMode;
    private RadioButton radioButtonExpense, radioButtonIncome, radioButtonUPI, radioButtonCash, radioButtonCard, radioButtonExpenseType, radioButtonExpenseMode;
    private Button updateTransactionBtn, deleteTransactionBtn;

    private DatePickerDialog picker;
    private ImageView imageViewDatePicker;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    String newType, newMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transaction);

        updateTransactionBtn = findViewById(R.id.update_transaction_button);
        deleteTransactionBtn = findViewById(R.id.delete_transaction_button);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        String id = getIntent().getStringExtra("id");
        String amount = getIntent().getStringExtra("amount");
        String date = getIntent().getStringExtra("date");
        String note = getIntent().getStringExtra("note");
        String category = getIntent().getStringExtra("category");
        String type = getIntent().getStringExtra("type");
        String mode = getIntent().getStringExtra("mode");

        editTextAmount = findViewById(R.id.editText_update_transaction_amount);
        editTextDate = findViewById(R.id.editText_update_transaction_date);
        editTextNote = findViewById(R.id.editText_update_transaction_note);

        radioGroupExpenseType = findViewById(R.id.radio_group_expense_type_update);
        radioGroupExpenseType.clearCheck();
        radioGroupExpenseMode = findViewById(R.id.radio_group_expense_mode_update);
        radioGroupExpenseMode.clearCheck();
        radioButtonExpense = findViewById(R.id.radio_expense_update);
        radioButtonIncome = findViewById(R.id.radio_income_update);
        radioButtonCard = findViewById(R.id.radio_card_update);
        radioButtonCash = findViewById(R.id.radio_cash_update);
        radioButtonUPI = findViewById(R.id.radio_UPI_update);

        imageViewDatePicker = findViewById(R.id.imageView_update_transaction_date_picker);
        imageViewDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(UpdateTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editTextDate.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        spinnerCategoryUpdate = findViewById(R.id.spinner_category_update);

        categoryAdapter = ArrayAdapter.createFromResource(UpdateTransactionActivity.this, R.array.array_categories, R.layout.spinner_layout);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoryUpdate.setAdapter(categoryAdapter);

        editTextAmount.setText(amount);
        editTextNote.setText(note);
        editTextDate.setText(date);

        switch (type) {
            case "Income":
                newType = "Income";
                radioButtonIncome.setChecked(true);
                break;

            case "Expense":
                newType = "Expense";
                radioButtonExpense.setChecked(true);
                break;
        }

        switch (mode) {
            case "Cash":
                newMode = "Cash";
                radioButtonCash.setChecked(true);
                break;

            case "UPI":
                newMode = "UPI";
                radioButtonUPI.setChecked(true);
                break;

            case "Card":
                newMode = "Card";
                radioButtonCard.setChecked(true);
                break;
        }

        radioButtonIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newType = "Income";
            }
        });
        radioButtonExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newType = "Expense";
            }
        });
        radioButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMode = "Card";
            }
        });
        radioButtonUPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMode = "UPI";
            }
        });
        radioButtonCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMode = "Cash";
            }
        });

        updateTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedExpenseType = radioGroupExpenseType.getCheckedRadioButtonId();
                radioButtonExpenseType = findViewById(selectedExpenseType);

                int selectedExpenseMode = radioGroupExpenseMode.getCheckedRadioButtonId();
                radioButtonExpenseMode = findViewById(selectedExpenseMode);

                String amount = editTextAmount.getText().toString().trim();
                String date = editTextDate.getText().toString().trim();
                String note = editTextNote.getText().toString().trim();
                String newCategory = spinnerCategoryUpdate.getSelectedItem().toString();

                if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(UpdateTransactionActivity.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                    editTextAmount.setError("Amount is required");
                    editTextAmount.requestFocus();
                } else if (TextUtils.isEmpty(date)) {
                    Toast.makeText(UpdateTransactionActivity.this, "Please enter the date", Toast.LENGTH_SHORT).show();
                    editTextDate.setError("Date is required");
                    editTextDate.requestFocus();
                } else if (radioGroupExpenseType.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(UpdateTransactionActivity.this, "Please select the expense type", Toast.LENGTH_SHORT).show();
                    radioButtonExpenseType.setError("Expense type is required");
                    radioGroupExpenseType.requestFocus();
                } else if (radioGroupExpenseMode.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(UpdateTransactionActivity.this, "Please select the expense mode", Toast.LENGTH_SHORT).show();
                    radioButtonExpenseMode.setError("Expense mode is required");
                    radioGroupExpenseMode.requestFocus();
                } else {
                    firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                            .collection("Notes").document(id)
                            .update("amount", amount, "note", note, "date", date, "category", newCategory, "mode", newMode, "type", newType)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    onBackPressed();
                                    Toast.makeText(UpdateTransactionActivity.this, "Updated the details", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateTransactionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        deleteTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid())
                        .collection("Notes")
                        .document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(UpdateTransactionActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateTransactionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}