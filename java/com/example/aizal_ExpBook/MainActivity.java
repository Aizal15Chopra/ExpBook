
//MainActivity serves as the main screen, displaying a list of expenses and allowing users to add, edit, and delete expenses.
// The class initializes UI elements, handles user interactions, updates the expense summary and display total monthly charge, and manages the data model.
// It communicates with the ExpenseEntry activity for adding and editing expenses,

package com.example.aizal_ExpBook;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Gravity;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.util.Iterator;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.TextView;
import android.graphics.Typeface;

import java.text.NumberFormat;

/**
 * MainActivity represents the main screen of the Expense Tracker application.
 * It displays a list of expenses, allows users to add, edit, and delete expenses,
 * and shows the total expenses calculated from the individual expenses.
 */
public class MainActivity extends AppCompatActivity {

    // Constants for activity result handling
    private static final int ADD_EXPENSE_REQUEST = 1;

    // UI elements
    private Button AddExpenseButton;
    private Button DeleteExpenseButton;
    private Button EditExpenseButton;
    private ListView expenseListView;
    private TextView ExpenseSummaryTextView;

    // Data structures
    private ArrayList<Expense> expensesList;
    private ArrayAdapter<Expense> adapter;
    private String selectedExpenseId;

    // Activity result launcher for handling expense entry activity
    private ActivityResultLauncher<Intent> expenseEntryLauncher;

    /**
     * Updates the expense summary by calculating and displaying the total amount of all expenses.
     */
    private void updateExpenseSummary() {
        double totalAmount = 0.0;
        for (Expense expense : expensesList) {
            totalAmount += expense.getAmount();
        }
        // Format the total amount as a currency and set it to the TextView
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        String formattedTotalAmount = currencyFormat.format(totalAmount);
        String expenseSummary = "Expense Summary         Total charge: " + formattedTotalAmount;

        ExpenseSummaryTextView.setText(expenseSummary);
    }

    /**
     * Called when the activity is first created. Responsible for initializing UI components,
     * setting up click listeners, and handling activity results.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the custom toolbar with a centered title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView customTitle = new TextView(this);
        customTitle.setText("EXPENSE TRACKER");
        customTitle.setTypeface(null, Typeface.BOLD);
        customTitle.setTextSize(30); // Set your desired text size
        customTitle.setTextColor(getResources().getColor(R.color.white));
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        customTitle.setLayoutParams(layoutParams);
        toolbar.addView(customTitle);

        // Initialize UI elements and data structures
        AddExpenseButton = findViewById(R.id.AddExpense);
        DeleteExpenseButton = findViewById(R.id.DeleteExpense);
        EditExpenseButton = findViewById(R.id.EditExpense);
        expensesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expensesList);
        expenseListView = findViewById(R.id.expenseList);
        expenseListView.setAdapter(adapter);
        ExpenseSummaryTextView = findViewById(R.id.ExpenseSummary);

        // Update the expense summary when expensesList is updated
        updateExpenseSummary();

        // Initialize the expense entry activity result launcher
        expenseEntryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String editedExpenseId = data.getStringExtra("id");
                            String updatedName = data.getStringExtra("name");
                            double updatedAmount = data.getDoubleExtra("amount", 0.0);
                            String updatedDate = data.getStringExtra("date");
                            String updatedComment = data.getStringExtra("comment");

                            if (editedExpenseId != null) {
                                // Editing an existing expense
                                for (Expense expense : expensesList) {
                                    if (expense.getId().equals(editedExpenseId)) {
                                        expense.setName(updatedName);
                                        expense.setAmount(updatedAmount);
                                        expense.setDate(updatedDate);
                                        expense.setComment(updatedComment);
                                        adapter.notifyDataSetChanged(); // Update the ListView
                                        updateExpenseSummary(); // Update the summary
                                        break;
                                    }
                                }
                            } else {
                                // Adding a new expense
                                Expense expense = new Expense(updatedName, updatedAmount, updatedDate, updatedComment);
                                expensesList.add(expense);
                                adapter.notifyDataSetChanged(); // Update the ListView
                                updateExpenseSummary(); // Update the summary
                            }
                        }
                    }
                }
        );

        // Set click listeners for buttons
        AddExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Add Expense button click by navigating to the ExpenseEntryActivity
                Intent intent = new Intent(MainActivity.this, ExpenseEntry.class);
                expenseEntryLauncher.launch(intent);
            }
        });

        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click: get the selected Expense's ID
                selectedExpenseId = expensesList.get(position).getId();
            }
        });

        DeleteExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedExpenseId != null) {
                    // Find and remove the expense with the selected ID from the list
                    Iterator<Expense> iterator = expensesList.iterator();
                    while (iterator.hasNext()) {
                        Expense expense = iterator.next();
                        if (expense.getId().equals(selectedExpenseId)) {
                            iterator.remove();
                            adapter.notifyDataSetChanged();
                            selectedExpenseId = null; // Reset selected expense ID after deletion
                            updateExpenseSummary();
                            break;
                        }
                    }
                } else {
                    // Inform the user to select an expense before deleting
                    Toast.makeText(MainActivity.this, "No expense selected for deletion", Toast.LENGTH_SHORT).show();
                }
            }
        });

        EditExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedExpenseId != null) {
                    // Find the selected expense by its ID
                    for (Expense expense : expensesList) {
                        if (expense.getId().equals(selectedExpenseId)) {
                            // Launch ExpenseEntry activity in edit mode with selected expense data
                            Intent intent = new Intent(MainActivity.this, ExpenseEntry.class);
                            intent.putExtra("editMode", true);
                            intent.putExtra("id", selectedExpenseId);
                            intent.putExtra("name", expense.getName());
                            intent.putExtra("amount", expense.getAmount());
                            intent.putExtra("date", expense.getDate());
                            intent.putExtra("comment", expense.getComment());
                            expenseEntryLauncher.launch(intent);
                            break;
                        }
                    }
                } else {
                    // Inform the user to select an expense before editing
                    Toast.makeText(MainActivity.this, "Please select an expense to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
