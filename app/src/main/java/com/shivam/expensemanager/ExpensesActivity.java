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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ExpensesActivity extends AppCompatActivity {

    TextView category;
    TextView date;
    TextView amount;
    ListView listView;
    String TRIP_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        getSupportActionBar().setTitle("Expenses");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        category = (TextView) findViewById(R.id.expensesActivityCategoryTextView);
        date = (TextView) findViewById(R.id.expensesActivityDateTextView);
        amount = (TextView) findViewById(R.id.expensesActivityAmountTextView);
        listView = (ListView) findViewById(R.id.expenseActivityDetailListView);

    }

    class MyCustomListAdapter extends CursorAdapter{

        public MyCustomListAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = null;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expenses_activity_list_view_theme_detial,parent,false);
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
                Intent intent = new Intent(ExpensesActivity.this,AddExpenseActivity.class);
                startActivity(intent);
                break;
            case R.id.expensesMenuBarGraphRepresentation:
                intent = new Intent(ExpensesActivity.this,BarChartRepresentationActivity.class);
                intent.putExtra("TRIP_ID",TRIP_ID);
                startActivity(intent);
                break;
            case R.id.expensesMenuShowCategoryWise:
                intent = new Intent(ExpensesActivity.this,CategoryWiseExpenseActivity.class);
                startActivity(intent);
                break;
            case R.id.expensesMenuShowDateWise:
                intent = new Intent(ExpensesActivity.this,DateWiseExpenseActivity.class);
                startActivity(intent);
                break;
            case R.id.expensesMenuShowInDetail:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        query = "SELECT * FROM EXPENSE_DETAILS WHERE TRIP_ID = '" + TRIP_ID + "'";
        cursor = db.rawQuery(query,null);

        CursorAdapter cursorAdapter = new MyCustomListAdapter(ExpensesActivity.this,cursor,0);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                String particulars = cursor.getString(cursor.getColumnIndex("PARTICULARS"));
                AlertDialog.Builder builder = new AlertDialog.Builder(ExpensesActivity.this);
                builder.setTitle("PARTICULARS");
                builder.setMessage(particulars);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
