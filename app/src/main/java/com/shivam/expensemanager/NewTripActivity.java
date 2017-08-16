package com.shivam.expensemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewTripActivity extends AppCompatActivity {

    Button cancel;
    Button startTrip;
    TextView dateTextView;
    EditText fromEditText;
    EditText toEditText;
    EditText totalBudgetEditText;
    String []month = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cancel = (Button) findViewById(R.id.newTripActivityCancelButton);
        dateTextView = (TextView) findViewById(R.id.newTripActivityDate);
        startTrip = (Button) findViewById(R.id.newTripActivityStartTripButton);
        fromEditText = (EditText) findViewById(R.id.newTripActivityFromEditText);
        toEditText = (EditText) findViewById(R.id.newTripActivityToEditText);
        totalBudgetEditText = (EditText) findViewById(R.id.newTripActivityTotalBudgetEditText);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewTripActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Date date = new Date();
                final GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date);
                final int y = gregorianCalendar.get(Calendar.YEAR);
                final int m = gregorianCalendar.get(Calendar.MONTH);
                final int d = gregorianCalendar.get(Calendar.DATE);
                DatePickerDialog dialog = new DatePickerDialog(NewTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //VALIDATION FOR NOT SELECTING DATE PRIOR TO CURRENT DATE
                        String selectedDate = i + "-" + i1 + "-" + i2;
                        String currentDate = y + "-" + m + "-" + d;
                        String str = i2 + " " + month[i1] + " " + i;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            if(sdf.parse(selectedDate).before(sdf.parse(currentDate))){
                                Toast.makeText(NewTripActivity.this, "Previous Date Can't Be Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                dateTextView.setText(str);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                },y,m,d);
                dialog.show();
            }
        });

        startTrip.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(fromEditText.getText().toString().equals("") || toEditText.getText().toString().equals("") ||
                        totalBudgetEditText.getText().toString().equals("") || dateTextView.getText().toString().equals("Select Date")){
                    Toast.makeText(NewTripActivity.this, "Insufficient Information", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(NewTripActivity.this, NewTripConfirmationActivity.class);
                    intent.putExtra("FROM", fromEditText.getText().toString());
                    intent.putExtra("TO", toEditText.getText().toString());
                    intent.putExtra("TOTAL_BUDGET", totalBudgetEditText.getText().toString());
                    intent.putExtra("START_DATE", dateTextView.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TO MAINTAIN THE FLOW OF THE PROGRAM (AFTER CONFIRMING NEW TRIP PRESS BACK BUTTON CAUSES OPENING NEW TRIP ACTIVITY)
        SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
        String query = "SELECT * FROM TRIP_DETAILS WHERE DATE_OF_END = 'NULL'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()){
            finish();
        }
    }
}
