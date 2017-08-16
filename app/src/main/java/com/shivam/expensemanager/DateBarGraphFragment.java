package com.shivam.expensemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DateBarGraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DateBarGraphFragment extends Fragment {

    BarChart barChart;
    String TRIP_ID;
    SQLiteDatabase db;
    Context mContext;

    private OnFragmentInteractionListener mListener;

    public DateBarGraphFragment() {
        // Required empty public constructor
    }

    public DateBarGraphFragment(String trip_id,Context context) {
        // Required empty public constructor
        TRIP_ID = trip_id;
        mContext = context;
        db = mContext.openOrCreateDatabase("EXPENSE_MANAGER_DB",Context.MODE_APPEND,null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_bar_graph, container, false);
        barChart = (BarChart) view.findViewById(R.id.dateBarChart);

        String query = "SELECT TRIP_ID FROM TRIP_DETAILS WHERE TRIP_ID = '" + TRIP_ID + "'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()){
            TRIP_ID = cursor.getString(cursor.getColumnIndex("TRIP_ID"));
            query = "SELECT * FROM EXPENSE_DETAILS WHERE TRIP_ID = '" + cursor.getString(cursor.getColumnIndex("TRIP_ID")) + "'";
            cursor = db.rawQuery(query,null);
            if(cursor.moveToNext()){
                BarData data = new BarData(getXAxisValues(), getDataSet());
                barChart.setData(data);
                barChart.setDescription("Expense Chart");
                barChart.animateXY(2000, 2000);
                barChart.invalidate();
            }
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        String query = "SELECT SUM(AMOUNT) FROM EXPENSE_DETAILS WHERE TRIP_ID = '" + TRIP_ID + "' GROUP BY DATE";
        Cursor cursor = db.rawQuery(query,null);

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        int index = 0;
        while (cursor.moveToNext()){
            BarEntry v1e1 = new BarEntry(Float.parseFloat(cursor.getString(cursor.getColumnIndex("SUM(AMOUNT)"))),index); // Jan
            valueSet1.add(v1e1);
            index++;
        }
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Price");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        String query = "SELECT DATE FROM EXPENSE_DETAILS WHERE TRIP_ID = '" + TRIP_ID + "' GROUP BY DATE";
        Cursor cursor = db.rawQuery(query,null);
        ArrayList<String> xAxis = new ArrayList<>();
        while (cursor.moveToNext()){
            xAxis.add(cursor.getString(cursor.getColumnIndex("DATE")));
        }
        return xAxis;
    }

}
