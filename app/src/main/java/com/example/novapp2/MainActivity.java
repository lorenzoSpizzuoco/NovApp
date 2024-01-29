package com.example.novapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.example.novapp2.utils.FixCursorWindow;

public class MainActivity extends AppCompatActivity {

    private static NavController navController ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FixCursorWindow.fix();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostContainer);
        navController = navHostFragment.getNavController();
    }

    public static NavController getNavController() {
        return navController;
    }
}