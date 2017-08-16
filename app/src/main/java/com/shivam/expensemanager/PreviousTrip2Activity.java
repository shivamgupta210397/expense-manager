package com.shivam.expensemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PreviousTrip2Activity extends AppCompatActivity {

    TextView from;
    TextView to;
    TextView startDate;
    TextView endDate;
    TextView totalBudget;
    TextView amountSpent;
    TextView remainingBudget;
    ListView listView;
    String TRIP_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_trip2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TRIP_ID = bundle.getString("TRIP_ID");

        getSupportActionBar().setTitle("Previous Trip");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        from = (TextView) findViewById(R.id.activityPreviousTrip2FromTextView);
        to = (TextView) findViewById(R.id.activityPreviousTrip2ToTextView);
        startDate = (TextView) findViewById(R.id.activityPreviousTrip2StartDateTextView);
        endDate = (TextView) findViewById(R.id.activityPreviousTrip2EndDateTextView);
        totalBudget = (TextView) findViewById(R.id.activityPreviousTrip2TotalBudgetTextView);
        amountSpent = (TextView) findViewById(R.id.activityPreviousTrip2AmountSpentTextView);
        remainingBudget = (TextView) findViewById(R.id.activityPreviousTrip2RemainingAmountTextView);
        listView = (ListView) findViewById(R.id.activityPreviousTrip2ListView);

        SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
        String query = "SELECT * FROM TRIP_DETAILS WHERE TRIP_ID = '" + TRIP_ID + "'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()){
            from.setText(cursor.getString(cursor.getColumnIndex("SOURCE")));
            to.setText(cursor.getString(cursor.getColumnIndex("DESTINATION")));
            startDate.setText(cursor.getString(cursor.getColumnIndex("DATE_OF_START")));
            endDate.setText(cursor.getString(cursor.getColumnIndex("DATE_OF_END")));
            totalBudget.setText("RS " + String.valueOf(cursor.getInt(cursor.getColumnIndex("APPROVED_BUDGET"))));
            remainingBudget.setText("RS " + String.valueOf(cursor.getInt(cursor.getColumnIndex("BALANCED_BUDGET"))));
            int amount_spent = (cursor.getInt(cursor.getColumnIndex("APPROVED_BUDGET"))) -
                    (cursor.getInt(cursor.getColumnIndex("BALANCED_BUDGET")));
            amountSpent.setText("RS " + String.valueOf(amount_spent));
        }

        query = "SELECT * FROM EXPENSE_DETAILS WHERE TRIP_ID = '" + TRIP_ID + "'";
        cursor = db.rawQuery(query,null);
        CursorAdapter cursorAdapter = new MyAdapter(this,cursor,0);
        listView.setAdapter(cursorAdapter);
    }

    class MyAdapter extends CursorAdapter{

        public MyAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = null;
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.expenses_activity_list_view_theme_detial,parent,false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            ImageView imageView = (ImageView) view.findViewById(R.id.expensesActivityListViewThemeDetailImageView);
            TextView category = (TextView) view.findViewById(R.id.expensesActivityListViewThemeDetailTextView);
            TextView date = (TextView) view.findViewById(R.id.expensesActivityListViewThemeDetailTextView2);
            TextView price = (TextView) view.findViewById(R.id.expensesActivityListViewThemeDetailTextView3);

            String CATEGORY = cursor.getString(cursor.getColumnIndex("CATEGORY"));
            String DATE = cursor.getString(cursor.getColumnIndex("DATE"));
            String PRICE = cursor.getString(cursor.getColumnIndex("AMOUNT"));

            switch (CATEGORY){
                case "Travel":
                    imageView.setImageResource(R.drawable.travel_icon);
                    break;
                case "Lodging":
                    imageView.setImageResource(R.drawable.lodging_icon);
                    break;
                case "Food":
                    imageView.setImageResource(R.drawable.breakfast_icon);
                    break;
                case "Shopping":
                    imageView.setImageResource(R.drawable.shopping_cart_icon);
                    break;
                case "Miscellaneous":
                    imageView.setImageResource(R.drawable.miscellaneous_icon);
                    break;
            }
            category.setText(CATEGORY);
            date.setText(DATE);
            price.setText("RS " + PRICE);

        }
    }

    private void deleteTrip(){

        AlertDialog.Builder builder = new AlertDialog.Builder(PreviousTrip2Activity.this);
        builder.setTitle("Delete Trip");
        builder.setMessage("Are you sure you want to delete this trip. This can't be undone.");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
                String query = "DELETE FROM EXPENSE_DETAILS WHERE TRIP_ID = '" + TRIP_ID + "'" ;
                db.execSQL(query);
                query = "DELETE FROM TRIP_DETAILS WHERE TRIP_ID = '" + TRIP_ID + "'";
                db.execSQL(query);
                Toast.makeText(PreviousTrip2Activity.this, "Trip Deleted Successfully", Toast.LENGTH_SHORT).show();
             //   Intent intent = new Intent(PreviousTrip2Activity.this,PreviousTrip1Activity.class);
             //   startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.previous_trip_menu_file,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
            case R.id.previousTripMenuDelete:
                deleteTrip();
                break;
            case R.id.previousTripMenuBarGraphRepresentation:
                Intent intent = new Intent(PreviousTrip2Activity.this,BarChartRepresentationActivity.class);
                intent.putExtra("TRIP_ID",TRIP_ID);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
