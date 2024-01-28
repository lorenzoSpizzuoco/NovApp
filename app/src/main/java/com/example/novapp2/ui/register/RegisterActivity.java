package com.example.novapp2.ui.register;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.novapp2.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // hiding action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }


}