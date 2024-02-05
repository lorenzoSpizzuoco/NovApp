package com.novapp.bclub.ui.register;

import static com.novapp.bclub.utils.Constants.DB_USERS_IMAGES;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.novapp.bclub.MainActivity;
import com.novapp.bclub.R;
import com.novapp.bclub.entity.user.User;
import com.novapp.bclub.service.nativeapi.UserService;
import com.novapp.bclub.utils.Constants;
import com.novapp.bclub.utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Pattern;



public class FullRegisterFragment extends Fragment {


    private Uri imageUri = null;
    private Bitmap eventPhoto = null;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private FloatingActionButton delPhoto;
    private ImageView eventImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_register, container, false);
    }

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
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle safeInstanceState){
        super.onViewCreated(view, safeInstanceState);

        Bundle args = getArguments();
        final TextInputEditText inputName = view.findViewById(R.id.full_register_name_inner);
        final TextInputEditText inputSurname = view.findViewById(R.id.full_register_surname_inner);
        final TextInputEditText inputBio = view.findViewById(R.id.full_register_bio_inner);
        final Button finishButton = view.findViewById(R.id.full_register_end_flow_button);

        delPhoto = view.findViewById(R.id.fab_delete_photo);
        MaterialButton photoButton = view.findViewById(R.id.full_register_photo_button);
        eventImageView = view.findViewById(R.id.full_register_photo_view);

        InputFilter[] filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_SMALL_TEXT, requireView(), requireContext());
        inputName.setFilters(filters);
        inputSurname.setFilters(filters);
        filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_LONG_TEXT, requireView(), requireContext());
        inputBio.setFilters(filters);

        setUpDelPhoto(view, delPhoto, eventImageView);
        setUpFinishButton(args, inputName, inputSurname, inputBio, finishButton);
        setUpPhotoButton(photoButton);
    }

    private void setUpDelPhoto(@NonNull View view, FloatingActionButton delPhoto, ImageView eventImageView) {
        delPhoto.setOnClickListener(v -> {
            if (eventImageView.getDrawable() != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity()).setTitle(R.string.event_photo)
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
    }

    private void setUpPhotoButton(MaterialButton photoButton) {
        photoButton.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));
    }

    private void setUpFinishButton(Bundle args, TextInputEditText inputName, TextInputEditText inputSurname, TextInputEditText inputBio, Button finishButton) {
        finishButton.setOnClickListener(v -> {
            String name = Objects.requireNonNull(inputName.getText()).toString();
            String surname = Objects.requireNonNull(inputSurname.getText()).toString();
            String bio = Objects.requireNonNull(inputBio.getText()).toString();
            String userId = args.getString("userId");
            String email = args.getString("userEmail");

            if(!name.isEmpty() && !surname.isEmpty() && !bio.isEmpty()) {
                if (imageUri != null) {
                    Utils.uploadImage(imageUri, DB_USERS_IMAGES, userId).addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String url = downloadUri.toString();
                                    User updatedUser = new User(userId, name, email, surname, bio, null, null, isBicocca(email), url, null);
                                    updateUser(updatedUser);
                                }
                            }
                    );
                } else {
                    createAlert(name, surname, bio, userId, email);
                }
            } else {
                Snackbar.make(requireView(), getString(R.string.empty_fields), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void createAlert(String name, String surname, String bio, String userId, String email) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireParentFragment().requireActivity()).setTitle(R.string.event_photo)
                .setMessage(R.string.no_photo_reg)
                .setPositiveButton(R.string.ok_button, (di, i) -> {
                    User updatedUser = new User(userId, name, email, surname, bio, null, null, isBicocca(email), "", null);
                    updateUser(updatedUser);
                })
                .setNegativeButton(R.string.dialog_close, (di, i) -> {
                });

        builder.create();
        builder.show();
    }

    public static boolean isBicocca(String email) {
        String regex = Constants.BICOCCA_REGEX;
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private void updateUser(User updatedUser) {
        Task<Void> updateTask = UserService.updateUserById(updatedUser.getID(), updatedUser);
        updateTask.addOnCompleteListener(taskInner -> {
            if (taskInner.isSuccessful()) {
                MainActivity.getNavController().navigate(R.id.action_fullRegister_to_home);
            } else {
                Snackbar.make(requireView(), requireContext().getString(R.string.error_fullregister), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}

