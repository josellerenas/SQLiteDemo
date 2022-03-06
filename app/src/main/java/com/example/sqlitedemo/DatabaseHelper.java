package com.example.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // These are constants. We defined them because we will be using these strings a lot in this project
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";

    /* This is one of the constructors defined in SQLiteOpenHelper class.
        We modified the constructor with hardcoded data */
    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }
    /* The next two methods I had to implement them because the SQLiteOpenHelper needed it
        in order to not fail.  */

    /* This is called the first time the database is accessed. There should be code in here
        to create a new database */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME +
                " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";
        db.execSQL(createTableStatement);
    }
    /* This is called if the database version number changes. It prevents previous users
        app from breaking when you change the database design */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // This is the method we call to add a new register in the table
    public boolean addOne (Customer customer) {

        // Here we are creating a SQLiteDatabase object
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating a ContentValues object, needed as intermediary for adding a register to the table
        ContentValues cv = new ContentValues();

        // Filling the ContentValues object with the new register I want to add
        cv.put(COLUMN_CUSTOMER_NAME, customer.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customer.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customer.isActive());

        /* Inserting the ContentValues object into the table. the "if" statement is in order to
            know if the method "insert" was successful. It will give you a positive number if
             is successful, and a negative number if not */
        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        if (insert ==-1) {
            return false;
        } else {
            return true;
        }
    }

    public List<Customer> getAll() {
        // Create the ArrayList that the method will return
        List<Customer> returnList = new ArrayList<>();

        // Get data from the database
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        /* We execute the SQL query. We use .rawQuery because the return type is Cursor.
            Cursor is the set of data that we obtain when executing the SQL statement. */
        Cursor cursor = db.rawQuery(queryString,null);
        // Here we use .moveToFirst to know if the cursor has data.
        if (cursor.moveToFirst()) {
            /* Loop through the cursor (result set) and create a new customer object.
                Put them into the returnList */
            do {
                // We obtain the data from the columns, one by one
                int customerId = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                /* SQLite doesn't support boolean, just integer. So we made this trick.
                    Ternary operation: result = theSkyIsBlue ? true: false;
                    Here, first we put a boolean expression, then a question mark, then the expression
                    we want to store if the boolean expression is true, then the one we want to
                    store if the boolean expresion is false */
                boolean customerActive = cursor.getInt(3) == 1 ? true: false;

                // We add the new customer to the ListView
                Customer newCustomer = new Customer(customerId,customerName,customerAge,customerActive);
                returnList.add(newCustomer);

            } while (cursor.moveToNext()); // While there's still data to be checked
        } else {
            // Failure. Do not add anything to the list
        }
        return returnList;
    }

    public List<Customer> getSearchedCustomer(String searchedCustomer, Context context) {
        // Create the ArrayList that the method will return
        List<Customer> returnList = new ArrayList<>();

        // Get data from the database
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_CUSTOMER_NAME +
                "='" + searchedCustomer + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        /* We execute the SQL query. We use .rawQuery because the return type is Cursor.
            Cursor is the set of data that we obtain when executing the SQL statement. */
        Cursor cursor = db.rawQuery(queryString,null);
        // Here we use .moveToFirst to know if the cursor has data.
        if (cursor.moveToFirst()) {
            /* Loop through the cursor (result set) and create a new customer object.
                Put them into the returnList */
            do {
                // We obtain the data from the columns, one by one
                int customerId = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                /* SQLite doesn't support boolean, just integer. So we made this trick.
                    Ternary operation: result = theSkyIsBlue ? true: false;
                    Here, first we put a boolean expression, then a question mark, then the expression
                    we want to store if the boolean expression is true, then the one we want to
                    store if the boolean expresion is false */
                boolean customerActive = cursor.getInt(3) == 1 ? true: false;

                // We add the new customer to the Listview
                Customer newCustomer = new Customer(customerId,customerName,customerAge,customerActive);
                returnList.add(newCustomer);

            } while (cursor.moveToNext()); // While there's still data to be checked
        } else {
            Toast.makeText(context, "Customer not found", Toast.LENGTH_SHORT).show();
            //Tell the user the Customer was not found
        }
        return returnList;
    }

    public boolean deleteOne(Customer customer) {
        // Find Customer in the database. If it's found, delete it and return true
        // If it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " +
                customer.getId();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }
}
