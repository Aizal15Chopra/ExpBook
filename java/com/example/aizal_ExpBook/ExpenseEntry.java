
//The ExpenseEntry class is an Android activity responsible for capturing and editing expense data.
// It contains UI elements such as text fields for the expense name, amount, date, and comments,
// along with buttons to save or cancel the expense entry. The class handles incoming data via intents from the MainActivity,
// allowing users to either edit existing expenses or add new ones. It performs basic validation to ensure essential
// fields are not empty, and upon saving, sends the updated/added expense information back to the MainActivity.
// Users can also cancel the expense entry, returning to the main screen without saving changes.
package com.example.aizal_ExpBook;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

public class ExpenseEntry extends AppCompatActivity {

    private EditText expenseNameEditText; // Represents an EditText widget for entering the expense name.
    private EditText expenseAmountEditText; // Represents an EditText widget for entering the expense amount.
    private EditText expenseDateEditText; // Represents an EditText widget for entering the expense date.
    private EditText expenseCommentEditText; // Represents an EditText widget for entering comments related to the expense.
    private Button saveButton; // Represents a Button widget for saving the expense data.
    private Button cancelButton; // Represents a Button widget for canceling the expense entry.
    private String id; // Stores the unique identifier of the expense (used in edit mode).
    private boolean editMode = false; // Indicates whether the activity is in edit mode or not.

// This method is called when the activity is first created.
// It initializes the UI components and handles data received from the MainActivity for editing expenses.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_activity);

        // Initialize EditText fields and buttons
        expenseNameEditText = findViewById(R.id.ExpenseName);
        expenseAmountEditText = findViewById(R.id.ExpenseAmount);
        expenseDateEditText = findViewById(R.id.ExpenseDate);
        expenseCommentEditText = findViewById(R.id.ExpenseComment);
        saveButton = findViewById(R.id.SaveButton);
        cancelButton = findViewById(R.id.CancelButton);

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", false);
        if (editMode) {
            id = intent.getStringExtra("id");
            String name = intent.getStringExtra("name");
            double amount = intent.getDoubleExtra("amount", 0.0);
            String date = intent.getStringExtra("date");
            String comment = intent.getStringExtra("comment");

            // Populate UI fields with the received data
            expenseNameEditText.setText(name);
            expenseAmountEditText.setText(String.valueOf(amount));
            expenseDateEditText.setText(date);
            expenseCommentEditText.setText(comment);
        }

        // Retrieves the entered expense information (name, amount, date, comment).
        //Validates that the name and date fields are not empty.
        // Set click listener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = expenseNameEditText.getText().toString();
                double amount = Double.parseDouble(expenseAmountEditText.getText().toString());
                String date = expenseDateEditText.getText().toString();
                String comment = expenseCommentEditText.getText().toString();

                if(name.equals("")|| date.equals("")){
                    Toast.makeText(ExpenseEntry.this, "Please insert the expense information", Toast.LENGTH_SHORT);
                }
                else {
//                Intent to send back to the main activity
                    Intent resultIntent = new Intent(ExpenseEntry.this, MainActivity.class);
                    resultIntent.putExtra("id", id);
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("amount", amount);
                    resultIntent.putExtra("date", date);
                    resultIntent.putExtra("comment", comment);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        // Set click listener for the Cancel button
        // Finishes the ExpenseEntry activity without saving any changes, returning to the MainActivity.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the cancel button click (implement your logic here)
                finish(); // Close the ExpenseActivity and return to the previous screen (MainActivity)
            }
        });
    }
}
