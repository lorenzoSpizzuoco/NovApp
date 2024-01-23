package com.example.novapp2.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import java.util.stream.Collectors;

public class DashboardFragment extends Fragment {

    private static final String TAG = DashboardFragment.class.getSimpleName();
    private FragmentDashboardBinding binding;

    private SearchView postSearchView;
    private RecyclerView postView;

    private PostViewModel postViewModel;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<Post> filteredList;

    private Button eventsButton;

    private Button gsButton;

    private Button infoButton;

    private Button ripetizioniButton;

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

        eventsButton = view.findViewById(R.id.button_event);
        infoButton = view.findViewById(R.id.button_ui);
        gsButton = view.findViewById(R.id.button_gs);
        ripetizioniButton = view.findViewById(R.id.button_ripetizioni);

        // filtering buttons onClick listeners

        eventsButton.setOnClickListener(v -> {
            filterPostList(1);
        });

        infoButton.setOnClickListener(v -> {
            filterPostList(2);
        });

        gsButton.setOnClickListener(v -> {
            filterPostList(4);
        });

        ripetizioniButton.setOnClickListener(v -> {
            filterPostList(3);
        });

        // top search bar configuration
        postSearchView = view.findViewById(R.id.courseSearchView);
        EditText searchText = postSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));

        //post RecycleView
        postView = view.findViewById(R.id.courseView);
        postView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // creating recyclerView adapter
        postAdapter = new PostAdapter(requireContext(), postList, new PostAdapter.OnItemClickListener() {
            // navigation to post details fragment
            @Override
            public void onPostItemClick(Post post) {
                // navigation to details fragment
                DashboardFragmentDirections.ActionNavigationDashboardToPostDetailsFragment action =
                        DashboardFragmentDirections.actionNavigationDashboardToPostDetailsFragment(post);
                Navigation.findNavController(view).navigate(action);
            }
        });

        postView.setAdapter(postAdapter);

        postSearchView.clearFocus();
        postSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d(TAG, "onQueryTextChange call");
                filteredList = Utils.sortCourseByString(postList, newText);
                if (filteredList.isEmpty()) {
                    Snackbar.make(getView(), "no data found", Snackbar.LENGTH_SHORT).show();
                }
                postAdapter.setPostList(filteredList);
                return false;
            }
        });

        // adapter for the recyclerView
        // observing viewModel
        postViewModel.getAllPost().observe(getViewLifecycleOwner(), posts -> {
            this.postList.clear();
            this.postList.addAll(posts);
            postAdapter.notifyItemChanged(0, posts.size());
        });
    }

    private void filterPostList(int category) {

        List<Post> newFilteredList = postList.stream()
                .filter(post -> post.getCategory() == category)
                .collect(Collectors.toList());


        this.postAdapter.setPostList(newFilteredList);
        postAdapter.notifyItemChanged(0, newFilteredList.size());
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addItemsToList() {

        Utils.sortCourseByName(postList);
    }
}
