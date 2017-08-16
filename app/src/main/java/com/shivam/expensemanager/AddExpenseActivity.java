package com.shivam.expensemanager;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    Spinner spinner;
    EditText particulars;
    EditText amount;
    TextView date;
    TextView categorySelected;
    Button cancelButton;
    Button addExpenseButton;

    String []category = {"Travel","Lodging","Food","Shopping","Miscellaneous"};
    String []month = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        getSupportActionBar().setTitle("Add Expense");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (Spinner) findViewById(R.id.addExpenseActivitySpinner);
        particulars = (EditText) findViewById(R.id.addExpenseActivityParticulars);
        amount = (EditText) findViewById(R.id.addExpenseActivityAmount);
        date = (TextView) findViewById(R.id.addExpenseActivityDate);
        categorySelected = (TextView) findViewById(R.id.addExpenseActivityCategorySelected);
        cancelButton = (Button) findViewById(R.id.addExpenseActivityCancelButton);
        addExpenseButton = (Button) findViewById(R.id.addExpenseActivityAddExpenseButton);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categorySelected.setText(category[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Date date1 = new Date();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date1);
                final int y = gregorianCalendar.get(Calendar.YEAR);
                final int m = gregorianCalendar.get(Calendar.MONTH);
                final int d = gregorianCalendar.get(Calendar.DATE);
                DatePickerDialog dialog = new DatePickerDialog(AddExpenseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //VALIDATION FOR NOT SELECTING DATE PRIOR TO CURRENT DATE
                        String selectedDate = i + "-" + i1 + "-" + i2;
                        String currentDate = y + "-" + m + "-" + d;
                        String str = i2 + " " + month[i1] + " " + i;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            if(sdf.parse(selectedDate).before(sdf.parse(currentDate))){
                                Toast.makeText(AddExpenseActivity.this, "Previous Date Can't Be Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                date.setText(str);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },y,m,d);
                dialog.show();
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(particulars.getText().toString().equals("") || amount.getText().toString().equals("") || date.getText().toString()
                        .equals("Select Date")){
                    Toast.makeText(AddExpenseActivity.this, "Insufficient Information", Toast.LENGTH_SHORT).show();
                } else {
                    String CATEGORY = categorySelected.getText().toString();
                    String PARTICULARS = particulars.getText().toString();
                    int AMOUNT = Integer.parseInt(amount.getText().toString());
                    String DATE = date.getText().toString();
                    String TRIP_ID = "";
                    int BALANCED_AMOUNT = 0;
                    int APPROVED_AMOUNT = 0;

                    SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB", MODE_APPEND, null);
                    String query = "SELECT TRIP_ID,BALANCED_BUDGET,APPROVED_BUDGET FROM TRIP_DETAILS WHERE DATE_OF_END = 'NULL'";
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor.moveToNext()) {
                        TRIP_ID = cursor.getString(cursor.getColumnIndex("TRIP_ID"));
                        BALANCED_AMOUNT = cursor.getInt(cursor.getColumnIndex("BALANCED_BUDGET"));
                        APPROVED_AMOUNT = cursor.getInt(cursor.getColumnIndex("APPROVED_BUDGET"));
                    }

                    //INSERT RECORD INTO EXPENSE_DETAILS OF A PARTICULAR TRIP
                    query = "INSERT INTO EXPENSE_DETAILS(CATEGORY,PARTICULARS,AMOUNT,DATE,TRIP_ID)" +
                            "VALUES('" + CATEGORY + "'," + "'" + PARTICULARS + "'," + AMOUNT +
                            ",'" + DATE + "'," + "'" + TRIP_ID + "')";
                    db.execSQL(query);
                    Toast.makeText(AddExpenseActivity.this, "Expense Added", Toast.LENGTH_SHORT).show();

                    //ALSO UPDATE THE BALANCED AMOUNT OF TRIP_DETAILS
                    query = "UPDATE TRIP_DETAILS SET BALANCED_BUDGET = " + (BALANCED_AMOUNT - AMOUNT) +
                            " WHERE DATE_OF_END = 'NULL'";
                    db.execSQL(query);

                    //NOTIFICATION TO ALERT THE USER IF BALANCED_BUDGET IS LESS THAN OR EQUAL TO 10% OF ALLOCATED BUDGET
                    if ((BALANCED_AMOUNT - AMOUNT) <= (0.1*APPROVED_AMOUNT)) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(AddExpenseActivity.this);
                        builder.setSmallIcon(R.drawable.low_balance_icon);
                        builder.setContentTitle("Budget Reminder");
                        builder.setContentText("You have spent 90% of your allocated budget.");
                        builder.setDefaults(Notification.DEFAULT_ALL);

                        Notification notification = builder.build();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        int count = 0;
                        notificationManager.notify(++count, notification);
                    }

                    //Intent intent = new Intent(AddExpenseActivity.this, ExpensesActivity.class);
                    //startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
