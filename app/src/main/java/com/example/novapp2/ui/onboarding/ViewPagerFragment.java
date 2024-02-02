package com.example.novapp2.ui.onboarding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.utils.Constants;


public class ViewPagerFragment extends Fragment {

    private ViewPager2 viewPager;
    private Button loginButton;

    private static final float NEUTRAL_HEIGHT = 8f;
    private static final float ACTIVE_HEIGHT = 15f;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.pager);
        loginButton = view.findViewById(R.id.login_button);
        loginButton.setVisibility(View.GONE);
        Button nextScreenButton = view.findViewById(R.id.next_button);
        //nextScreenButton.setVisibility(View.GONE);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                togglePointer(position);
                if (position == Constants.NUM_PAGES-1) {
                    toggleVisibility(View.VISIBLE);
                } else {
                    toggleVisibility(View.GONE);
                }
            }
        });

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        loginButton.setOnClickListener(v -> MainActivity.getNavController().navigate(R.id.action_onboarding_to_login));
        nextScreenButton.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        });
    }

    private void togglePointer(int position) {
        for (int i = 0; i < Constants.NUM_PAGES; i++) {
            int id = 0;  // Move id inside the loop to get the correct id for each iteration
            switch (i) {
                case 0:
                    id = R.id.pointer0;
                    break;
                case 1:
                    id = R.id.pointer1;
                    break;
                case 2:
                    id = R.id.pointer2;
                    break;
            }

            if (i == position) {
                toggleActivePointer(requireView().findViewById(id));
            } else {
                toggleNeutralPointer(requireView().findViewById(id));
            }
        }
    }

    private void toggleNeutralPointer(View pointer) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        int newHeightInPixels = (int) (NEUTRAL_HEIGHT * density);
        ViewGroup.LayoutParams layoutParams = pointer.getLayoutParams();
        layoutParams.height = newHeightInPixels;
        pointer.setLayoutParams(layoutParams);
        pointer.setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_secondary));
    }

    private void toggleActivePointer(View pointer) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        int newHeightInPixels = (int) (ACTIVE_HEIGHT * density);
        ViewGroup.LayoutParams layoutParams = pointer.getLayoutParams();
        layoutParams.height = newHeightInPixels;
        pointer.setLayoutParams(layoutParams);
        pointer.setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_primary));
    }

    private void toggleVisibility(int status) {
        float from, to;
        if (status == View.VISIBLE) {
            from = 0f;
            to = 1f;
        } else {
            from = 1f;
            to = 0f;
        }
        ObjectAnimator fade = ObjectAnimator.ofFloat(loginButton, "alpha", from, to);
        fade.setDuration(200);
        fade.start();

        if (status == View.GONE) {
            fade.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    // Set the visibility after the animation ends
                    loginButton.setVisibility(status);
                }
            });
        } else {
            loginButton.setVisibility(status);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });
    }

}