package com.example.novapp2.ui.register;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.CreateCredentialResponse;
import androidx.credentials.CreatePasswordRequest;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.exceptions.CreateCredentialException;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.novapp2.R;
import com.example.novapp2.ui.login.LoginFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    final private String TAG = RegisterFragment.class.getSimpleName();
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutPasswordConfirm;
    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);


    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        textInputLayoutEmail = view.findViewById(R.id.text_input_layout_email);
        textInputLayoutPassword = view.findViewById(R.id.text_input_layout_password);
        textInputLayoutPasswordConfirm = view.findViewById(R.id.text_input_layout_password_confirm);
        // TODO: implement password confirm

        final Button buttonRegister = view.findViewById(R.id.button_register);

        buttonRegister.setOnClickListener(v -> {

            String email = textInputLayoutEmail.getEditText().getText().toString();
            String password = textInputLayoutPassword.getEditText().getText().toString();


            registerUser(email, password, new RegisterCallback() {
                @Override
                public void onRegisterSuccess() {
                    // Handle success, for example, navigate to the next activity
                    Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_mainActivity2);
                }

                @Override
                public void onRegisterFailure() {
                    // Handle failure, for example, show an error message
                }
            });

            registerPassword(email, password);
        });

    }

    public interface RegisterCallback {
        void onRegisterSuccess();
        void onRegisterFailure();
    }

    void registerPassword(String username, String password) {
        // Initialize a CreatePasswordRequest object.
        CreatePasswordRequest createPasswordRequest =
                new CreatePasswordRequest(username, password);


        // Register the username and password.
        credentialManager.createCredentialAsync(
                // Use an activity-based context to avoid undefined
                // system UI launching behavior
                requireActivity(),
                createPasswordRequest,
                null,
                AsyncTask.THREAD_POOL_EXECUTOR,
                new CredentialManagerCallback<CreateCredentialResponse, CreateCredentialException>() {
                    @Override
                    public void onResult(CreateCredentialResponse result) {
                        Log.e(TAG, "Handle result");
                    }

                    @Override
                    public void onError(CreateCredentialException e) {
                        Log.e(TAG, "Handle failure");
                    }
                }
        );
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

}