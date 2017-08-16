package com.shivam.expensemanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DateWiseExpenseActivity extends AppCompatActivity {

    TextView date;
    TextView amount;
    ListView listView;
    String TRIP_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise_expense);

        getSupportActionBar().setTitle("Expenses");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date = (TextView) findViewById(R.id.expensesActivityDateTextView);
        amount = (TextView) findViewById(R.id.expensesActivityAmountTextView);
        listView = (ListView) findViewById(R.id.expenseActivityDetailListView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //DATABASE HANDLER CODE(INFLATE THE EXPENSE IF ANY -> DETAIL VIEW)
        SQLiteDatabase db = openOrCreateDatabase("EXPENSE_MANAGER_DB",MODE_APPEND,null);
        String query = "SELECT * FROM TRIP_DETAILS WHERE DATE_OF_END = 'NULL' ";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()){
            TRIP_ID = cursor.getString(cursor.getColumnIndex("TRIP_ID"));
        }
        query = "SELECT _id,DATE,SUM(AMOUNT) FROM EXPENSE_DETAILS WHERE TRIP_ID = '" + TRIP_ID + "' GROUP BY DATE";
        cursor = db.rawQuery(query,null);

        CursorAdapter cursorAdapter = new MyCustomListAdapter(DateWiseExpenseActivity.this,cursor,0);
        listView.setAdapter(cursorAdapter);
    }

    class MyCustomListAdapter extends CursorAdapter{

        public MyCustomListAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = null;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expense_activity_list_view_theme_date,parent,false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView date = (TextView) view.findViewById(R.id.expensesActivityListViewThemeDateDateTextView);
            TextView price = (TextView) view.findViewById(R.id.expensesActivityListViewThemeDateAmountTextView);

            String DATE = cursor.getString(cursor.getColumnIndex("DATE"));
            String PRICE = cursor.getString(cursor.getColumnIndex("SUM(AMOUNT)"));

            date.setText(DATE);
            price.setText("RS " + PRICE);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.expenses_activity_menu_file,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                supportFinishAfterTransition();
                break;
            case R.id.expensesMenuAddExpense:
                Intent intent = new Intent(this,AddExpenseActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.expensesMenuBarGraphRepresentation:
                intent = new Intent(this,BarChartRepresentationActivity.class);
                intent.putExtra("TRIP_ID",TRIP_ID);
                startActivity(intent);
                break;
            case R.id.expensesMenuShowCategoryWise:
                intent = new Intent(this,CategoryWiseExpenseActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.expensesMenuShowDateWise:
                break;
            case R.id.expensesMenuShowInDetail:
                //intent = new Intent(this,ExpensesActivity.class);
                //startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
