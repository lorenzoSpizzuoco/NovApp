package com.example.novapp2.ui.register;

import static com.example.novapp2.utils.Constants.DB_USERS_IMAGES;
import static com.example.novapp2.utils.Constants.POST_DATABASE_NAME;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.entity.User;
import com.example.novapp2.service.UserService;
import com.example.novapp2.utils.UploadImage;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import java.util.regex.Pattern;



public class FullRegisterFragment extends Fragment {


    private Uri imageUri = null;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_register, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        imageUri = uri;
                    }
                    else {
                        Snackbar.make(getView(), R.string.image_error, Snackbar.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle safeInstanceState){
        super.onViewCreated(view, safeInstanceState);

        Bundle args = getArguments();
        final TextInputLayout inputName = view.findViewById(R.id.full_register_name);
        final TextInputLayout inputSurname = view.findViewById(R.id.full_register_surname);
        final TextInputLayout inputBio = view.findViewById(R.id.full_register_bio);
        final Button finishButton = view.findViewById(R.id.full_register_end_flow_button);
        final Button imageButton = view.findViewById(R.id.full_register_image_button);

        imageButton.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        finishButton.setOnClickListener(v -> {
            String name = inputName.getEditText().getText().toString();
            String surname = inputSurname.getEditText().getText().toString();
            String bio = inputBio.getEditText().getText().toString();
            String userId = args.getString("userId");
            String email = args.getString("userEmail");

            if (imageUri != null) {
                UploadImage.uploadImage(imageUri, DB_USERS_IMAGES, userId).addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String url = downloadUri.toString();
                                User updatedUser = new User(userId, name, email, surname, bio, null, null, isBicocca(email), url, null);
                                udpateUser(updatedUser);
                            }
                        }
                );
            }
            else {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getParentFragment().getActivity()).setTitle(R.string.event_photo)
                        .setMessage(R.string.no_photo_reg)
                        .setPositiveButton(R.string.ok_button, (di, i) -> {
                            User updatedUser = new User(userId, name, email, surname, bio, null, null, isBicocca(email), "", null);
                            udpateUser(updatedUser);
                        })
                        .setNegativeButton(R.string.dialog_close, (di, i) -> {
                        });

                builder.create();
                builder.show();

            }
        });
    }

    public static boolean isBicocca(String email) {
        String regex = ".+@campus\\.unimib\\.it$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private void udpateUser(User updatedUser) {
        Task<Void> updateTask = UserService.updateUserById(updatedUser.getID(), updatedUser);
        updateTask.addOnCompleteListener(taskInner -> {
            if (taskInner.isSuccessful()) {
                MainActivity.getNavController().navigate(R.id.action_fullRegister_to_home);
            } else {
                // TODO Handle the error
                Snackbar.make(getView(), "error", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}

