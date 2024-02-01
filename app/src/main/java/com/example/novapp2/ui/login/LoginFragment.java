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
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO Add loading
public class LoginFragment extends Fragment {

    final private String TAG = LoginFragment.class.getSimpleName();
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private Boolean foundCredentials = false;

    // AUTH
    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
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
            foundCredentials = true;
            //TODO se l'auth non va a buon fine deve rimandare sulla login.
            signInImpl(savedCredentials.getString(USER_LOCAL_MAIL), savedCredentials.getString(USER_LOCAL_PASSWORD), foundCredentials);
        } else {


            Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake);

            buttonLogin.setOnClickListener(v -> {

                String email = textInputLayoutEmail.getEditText().getText().toString();
                String password = textInputLayoutPassword.getEditText().getText().toString();

                if (!isValidEmail(email) || !isValidPassword(password)) {
                    Snackbar.make(view, R.string.login_error, Snackbar.LENGTH_SHORT).show();
                    textInputLayoutEmail.startAnimation(animation);
                } else {
                    signInImpl(email, password, foundCredentials);
                }
            });

            toRegisterPage.setOnClickListener(v -> {
                Log.i(TAG, "Clicked");
                MainActivity.getNavController().navigate(R.id.action_login_to_register);
            });
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
                // Handle success, for example, navigate to the next activity
                MainActivity.getNavController().navigate(R.id.action_login_to_home);
            }

            @Override
            public void onSignInFailure() {
                // Handle failure, for example, show an error message
                Toast.makeText(requireContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // VALIDATION
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }


    // SIGN IN PROCESS using AUTH - FIREBASE//
    // Use of callbacks because mAuth is async
    public interface SignInCallback {
        void onSignInSuccess();
        void onSignInFailure();
    }

    public void signIn(String email, String password, SignInCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onSignInSuccess();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure");
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
        editor.apply(); // Salva in modo asincrono
    }



}
