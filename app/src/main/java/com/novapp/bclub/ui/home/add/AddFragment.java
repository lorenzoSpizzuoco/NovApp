package com.novapp.bclub.ui.home.add;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.novapp.bclub.R;

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
            new NewGsDialog().show(getChildFragmentManager(), null);
        });

        newRipet.setOnClickListener(v -> {
            new NewRipetDialog().show(getChildFragmentManager(), null);
        });

        newEvent.setOnClickListener(v -> {
            new NewEventDialog().show(getChildFragmentManager(), null);
        });

        newInfo.setOnClickListener(v -> {
            new NewInfoDialog().show(getChildFragmentManager(), null);
        });
    }
}