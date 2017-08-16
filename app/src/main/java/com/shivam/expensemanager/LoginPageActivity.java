package com.shivam.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPageActivity extends AppCompatActivity {

    EditText username;
    EditText phoneNumber;
    Button login;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = (EditText) findViewById(R.id.loginPageUsernameEditText);
        phoneNumber = (EditText) findViewById(R.id.loginPagePhoneNumberEditText);
        login = (Button) findViewById(R.id.loginPageloginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("") || phoneNumber.getText().toString().equals("")){
                    Toast.makeText(LoginPageActivity.this, "Insufficient Information", Toast.LENGTH_SHORT).show();
                } else{
                    sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERNAME",username.getText().toString());
                    editor.putString("PHONE_NUMBER",phoneNumber.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(LoginPageActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
