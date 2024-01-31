package com.example.novapp2.ui.home.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.novapp2.R;
import com.example.novapp2.databinding.FragmentUserBinding;
import com.example.novapp2.entity.User;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.entity.post.PostAdapter;
import com.example.novapp2.entity.post.PostViewModel;
import com.example.novapp2.entity.post.SavedPostAdapter;
import com.example.novapp2.ui.home.HomeFragment;
import com.example.novapp2.ui.home.user.UserFragmentDirections;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.base.Predicates;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {


    private FragmentUserBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView userMail;
    private TextView userHi;
    private View root;
    private SavedPostAdapter savedPostAdapter;
    private PostViewModel postViewModel;

    private RecyclerView mySavedView;
    private RecyclerView myPostsView;

    private List<Post> postList;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private User user;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentUserBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        user = HomeFragment.getActiveUser();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mySavedView = view.findViewById(R.id.mySavedPosts);
        myPostsView = view.findViewById(R.id.myPosts);
        userMail = view.findViewById(R.id.userMailTextVew);
        userHi = view.findViewById(R.id.userHiTextView);

        mySavedView.setLayoutManager(new LinearLayoutManager(requireContext()));
        myPostsView.setLayoutManager(new LinearLayoutManager(requireContext()));

        if(null !=user && user.notNull()){
            userMail.setText(HomeFragment.getActiveUser().getEmail());
            userHi.setText(getString(R.string.hello) + HomeFragment.getActiveUser().getName() + getString(R.string.esclamation));
        }

        RecyclerView mySavedPostsRecyclerView = root.findViewById(R.id.mySavedPosts);
        RecyclerView myPostsRecyclerView = root.findViewById(R.id.myPosts);

        // Impostazione dell'orientamento orizzontale
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        mySavedPostsRecyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        myPostsRecyclerView.setLayoutManager(layoutManager2);

        savedPostAdapter = new SavedPostAdapter(requireContext(), postList, new PostAdapter.OnItemClickListener() {
            // navigation to post details fragment
            @Override
            public void onPostItemClick(Post post) {
                // Naviga verso il dettaglio del post solo se c'è una View valida
                if (getView() != null) {
                    // navigation to details fragment using Safe Args
                    UserFragmentDirections.ActionNavigationProfileToPostDetailsFragmentFragment action =
                            UserFragmentDirections.actionNavigationProfileToPostDetailsFragmentFragment(post);
                    Navigation.findNavController(getView()).navigate(action);
                }
            }

        });

        mySavedView.setAdapter(savedPostAdapter);
        mySavedView.clearFocus();

        myPostsView.setAdapter(savedPostAdapter);
        myPostsView.clearFocus();

        postViewModel.getFavoritePosts().observe(getViewLifecycleOwner(), posts -> {
            this.postList.clear();
            this.postList.addAll(posts);
            savedPostAdapter.notifyItemChanged(0, posts.size());
        });

        Button settingsButton = this.root.findViewById(R.id.user_settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View settingsButton) {
                settingsButton.setEnabled(!settingsButton.isEnabled());
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}