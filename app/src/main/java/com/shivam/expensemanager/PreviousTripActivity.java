package com.shivam.expensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class PreviousTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_trip);

        getSupportActionBar().setTitle("Previous Trips");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
        String query = "SELECT * FROM TRIP_DETAILS WHERE DATE_OF_END != 'NULL'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()){
            Intent intent = new Intent(this,PreviousTrip1Activity.class);
            startActivity(intent);
            finish();
        }

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
