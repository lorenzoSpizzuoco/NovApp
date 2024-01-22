package com.example.novapp2.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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

import org.w3c.dom.Text;

public class PostDetailsFragment extends Fragment {


    private ImageView image;

    private TextView title;

    private TextView description;

    private TextView date;

    private TextView place;

    private ImageView favoriteIcon;

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

        if (p.getFavorite() == 1) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_24);
            favoriteIcon.setColorFilter(R.color.main_red);
        }

        image.setImageResource(p.getImage());
        title.setText(p.getTitle());
        date.setText(p.getDate());
        place.setText(p.getPlace());
        description.setText(p.getContent());

        favoriteIcon.setOnClickListener(v -> {
            if (p.getFavorite() == 0) {
                postViewModel.setFavorite(p.getTitle(), 1);
            }
            else {
                postViewModel.setFavorite(p.getTitle(), 0);
            }


        });

    }
}