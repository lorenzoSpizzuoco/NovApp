package com.example.novapp2.ui.home.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.novapp2.R;
import com.example.novapp2.databinding.FragmentUserBinding;
import com.example.novapp2.entity.User;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.entity.post.PostAdapter;
import com.example.novapp2.entity.post.PostViewModel;
import com.example.novapp2.entity.post.SavedPostAdapter;
import com.example.novapp2.ui.home.HomeFragment;
import com.example.novapp2.ui.home.user.UserFragmentDirections;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.base.Predicates;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {


    private FragmentUserBinding binding;


    private TextView userMail;
    private TextView userHi;
    private View root;
    private SavedPostAdapter savedPostAdapter;
    private PostViewModel postViewModel;

    private BottomSheetBehavior bottomSheetBehavior;

    private ImageView userImage;

    private RecyclerView mySavedView;
    private RecyclerView myPostsView;

    private List<Post> postList;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    private RecyclerView mySavedPostsRecyclerView;

    private FrameLayout bottomSheet;

    private User user;

    private Button settingsButton;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentUserBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        user = HomeFragment.getActiveUser();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inizializzazione e configurazione degli elementi dell'interfaccia utente
        mySavedView = view.findViewById(R.id.mySavedPosts);
        userMail = view.findViewById(R.id.userMailTextVew);
        userHi = view.findViewById(R.id.userHiTextView);
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        mySavedPostsRecyclerView = view.findViewById(R.id.mySavedPosts);
        //RecyclerView myPostsRecyclerView = view.findViewById(R.id.myPosts);
        userImage = view.findViewById(R.id.userProfilePhoto);
        settingsButton = view.findViewById(R.id.user_settings_button);

        setupUserProfile();
        setupSavedPostsRecyclerView();
        observeSavedPosts();
        setupSettingsButton();
        initializeBottomSheet();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Rilascia il riferimento al binding
    }

    private void setupUserProfile() {
        // Controlla se l'oggetto user è null
        if (user != null) {
            String imageUrl = user.getProfileImg();
            if (imageUrl != null) {
                Glide.with(requireView())
                        .load(imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.analisi)
                        .into(userImage);
            }

            userMail.setText(user.getEmail());
            String userHiText = getString(R.string.hello) + " " + user.getName() + getString(R.string.esclamation);
            userHi.setText(userHiText);
        } else {
            userMail.setText("Email non disponibile");
            userHi.setText(getString(R.string.hello));
        }
    }


    private void setupSavedPostsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        mySavedPostsRecyclerView.setLayoutManager(layoutManager);
        mySavedView.setLayoutManager(layoutManager);
        // navigation to post details fragment
        savedPostAdapter = new SavedPostAdapter(requireContext(), postList, post -> {

            if (getView() != null) {
                UserFragmentDirections.ActionNavigationProfileToPostDetailsFragmentFragment action =
                        UserFragmentDirections.actionNavigationProfileToPostDetailsFragmentFragment(post);
                Navigation.findNavController(getView()).navigate(action);
            }
        });
        mySavedView.setAdapter(savedPostAdapter);
        mySavedView.clearFocus();
    }

    private void observeSavedPosts() {
        postViewModel.getFavoritePosts().observe(getViewLifecycleOwner(), posts -> {
            postList.clear();
            postList.addAll(posts);
            savedPostAdapter.notifyDataSetChanged();
        });
    }

    private void initializeBottomSheet() {
        if (getView() == null) return;
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setDraggable(true);

    }
    private void setupSettingsButton() {
        settingsButton.setOnClickListener(v -> {
            // Alterna tra mostrare e nascondere il Bottom Sheet
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                // Se il Bottom Sheet è già espanso, allora lo nasconde
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            } else {
                // Se il Bottom Sheet è nascosto o collassato, allora lo espande
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }








}



