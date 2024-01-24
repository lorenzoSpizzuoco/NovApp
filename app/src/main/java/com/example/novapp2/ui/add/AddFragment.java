package com.example.novapp2.ui.add;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.novapp2.R;

public class AddFragment extends Fragment {

    private Button newEvent;
    private Button newGs;
    private Button newInfo;
    private Button newRipet;


    public AddFragment() {
        // Required empty public constructor
    }


    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
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
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle newInstanceState) {
        newGs = view.findViewById(R.id.new_gs_button);
        newRipet = view.findViewById(R.id.new_ripetizioni_button);
        newEvent = view.findViewById(R.id.new_event_button);
        newInfo = view.findViewById(R.id.new_info_button);

        newGs.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_navigation_add_to_newGsFragment);
        });

        newRipet.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_navigation_add_to_newRipetizioniFragment);
        });

        newEvent.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_navigation_add_to_newEventFragment);
        });

        newInfo.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_navigation_add_to_newInfoFragment);
        });
    }
}