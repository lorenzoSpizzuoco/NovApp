package com.novapp.bclub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;

import com.novapp.bclub.R;

import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.entity.post.SavedPostsViewModel;
import com.novapp.bclub.service.nativeapi.PostService;
import com.novapp.bclub.service.nativeapi.UserService;


public class MainActivity extends AppCompatActivity {

    private static NavController navController ;
    private final UserService userService = new UserService();
    private PostService postService;
    private SavedPostsViewModel savedPostsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postService = new PostService(getApplication());
        setContentView(R.layout.activity_main);
        //TODO check di utente già loggato, se è già loggato va nello start screen
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostContainer);
        navController = navHostFragment.getNavController();
        savedPostsViewModel = new ViewModelProvider(this).get(SavedPostsViewModel.class);
        postService.deleteAll();
    }

    public static NavController getNavController() {
        return navController;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "on stop main activity");
        savedPostsViewModel.getSavedPosts().observe(this, posts -> {
            Log.d("MainActivity", "sono quiiii");
            userService.setRemoteSaved(posts);
        });

        //userService.setRemoteSaved();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();


/*        String fileContents = "Hello world!";
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.toByteArray());
        }*/
    }
}