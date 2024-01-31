package com.example.novapp2.ui.dashboard;


import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.example.novapp2.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PostDetailsFragment extends Fragment {

    private static final String TAG = PostDetailsFragment.class.getSimpleName();

    private ImageView image;

    private ImageView authorProfileImage;

    private TextView title;

    private TextView description;

    private TextView date;

    private TextView place;

    private TextView username;

    private Chip chip;


    private FloatingActionButton favoriteIcon;

    private PostViewModel postViewModel;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    public static PostDetailsFragment newInstance(String param1, String param2) {
        PostDetailsFragment fragment = new PostDetailsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {

        Post p = PostDetailsFragmentArgs.fromBundle(getArguments()).getPost();
        authorProfileImage = view.findViewById(R.id.post_user_image);
        chip = view.findViewById(R.id.postDetailChip);
        image = view.findViewById(R.id.PostdetailsImageView);
        title = view.findViewById(R.id.postTitle);
        date = view.findViewById(R.id.postDate);
        place = view.findViewById(R.id.postPlace);
        description = view.findViewById(R.id.postDescription);
        favoriteIcon = view.findViewById(R.id.imageview_favorite_post);
        username = view.findViewById(R.id.user_name_post);
        username.setText(p.getAuthor());

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
                break;
        }

        if (p.getPostImage() == null) {
            image.setImageResource(p.getImage());
        }
        else {
            Glide.with(getContext())
                    .load(p.getPostImage())
                    .centerCrop()
                    .placeholder(R.drawable.analisi)
                    .into(image);
        }

        title.setText(p.getTitle());
        date.setText(p.getDate());
        place.setText(p.getPlace());
        description.setText(p.getContent());

        // colors for icon filtering
        int red = ContextCompat.getColor(this.getContext(), android.R.color.holo_red_dark);
        int white = ContextCompat.getColor(this.getContext(), android.R.color.white);


        // observing livedata
        postViewModel.getIsFavorite(HomeFragment.getActiveUser().getID(), p.getDbId()).observe(getViewLifecycleOwner(), favorite -> {
            if (favorite == 1) {
                favoriteIcon.setImageResource(R.drawable.ic_favorite_24);
                favoriteIcon.setColorFilter(red);
            }
            else {
                favoriteIcon.setImageResource(R.drawable.baseline_favorite_border_24);
                favoriteIcon.setColorFilter(white);
            }
            p.setFavorite(favorite);
        });

        // click listener
        favoriteIcon.setOnClickListener(v -> {
            if (p.getFavorite() == 1) {
                postViewModel.setFavorite(p.getDbId(), 0, p.getCategory());
            }
            else {
                postViewModel.setFavorite(p.getDbId(), 1, p.getCategory());
            }
        });

        NavBackStackEntry navBackStackEntry = Navigation.
                findNavController(view).getPreviousBackStackEntry();

        // selecting right bottombar icon
        if (navBackStackEntry != null &&
                navBackStackEntry.getDestination().getId() == R.id.navigation_dashboard) {
            ((BottomNavigationView) requireActivity().findViewById(R.id.nav_view)).
                    getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
        }

    }
}