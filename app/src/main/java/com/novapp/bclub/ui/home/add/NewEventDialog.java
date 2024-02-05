package com.novapp.bclub.ui.home.add;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.novapp.bclub.R;
import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.service.nativeapi.UserService;
import com.novapp.bclub.utils.Constants;
import com.novapp.bclub.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class NewEventDialog extends DialogFragment {

    private final UserService userService = new UserService();

    private ImageView eventImageView;

    private Bitmap eventPhoto = null;

    private TextInputEditText eventDateTextInner;

    private FloatingActionButton delPhoto;

    private TextInputEditText eventTitle;

    private TextInputEditText eventPlaceInner;

    private Uri imageUri;

    private TextInputEditText eventDescInner;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public NewEventDialog() {
        // Required empty public constructor
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
        Button saveEvent = view.findViewById(R.id.save_button_event);
        delPhoto = view.findViewById(R.id.fab_delete_photo);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        eventDateTextInner = view.findViewById(R.id.date_input_text_inner);
        MaterialButton photoButton = view.findViewById(R.id.event_photo_button);
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

        saveEvent.setOnClickListener(v -> checkModal());

        delPhoto.setOnClickListener(v -> {
            if (eventImageView.getDrawable() != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireParentFragment().requireActivity()).setTitle(R.string.event_photo)
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

        eventDateTextInner.setOnFocusChangeListener((v, sel) -> {
            if(v.getId() == R.id.date_input_text_inner  && sel) {

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now());

                MaterialDatePicker<Long> dp = MaterialDatePicker.Builder.datePicker()
                        .setTitleText(getString(R.string.event_data_pick))
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();

                dp.show(getChildFragmentManager(), getString(R.string.tag));

                dp.addOnPositiveButtonClickListener(selection -> {
                    String date = new SimpleDateFormat(Constants.DATA_PATTERN, Locale.getDefault()).format(new Date(selection));
                    eventDateTextInner.setText(date);
                });
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
            Objects.requireNonNull(dialog.getWindow()).setWindowAnimations(R.style.Theme_NovApp2_Slide);
        }
    }

    private void checkModal() {
        boolean valid = true;

        // checking event modal
        if (Objects.requireNonNull(eventDateTextInner.getText()).toString().compareTo("") == 0) {
            valid = false;
            eventDateTextInner.setError(ContextCompat.getString(requireContext(), R.string.date_error));
        }
        else {
           eventDateTextInner.setError(null);
        }

        if (Objects.requireNonNull(eventTitle.getText()).toString().compareTo("") == 0){
            valid = false;
            eventTitle.setError(ContextCompat.getString(requireContext(), R.string.title_error));
        }
        else {
            eventTitle.setError(null);
        }

        if(Objects.requireNonNull(eventPlaceInner.getText()).toString().compareTo("") == 0) {
            valid = false;
            eventPlaceInner.setError(ContextCompat.getString(requireContext(), R.string.place_error));
        }
        else {
            eventPlaceInner.setError(null);
        }

        if(Objects.requireNonNull(eventDescInner.getText()).toString().compareTo("") == 0) {
            valid = false;
            eventDescInner.setError(ContextCompat.getString(requireContext(), R.string.desc_error));
        }
        else {
            eventDescInner.setError(null);
        }

        if(eventPhoto == null) {
            valid = false;
            Snackbar.make(getView(), R.string.missing_image, Snackbar.LENGTH_SHORT).show();
        }

        if (valid) {
            Bundle b = new Bundle();
            b.putParcelable("post", new Post(
                    eventTitle.getText().toString(),
                    null,
                    userService.getCurrentUser().getEmail(),
                    R.mipmap.ic_launcher,
                    null,
                    eventDescInner.getText().toString(),
                    1,
                    eventDateTextInner.getText().toString(),
                    eventPlaceInner.getText().toString(),
                    0 ));

            b.putParcelable("image", imageUri);
            Navigation.findNavController(requireParentFragment().requireView()).navigate(R.id.action_navigation_add_to_loadingFragment, b);
        }

    }

}




