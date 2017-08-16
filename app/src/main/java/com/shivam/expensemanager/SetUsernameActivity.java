package com.shivam.expensemanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SetUsernameActivity extends AppCompatActivity {

    EditText username;
    TextView cancel;
    TextView ok;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);

        cancel = (TextView) findViewById(R.id.activitySetUsernameCancel);
        ok = (TextView) findViewById(R.id.activitySetUsernameOk);
        username = (EditText) findViewById(R.id.settingsActivityUserName);

        sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
        String mUsername = sharedPreferences.getString("USERNAME","User Name");
        username.setText(mUsername);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USERNAME = username.getText().toString();
                sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USERNAME",USERNAME);
                editor.commit();
                supportFinishAfterTransition();
            }
        });

    }
}
