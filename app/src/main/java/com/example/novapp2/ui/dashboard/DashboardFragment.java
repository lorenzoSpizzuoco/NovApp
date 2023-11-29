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

    private CourseAdapter courseAdapter;
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

        courseAdapter = new CourseAdapter(requireContext(), courseList);
        courseView.setAdapter(courseAdapter);

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
                courseAdapter.setCourseList(filteredList);
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
        courseList.add(new Course("Web Development Basics", "Learn the essentials of building websites and web applications.", R.drawable.libri));
        courseList.add(new Course("Photography Fundamentals", "Master the art of capturing stunning photographs with your camera.", R.drawable.analisi));
        courseList.add(new Course("Java Programming Mastery", "Deepen your understanding of Java programming and application development.", R.drawable.matematica));
        courseList.add(new Course("Exploring World Cultures", "Discover diverse cultures and traditions from around the globe.", R.drawable.libri));
        courseList.add(new Course("Machine Learning Essentials", "Dive into the foundations of machine learning and artificial intelligence.", R.drawable.matematica));
        courseList.add(new Course("Graphic Design Workshop", "Unleash your creativity through hands-on graphic design projects.", R.drawable.analisi));
        courseList.add(new Course("Fitness and Wellness Journey", "Embark on a holistic journey to enhance your fitness and well-being.", R.drawable.matematica));
        courseList.add(new Course("Space Exploration History", "Explore the history of space exploration and scientific discoveries.", R.drawable.analisi));
        courseList.add(new Course("Mobile App Development", "Build mobile applications for Android and iOS platforms.", R.drawable.libri));
        courseList.add(new Course("Creative Cooking Techniques", "Learn innovative cooking techniques and experiment with flavors.", R.drawable.analisi));
        courseList.add(new Course("Mindfulness Meditation", "Cultivate mindfulness and enhance your mental well-being through meditation.", R.drawable.matematica));
        courseList.add(new Course("Financial Literacy Essentials", "Develop essential financial skills for managing personal finances.", R.drawable.analisi));
        courseList.add(new Course("Environmental Sustainability", "Explore solutions for a sustainable and eco-friendly future.", R.drawable.matematica));
        courseList.add(new Course("Introduction to Psychology", "Understand the basics of psychology and human behavior.", R.drawable.libri));
        courseList.add(new Course("Digital Marketing Strategies", "Master the strategies and tools for effective digital marketing.", R.drawable.matematica));
        courseList.add(new Course("Artificial Intelligence Ethics", "Examine ethical considerations in the field of artificial intelligence.", R.drawable.libri));
        courseList.add(new Course("Effective Communication Skills", "Enhance your communication skills for personal and professional success.", R.drawable.analisi));
        courseList.add(new Course("Music Production Basics", "Learn the fundamentals of music production and audio engineering.", R.drawable.matematica));
        courseList.add(new Course("Health and Nutrition Science", "Explore the science behind health and nutrition for a balanced life.", R.drawable.analisi));
        courseList.add(new Course("Introduction to Cryptocurrency", "Understand the basics of cryptocurrencies and blockchain technology.", R.drawable.matematica));

        Utils.sortCourseByName(courseList);
    }


}
