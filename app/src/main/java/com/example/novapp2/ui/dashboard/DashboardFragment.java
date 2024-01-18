package com.example.novapp2.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.example.novapp2.ui.ad.Ad;
import com.example.novapp2.ui.ad.AdViewModel;
import com.example.novapp2.ui.course.Course;
import com.example.novapp2.ui.course.CourseAdapter;
import com.example.novapp2.R;
import com.example.novapp2.databinding.FragmentDashboardBinding;
import com.example.novapp2.ui.post.Post;
import com.example.novapp2.ui.post.PostAdapter;
import com.example.novapp2.ui.post.PostViewModel;
import com.example.novapp2.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private FragmentDashboardBinding binding;

    private SearchView postSearchView;
    private RecyclerView courseView;

    private PostViewModel postViewModel;
    private PostAdapter postAdapter;
    //private List<Course> courseList;
    private List<Post> postList;
    private List<Post> filteredList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postList = new ArrayList<>();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

        // Ottieni il riferimento alla barra di ricerca
        postSearchView = view.findViewById(R.id.courseSearchView);

        // Imposta il colore del testo nella barra di ricerca
        EditText searchText = postSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));

        //course RecycleView
        courseView = view.findViewById(R.id.courseView);
        //addItemsToList();
        courseView.setLayoutManager(new LinearLayoutManager(requireContext()));

        postAdapter = new PostAdapter(requireContext(), postList, new PostAdapter.OnItemClickListener() {
            @Override
            public void onPostItemClick(Post post) {
                Snackbar.make(view, "click on " + post.getTitle().toString(), Snackbar.LENGTH_SHORT).show();
            }
        });
        courseView.setAdapter(postAdapter);

        //courseSearchView
        postSearchView.clearFocus();
        postSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList = Utils.sortCourseByString(postList, newText);
                if (filteredList.isEmpty()) {
                    Snackbar.make(getView(), "no data found", Snackbar.LENGTH_SHORT).show();
                }
                postAdapter.setPostList(filteredList);
                return false;
            }
        });

        // observing viewModel
        postViewModel.getAllPost().observe(getViewLifecycleOwner(), posts -> {
            this.postList.addAll(posts);

            postAdapter.notifyItemChanged(0, posts.size());
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addItemsToList() {

        Utils.sortCourseByName(postList);
    }
}
