package com.example.novapp2.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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


import com.example.novapp2.entity.course.Course;
import com.example.novapp2.entity.course.CourseAdapter;
import com.example.novapp2.R;
import com.example.novapp2.databinding.FragmentDashboardBinding;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.entity.post.PostAdapter;
import com.example.novapp2.entity.post.PostViewModel;
import com.example.novapp2.utils.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.search.SearchBar;

import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private com.google.android.material.search.SearchView postSearchView;

    private SearchBar searchBar;
    private RecyclerView postView;
    private PostViewModel postViewModel;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private Set<Integer> selectedCategories;
    SwipeRefreshLayout swipeRefreshLayout;
    private Chip eventsChip;
    private Chip gsChip;
    private Chip infoChip;
    private Chip ripetizioniChip;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postList = new ArrayList<>();
        selectedCategories = new HashSet<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        eventsChip = view.findViewById(R.id.chip_event);
        infoChip = view.findViewById(R.id.chip_ui);
        gsChip = view.findViewById(R.id.chip_gs);
        ripetizioniChip= view.findViewById(R.id.chip_ripetizioni);

        // Set up chip click listeners
        setUpChipClickListener(eventsChip, 1);
        setUpChipClickListener(infoChip, 2);
        setUpChipClickListener(gsChip, 4);
        setUpChipClickListener(ripetizioniChip, 3);

        //post RecyclerView
        postView = view.findViewById(R.id.courseView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        postView.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    swipeRefreshLayout.setRefreshing(false);
                    //fetchPosts();
                    postViewModel.refresh();
                }
        );
        // creating recyclerView adapter
        postAdapter = new PostAdapter(requireContext(), postList, post -> {
            // navigation to details fragment
            DashboardFragmentDirections.ActionNavigationDashboardToPostDetailsFragment action =
                    DashboardFragmentDirections.actionNavigationDashboardToPostDetailsFragment(post);
            Navigation.findNavController(view).navigate(action);
        });

        postView.setAdapter(postAdapter);

        searchBar = view.findViewById(R.id.search_bar);

        searchBar.setOnClickListener(v -> {
            searchBar.setFocusable(true);
            searchBar.setFocusableInTouchMode(true);
            searchBar.requestFocus();

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
        });

        com.google.android.material.search.SearchView searchView = view.findViewById(R.id.post_search_view);

        // Imposta un listener per il cambiamento del testo di ricerca
        searchView
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            searchBar.setText(searchView.getText());
                            searchView.hide();
                            filterPostsByTitle(searchView.getText().toString());
                            return false;
                        });


        // Observing viewModel
        postViewModel.getAllPost().observe(getViewLifecycleOwner(), posts -> {
            postList.clear();
            postList.addAll(posts);
            postAdapter.notifyDataSetChanged();
            updateFilteredList();
        });

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

    public void addItemsToList() {
        Utils.sortCourseByName(postList);
    }
}
