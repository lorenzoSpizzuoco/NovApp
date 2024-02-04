package com.example.novapp2.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.service.nativeapi.UserService;
import com.example.novapp2.service.nativeapi.AuthService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class RegisterFragment extends Fragment {

    final private String TAG = RegisterFragment.class.getSimpleName();
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutPasswordConfirm;

    private FirebaseAuth mAuth;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        Button pwInfo = view.findViewById(R.id.button_pw_info);
        textInputLayoutEmail = view.findViewById(R.id.text_input_layout_email);
        textInputLayoutPassword = view.findViewById(R.id.text_input_layout_password);
        textInputLayoutPasswordConfirm = view.findViewById(R.id.text_input_layout_password_confirm);

        final Button buttonRegister = view.findViewById(R.id.button_register);
        final Button buttonLogin = view.findViewById(R.id.button_login);

        setUpButtonLogin(buttonLogin);
        setUpPwInfo(pwInfo);
        setUpButtonRegister(view, buttonRegister);
    }

    private void setUpButtonRegister(@NonNull View view, Button buttonRegister) {
        buttonRegister.setOnClickListener(v -> {

            String email = Objects.requireNonNull(textInputLayoutEmail.getEditText()).getText().toString();
            String password = Objects.requireNonNull(textInputLayoutPassword.getEditText()).getText().toString();
            String confirmPassword = Objects.requireNonNull(textInputLayoutPasswordConfirm.getEditText()).getText().toString();

            if (isValidData(email, password, confirmPassword, view)) {
                registerUser(email, password, new RegisterCallback() {
                    @Override
                    public void onRegisterSuccess() {
                        if (mAuth.getCurrentUser() != null) {
                            UserService.createUser(mAuth.getCurrentUser().getUid(), email);
                            AuthService.saveUserCredentials(email, password, requireContext());
                            MainActivity.getNavController().navigate(R.id.action_register_to_home);
                        } else {
                            Snackbar.make(view, R.string.error, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onRegisterFailure() {
                        Snackbar.make(view, R.string.error, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private static void setUpButtonLogin(Button buttonLogin) {
        buttonLogin.setOnClickListener(v ->
                MainActivity.getNavController().navigate(R.id.action_register_to_login));
    }

    private void setUpPwInfo(Button pwInfo) {
        pwInfo.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
            builder.setTitle(R.string.password_dialog_title);
            builder.setMessage(R.string.password_infos);
            builder.setPositiveButton(R.string.okDialog, (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public boolean isValidData(String email, String password, String confirmPassword, View view) {
        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake);
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Snackbar.make(view, R.string.empty_fields, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if (!AuthService.isValidEmail(email)){
            Snackbar.make(view, R.string.wrong_email_format, Snackbar.LENGTH_SHORT).show();
            textInputLayoutEmail.startAnimation(animation);
            return false;
        } else if (!AuthService.isValidPassword(password)){
            Snackbar.make(view, R.string.wrong_pass_format, Snackbar.LENGTH_SHORT).show();
            textInputLayoutPassword.startAnimation(animation);
            return false;
        } else if (!password.equals(confirmPassword)) {
            Snackbar.make(view, R.string.no_pw_match, Snackbar.LENGTH_SHORT).show();
            textInputLayoutPassword.startAnimation(animation);
            return false;
        }
        else return true;
    }


    public interface RegisterCallback {
        void onRegisterSuccess();
        void onRegisterFailure();
    }


    // TODO: waiting screen, register requires time //
    public void registerUser(String email, String password, RegisterCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    callback.onRegisterSuccess();
                } else {
                    callback.onRegisterFailure();
                }
            });
    }
}