package com.example.novapp2.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.entity.User;
import com.example.novapp2.service.UserService;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import java.util.regex.Pattern;



public class FullRegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_register, container, false);
    }

    // TODO image picker

    @Override
    public void onViewCreated(@NonNull View view, Bundle safeInstanceState){
        super.onViewCreated(view, safeInstanceState);

        Bundle args = getArguments();
        final TextInputLayout inputName = view.findViewById(R.id.full_register_name);
        final TextInputLayout inputSurname = view.findViewById(R.id.full_register_surname);
        final TextInputLayout inputBio = view.findViewById(R.id.full_register_bio);
        final Button finishButton = view.findViewById(R.id.full_register_end_flow_button);
        final Button imageButton = view.findViewById(R.id.full_register_image_button);
        // TODO empty strings
        // TODO image ??? don't worry, working on it
        finishButton.setOnClickListener(v -> {
            String name = inputName.getEditText().getText().toString();
            String surname = inputSurname.getEditText().getText().toString();
            String bio = inputBio.getEditText().getText().toString();
            String userId = args.getString("userId");
            String email = args.getString("userEmail");


            User updatedUser = new User(userId, name, email, surname, bio, null, null, isBicocca(email), "", null);
            UserService.updateUserById(userId, updatedUser);
            Task<Void> updateTask = UserService.updateUserById(userId, updatedUser);
            updateTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    MainActivity.getNavController().navigate(R.id.action_fullRegister_to_home);
                } else {
                    // TODO Handle the error
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public static boolean isBicocca(String email) {
        String regex = ".+@campus\\.unimib\\.it$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }
}

