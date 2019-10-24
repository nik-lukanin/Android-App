package com.example.insight.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.insight.base.DataBase;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        DataBase db = new DataBase(this);
        if(db.getUser(10) != null)
            intent = new Intent(this, MainActivity.class);
        else
            intent = new Intent(this, RegistrationActivity.class);

        startActivity(intent);
        finish();
    }
}