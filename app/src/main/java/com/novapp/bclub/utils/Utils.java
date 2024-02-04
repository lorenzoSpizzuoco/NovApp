package com.novapp.bclub.utils;

import static com.novapp.bclub.utils.Constants.DB_EVENTS;
import static com.novapp.bclub.utils.Constants.DB_GS;
import static com.novapp.bclub.utils.Constants.DB_INFOS;
import static com.novapp.bclub.utils.Constants.DB_RIPET;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputFilter;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.novapp.bclub.R;
import com.novapp.bclub.entity.post.Post;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

    // IMAGE UPLOAD
    public static Task<Uri> uploadImage(Uri image, String child, String name) {

        StorageReference ref = FirebaseStorage.getInstance().getReference();

        StorageReference imRef = ref.child(child).child(name + image.getLastPathSegment());

        return imRef.putFile(image).continueWithTask(
                task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return imRef.getDownloadUrl();
                });
    }

}
