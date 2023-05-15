package com.example.digitalexpensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private TextView textViewTotalIncome, textViewTotalExpense, textViewTotalBalance;

    private int sumExpense = 0, sumIncome=0;
    private ArrayList<TransactionModel> transactionModelArrayList;
    private TransactionAdapter transactionAdapter;
    private RecyclerView homeScreenRecyclerView;
    private RelativeLayout RLAddTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        textViewTotalBalance = findViewById(R.id.total_balance);
        textViewTotalExpense = findViewById(R.id.total_expense);
        textViewTotalIncome = findViewById(R.id.total_income);

        // Getting instance of firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        // Getting instance of firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        // TransactionModel ArrayList to get dynamic array of transaction
        transactionModelArrayList = new ArrayList<>();

        // Get Recycler view from xml by ID
        homeScreenRecyclerView = findViewById(R.id.RecyclerView_history);

        homeScreenRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeScreenRecyclerView.setHasFixedSize(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }

        SMSReceive smsReceiver = new SMSReceive();

        getSupportActionBar().setTitle("Dashboard");

        RLAddTransaction = findViewById(R.id.RL_add_transaction_button);

        RLAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AddTransactionActivity.class));
            }
        });

        loadData();
    }

    private void loadData() {
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot ds:task.getResult()){
                            TransactionModel model = new TransactionModel(
                                    ds.getString("id"),
                                    ds.getString("amount"),
                                    ds.getString("note"),
                                    ds.getString("category"),
                                    ds.getString("date"),
                                    ds.getString("mode"),
                                    ds.getString("type"));
                            int amount = Integer.parseInt(ds.getString("amount"));
                            if(ds.getString("type").equals("Expense")){
                                sumExpense += amount;
                            }
                            else{
                                sumIncome += amount;
                            }
                            transactionModelArrayList.add(model);
                        }

                        textViewTotalExpense.setText(String.valueOf(sumExpense));
                        textViewTotalIncome.setText(String.valueOf(sumIncome));
                        textViewTotalBalance.setText(String.valueOf(sumIncome-sumExpense));

                        // Transaction adapter to load data in recycler view
                        transactionAdapter = new TransactionAdapter(DashboardActivity.this,transactionModelArrayList);
                        // Setting the adapter on recycler view
                        homeScreenRecyclerView.setAdapter(transactionAdapter);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.referesh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.refresh_page){
            try {
                startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                finish();
            } catch (Exception e){
                Toast.makeText(DashboardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DashboardActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DashboardActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}