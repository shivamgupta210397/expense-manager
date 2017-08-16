package com.shivam.expensemanager;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PreviousTrip1Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_trip1);

        getSupportActionBar().setTitle("Previous Trip");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.previousTripActivityListView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //INFLATE THE PREVIOUS ACTIVITY IF THERE ARE ANY PREVIOUS TRIP
        SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
        String query = "SELECT * FROM TRIP_DETAILS WHERE DATE_OF_END !='NULL'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            CursorAdapter cursorAdapter = new MyCursorAdapter(this, cursor, 0);
            listView.setAdapter(cursorAdapter);
            listView.setOnItemClickListener(this);
        } else {
            Intent intent = new Intent(PreviousTrip1Activity.this,PreviousTripActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        String TRIP_ID = cursor.getString(cursor.getColumnIndex("TRIP_ID"));
        Intent intent = new Intent(PreviousTrip1Activity.this,PreviousTrip2Activity.class);
        intent.putExtra("TRIP_ID",TRIP_ID);
        startActivity(intent);
    }

    class MyCursorAdapter extends CursorAdapter{

        public MyCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = null;
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.previous_trip_list_view_theme,parent,false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView from = (TextView) view.findViewById(R.id.activityPreviousTrip1FromTextView);
            TextView to = (TextView) view.findViewById(R.id.activityPreviousTrip1ToTextView);
            TextView totalBudget = (TextView) view.findViewById(R.id.activityPreviousTrip1TotalBudgetTextView);
            TextView progressUpdate = (TextView) view.findViewById(R.id.activityPreviousTrip1ProgressTextView);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.activityPreviousTrip1ProgressBar);

            from.setText(cursor.getString(cursor.getColumnIndex("SOURCE")));
            to.setText(cursor.getString(cursor.getColumnIndex("DESTINATION")));
            totalBudget.setText("Rs " + cursor.getInt(cursor.getColumnIndex("APPROVED_BUDGET")));

            float balanced_amount = (float)cursor.getInt(cursor.getColumnIndex("BALANCED_BUDGET"));
            float approved_amount = (float)cursor.getInt(cursor.getColumnIndex("APPROVED_BUDGET"));
            int progressStatus = (int)(100-(((approved_amount-balanced_amount)/approved_amount)*100));
            if(progressStatus<0)
                progressStatus = 0;
            progressUpdate.setText(progressStatus + "% left");
            progressBar.setProgress(progressStatus);
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
