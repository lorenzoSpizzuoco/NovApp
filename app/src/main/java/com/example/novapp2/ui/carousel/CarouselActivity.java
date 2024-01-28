package com.example.novapp2.ui.carousel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.novapp2.R;
import com.example.novapp2.ui.onboarding.pages.FirstPageOnboarding;
import com.example.novapp2.ui.onboarding.pages.SecondPageOnboarding;
import com.example.novapp2.ui.onboarding.pages.ThirdPageOnboarding;

// TODO rimuovere questa classe

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
            replaceFragment(FirstPageOnboarding.class);
            state = 0;
            // Nascondi bottone
            precButton.setVisibility(View.GONE);
        }

        succButton.setOnClickListener(v -> {
            switch(state){
                case 0:
                    replaceFragment(SecondPageOnboarding.class);
                    state += 1;
                    precButton.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    replaceFragment(ThirdPageOnboarding.class);
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
                    replaceFragment(FirstPageOnboarding.class);
                    state -= 1;
                    precButton.setVisibility(View.GONE);
                    break;
                case 2:
                    replaceFragment(SecondPageOnboarding.class);
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
        //Intent intent = new Intent(CarouselActivity.this, LoginActivity.class);
        //startActivity(intent);
    }

}