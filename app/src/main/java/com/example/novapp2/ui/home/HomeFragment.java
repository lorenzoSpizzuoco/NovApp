package com.example.novapp2.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.entity.User;
import com.example.novapp2.entity.chat.group.GroupChat;
import com.example.novapp2.service.GroupChatsService;
import com.example.novapp2.service.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {

    private static User activeUser;

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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Task<User> activeUserTask = UserService.getUserById(mAuth.getCurrentUser().getUid());
            activeUserTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    activeUser = task.getResult();
                    if(activeUser.getGroupChats() == null) {
                        activeUser.setGroupChats(new ArrayList<>());
                    }

                    if (!userFullyRegistered(activeUser)){
                        Bundle args = new Bundle();
                        args.putString("userId", activeUser.getID());
                        args.putString("userEmail", activeUser.getEmail());


                        NavController mainNavController = MainActivity.getNavController();
                        mainNavController.navigate(R.id.action_home_to_fullRegister, args);
                    }
                } else {
                    Snackbar.make(view, R.string.login_fail, Snackbar.LENGTH_SHORT).show();
                }
            });

        } else {
            MainActivity.getNavController().navigate(R.id.action_home_to_login);
        }
}


    private boolean userFullyRegistered(User user) {
        return !user.name.equals("") && !user.surname.equals("") && !user.bio.equals("");
    }

    public static User getActiveUser(){
        return activeUser;
    }

}