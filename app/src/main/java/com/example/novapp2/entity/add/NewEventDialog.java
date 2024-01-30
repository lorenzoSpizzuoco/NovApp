package com.example.novapp2.entity.add;

import static com.example.novapp2.utils.Utils.bitmapToBase64;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
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


public class NewEventDialog extends DialogFragment {

    private static final String TAG = NewEventDialog.class.getSimpleName();
    private Toolbar toolbar;
    private DatePicker eventDatePicker;

    private TextInputLayout eventDateText;

    private ImageView eventImageView;

    private Bitmap eventPhoto = null;

    private TextInputEditText eventDateTextInner;

    private MaterialButton photoButton;

    private FloatingActionButton delPhoto;

    private TextView saveEvent;

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
                        // image bitmap (don't know what to do with it tho)
                        eventPhoto = draw.getBitmap();
                        eventImageView.setImageBitmap(eventPhoto);
                        delPhoto.setVisibility(View.VISIBLE);
                        delPhoto.setColorFilter(ContextCompat.getColor(this.getContext(), android.R.color.white));
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
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        saveEvent = view.findViewById(R.id.save_button_event);
        eventPlaceInner = view.findViewById(R.id.event_place_inner);
        delPhoto = view.findViewById(R.id.fab_delete_photo);
        toolbar = view.findViewById(R.id.toolbar);
        eventDateText = view.findViewById(R.id.date_picker_input_text);
        eventDateTextInner = view.findViewById(R.id.date_input_text_inner);
        eventDescInner = view.findViewById(R.id.event_desc_inner);
        photoButton = view.findViewById(R.id.event_photo_button);
        eventImageView = view.findViewById(R.id.event_photo_view);
        eventTitle = view.findViewById(R.id.event_title_inner);
        eventDateTextInner.setInputType(InputType.TYPE_NULL);

        photoButton.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

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
            HomeFragment hf = new HomeFragment();
            Bundle b = new Bundle();
            b.putParcelable("post", new Post(
                    eventTitle.getText().toString(),
                    "author",
                    R.drawable.analisi,
                    bitmapToBase64(eventPhoto),
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




