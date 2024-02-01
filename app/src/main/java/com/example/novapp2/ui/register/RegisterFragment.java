package com.example.novapp2.ui.register;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.compose.material3.SnackbarKt;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.service.UserService;
import com.example.novapp2.ui.login.LoginFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterFragment extends Fragment {

    final private String TAG = RegisterFragment.class.getSimpleName();
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutPasswordConfirm;
    private Button pwInfos;

    private FirebaseAuth mAuth;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        pwInfos = view.findViewById(R.id.button_pw_info);
        textInputLayoutEmail = view.findViewById(R.id.text_input_layout_email);
        textInputLayoutPassword = view.findViewById(R.id.text_input_layout_password);
        textInputLayoutPasswordConfirm = view.findViewById(R.id.text_input_layout_password_confirm);
        // TODO: implement password confirm

        final Button buttonRegister = view.findViewById(R.id.button_register);
        final Button buttonLogin = view.findViewById(R.id.button_login);
        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake);

        buttonLogin.setOnClickListener(v ->
                MainActivity.getNavController().navigate(R.id.action_register_to_login));

        pwInfos.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.password_dialog_title);
            builder.setMessage(R.string.password_infos);
            builder.setPositiveButton(R.string.okDialog, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        buttonRegister.setOnClickListener(v -> {

            String email = textInputLayoutEmail.getEditText().getText().toString();
            String password = textInputLayoutPassword.getEditText().getText().toString();
            String confirmPassword = textInputLayoutPasswordConfirm.getEditText().getText().toString();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Snackbar.make(view, R.string.empty_fields, Snackbar.LENGTH_SHORT).show();
            }
            else if (!isValidEmail(email)){
                Snackbar.make(view, R.string.wrong_email_format, Snackbar.LENGTH_SHORT).show();
                textInputLayoutEmail.startAnimation(animation);
            } else if (!isValidPassword(password)){
                Snackbar.make(view, R.string.wrong_pass_format, Snackbar.LENGTH_SHORT).show();
                textInputLayoutPassword.startAnimation(animation);
            } else if (!password.equals(confirmPassword)) {
                Snackbar.make(view, R.string.no_pw_match, Snackbar.LENGTH_SHORT).show();
                textInputLayoutPassword.startAnimation(animation);

            }else {
                registerUser(email, password, new RegisterCallback() {
                    @Override
                    public void onRegisterSuccess() {
                        // Handle success, for example, navigate to the next activity
                        if (mAuth.getCurrentUser() != null) {
                            UserService.createUser(mAuth.getCurrentUser().getUid(), email);
                            MainActivity.getNavController().navigate(R.id.action_register_to_home);
                        } else {
                            Toast.makeText(requireContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onRegisterFailure() {
                        // Handle failure, for example, show an error message
                        Toast.makeText(requireContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    callback.onRegisterSuccess();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure");
                    callback.onRegisterFailure();
                }
            });
    }

    // VALIDATION TODO: Duplicated code login and register
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

}