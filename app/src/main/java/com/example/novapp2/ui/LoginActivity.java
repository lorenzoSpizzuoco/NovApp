package com.example.novapp2.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.novapp2.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // hiding action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}