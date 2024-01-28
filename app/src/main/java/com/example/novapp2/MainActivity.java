package com.example.novapp2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.example.novapp2.entity.User;
import com.example.novapp2.service.UserService;
import com.example.novapp2.ui.login.LoginActivity;
import com.example.novapp2.ui.register.FullRegisterActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.novapp2.databinding.ActivityMainBinding;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_chat, R.id.navigation_add, R.id.navigation_notifications, R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);


        mAuth = FirebaseAuth.getInstance();
        /*if (mAuth.getCurrentUser() != null) {
            Task<User> activeUserTask = UserService.getUserById(mAuth.getCurrentUser().getUid());
            activeUserTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    activeUser = task.getResult();

                    if (!userFullyRegistered(activeUser)){
                        Intent intent = new Intent(this, FullRegisterActivity.class);
                        intent.putExtra("activeUser", (Parcelable) activeUser);
                        startActivity(intent);
                    }
                } else {
                    // TODO Handle the error
                }
            });

        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } */


    }

    private boolean userFullyRegistered(User user) {
        // TODO: implementare sta cosa
        if (user.name.equals(""))
            return false;
        else return true;
    }
}