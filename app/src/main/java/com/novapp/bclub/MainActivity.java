package com.novapp.bclub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.novapp.bclub.service.nativeapi.UserService;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public static NavController navController ;
    private final UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostContainer);
        navController = Objects.requireNonNull(navHostFragment).getNavController();
    }

    public static NavController getNavController() {
        return navController;
    }

    @Override
    protected void onPause() {
        super.onPause();
        userService.setRemoteSaved();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}