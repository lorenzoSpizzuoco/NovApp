package com.example.novapp2.ui.dashboard;

import static com.example.novapp2.utils.Utils.base64ToBitmap;

import android.content.res.ColorStateList;
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

import com.example.novapp2.R;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.entity.post.PostViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PostDetailsFragment extends Fragment {

    private static final String TAG = PostDetailsFragment.class.getSimpleName();

    private ImageView image;

    private TextView title;

    private TextView description;

    private TextView date;

    private TextView place;

    private TextView username;


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
        image = view.findViewById(R.id.PostdetailsImageView);
        title = view.findViewById(R.id.postTitle);
        date = view.findViewById(R.id.postDate);
        place = view.findViewById(R.id.postPlace);
        description = view.findViewById(R.id.postDescription);
        favoriteIcon = view.findViewById(R.id.imageview_favorite_post);
        username = view.findViewById(R.id.user_name_post);
        username.setText(p.getAuthor());
        if (p.getPostImage() == null) {
            image.setImageResource(p.getImage());
        }
        else {
            image.setImageBitmap(base64ToBitmap(p.getPostImage()));
        }

        title.setText(p.getTitle());
        date.setText(p.getDate());
        place.setText(p.getPlace());
        description.setText(p.getContent());

        // colors for icon filtering
        int red = ContextCompat.getColor(this.getContext(), android.R.color.holo_red_dark);
        int white = ContextCompat.getColor(this.getContext(), android.R.color.white);

        // setting livedata value to 1 if post is listed as favorite
        if (p.getFavorite() == 1) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_24);
            favoriteIcon.setColorFilter(red);
            postViewModel.setFavorite(p.getId(), 1);
        }

        // observing livedata
        postViewModel.getIsFavorite().observe(getViewLifecycleOwner(), favorite -> {
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
                //p.setFavorite(0);
                postViewModel.setFavorite(p.getId(), 0);
            }
            else {
                //p.setFavorite(1);
                postViewModel.setFavorite(p.getId(), 1);
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