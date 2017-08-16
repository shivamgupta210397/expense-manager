package com.shivam.expensemanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SetPinActivity extends AppCompatActivity {

    EditText pin;
    TextView cancel;
    TextView ok;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);

        sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
        String mPin = sharedPreferences.getString("PIN","NULL");

        cancel = (TextView) findViewById(R.id.activitySetPinCancel);
        ok = (TextView) findViewById(R.id.activitySetPinOk);
        pin = (EditText) findViewById(R.id.settingsActivityPin);
        pin.setText(mPin);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("PIN",pin.getText().toString());
                editor.commit();
                supportFinishAfterTransition();
            }
        });
    }
}
