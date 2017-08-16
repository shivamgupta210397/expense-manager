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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout userName;
    LinearLayout pin;
    CheckBox checkBox;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("EXPENSE_MANAGER_FILE",0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        userName = (LinearLayout) findViewById(R.id.settingsActivityLinearLayout1);
        pin = (LinearLayout) findViewById(R.id.settingsActivityLinearLayout3);
        checkBox = (CheckBox) findViewById(R.id.settingsActivityCheckBox);

        String pinStatus = sharedPreferences.getString("PIN_STATUS","FALSE");
        if(pinStatus.equals("TRUE")){
            checkBox.setChecked(true);
            pin.setEnabled(true);
            pin.setAlpha(1.0F);
        } else{
            checkBox.setChecked(false);
            pin.setEnabled(false);
            pin.setAlpha(0.4F);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pin.setEnabled(true);
                    pin.setAlpha(1.0F);
                    editor.putString("PIN_STATUS","TRUE");
                    editor.commit();
                } else{
                    pin.setEnabled(false);
                    pin.setAlpha(0.6F);
                    editor.putString("PIN_STATUS","FALSE");
                    editor.commit();
                }
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(SettingsActivity.this,SetUsernameActivity.class);
                startActivity(intent);
            }
        });

        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(SettingsActivity.this,SetPinActivity.class);
                startActivity(intent);
            }
        });
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
