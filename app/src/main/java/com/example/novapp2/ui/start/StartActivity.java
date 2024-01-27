package com.example.novapp2.ui.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.ui.carousel.CarouselActivity;
import com.example.novapp2.ui.login.LoginActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(StartActivity.this, CarouselActivity.class);
                startActivity(intent);
            }
        }, 1500);

    }
}