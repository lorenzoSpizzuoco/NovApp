package com.example.novapp2.entity.add;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.text.InputType;
import android.util.Log;
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
import com.example.novapp2.ui.home.HomeFragmentDirections;
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


public class NewGsDialog extends DialogFragment {

    private static final String TAG = NewEventDialog.class.getSimpleName();
    private Toolbar toolbar;

    private TextInputEditText gsTitle;

    private TextInputEditText gsDesc;

    public ImageView eventImage;

    private TextInputLayout eventDateText;

    private Bitmap eventPhoto;

    private Uri imageUri;

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
                        //delPhoto.setColorFilter(ContextCompat.getColor(this.getContext(), android.R.color.black));

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
        return inflater.inflate(R.layout.fragment_new_gs_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        gsTitle = view.findViewById(R.id.new_gs_title_inner);
        gsDesc = view.findViewById(R.id.new_gs_desc_inner);
        eventImage = view.findViewById(R.id.gs_photo_view);
        saveEvent = view.findViewById(R.id.save_button_gs);
        delPhoto = view.findViewById(R.id.fab_delete_photo);
        toolbar = view.findViewById(R.id.toolbar_gs);
        photoButton = view.findViewById(R.id.gs_photo_button);


        toolbar.setNavigationOnClickListener(v -> dismiss());

        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });

        // launching pick media launcher
        photoButton.setOnClickListener(v -> {

            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        // submit modal
        saveEvent.setOnClickListener(v -> {
            checkModal();
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

        if (gsTitle.getText().toString().compareTo("") == 0){
            valid = false;
            gsTitle.setError(ContextCompat.getString(getContext(), R.string.title_error));
        }
        else{
            gsTitle.setError(null);
        }

        if(gsDesc.getText().toString().compareTo("") == 0) {
            valid = false;
            gsDesc.setError(ContextCompat.getString(getContext(), R.string.desc_error));
        }
        else{
            gsDesc.setError(null);
        }

        if (valid) {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date = dateFormat.format(currentDate);

            Bundle b = new Bundle();
            b.putParcelable("post", new Post(
                    gsTitle.getText().toString(),
                    null,
                    HomeFragment.getActiveUser().getEmail(),
                    R.drawable.analisi,
                    null,
                    gsDesc.getText().toString(),
                    4,
                    date,
                    "",
                    0 ));

            b.putParcelable("image", imageUri);
            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_navigation_add_to_loadingFragment, b);
        }

    }

}