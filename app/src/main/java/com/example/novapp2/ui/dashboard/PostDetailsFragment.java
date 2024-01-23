package com.example.novapp2.ui.dashboard;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novapp2.R;
import com.example.novapp2.ui.post.Post;
import com.example.novapp2.ui.post.PostViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

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

    private Chip postChip;

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
        postChip = view.findViewById(R.id.postDetailChip);

        switch(p.getCategory()) {
            case 1:
                postChip.setText(R.string.button_event);
                postChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this.getContext(), R.color.event_chip_background)));
                postChip.setTextColor(ContextCompat.getColor(this.getContext(), R.color.event_chip_text_color));
                break;
            case 2:
                postChip.setText(R.string.button_info);
                postChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this.getContext(), R.color.info_chip_background)));
                postChip.setTextColor(ContextCompat.getColor(this.getContext(), R.color.main_blue));
                break;
            case 3:
                postChip.setText(R.string.button_ripetizioni);
                postChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this.getContext(), R.color.ripetizioni_chip_background)));
                postChip.setTextColor(ContextCompat.getColor(this.getContext(), R.color.main_red));
                break;
            case 4:
                postChip.setText(R.string.button_sg);
                postChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this.getContext(), R.color.gs_background_color)));
                postChip.setTextColor(ContextCompat.getColor(this.getContext(), R.color.gs_text_color));
                break;
            default:
                Log.d(TAG, "no match");
        }
        username.setText(p.getAuthor());
        image.setImageResource(p.getImage());
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

    }
}