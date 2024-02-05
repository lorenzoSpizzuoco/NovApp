package com.novapp.bclub.ui.home.add;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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


public class NewRipetDialog extends DialogFragment {


    private static final String TAG = NewRipetDialog.class.getSimpleName();

    private ImageView eventImage;

    private final UserService userService = new UserService();

    private Bitmap eventPhoto;

    private TextInputEditText eventDateTextInner;

    private FloatingActionButton delPhoto;

    private Uri imageUri;

    private TextInputEditText ripetTitle;

    private TextInputEditText ripetDesc;

    private TextInputEditText ripetPlace;


    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public NewRipetDialog() {
        // Required empty public constructor
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ripetPlace = view.findViewById(R.id.new_ripet_place_inner);
        ripetDesc = view.findViewById(R.id.new_ripet_desc_inner);
        ripetTitle = view.findViewById(R.id.new_ripet_title_inner);
        Button saveEvent = view.findViewById(R.id.save_button_ripet);
        delPhoto = view.findViewById(R.id.fab_delete_photo);
        Toolbar toolbar = view.findViewById(R.id.toolbar_ripet);
        eventDateTextInner = view.findViewById(R.id.date_input_text_inner_ripet);
        MaterialButton photoButton = view.findViewById(R.id.ripet_photo_button);
        eventImage = view.findViewById(R.id.ripet_photo_view);
        eventDateTextInner.setInputType(InputType.TYPE_NULL);

        InputFilter[] filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_SMALL_TEXT, requireView(), requireContext());
        ripetTitle.setFilters(filters);
        ripetPlace.setFilters(filters);
        filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_LONG_TEXT, requireView(), requireContext());
        ripetDesc.setFilters(filters);


        photoButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));



        delPhoto.setOnClickListener(v -> {
            if (eventImage.getDrawable() != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireParentFragment().requireActivity()).setTitle(R.string.ripet_photo_del)
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

        eventDateTextInner.setOnFocusChangeListener((v, sel) -> {

            if(v.getId() == R.id.date_input_text_inner_ripet  && sel) {

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now());

                MaterialDatePicker<Long> dp = MaterialDatePicker.Builder.datePicker()
                        .setTitleText(getString(R.string.event_data_pick))
                        .setCalendarConstraints(constraintsBuilder.build())
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
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

        saveEvent.setOnClickListener(v -> checkModal());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.Theme_NovApp2_Slide);
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

        if (Objects.requireNonNull(ripetTitle.getText()).toString().compareTo("") == 0){
            valid = false;
            ripetTitle.setError(ContextCompat.getString(requireContext(), R.string.title_error));
        }
        else {
            ripetTitle.setError(null);
        }

        if(Objects.requireNonNull(ripetPlace.getText()).toString().compareTo("") == 0) {
            valid = false;
            ripetPlace.setError(ContextCompat.getString(requireContext(), R.string.place_error));
        }
        else {
            ripetPlace.setError(null);
        }


        if(Objects.requireNonNull(ripetDesc.getText()).toString().compareTo("") == 0) {
            valid = false;
            ripetDesc.setError(ContextCompat.getString(requireContext(), R.string.desc_error));
        }
        else {
            ripetDesc.setError(null);
        }

        if(eventPhoto == null) {
            valid = false;
            Snackbar.make(getView(), R.string.missing_image, Snackbar.LENGTH_SHORT).show();
        }

        if (valid) {
            Bundle b = new Bundle();
            b.putParcelable("post", new Post(
                    ripetTitle.getText().toString(),
                    null,
                    userService.getCurrentUser().getEmail(),
                    R.mipmap.ic_launcher,
                    null,
                    ripetDesc.getText().toString(),
                    3,
                    eventDateTextInner.getText().toString(),
                    ripetPlace.getText().toString(),
                    0 ));

            b.putParcelable("image", imageUri);
            Log.d(TAG, String.valueOf(imageUri == null));
            Navigation.findNavController(requireParentFragment().requireView()).navigate(R.id.action_navigation_add_to_loadingFragment, b);
        }

    }

}




