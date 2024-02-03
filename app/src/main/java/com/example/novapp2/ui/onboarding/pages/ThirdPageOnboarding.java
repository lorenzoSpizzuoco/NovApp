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
}