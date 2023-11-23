package com.example.novapp2.utils;

import com.example.novapp2.ui.course.Course;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {


    public static void sortCourseByName(List<Course> courseList){
        Collections.sort(courseList, new Comparator<Course>() {
            @Override
            public int compare(Course course1, Course course2) {
                return course1.getTitle().compareTo(course2.getTitle());
            }
        });
    }
}
