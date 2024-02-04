package com.example.novapp2.utils;

import static com.example.novapp2.utils.Constants.DB_EVENTS;
import static com.example.novapp2.utils.Constants.DB_GS;
import static com.example.novapp2.utils.Constants.DB_INFOS;
import static com.example.novapp2.utils.Constants.DB_RIPET;
import static com.example.novapp2.utils.Constants.USER_LOCAL_FILE;
import static com.example.novapp2.utils.Constants.USER_LOCAL_MAIL;
import static com.example.novapp2.utils.Constants.USER_LOCAL_PASSWORD;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.novapp2.R;
import com.example.novapp2.entity.post.Post;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void sortCourseByName(List<Post> courseList){
        courseList.sort(Comparator.comparing(Post::getTitle));
    }

    // POST CATEGORY

    public static String getChildCategory(int category) {
        switch (category) {
            case 1:
                return DB_EVENTS;
            case 2:
                return DB_INFOS;
            case 3:
                return DB_RIPET;
            case 4:
                return DB_GS;
            default:
                return DB_INFOS;
        }
    }

    public static boolean checkResponse(String body) {
        Gson gson = new Gson();
        ProfanityResponse resp = gson.fromJson(body, ProfanityResponse.class);
        double profanity = resp.getAttributeScores().getProfanityScore().getSummaryScore().getValue();
        double toxicity = resp.getAttributeScores().getToxicityScore().getSummaryScore().getValue();

        return !(toxicity > 0.6) && !(profanity > 0.6);
    }

    public static void vibration(Context context) {
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.cancel();
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
        }
    }

    @NonNull
    public static InputFilter[] setMaxCharFilter(int maxNumber, View view, Context context) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (dest.length() >= maxNumber) {
                    Snackbar.make(view, maxNumber + " " + context.getString(R.string.max_char_text), Snackbar.LENGTH_SHORT).show();
                    vibration(context);
                    return "";
                }
            }
            return null;
        };
        return filters;
    }

}
