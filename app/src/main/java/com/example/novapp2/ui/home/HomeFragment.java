package com.example.novapp2.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.entity.User;
import com.example.novapp2.entity.post.PostViewModel;
import com.example.novapp2.service.UserService;
import com.example.novapp2.ui.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    //private static User activeUser;
    private UserViewModel userViewModel;
    private final UserService userService = new UserService();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView navView = requireView().findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);

        userViewModel.getUser().observe(this.getViewLifecycleOwner(), activeUser -> {

            userViewModel.getSavedPosts().observe(this.getViewLifecycleOwner(), savedPosts -> {
                userService.getCurrentUser().setFavourites(savedPosts);
            });

            Log.d(TAG, activeUser.getEmail());
            if (!userFullyRegistered(activeUser)) {
                Bundle args = new Bundle();
                args.putString("userId", activeUser.getID());
                args.putString("userEmail", activeUser.getEmail());
                NavController mainNavController = MainActivity.getNavController();
                mainNavController.navigate(R.id.action_home_to_fullRegister, args);
            }
            else {
                //MainActivity.getNavController().navigate();
                //MainActivity.getNavController().navigate(R.id.action_home_to_login);
            }
        });

    }

    private boolean userFullyRegistered(User user) {
        return !user.name.equals("") && !user.surname.equals("") && !user.bio.equals("");
    }


}