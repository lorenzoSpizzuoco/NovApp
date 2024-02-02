package com.example.novapp2.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

// TODO Add loading
public class LoginFragment extends Fragment {

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

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

        buttonLogin.setOnClickListener(v -> {

            String email = Objects.requireNonNull(textInputLayoutEmail.getEditText()).getText().toString();
            String password = Objects.requireNonNull(textInputLayoutPassword.getEditText()).getText().toString();

            if (Utils.isValidEmail(email) && Utils.isValidPassword(password)) {
                Utils.signIn(email, password, requireActivity(), new Utils.SignInCallback() {
                    @Override
                    public void onSignInSuccess() {
                        Utils.saveUserCredentials(email, password, requireContext());
                        MainActivity.getNavController().navigate(R.id.action_login_to_home);
                    }

                    @Override
                    public void onSignInFailure() {
                        Toast.makeText(requireContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Snackbar.make(view, R.string.login_error, Snackbar.LENGTH_SHORT).show();
                Utils.vibration(requireContext());
                textInputLayoutEmail.startAnimation(animation);
                textInputLayoutPassword.startAnimation(animation);
            }
        });

        toRegisterPage.setOnClickListener(v -> MainActivity.getNavController().navigate(R.id.action_login_to_register));

    }



}
