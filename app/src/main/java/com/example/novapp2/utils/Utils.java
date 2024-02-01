package com.example.novapp2.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.novapp2.entity.post.Post;
import com.google.gson.Gson;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


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
        return (TextUtils.isEmpty(target) || !Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern = Pattern.compile(Constants.PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    public static void vibration(Context context) {
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.cancel();
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
        }
    }


}
