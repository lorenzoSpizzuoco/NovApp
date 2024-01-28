package com.example.novapp2.ui.add;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.ui.graphics.ImageBitmap;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.novapp2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NewGsDialog extends DialogFragment {

    private static final String TAG = NewEventDialog.class.getSimpleName();
    private Toolbar toolbar;

    private TextInputEditText gsTitle;

    private TextInputEditText gsPlace;

    private TextInputEditText gsDesc;

    private DatePicker eventDatePicker;

    private TextInputLayout eventDateText;
    private TextInputEditText eventDateTextInner;

    private MaterialButton photoButton;

    private FloatingActionButton delPhoto;

    private TextView saveEvent;


    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public NewGsDialog() {
        // Required empty public constructor
    }

    public static NewGsDialog newInstance(String param1, String param2) {
        NewGsDialog fragment = new NewGsDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_NovApp2_Slide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_gs_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        gsTitle = view.findViewById(R.id.new_gs_title_inner);
        gsDesc = view.findViewById(R.id.new_gs_desc_inner);
        gsPlace = view.findViewById(R.id.new_gs_place_inner);
        saveEvent = view.findViewById(R.id.save_button_gs);
        delPhoto = view.findViewById(R.id.fab_delete_photo);
        toolbar = view.findViewById(R.id.toolbar_gs);
        eventDateText = view.findViewById(R.id.date_picker_input_text_gs);
        eventDateTextInner = view.findViewById(R.id.date_input_text_inner_gs);

        eventDateTextInner.setInputType(InputType.TYPE_NULL);

        eventDateTextInner.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean sel) {
                if(v.getId() == R.id.date_input_text_inner_gs  && sel) {
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

        saveEvent.setOnClickListener(v -> {
            checkModal();
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

    private void checkModal() {
        boolean valid = true;

        // checking event modal
        if (eventDateTextInner.getText().toString().compareTo("") == 0) {
            valid = false;
            eventDateTextInner.setError(ContextCompat.getString(getContext(), R.string.date_error));
        }
        else{
            eventDateTextInner.setError(null);
        }

        if (gsTitle.getText().toString().compareTo("") == 0){
            valid = false;
            gsTitle.setError(ContextCompat.getString(getContext(), R.string.title_error));
        }
        else{
            gsTitle.setError(null);
        }

        if(gsPlace.getText().toString().compareTo("") == 0) {
            valid = false;
            gsPlace.setError(ContextCompat.getString(getContext(), R.string.place_error));
        }
        else{
            gsPlace.setError(null);
        }

        if(gsDesc.getText().toString().compareTo("") == 0) {
            valid = false;
            gsDesc.setError(ContextCompat.getString(getContext(), R.string.desc_error));
        }
        else{
            gsDesc.setError(null);
        }

        if (valid) {
            // insert event
        }

    }

}




