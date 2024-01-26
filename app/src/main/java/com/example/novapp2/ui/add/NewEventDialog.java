package com.example.novapp2.ui.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.appcompat.widget.Toolbar;

import com.example.novapp2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NewEventDialog extends DialogFragment {

    private static final String TAG = NewEventDialog.class.getSimpleName();
    private Toolbar toolbar;
    private DatePicker eventDatePicker;

    private TextInputLayout eventDateText;

    private TextInputEditText eventDateTextInner;

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
        eventDateText = view.findViewById(R.id.date_picker_input_text);
        eventDateTextInner = view.findViewById(R.id.date_input_text_inner);
        eventDateTextInner.setInputType(InputType.TYPE_NULL);
        eventDateTextInner.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean sel) {
                Log.d("tagtag", "faccio qualcosa nella mia vita");
                if(v.getId() == R.id.date_input_text_inner  && sel) {
                    MaterialDatePicker<Long> dp = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Seleziona la data dell'evento")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build();

                    dp.show(getChildFragmentManager(), "TAG");

                    dp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @Override
                        public void onPositiveButtonClick(Long selection) {
                            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(selection));
                            eventDateTextInner.setText(date);
                        }
                    });
                }
            }
        });







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