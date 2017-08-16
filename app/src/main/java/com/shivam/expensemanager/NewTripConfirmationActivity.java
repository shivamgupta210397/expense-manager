package com.shivam.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewTripConfirmationActivity extends AppCompatActivity {

    TextView from;
    TextView to;
    TextView totalBudget;
    TextView startDate;
    Button cancel;
    Button confirm;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip_confirmation);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        from = (TextView) findViewById(R.id.newTripConfirmationFromTextView);
        to = (TextView) findViewById(R.id.newTripConfirmationToTextView);
        totalBudget = (TextView) findViewById(R.id.newTripConfirmationTotalBudgetTextView);
        startDate = (TextView) findViewById(R.id.newTripConfirmationStartDateTextView);
        cancel = (Button) findViewById(R.id.newTripConfirmationCancelButton);
        confirm = (Button) findViewById(R.id.newTripConfirmationConfirmButton);

        from.setText("From: " + bundle.getString("FROM"));
        to.setText("To: " + bundle.getString("TO"));
        totalBudget.setText("Total Budget: " + bundle.getString("TOTAL_BUDGET"));
        startDate.setText("Start Date: " + bundle.getString("START_DATE"));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewTripConfirmationActivity.this, "Trip Added Successfully", Toast.LENGTH_SHORT).show();

                // WE GENERATE THE TRIP_ID BY COMBING TO AND START DATE
                String TRIP_ID = bundle.getString("TO") + bundle.getString("START_DATE");
                String TRIP_DESTINATION = bundle.getString("TO");
                String TRIP_SOURCE = bundle.getString("FROM");
                String TRIP_START_DATE = bundle.getString("START_DATE");
                String TRIP_END_DATE = "NULL";
                int TRIP_APPROVED_BUDGET = Integer.parseInt(bundle.getString("TOTAL_BUDGET"));
                int TRIP_BALANCED_BUDGET = Integer.parseInt(bundle.getString("TOTAL_BUDGET"));

                //DATABASE HANDLER CODE(INSERT NEW RECORD INTO TRIP_DETAILS)
                SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
                String insertQuery = "INSERT INTO TRIP_DETAILS(TRIP_ID,DESTINATION,SOURCE,DATE_OF_START,DATE_OF_END," +
                        "APPROVED_BUDGET,BALANCED_BUDGET) " +
                        "VALUES('" + TRIP_ID + "'," + "'" + TRIP_DESTINATION + "'," + "'" + TRIP_SOURCE + "'," +
                                "'" + TRIP_START_DATE + "'," + "'" + TRIP_END_DATE + "'," + TRIP_APPROVED_BUDGET + "," +
                                TRIP_BALANCED_BUDGET + ")";
                db.execSQL(insertQuery);

                sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("CURRENT_TRIP_EXISTS","TRUE");
                editor.commit();

                Intent intent = new Intent(NewTripConfirmationActivity.this,CurrentTripActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
