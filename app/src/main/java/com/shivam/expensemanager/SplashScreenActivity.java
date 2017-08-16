package com.shivam.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
        final String username = sharedPreferences.getString("USERNAME","User Name");

        Thread t = new Thread()
        {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if(username.equals("User Name")){
                        Intent intent = new Intent(SplashScreenActivity.this,LoginPageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }
}
