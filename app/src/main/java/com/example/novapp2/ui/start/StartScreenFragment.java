package com.example.novapp2.ui.start;

import static com.example.novapp2.utils.Constants.USER_LOCAL_MAIL;
import static com.example.novapp2.utils.Constants.USER_LOCAL_PASSWORD;

import com.example.novapp2.service.AuthService;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.google.android.material.snackbar.Snackbar;


import java.io.File;
import java.util.Map;

public class StartScreenFragment extends Fragment {

    public Map<String, String> savedValuesMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = requireContext();
        savedValuesMap = AuthService.getUserCredentials(context);
        return inflater.inflate(R.layout.fragment_start_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != savedValuesMap) {
            AuthService.signIn(savedValuesMap.get(USER_LOCAL_MAIL), savedValuesMap.get(USER_LOCAL_PASSWORD), requireActivity(), new AuthService.SignInCallback() {
                @Override
                public void onSignInSuccess() {
                    MainActivity.getNavController().navigate(R.id.action_start_to_home);
                }

                @Override
                public void onSignInFailure() {
                    Snackbar.make(getView(), R.string.error, Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                MainActivity.getNavController().navigate(R.id.action_start_to_onboarding);
            }, 1500);
        }
    }

}