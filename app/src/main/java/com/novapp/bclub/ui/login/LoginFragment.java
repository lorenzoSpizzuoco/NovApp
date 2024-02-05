package com.novapp.bclub.ui.login;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


import com.novapp.bclub.MainActivity;
import com.novapp.bclub.R;
import com.novapp.bclub.service.nativeapi.AuthService;
import com.novapp.bclub.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

// TODO Add loading
public class LoginFragment extends Fragment {

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        textInputLayoutEmail = view.findViewById(R.id.text_input_layout_email);
        textInputLayoutPassword = view.findViewById(R.id.text_input_layout_password);

        final Button buttonLogin = view.findViewById(R.id.button_login);
        final Button toRegisterPage = view.findViewById(R.id.button_register);

        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake);

        setUpLoginButton(view, buttonLogin, animation);
        toRegisterPage.setOnClickListener(v -> MainActivity.getNavController().navigate(R.id.action_login_to_register));
        disableBackButton();

    }

    private void disableBackButton() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Disable back button
            }
        });
    }

    private void setUpLoginButton(@NonNull View view, Button buttonLogin, Animation animation) {
        buttonLogin.setOnClickListener(v -> {

            String email = Objects.requireNonNull(textInputLayoutEmail.getEditText()).getText().toString();
            String password = Objects.requireNonNull(textInputLayoutPassword.getEditText()).getText().toString();

            if (AuthService.isValidEmail(email) && AuthService.isValidPassword(password)) {
                AuthService.signIn(email, password, requireActivity(), new AuthService.SignInCallback() {
                    @Override
                    public void onSignInSuccess() {
                        AuthService.saveUserCredentials(email, password, requireContext());
                        MainActivity.getNavController().navigate(R.id.action_login_to_home);
                    }

                    @Override
                    public void onSignInFailure() {
                        Snackbar.make(view, R.string.error, Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                Snackbar.make(view, R.string.login_error, Snackbar.LENGTH_SHORT).show();
                Utils.vibration(requireContext());
                textInputLayoutEmail.startAnimation(animation);
                textInputLayoutPassword.startAnimation(animation);
            }
        });
    }


}
