package com.example.novapp2.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import java.util.List;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private FragmentDashboardBinding binding;

    private SearchView courseSearchView;
    private RecyclerView courseView;

    private List<Course> courseList;
    private List<Course> filteredList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //course RecycleView
        courseView = root.findViewById(R.id.courseView);
        addItemsToList();
        courseView.setLayoutManager(new LinearLayoutManager(requireContext()));
        courseView.setAdapter(new CourseAdapter(requireContext(), courseList));

        //courseSearchView
        courseSearchView = root.findViewById(R.id.courseSearchView);
        courseSearchView.clearFocus();
        courseSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList = Utils.sortCourseByString(courseList, newText);
                if(filteredList.isEmpty()){
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                }
                courseView.setAdapter(new CourseAdapter(requireContext(), filteredList));
                return false;
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addItemsToList(){
        courseList = new ArrayList<Course>();
        courseList.add(new Course("John wick", "bla bla bla", R.drawable.libri));
        courseList.add(new Course("Robert j", "bla bla bla", R.drawable.analisi));
        courseList.add(new Course("James Gunn", "bla bla bla", R.drawable.matematica));
        courseList.add(new Course("Ricky tales", "bla bla bla", R.drawable.libri));
        courseList.add(new Course("Micky mose", "bla bla bla", R.drawable.matematica));
        courseList.add(new Course("Pick War", "bla bla bla", R.drawable.analisi));
        courseList.add(new Course("Leg piece", "bla bla bla", R.drawable.matematica));
        courseList.add(new Course("Apple Mac", "bla bla bla", R.drawable.analisi));
        Utils.sortCourseByName(courseList);
    }


}
