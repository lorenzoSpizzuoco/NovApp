package com.novapp.bclub.ui.home.dashboard;

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

import com.novapp.bclub.R;
import com.novapp.bclub.databinding.FragmentDashboardBinding;
import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.entity.post.PostAdapter;
import com.novapp.bclub.entity.post.PostViewModel;
import com.novapp.bclub.utils.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.search.SearchBar;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private SearchBar searchBar;
    private PostViewModel postViewModel;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private Set<Integer> selectedCategories;
    SwipeRefreshLayout swipeRefreshLayout;
    private Chip eventsChip;
    private Chip gsChip;
    private Chip infoChip;
    private Chip ripetizioniChip;
    private com.google.android.material.search.SearchView searchView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postList = new ArrayList<>();
        selectedCategories = new HashSet<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        eventsChip = view.findViewById(R.id.chip_event);
        infoChip = view.findViewById(R.id.chip_ui);
        gsChip = view.findViewById(R.id.chip_gs);
        ripetizioniChip= view.findViewById(R.id.chip_ripetizioni);
        searchBar = view.findViewById(R.id.search_bar);
        searchView = view.findViewById(R.id.post_search_view);

        // Set up chip click listeners
        setupChipClickListeners();

        setupPostRecyclerView(view);

        setupSwipe();

        setupSearchBar();

        setupSearchView();

        // Observing viewModel
        postViewModel.getPostsRoom().observe(getViewLifecycleOwner(), posts -> {
            Collections.reverse(posts);
            postList.clear();
            postList.addAll(posts);
            postAdapter.notifyDataSetChanged();
            updateFilteredList();
        });

    }

    private void setupSearchView() {
        // listener for text changes
        searchView
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            searchBar.setText(searchView.getText());
                            searchView.hide();
                            filterPostsByTitle(searchView.getText().toString());
                            return false;
                        });

    }

    private void setupSearchBar() {

        searchBar.setOnClickListener(v -> {
            searchBar.setFocusable(true);
            searchBar.setFocusableInTouchMode(true);
            searchBar.requestFocus();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    // setup pull down refresh gesture
    private void setupSwipe() {
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    swipeRefreshLayout.setRefreshing(false);
                    postViewModel.refresh();
                }
        );
    }

    // setup recycler view showing posts
    private void setupPostRecyclerView(View view) {
        //post RecyclerView
        RecyclerView postView = view.findViewById(R.id.courseView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        postView.setLayoutManager(linearLayoutManager);


        // creating recyclerView adapter
        postAdapter = new PostAdapter(requireContext(), postList, post -> {
            // navigation to details fragment
            DashboardFragmentDirections.ActionNavigationDashboardToPostDetailsFragment action =
                    DashboardFragmentDirections.actionNavigationDashboardToPostDetailsFragment(post);
            Navigation.findNavController(view).navigate(action);
        });
        postView.setAdapter(postAdapter);
    }

    // setup chip click listeners for list filtering
    private void setupChipClickListeners() {
        setUpChipClickListener(eventsChip, 1);
        setUpChipClickListener(infoChip, 2);
        setUpChipClickListener(gsChip, 4);
        setUpChipClickListener(ripetizioniChip, 3);
    }


    private void setUpChipClickListener(Chip chip, int category) {
        chip.setOnClickListener(v -> {
            if (chip.isChecked()) {
                selectedCategories.add(category);
            } else {
                selectedCategories.remove(category);
            }
            updateFilteredList();
        });
    }


    private void updateFilteredList() {
        if (selectedCategories.isEmpty()) {
            postAdapter.setPostList(postList);
        } else {
            List<Post> newFilteredList = postList.stream()
                    .filter(post -> selectedCategories.contains(post.getCategory()))
                    .collect(Collectors.toList());
            postAdapter.setPostList(newFilteredList);
        }

        postAdapter.notifyDataSetChanged();

    }

    private void filterPostsByTitle(String query) {

        Stream<Post> stream = postList.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(query.toLowerCase()));

        if (!selectedCategories.isEmpty()) {
            stream = stream.filter(post -> selectedCategories.contains(post.getCategory()));
        }

        List<Post> filteredPosts = stream.collect(Collectors.toList());
        postAdapter.setPostList(filteredPosts);
        postAdapter.notifyDataSetChanged();
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
