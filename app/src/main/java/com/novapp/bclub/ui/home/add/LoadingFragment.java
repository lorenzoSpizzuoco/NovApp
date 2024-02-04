package com.novapp.bclub.ui.home.add;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novapp.bclub.R;

import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.entity.post.PostViewModel;


public class LoadingFragment extends Fragment {

    private PostViewModel postViewModel;

    private Post post;

    private Uri imageUri;
    private final String TAG = LoadingFragment.class.getSimpleName();

    public static LoadingFragment newInstance(String param1, String param2) {
        return new LoadingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getParcelable("post") != null) {
            post = bundle.getParcelable("post");
            imageUri = bundle.getParcelable("image");
        }
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstaceState) {
        // inserting new add
        postViewModel.insert(post, imageUri);
        postViewModel.getDoneLoading().observe(getViewLifecycleOwner(), doneLoading -> {
            if (doneLoading != null && doneLoading) {
                Navigation.findNavController(getView()).navigate(R.id.action_loadingFragment_to_navigation_dashboard);
            }
        });
    }
}