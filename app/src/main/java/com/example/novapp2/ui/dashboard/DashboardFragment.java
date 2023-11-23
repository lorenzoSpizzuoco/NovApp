package com.example.novapp2.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.ui.course.Course;
import com.example.novapp2.ui.course.CourseAdapter;
import com.example.novapp2.R;
import com.example.novapp2.databinding.FragmentDashboardBinding;
import com.example.novapp2.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Ottieni un riferimento al RecyclerView dal file layout XML
        RecyclerView courseView = root.findViewById(R.id.courseView);

        List<Course> courseList = new ArrayList<Course>();
        courseList.add(new Course("John wick", "bla bla bla", R.drawable.a));
        courseList.add(new Course("Robert j", "bla bla bla", R.drawable.analisi));
        courseList.add(new Course("James Gunn", "bla bla bla", R.drawable.matematica));
        courseList.add(new Course("Ricky tales", "bla bla bla", R.drawable.libri));
        courseList.add(new Course("Micky mose", "bla bla bla", R.drawable.matematica));
        courseList.add(new Course("Pick War", "bla bla bla", R.drawable.analisi));
        courseList.add(new Course("Leg piece", "bla bla bla", R.drawable.matematica));
        courseList.add(new Course("Apple Mac", "bla bla bla", R.drawable.analisi));

        Utils.sortCourseByName(courseList);

        // Configura il LinearLayoutManager e l'Adapter per il RecyclerView
        courseView.setLayoutManager(new LinearLayoutManager(requireContext()));
        courseView.setAdapter(new CourseAdapter(requireContext(), courseList));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
