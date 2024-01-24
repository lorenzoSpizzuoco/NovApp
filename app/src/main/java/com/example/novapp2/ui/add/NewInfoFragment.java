package com.example.novapp2.ui.add;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novapp2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class NewInfoFragment extends Fragment {



    public NewInfoFragment() {
        // Required empty public constructor
    }


    public static NewInfoFragment newInstance(String param1, String param2) {
        NewInfoFragment fragment = new NewInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_info, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {

        NavBackStackEntry navBackStackEntry = Navigation.
                findNavController(view).getPreviousBackStackEntry();

        // selecting right bottombar icon
        if (navBackStackEntry != null &&
                navBackStackEntry.getDestination().getId() == R.id.navigation_add) {
            ((BottomNavigationView) requireActivity().findViewById(R.id.nav_view)).
                    getMenu().findItem(R.id.navigation_add).setChecked(true);
        }
    }

}