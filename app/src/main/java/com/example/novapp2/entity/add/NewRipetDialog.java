package com.example.novapp2.entity.add;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.novapp2.R;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.ui.home.HomeFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NewRipetDialog extends DialogFragment {

    private static final String TAG = NewEventDialog.class.getSimpleName();
    private Toolbar toolbar;
    private DatePicker eventDatePicker;

    private TextInputLayout eventDateText;

    private ImageView eventImage;

    private Bitmap eventPhoto;

    private TextInputEditText eventDateTextInner;

    private MaterialButton photoButton;

    private FloatingActionButton delPhoto;

    private TextView saveEvent;

    private Uri imageUri;

    private TextInputEditText ripetTitle;

    private TextInputEditText ripetDesc;

    private TextInputEditText ripetPlace;


    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public NewRipetDialog() {
        // Required empty public constructor
    }



    public static NewRipetDialog newInstance(String param1, String param2) {
        NewRipetDialog fragment = new NewRipetDialog();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        imageUri = uri;
                        ImageView ev = new ImageView(getContext());
                        ev.setImageURI(uri);
                        BitmapDrawable draw = (BitmapDrawable) ev.getDrawable();

                        eventPhoto = draw.getBitmap();
                        eventImage.setImageBitmap(eventPhoto);
                        delPhoto.setVisibility(View.VISIBLE);
                        delPhoto.setColorFilter(ContextCompat.getColor(this.getContext(), android.R.color.white));

                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_NovApp2_Slide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_ripet_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ripetPlace = view.findViewById(R.id.new_ripet_place_inner);
        ripetDesc = view.findViewById(R.id.new_ripet_desc_inner);
        ripetTitle = view.findViewById(R.id.new_ripet_title_inner);
        saveEvent = view.findViewById(R.id.save_button_ripet);
        delPhoto = view.findViewById(R.id.fab_delete_photo);
        toolbar = view.findViewById(R.id.toolbar_ripet);
        eventDateText = view.findViewById(R.id.date_picker_input_text_ripet);
        eventDateTextInner = view.findViewById(R.id.date_input_text_inner_ripet);
        photoButton = view.findViewById(R.id.ripet_photo_button);
        eventImage = view.findViewById(R.id.ripet_photo_view);
        eventDateTextInner.setInputType(InputType.TYPE_NULL);

        photoButton.setOnClickListener(v -> {

            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        delPhoto.setOnClickListener(v -> {
            if (eventImage.getDrawable() != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getParentFragment().getActivity()).setTitle(R.string.event_photo)
                        .setMessage(R.string.photo_delete)
                        .setPositiveButton(R.string.dialog_ok_event_photo_delete, (di, i) -> {
                            eventImage.setImageBitmap(null);
                            eventPhoto = null;
                            delPhoto.setVisibility(View.GONE);
                            Snackbar.make(view, R.string.image_delete_snackbar, Snackbar.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(R.string.dialog_close, (di, i) -> {

                        });

                builder.create();
                builder.show();
            }
        });

        eventDateTextInner.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean sel) {

                if(v.getId() == R.id.date_input_text_inner_ripet  && sel) {
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
        else {
            eventDateTextInner.setError(null);
        }

        if (ripetTitle.getText().toString().compareTo("") == 0){
            valid = false;
            ripetTitle.setError(ContextCompat.getString(getContext(), R.string.title_error));
        }
        else {
            ripetTitle.setError(null);
        }

        if(ripetPlace.getText().toString().compareTo("") == 0) {
            valid = false;
            ripetPlace.setError(ContextCompat.getString(getContext(), R.string.place_error));
        }
        else {
            ripetPlace.setError(null);
        }


        if(ripetDesc.getText().toString().compareTo("") == 0) {
            valid = false;
            ripetDesc.setError(ContextCompat.getString(getContext(), R.string.desc_error));
        }
        else {
            ripetDesc.setError(null);
        }

        if(eventPhoto == null) {
            valid = false;
        }

        if (valid) {
            HomeFragment hf = new HomeFragment();
            Bundle b = new Bundle();
            b.putParcelable("post", new Post(
                    ripetTitle.getText().toString(),
                    "user",
                    R.drawable.analisi,
                    null,
                    ripetDesc.getText().toString(),
                    3,
                    eventDateTextInner.getText().toString(),
                    ripetPlace.getText().toString(),
                    0 ));

            b.putParcelable("image", imageUri);
            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_navigation_add_to_loadingFragment, b);
        }

    }

}




