package com.example.novapp2.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;
import com.example.novapp2.databinding.FragmentDashboardBinding;
import com.example.novapp2.ui.post.Post;
import com.example.novapp2.ui.post.PostAdapter;
import com.example.novapp2.ui.post.PostViewModel;
import com.example.novapp2.utils.Utils;
import com.google.android.material.chip.Chip;
import androidx.appcompat.widget.SearchView;


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

    private Chip eventsChip;

    private Chip gsChip;

    private Chip infoChip;

    private Chip ripetizioniChip;


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

        eventsChip = view.findViewById(R.id.chip_event);
        infoChip = view.findViewById(R.id.chip_ui);
        gsChip = view.findViewById(R.id.chip_gs);
        ripetizioniChip= view.findViewById(R.id.chip_ripetizioni);

        // filtering buttons onClick listeners

        eventsChip.setOnClickListener(v -> {
            filterPostList(1);
        });

        infoChip.setOnClickListener(v -> {
            filterPostList(2);
        });

        gsChip.setOnClickListener(v -> {
            filterPostList(4);
        });

        ripetizioniChip.setOnClickListener(v -> {
            filterPostList(3);
        });

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

        // top search bar configuration
        postSearchView = view.findViewById(R.id.courseSearchView);


        // Open the keyboard when the search view is clicked
        postSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(postSearchView, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        postSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Puoi gestire anche la ricerca su invio, se necessario
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPostsByTitle(newText);
                return true;
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

    private void filterPostsByTitle(String query) {
        List<Post> filteredPosts = postList.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        postAdapter.setPostList(filteredPosts);
        postAdapter.notifyDataSetChanged();
    }
}
