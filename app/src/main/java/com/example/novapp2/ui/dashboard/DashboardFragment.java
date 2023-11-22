package com.example.novapp2.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.Course;
import com.example.novapp2.CourseAdapter;
import com.example.novapp2.R;
import com.example.novapp2.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
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
        courseList.add(new Course("John wick","bla bla bla", R.drawable.a));
        courseList.add(new Course("John wick","bla bla bla", R.drawable.a));
        courseList.add(new Course("John wick","bla bla bla", R.drawable.a));
        courseList.add(new Course("John wick","bla bla bla", R.drawable.a));
        courseList.add(new Course("John wick","bla bla bla", R.drawable.a));

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
