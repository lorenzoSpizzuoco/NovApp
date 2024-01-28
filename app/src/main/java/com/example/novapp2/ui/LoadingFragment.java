package com.example.novapp2.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novapp2.R;
import com.example.novapp2.entity.ad.Ad;
import com.example.novapp2.entity.ad.AdViewModel;


public class LoadingFragment extends Fragment {

    private AdViewModel adViewModel;
    private Ad ad;
    private final String TAG = LoadingFragment.class.getSimpleName();

    public static LoadingFragment newInstance(String param1, String param2) {
        LoadingFragment fragment = new LoadingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adViewModel = new ViewModelProvider(this).get(AdViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getParcelable("adobj") != null) {
            ad = bundle.getParcelable("adobj");
        }
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstaceState) {
        Log.d(TAG, "sono qui");

        // inserting new add
        adViewModel.insert(ad);


        adViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                Log.d(TAG, "inside observer");
                Navigation.findNavController(getView()).navigate(R.id.action_loadingFragment_to_navigation_home);

                //Navigation.findNavController(requireView()).navigateUp();
            }
        });
    }
}