package com.example.insight.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.insight.R;
import com.example.insight.base.DataBase;

public class AddUserActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_key);
        EditText ViewKey = (EditText) findViewById(R.id.ViewKey);

        DataBase db = new DataBase(this);
        String key = db.getUser(10).getKey();
        ViewKey.setText(key);
    }

    public void onClickExit(View view){
        finish();
    }
}
