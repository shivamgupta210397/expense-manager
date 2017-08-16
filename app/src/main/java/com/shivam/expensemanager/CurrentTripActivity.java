package com.shivam.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CurrentTripActivity extends AppCompatActivity {

    String []month = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    private TextView totalBudget;
    private Button endTrip;
    private TextView budgetLeft;
    private TextView from;
    private TextView to;
    private TextView startDate;
    private Button viewExpenses;
    TextView username;
    TextView phoneNumber;
    TextView totalTrips;
    TextView totalMoneySpent;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String TOTAL_TRIPS;
    private String TOTAL_MONEY_SPENT;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_trip);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inflateCurrentTrip();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.activityMainDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.activityMainNavigationView);
        View drawerHeader = navigationView.getHeaderView(0);
        username = (TextView) drawerHeader.findViewById(R.id.navigationHeaderUsername);
        phoneNumber = (TextView) drawerHeader.findViewById(R.id.navigationHeadePhoneNumber);
        totalTrips = (TextView) drawerHeader.findViewById(R.id.navigationHeaderTotalTrips);
        totalMoneySpent = (TextView) drawerHeader.findViewById(R.id.navigationHeaderTotalMoneySpent);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigationMenuCurrentTrip:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return true;

                    case R.id.navigationMenuPreviousTrip:
                        Intent intent = new Intent(CurrentTripActivity.this,PreviousTripActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navigationMenuSettings:
                        intent = new Intent(CurrentTripActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navigationMenuAbout:
                        intent = new Intent(CurrentTripActivity.this,AboutActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        inflateCurrentTrip();
        inflateDrawerHeader();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void inflateCurrentTrip(){
        // DATABASE HANDLER CODE
        final SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
        String query = "SELECT * FROM TRIP_DETAILS WHERE DATE_OF_END = 'NULL'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()){

            totalBudget = (TextView) findViewById(R.id.currentTripTotalBudgetTextView);
            budgetLeft = (TextView) findViewById(R.id.currentTripBudgetLeftTextView);
            from = (TextView) findViewById(R.id.currentTripFromTextView);
            to = (TextView) findViewById(R.id.currentTripToTextView);
            startDate = (TextView) findViewById(R.id.currentTripDateTextView);
            viewExpenses = (Button) findViewById(R.id.currentTripViewExpensesButton);
            endTrip = (Button) findViewById(R.id.currentTripEndTripButton);

            totalBudget.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("APPROVED_BUDGET"))));
            budgetLeft.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("BALANCED_BUDGET"))));
            from.setText(cursor.getString(cursor.getColumnIndex("SOURCE")));
            to.setText(cursor.getString(cursor.getColumnIndex("DESTINATION")));
            startDate.setText(cursor.getString(cursor.getColumnIndex("DATE_OF_START")));

            viewExpenses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CurrentTripActivity.this,ExpensesActivity.class);
                    startActivity(intent);
                }
            });

            endTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date date = new Date();
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(date);
                    int y = gc.get(Calendar.YEAR);
                    int m = gc.get(Calendar.MONTH);
                    int d = gc.get(Calendar.DATE);
                    String value = d + " " + month[m] + " " + y;
                    String query = "UPDATE TRIP_DETAILS " +
                            "SET DATE_OF_END = '" + value +
                            "' WHERE DATE_OF_END = 'NULL' ";
                    db.execSQL(query);

                    sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("CURRENT_TRIP_EXISTS","FALSE");
                    editor.commit();

                    Intent intent = new Intent(CurrentTripActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void inflateDrawerHeader() {
        sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
        String USERNAME = sharedPreferences.getString("USERNAME","USER NAME");
        String PHONE_NUMBER = sharedPreferences.getString("PHONE_NUMBER"," ");
        SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
        String query = "SELECT COUNT(TRIP_ID) FROM TRIP_DETAILS";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            TOTAL_TRIPS = cursor.getString(cursor.getColumnIndex("COUNT(TRIP_ID)"));
        }
        query = "SELECT SUM(AMOUNT) FROM EXPENSE_DETAILS";
        cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            TOTAL_MONEY_SPENT = cursor.getString(cursor.getColumnIndex("SUM(AMOUNT)"));
        }

        username.setText(USERNAME);
        phoneNumber.setText(PHONE_NUMBER);
        totalTrips.setText("Total Trips: " + TOTAL_TRIPS);
        totalMoneySpent.setText("Money Spent: Rs " + TOTAL_MONEY_SPENT);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.LEFT);
                inflateDrawerHeader();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
