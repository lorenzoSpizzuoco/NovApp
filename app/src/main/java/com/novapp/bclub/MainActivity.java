package com.novapp.bclub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.novapp.bclub.R;

import com.novapp.bclub.service.nativeapi.UserService;


public class MainActivity extends AppCompatActivity {

    private static NavController navController ;
    private final UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO check di utente già loggato, se è già loggato va nello start screen
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostContainer);
        navController = navHostFragment.getNavController();
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

/*        String fileContents = "Hello world!";
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.toByteArray());
        }*/
    }
}