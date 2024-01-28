package com.example.novapp2.ui.onboarding.pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;

public class ThirdPageOnboarding extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third_page_carousel, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        final Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> MainActivity.getNavController().navigate(R.id.action_onboarding_to_login));
    }
}