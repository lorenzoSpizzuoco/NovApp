package com.example.novapp2.ui.login;

import static com.example.novapp2.utils.Constants.USER_LOCAL_FILE;
import static com.example.novapp2.utils.Constants.USER_LOCAL_MAIL;
import static com.example.novapp2.utils.Constants.USER_LOCAL_PASSWORD;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

// TODO Add loading
public class LoginFragment extends Fragment {

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private FirebaseAuth mAuth;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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

        Bundle savedCredentials = getArguments();
        if(null != savedCredentials){
            Boolean foundCredentials = true;
            signInImpl(savedCredentials.getString(USER_LOCAL_MAIL), savedCredentials.getString(USER_LOCAL_PASSWORD), foundCredentials);
        } else {


            Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake);

            buttonLogin.setOnClickListener(v -> {

                String email = Objects.requireNonNull(textInputLayoutEmail.getEditText()).getText().toString();
                String password = Objects.requireNonNull(textInputLayoutPassword.getEditText()).getText().toString();

                if (Utils.isValidEmail(email) || Utils.isValidPassword(password)) {
                    Snackbar.make(view, R.string.login_error, Snackbar.LENGTH_SHORT).show();

                    Utils.vibration(requireContext());
                    textInputLayoutEmail.startAnimation(animation);
                    textInputLayoutPassword.startAnimation(animation);

                } else {
                    signIn(email, password, new SignInCallback() {
                        @Override
                        public void onSignInSuccess() {
                            MainActivity.getNavController().navigate(R.id.action_login_to_home);
                        }

                        @Override
                        public void onSignInFailure() {
                            Snackbar.make(view, R.string.login_error, Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            toRegisterPage.setOnClickListener(v -> MainActivity.getNavController().navigate(R.id.action_login_to_register));
        }
    }

    private void signInImpl(String email, String password, Boolean foundCredentials) {
        // Start login if email and password are ok
        signIn(email, password, new SignInCallback() {
            @Override
            public void onSignInSuccess() {
                if (!foundCredentials){
                    saveUserCredentials(email, password);
                }
                MainActivity.getNavController().navigate(R.id.action_login_to_home);
            }

            @Override
            public void onSignInFailure() {
                Toast.makeText(requireContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface SignInCallback {
        void onSignInSuccess();
        void onSignInFailure();
    }

    public void signIn(String email, String password, SignInCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        callback.onSignInSuccess();
                    } else {
                        callback.onSignInFailure();
                    }
                });
    }

    public void saveUserCredentials(String email, String password) {
        Context context = requireContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_LOCAL_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LOCAL_MAIL, email);
        editor.putString(USER_LOCAL_PASSWORD, password);
        editor.apply();
    }

}
