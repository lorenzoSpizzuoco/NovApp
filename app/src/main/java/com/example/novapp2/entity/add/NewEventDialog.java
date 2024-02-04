package com.example.novapp2.entity.add;



import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import com.example.novapp2.R;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.service.UserService;
import com.example.novapp2.utils.Constants;
import com.example.novapp2.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
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


public class NewEventDialog extends DialogFragment {

    private static final String TAG = NewEventDialog.class.getSimpleName();

    private final UserService userService = new UserService();

    private Toolbar toolbar;
    private DatePicker eventDatePicker;

    private TextInputLayout eventDateText;

    private ImageView eventImageView;

    private Bitmap eventPhoto = null;

    private TextInputEditText eventDateTextInner;

    private MaterialButton photoButton;

    private FloatingActionButton delPhoto;

    private Button saveEvent;

    private TextInputEditText eventTitle;

    private TextInputEditText eventPlaceInner;

    private Uri imageUri;

    private TextInputEditText eventDescInner;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

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
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        ImageView ev = new ImageView(getContext());
                        imageUri = uri;
                        ev.setImageURI(uri);
                        BitmapDrawable draw = (BitmapDrawable) ev.getDrawable();
                        eventPhoto = draw.getBitmap();
                        eventImageView.setImageBitmap(eventPhoto);
                        delPhoto.setVisibility(View.VISIBLE);
                    }
                });

       setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_NovApp2_Slide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_event_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        eventTitle = view.findViewById(R.id.event_title_inner);
        eventPlaceInner = view.findViewById(R.id.event_place_inner);
        eventDescInner = view.findViewById(R.id.event_desc_inner);
        saveEvent = view.findViewById(R.id.save_button_event);
        delPhoto = view.findViewById(R.id.fab_delete_photo);
        toolbar = view.findViewById(R.id.toolbar);
        eventDateText = view.findViewById(R.id.date_picker_input_text);
        eventDateTextInner = view.findViewById(R.id.date_input_text_inner);
        photoButton = view.findViewById(R.id.event_photo_button);
        eventImageView = view.findViewById(R.id.event_photo_view);
        eventDateTextInner.setInputType(InputType.TYPE_NULL);

        InputFilter[] filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_SMALL_TEXT, requireView(), requireContext());
        eventTitle.setFilters(filters);
        eventPlaceInner.setFilters(filters);
        filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_LONG_TEXT, requireView(), requireContext());
        eventDescInner.setFilters(filters);

        photoButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        saveEvent.setOnClickListener(v -> {
            checkModal(view);
        });

        delPhoto.setOnClickListener(v -> {
            if (eventImageView.getDrawable() != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getParentFragment().getActivity()).setTitle(R.string.event_photo)
                        .setMessage(R.string.photo_delete)
                        .setPositiveButton(R.string.dialog_ok_event_photo_delete, (di, i) -> {
                            eventImageView.setImageBitmap(null);
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
                if(v.getId() == R.id.date_input_text_inner  && sel) {

                    CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                            .setValidator(DateValidatorPointForward.now());

                    MaterialDatePicker<Long> dp = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Seleziona la data dell'evento")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .setCalendarConstraints(constraintsBuilder.build())
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
            dialog.getWindow().setWindowAnimations(R.style.Theme_NovApp2_Slide);
        }
    }

    private void checkModal(View view) {
        boolean valid = true;

        // checking event modal
        if (eventDateTextInner.getText().toString().compareTo("") == 0) {
            valid = false;
            eventDateTextInner.setError(ContextCompat.getString(getContext(), R.string.date_error));
        }
        else {
           eventDateTextInner.setError(null);
        }

        if (eventTitle.getText().toString().compareTo("") == 0){
            valid = false;
            eventTitle.setError(ContextCompat.getString(getContext(), R.string.title_error));
        }
        else {
            eventTitle.setError(null);
        }

        if(eventPlaceInner.getText().toString().compareTo("") == 0) {
            valid = false;
            eventPlaceInner.setError(ContextCompat.getString(getContext(), R.string.place_error));
        }
        else {
            eventPlaceInner.setError(null);
        }

        if(eventDescInner.getText().toString().compareTo("") == 0) {
            valid = false;
            eventDescInner.setError(ContextCompat.getString(getContext(), R.string.desc_error));
        }
        else {
            eventDescInner.setError(null);
        }

        if(eventPhoto == null) {
            valid = false;
        }

        if (valid) {
            Bundle b = new Bundle();
            b.putParcelable("post", new Post(
                    eventTitle.getText().toString(),
                    null,
                    userService.getCurrentUser().getEmail(),
                    R.drawable.analisi,
                    null,
                    eventDescInner.getText().toString(),
                    1,
                    eventDateTextInner.getText().toString(),
                    eventPlaceInner.getText().toString(),
                    0 ));

            b.putParcelable("image", imageUri);
             Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_navigation_add_to_loadingFragment, b);
        }

    }

}




