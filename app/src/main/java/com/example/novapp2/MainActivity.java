package com.example.novapp2;

import static com.example.novapp2.utils.Constants.USER_LOCAL_FILE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.os.Bundle;

import com.example.novapp2.utils.FixCursorWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static NavController navController ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FixCursorWindow.fix();
        //TODO check di utente già loggato, se è già loggato va nello start screen
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostContainer);
        navController = navHostFragment.getNavController();
    }

    public static NavController getNavController() {
        return navController;
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