package com.example.novapp2.utils;

import com.example.novapp2.ui.course.Course;
import com.example.novapp2.ui.post.Post;

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

}
