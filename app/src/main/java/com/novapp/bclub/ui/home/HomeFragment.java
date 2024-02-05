package com.novapp.bclub.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.novapp.bclub.MainActivity;
import com.novapp.bclub.R;
import com.novapp.bclub.entity.user.User;
import com.novapp.bclub.entity.user.UserViewModel;
import com.novapp.bclub.service.nativeapi.UserService;


public class HomeFragment extends Fragment {

    private UserViewModel userViewModel;
    private final UserService userService = new UserService();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView navView = requireView().findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);

        userViewModel.getUser().observe(this.getViewLifecycleOwner(), activeUser -> {

            userViewModel.getSavedPosts().observe(this.getViewLifecycleOwner(), savedPosts -> userService.getCurrentUser().setFavourites(savedPosts));

            if (!userFullyRegistered(activeUser)) {
                MainActivity mainActivity = new MainActivity();
                Bundle args = new Bundle();
                args.putString("userId", activeUser.getID());
                args.putString("userEmail", activeUser.getEmail());
                NavController mainNavController = mainActivity.getNavController();
                mainNavController.navigate(R.id.action_home_to_fullRegister, args);
            }
        });

        disableBackButton();
    }

    private void disableBackButton() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Disable back button
            }
        });
    }

    private boolean userFullyRegistered(User user) {
        return !user.name.equals("") && !user.surname.equals("") && !user.bio.equals("");
    }


}