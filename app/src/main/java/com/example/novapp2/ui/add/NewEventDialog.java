package com.example.novapp2.ui.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import com.example.novapp2.R;


public class NewEventDialog extends DialogFragment {


    private Toolbar toolbar;

    public NewEventDialog() {
        // Required empty public constructor
    }



    public static NewEventDialog newInstance(String param1, String param2) {
        NewEventDialog fragment = new NewEventDialog();
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
        return inflater.inflate(R.layout.fragment_new_event_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> dismiss());

        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.Theme_NovApp2_Slide);
        }
    }



}