package com.example.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

/*
    strings.xml:
        This file is for putting all the strings of the app, instead of hardcoding
        the strings. This is useful for translation purposes.
 */

public class MainActivity extends AppCompatActivity {

    // References to buttons and other controls on the layout
    Button btnViewAll, btnAdd;
    EditText editTxtName,editTxtAge;
    Switch swActiveCustomer;
    ListView lvCustomers;

    // We define this ones here in order to use them in many methods
    DatabaseHelper databaseHelper;
    ArrayAdapter customerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Code to link the variables with the layout objects
        btnViewAll = findViewById(R.id.btnViewAll);
        btnAdd = findViewById(R.id.btnAdd);
        editTxtName = findViewById(R.id.editTxtName);
        editTxtAge = findViewById(R.id.editTxtAge);
        swActiveCustomer = findViewById(R.id.swActiveCustomer);
        lvCustomers = findViewById(R.id.lvCustomers);

        // Fill the list view with all the elements on the db
        databaseHelper = new DatabaseHelper(MainActivity.this);
        showCostumersOnListView(databaseHelper);


        // On Click Listener for both buttons (View All and Add)
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We create a new instance of the DatabaseHelper class.
                databaseHelper = new DatabaseHelper(MainActivity.this);

                // We create an array adapter in order to fill the listview (the one in the UI)
                showCostumersOnListView(databaseHelper);

//                Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT)
//                        .show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Customer customer;

                /* TRY: We try to create a new instance of the class Customer.
                    CATCH: If the code in the TRY section fails, we add a register in our db
                    with a negative number as id, in order to know how many times the app
                    has failed */
                try {
                    customer = new Customer(-1,editTxtName.getText().toString(),
                            Integer.parseInt(editTxtAge.getText().toString()),swActiveCustomer.isChecked());
                    Toast.makeText(MainActivity.this, customer.toString(), Toast.LENGTH_SHORT)
                            .show();
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error creating the customer",
                            Toast.LENGTH_SHORT).show();
                    customer = new Customer(-1, "error", 0, false);
                }

                // Create a new instance of the DatabaseHelper class
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                // Add one register to the database, and store the result (true or false)
                boolean success = databaseHelper.addOne(customer);
                Toast.makeText(MainActivity.this, "Success: " + success, Toast.LENGTH_SHORT)
                        .show();
                // Update the listview
                showCostumersOnListView(databaseHelper);
            }
        });

        // Here the OnClickListener of our item selected from our ListView
        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer clickedCustomer = (Customer) parent.getItemAtPosition(position);
                databaseHelper.deleteOne(clickedCustomer);
                showCostumersOnListView(databaseHelper);
                Toast.makeText(MainActivity.this, "Deleted: " + clickedCustomer.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This is the method to update de ListView with the db data
    private void showCostumersOnListView(DatabaseHelper databaseHelper2) {
        customerArrayAdapter = new ArrayAdapter<Customer>(MainActivity.this,
                android.R.layout.simple_list_item_1, databaseHelper2.getAll());
        lvCustomers.setAdapter(customerArrayAdapter);
    }
}