package com.example.novapp2.ui.home.dashboard;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.novapp2.R;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.entity.post.PostViewModel;
import com.example.novapp2.service.MessageService;
import com.example.novapp2.service.UserService;
import com.example.novapp2.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PostDetailsFragment extends Fragment {

    private static final String TAG = PostDetailsFragment.class.getSimpleName();

    private ImageView authorProfileImage;

    private TextView title;

    private TextView description;

    private TextView date;

    private TextView place;

    private TextView username;

    private Chip chip;

    private FloatingActionButton backButton;

    ExtendedFloatingActionButton gs_button;


    private ExtendedFloatingActionButton favoriteIcon;

    private PostViewModel postViewModel;

    Post p;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    public static PostDetailsFragment newInstance(String param1, String param2) {
        return new PostDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate right layout based on post category
        Post p = PostDetailsFragmentArgs.fromBundle(getArguments()).getPost();
        if (p.getCategory() == 2) {
            return inflater.inflate(R.layout.fragment_info_details, container, false);
        }

        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {

        p = PostDetailsFragmentArgs.fromBundle(getArguments()).getPost();
        backButton = view.findViewById(R.id.backButton);
        authorProfileImage = view.findViewById(R.id.post_user_image);
        chip = view.findViewById(R.id.postDetailChip);
        gs_button = view.findViewById(R.id.gsButton);
        title = view.findViewById(R.id.postTitle);
        date = view.findViewById(R.id.postDate);
        place = view.findViewById(R.id.postPlace);
        description = view.findViewById(R.id.postDescription);
        favoriteIcon = view.findViewById(R.id.imageview_favorite_post);
        username = view.findViewById(R.id.user_name_post);



        setUpAuthorImage(view);

        setupChip();

        setupImage(view);

        setupFields(view);

        setupStudyGroupButton(view);

        fixBottomBar(view);

        setupFavoriteButtonListener();



        // observing livedata
        postViewModel.getIsFavorite(HomeFragment.getActiveUser().getID(), p.getDbId()).observe(getViewLifecycleOwner(), favorite -> {

            Log.d(TAG, String.valueOf(favorite));
            if (favorite == 1) {
                favoriteIcon.setIconResource(R.drawable.ic_favorite_24);
            }
            else {
                favoriteIcon.setIconResource(R.drawable.baseline_favorite_border_24);
            }
            p.setFavorite(favorite);
        });



    }


    private void setupFavoriteButtonListener() {

        // click listener
        favoriteIcon.setOnClickListener(v -> {
            if (p.getFavorite() == 1) {
                postViewModel.setFavorite(p.getDbId(), 0, p.getCategory());
            }
            else {
                postViewModel.setFavorite(p.getDbId(), 1, p.getCategory());
            }
        });
    }

    private void setupStudyGroupButton(View view) {
        if (p.getCategory() == 4) {
            gs_button.setVisibility(View.VISIBLE);
            gs_button.setOnClickListener(v ->{
                List<String> groupChats = HomeFragment.getActiveUser().getGroupChats();
                if(!groupChats.contains(p.getDbId())){
                    groupChats.add(p.getDbId());
                    UserService.updateUserById(HomeFragment.getActiveUser().getID(), HomeFragment.getActiveUser());
                    String t = getString(R.string.in_group) + " " + p.getTitle() + "!";

                    Snackbar.make(view, t, Snackbar.LENGTH_SHORT).show();
                    MessageService.createMessage(HomeFragment.getActiveUser().getEmail() + " " + "joined the chat", HomeFragment.getActiveUser().getEmail(), p.getDbId());

                } else {
                    Snackbar.make(view, R.string.already_in, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fixBottomBar(View view) {
        NavBackStackEntry navBackStackEntry = Navigation.
                findNavController(view).getPreviousBackStackEntry();

        // selecting right bottombar icon
        if (navBackStackEntry != null &&
                navBackStackEntry.getDestination().getId() == R.id.navigation_dashboard) {
            ((BottomNavigationView) requireActivity().findViewById(R.id.nav_view)).
                    getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
        }
    }

    private void setupFields(View view) {

        username.setText(p.getAuthor());
        backButton.setBackgroundTintMode(null);

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        gs_button.setVisibility(View.GONE);

        title.setText(p.getTitle());
        date.setText(p.getDate());
        place.setText(p.getPlace());
        description.setText(p.getContent());

    }

    private void setupImage(View view) {
        if (p.getCategory() != 2) {
            ImageView image = view.findViewById(R.id.PostdetailsImageView);

            if (p.getPostImage() == null) {
                image.setImageResource(p.getImage());
            } else {
                Glide.with(view)
                        .load(p.getPostImage())
                        .centerCrop()
                        .placeholder(R.drawable.analisi)
                        .into(image);
            }
        }

    }

    private void setUpAuthorImage(View view) {

        postViewModel.getAuthorImage(p.getAuthor()).observe(getViewLifecycleOwner(), imageUrl ->
                {
                    if (imageUrl != null) {
                        Glide.with(view)
                                .load(imageUrl)
                                .centerCrop()
                                .placeholder(R.drawable.analisi)
                                .into(authorProfileImage);
                    }
                }
        );
    }

    private void setupChip() {
        switch (p.getCategory()) {
            case 1:
                chip.setText(R.string.event_chip);
                break;
            case 2:
                chip.setText(R.string.infos_chip);
                break;
            case 3:
                chip.setText(R.string.ripet_chip);
                break;
            case 4:
                chip.setText(R.string.gs_chip);
                gs_button.setVisibility(View.VISIBLE);
                break;
        }
    }
}