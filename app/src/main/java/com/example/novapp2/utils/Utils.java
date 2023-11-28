package com.example.novapp2.utils;

import com.example.novapp2.ui.course.Course;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {


    public static void sortCourseByName(List<Course> courseList){
        courseList.sort(Comparator.comparing(Course::getTitle));
    }

    public static List<Course> sortCourseByString(List<Course> courseList, String newText) {
        List<Course> filteredList = courseList.stream()
                .filter(course -> course.getTitle().toUpperCase().contains(newText.toUpperCase()))
                .collect(Collectors.toList());
        return filteredList;
    }

}
