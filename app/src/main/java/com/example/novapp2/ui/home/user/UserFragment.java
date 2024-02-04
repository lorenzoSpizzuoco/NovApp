package com.example.novapp2.ui.home.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.databinding.FragmentUserBinding;
import com.example.novapp2.entity.user.User;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.entity.post.PostViewModel;
import com.example.novapp2.entity.post.SavedPostAdapter;
import com.example.novapp2.service.nativeapi.UserService;
import com.example.novapp2.service.nativeapi.AuthService;
import com.example.novapp2.entity.user.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {


    private FragmentUserBinding binding;


    private TextView userMail;
    private TextView userHi;

    private SavedPostAdapter savedPostAdapter;
    private SavedPostAdapter userPostAdapter;
    private PostViewModel postViewModel;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView userImage;
    private static final UserViewModel userViewModel = new UserViewModel();

    private RecyclerView mySavedView;

    private List<Post> postList;

    private List<Post> userPosts;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private RecyclerView mySavedPostsRecyclerView;

    private RecyclerView userPostsRecyclerView;

    private final UserService userService = new UserService();
    private FrameLayout bottomSheet;
    private User user;
    private FloatingActionButton settingsButton;
    private Button logoutButton;

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
        userPosts = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        user = userService.getCurrentUser();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inizializzazione e configurazione degli elementi dell'interfaccia utente
        userMail = view.findViewById(R.id.userMailTextVew);
        userHi = view.findViewById(R.id.userHiTextView);
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        mySavedPostsRecyclerView = view.findViewById(R.id.mySavedPosts);
        userPostsRecyclerView = view.findViewById(R.id.myPosts);
        userImage = view.findViewById(R.id.userProfilePhoto);
        logoutButton = view.findViewById(R.id.logoutButton);

        settingsButton = view.findViewById(R.id.user_settings_button);

        setupUserProfile();
        setupSavedPostsRecyclerView();
        setupUserPostsRecyclerView();
        observeSavedPosts();
        observeUserPosts();
        setupSettingsButton();
        setupLogoutButton();
        initializeBottomSheet();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupUserProfile() {

        // checking if user profile is null
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
            userMail.setText(getString(R.string.no_email_error));
            userHi.setText(getString(R.string.hello));
        }
    }

    private void setupUserPostsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        userPostsRecyclerView.setLayoutManager(layoutManager);
        // navigation to post details fragment
        userPostAdapter = new SavedPostAdapter(requireContext(), userPosts, post -> {

            if (getView() != null) {
                UserFragmentDirections.ActionNavigationProfileToPostDetailsFragmentFragment action =
                        UserFragmentDirections.actionNavigationProfileToPostDetailsFragmentFragment(post);
                Navigation.findNavController(getView()).navigate(action);
            }
        });

        userPostsRecyclerView.setAdapter(userPostAdapter);
        userPostsRecyclerView.clearFocus();
    }

    private void setupSavedPostsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        mySavedPostsRecyclerView.setLayoutManager(layoutManager);
        // navigation to post details fragment
        savedPostAdapter = new SavedPostAdapter(requireContext(), postList, post -> {

            if (getView() != null) {
                UserFragmentDirections.ActionNavigationProfileToPostDetailsFragmentFragment action =
                        UserFragmentDirections.actionNavigationProfileToPostDetailsFragmentFragment(post);
                Navigation.findNavController(getView()).navigate(action);
            }
        });
        mySavedPostsRecyclerView.setAdapter(savedPostAdapter);
        mySavedPostsRecyclerView.clearFocus();
    }

    private void observeSavedPosts() {
        userViewModel.getSavedPosts().observe(getViewLifecycleOwner(), posts -> {
            postList.clear();
            postList.addAll(posts);
            savedPostAdapter.notifyDataSetChanged();
        });
    }

    private void observeUserPosts() {
        postViewModel.getUserPosts().observe(getViewLifecycleOwner(), posts -> {
            userPosts.clear();
            userPosts.addAll(posts);
            userPostAdapter.notifyDataSetChanged();
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
    private void setupLogoutButton() {
        logoutButton.setOnClickListener(v -> {

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.event_logout)
                    .setMessage(R.string.message_logout)
                    .setPositiveButton(R.string.ok_button, (di, i) -> {
                        logout();
                    })
                    .setNegativeButton(R.string.dialog_close, (di, i) -> {
                    });

            builder.create();
            builder.show();
        });
    }

    private void logout() {
        userService.setRemoteSaved();
        FirebaseAuth.getInstance().signOut();
        AuthService.deleteUserCredentials(requireContext());
        MainActivity.getNavController().navigate(R.id.action_to_login);
    }

}



