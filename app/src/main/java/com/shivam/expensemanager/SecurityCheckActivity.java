package com.shivam.expensemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SecurityCheckActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText editText;
    TextView cancel;
    TextView ok;
    String pinStatus = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_check);

        editText = (EditText) findViewById(R.id.securityCheckEditText);
        cancel = (TextView) findViewById(R.id.securityCheckCancel);
        ok = (TextView) findViewById(R.id.securityCheckOk);

        sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
        pinStatus = sharedPreferences.getString("PIN_STATUS","FALSE");

        if(pinStatus.equals("FALSE")){
            Intent intent = new Intent(SecurityCheckActivity.this,SplashScreenActivity.class);
            startActivity(intent);
            supportFinishAfterTransition();
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String pin = sharedPreferences.getString("PIN","NULL");
                    if(editText.getText().toString().equals(pin)){
                        Intent intent = new Intent(SecurityCheckActivity.this,SplashScreenActivity.class);
                        startActivity(intent);
                        supportFinishAfterTransition();
                    } else{
                        editText.setText("");
                        Toast.makeText(SecurityCheckActivity.this, "Wrong Pin", Toast.LENGTH_SHORT).show();
                    }
                }
        });


    }
}
