package com.trodev.pennywise;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

//import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

//import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
import com.trodev.pennywise.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemsClick {

    ActivityMainBinding binding;
    private ExpensesAdapter expensesAdapter;
    Intent intent;

    private long income = 0, expense = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = new Intent(MainActivity.this, AddExpenseActivity.class);

        expensesAdapter = new ExpensesAdapter(this, this);
        binding.recycler.setAdapter(expensesAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));


        binding.addIncome.setOnClickListener(view -> {

            intent.putExtra("type", "Income");
            startActivity(intent);
        });


        binding.addExpense.setOnClickListener(view -> {
            intent.putExtra("type", "Expense");
            startActivity(intent);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // Handle logout action
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Perform logout actions, e.g., clear user session, navigate to login page
        // For example:
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
         finish(); // Optional: Close the current activity
    }

    @Override
    protected void onResume() {
        super.onResume();
        income = 0;
        expense = 0;
        getData();
    }



    private void getData() {

        FirebaseFirestore
                .getInstance()
                .collection("expenses")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    expensesAdapter.clear();
                    List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot ds : dsList) {
                        ExpenseModel expenseModel = ds.toObject(ExpenseModel.class);
                        if (expenseModel.getType().equals("Income")) {
                            income += expenseModel.getAmount();
                        } else {
                            expense += expenseModel.getAmount();
                        }
                        expensesAdapter.add(expenseModel);

                    }
                    setUpGraph();
                });
    }

    private void setUpGraph() {
        List<PieEntry> pieEntryList = new ArrayList<>();
        List<Integer> colorsList = new ArrayList<>();
        if (income != 0) {
            pieEntryList.add(new PieEntry(income, "Income"));
            colorsList.add(getResources().getColor(R.color.yellow));
        }

        if (expense != 0) {
            pieEntryList.add(new PieEntry(expense, "Expense"));
            colorsList.add(getResources().getColor(R.color.grey));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, String.valueOf(income - expense));
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(colorsList);

        binding.pieChart.setData(pieData);
        binding.pieChart.invalidate();
    }

    @Override
    public void onClick(ExpenseModel expenseModel) {
        intent.putExtra("model", expenseModel);
        startActivity(intent);
    }

}