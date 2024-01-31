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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postList = new ArrayList<>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        postViewModel.getAllPost().observe(getViewLifecycleOwner(), posts -> {
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