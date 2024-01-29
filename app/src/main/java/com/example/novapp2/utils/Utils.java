package com.example.novapp2.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.novapp2.entity.post.Post;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {


    public static void sortCourseByName(List<Post> courseList){
        courseList.sort(Comparator.comparing(Post::getTitle));
    }

    public static List<Post> sortCourseByString(List<Post> courseList, String newText) {
        List<Post> filteredList = courseList.stream()
                .filter(course -> course.getTitle().toUpperCase().contains(newText.toUpperCase()))
                .collect(Collectors.toList());
        return filteredList;
    }

    public static boolean checkResponse(String body) {
        Gson gson = new Gson();
        ProfanityResponse resp = gson.fromJson(body, ProfanityResponse.class);
        double profanity = resp.getAttributeScores().getProfanityScore().getSummaryScore().getValue();
        double toxicity = resp.getAttributeScores().getToxicityScore().getSummaryScore().getValue();

        if(toxicity > 0.6 || profanity > 0.6) {
            return false;
        }
        return true;
    }

    public static Bitmap base64ToBitmap(String string) {

        byte[] byteArray = Base64.getDecoder().decode(string);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(byteArray);
    }

}
