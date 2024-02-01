package com.example.novapp2.utils;

import static com.example.novapp2.utils.Constants.USER_LOCAL_FILE;
import static com.example.novapp2.utils.Constants.USER_LOCAL_MAIL;
import static com.example.novapp2.utils.Constants.USER_LOCAL_PASSWORD;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.novapp2.entity.post.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;


    public static void sortCourseByName(List<Post> courseList){
        courseList.sort(Comparator.comparing(Post::getTitle));
    }

    /* TODO remove this
    public static List<Post> sortCourseByString(List<Post> courseList, String newText) {
        List<Post> filteredList = courseList.stream()
                .filter(course -> course.getTitle().toUpperCase().contains(newText.toUpperCase()))
                .collect(Collectors.toList());
        return filteredList;
    }*/

    public static boolean checkResponse(String body) {
        Gson gson = new Gson();
        ProfanityResponse resp = gson.fromJson(body, ProfanityResponse.class);
        double profanity = resp.getAttributeScores().getProfanityScore().getSummaryScore().getValue();
        double toxicity = resp.getAttributeScores().getToxicityScore().getSummaryScore().getValue();

        return !(toxicity > 0.6) && !(profanity > 0.6);
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

    public static void vibration(Context context) {
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.cancel();
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
        }
    }

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


}
