package com.novapp.bclub.ui.home.add;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
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
import java.util.Objects;


public class NewGsDialog extends DialogFragment {

    private final UserService userService = new UserService();

    private TextInputEditText gsTitle;

    private TextInputEditText gsDesc;

    public ImageView eventImage;

    private Bitmap eventPhoto;

    private Uri imageUri;

    private FloatingActionButton delPhoto;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public NewGsDialog() {
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        gsTitle = view.findViewById(R.id.new_gs_title_inner);
        gsDesc = view.findViewById(R.id.new_gs_desc_inner);
        eventImage = view.findViewById(R.id.gs_photo_view);
        TextView saveEvent = view.findViewById(R.id.save_button_gs);
        delPhoto = view.findViewById(R.id.fab_delete_photo);
        Toolbar toolbar = view.findViewById(R.id.toolbar_gs);
        MaterialButton photoButton = view.findViewById(R.id.gs_photo_button);

        InputFilter[] filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_SMALL_TEXT, requireView(), requireContext());
        gsTitle.setFilters(filters);
        filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_LONG_TEXT, requireView(), requireContext());
        gsDesc.setFilters(filters);


        toolbar.setNavigationOnClickListener(v -> dismiss());

        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });

        // launching pick media launcher
        photoButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        // submit modal
        saveEvent.setOnClickListener(v -> checkModal());

        delPhoto.setOnClickListener(v -> {
            if (eventImage.getDrawable() != null) {
                assert getParentFragment() != null;
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getParentFragment().requireActivity()).setTitle(R.string.event_photo)
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
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.Theme_NovApp2_Slide);
        }
    }

    private void checkModal() {
        boolean valid = true;

        if (Objects.requireNonNull(gsTitle.getText()).toString().compareTo("") == 0){
            valid = false;
            gsTitle.setError(ContextCompat.getString(requireContext(), R.string.title_error));
        }
        else{
            gsTitle.setError(null);
        }

        if(Objects.requireNonNull(gsDesc.getText()).toString().compareTo("") == 0) {
            valid = false;
            gsDesc.setError(ContextCompat.getString(requireContext(), R.string.desc_error));
        }
        else{
            gsDesc.setError(null);
        }

        if (valid) {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATA_PATTERN);
            String date = dateFormat.format(currentDate);

            Bundle b = new Bundle();
            b.putParcelable("post", new Post(
                    gsTitle.getText().toString(),
                    null,
                    userService.getCurrentUser().getEmail(),
                    R.drawable.analisi,
                    null,
                    gsDesc.getText().toString(),
                    4,
                    date,
                    "",
                    0 ));

            b.putParcelable("image", imageUri);
            Navigation.findNavController(requireParentFragment().requireView()).navigate(R.id.action_navigation_add_to_loadingFragment, b);
        }

    }

}