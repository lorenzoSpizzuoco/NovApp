package com.example.novapp2.ui.start;

import static com.example.novapp2.utils.Constants.USER_LOCAL_FILE;
import static com.example.novapp2.utils.Constants.USER_LOCAL_MAIL;
import static com.example.novapp2.utils.Constants.USER_LOCAL_PASSWORD;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.ui.carousel.CarouselActivity;

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

public class StartScreenFragment extends Fragment {

    private static File file;

    private static String filename;

    private Context context;

    public Map<String, String> savedValuesMap;

    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = requireContext();
        savedValuesMap = getUserCredentials();
        Log.d("CHECK","" + savedValuesMap);
        return inflater.inflate(R.layout.fragment_start_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (null != savedValuesMap){
                    bundle = new Bundle();
                    bundle.putString(USER_LOCAL_MAIL, savedValuesMap.get(USER_LOCAL_MAIL));
                    bundle.putString(USER_LOCAL_PASSWORD, savedValuesMap.get(USER_LOCAL_PASSWORD));
                    MainActivity.getNavController().navigate(R.id.action_start_to_login, bundle);
                } else {
                    MainActivity.getNavController().navigate(R.id.action_start_to_onboarding);
                }
            }
        }, 2000);
    }

    public Map<String, String> getUserCredentials() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_LOCAL_FILE, Context.MODE_PRIVATE);
        Map<String, String> credentials = new HashMap<>();
        credentials.put(USER_LOCAL_MAIL, sharedPreferences.getString(USER_LOCAL_MAIL, null)); // Ritorna null se "Email" non esiste
        credentials.put(USER_LOCAL_PASSWORD, sharedPreferences.getString(USER_LOCAL_PASSWORD, null)); // Ritorna null se "Password" non esiste

        return (credentials.get(USER_LOCAL_MAIL) != null && credentials.get(USER_LOCAL_PASSWORD) != null) ? credentials : null;
    }

}