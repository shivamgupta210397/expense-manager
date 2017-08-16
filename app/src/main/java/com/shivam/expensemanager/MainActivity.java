package com.shivam.expensemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageView;
    TextView username;
    TextView phoneNumber;
    TextView totalTrips;
    TextView totalMoneySpent;
    String []month = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    private SharedPreferences sharedPreferences;
    private String TOTAL_TRIPS;
    private String TOTAL_MONEY_SPENT = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
        String value = sharedPreferences.getString("CURRENT_TRIP_EXISTS","FALSE");
        if(value.equals("TRUE")){
            Intent intent = new Intent(this,CurrentTripActivity.class);
            startActivity(intent);
            finish();
        }

        imageView = (ImageView) findViewById(R.id.mainActivityNoCurrentTripImageView);
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
                        Intent intent = new Intent(MainActivity.this,PreviousTripActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navigationMenuSettings:
                        intent = new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.navigationMenuAbout:
                        intent = new Intent(MainActivity.this,AboutActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,NewTripActivity.class);
                startActivity(intent);
                finish();
            }
        });

        createTables();
        inflateDrawerHeader();
    }

    private void inflateDrawerHeader() {
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


    private void createTables(){
        //DATABASE HANDLER CODE (CREATION OF TABLES)
        SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
        String createTableQuery = "Create table if not exists " +
                "TRIP_DETAILS ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TRIP_ID VARCHAR(50)," +
                "DESTINATION VARCHAR(30)," +
                "SOURCE VARCHAR(30)," +
                "DATE_OF_START VARCHAR(20)," +
                "DATE_OF_END VARCHAR(20)," +
                "APPROVED_BUDGET INTEGER," +
                "BALANCED_BUDGET INTEGER );";
        db.execSQL(createTableQuery);

        createTableQuery = "Create table if not exists " +
                "EXPENSE_DETAILS( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "CATEGORY varchar(20)," +
                "PARTICULARS varchar(40)," +
                "AMOUNT Integer," +
                "DATE varchar(20)," +
                "TRIP_ID varchar(30)," +
                "FOREIGN KEY(TRIP_ID) REFERENCES TRIP_DETAILS(TRIP_ID) );";
        db.execSQL(createTableQuery);

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
