package com.example.novapp2.service;

import static com.example.novapp2.utils.Constants.USER_LOCAL_FILE;
import static com.example.novapp2.utils.Constants.USER_LOCAL_MAIL;
import static com.example.novapp2.utils.Constants.USER_LOCAL_PASSWORD;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.novapp2.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthService {
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // SIGN IN
    public static void signIn(String email, String password, Activity activity, SignInCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        callback.onSignInSuccess();
                    } else {
                        callback.onSignInFailure();
                    }
                });
    }
    public interface SignInCallback {
        void onSignInSuccess();
        void onSignInFailure();
    }

    public static void saveUserCredentials(String email, String password, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_LOCAL_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LOCAL_MAIL, email);
        editor.putString(USER_LOCAL_PASSWORD, password);
        editor.apply();
    }

    public static Map<String, String> getUserCredentials(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_LOCAL_FILE, Context.MODE_PRIVATE);
        Map<String, String> credentials = new HashMap<>();
        credentials.put(USER_LOCAL_MAIL, sharedPreferences.getString(USER_LOCAL_MAIL, null)); // Ritorna null se "Email" non esiste
        credentials.put(USER_LOCAL_PASSWORD, sharedPreferences.getString(USER_LOCAL_PASSWORD, null)); // Ritorna null se "Password" non esiste

        return (credentials.get(USER_LOCAL_MAIL) != null && credentials.get(USER_LOCAL_PASSWORD) != null) ? credentials : null;
    }

    public static void deleteUserCredentials(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_LOCAL_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    // VALIDATION
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern = Pattern.compile(Constants.PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
