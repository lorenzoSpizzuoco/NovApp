package com.example.novapp2.ui.carousel;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import com.example.novapp2.R;
import com.example.novapp2.ui.login.LoginActivity;
import com.example.novapp2.ui.login.LoginFragment;

public class CarouselActivity extends AppCompatActivity {

    private ImageView precButton;
    private ImageView succButton;
    private Button loginButton;
    // 0 -> start, 1 -> middle, 2 -> end
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        // hiding action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        precButton = this.findViewById(R.id.prec_button);
        succButton = this.findViewById(R.id.succ_button);
        loginButton = this.findViewById(R.id.login_button);

        loginButton.setVisibility(View.GONE);


        if (savedInstanceState == null) {
            replaceFragment(FirstPageCarouselFragment.class);
            state = 0;
            // Nascondi bottone
            precButton.setVisibility(View.GONE);
        }

        succButton.setOnClickListener(v -> {
            switch(state){
                case 0:
                    replaceFragment(SecondPageCarouselFragment.class);
                    state += 1;
                    precButton.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    replaceFragment(ThirdPageCarouselFragment.class);
                    state += 1;
                    succButton.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    break;
            }
        });

        precButton.setOnClickListener(v -> {
            switch(state){
                case 0:
                    break;
                case 1:
                    replaceFragment(FirstPageCarouselFragment.class);
                    state -= 1;
                    precButton.setVisibility(View.GONE);
                    break;
                case 2:
                    replaceFragment(SecondPageCarouselFragment.class);
                    state -= 1;
                    succButton.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE);
                    break;
            }
        });

        loginButton.setOnClickListener(v -> toLoginPage());
    }

    private void replaceFragment(Class fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment, null);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    private void toLoginPage(){
        Intent intent = new Intent(CarouselActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}