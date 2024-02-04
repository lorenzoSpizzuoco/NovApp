package com.novapp.bclub.ui.onboarding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novapp.bclub.MainActivity;
import com.novapp.bclub.R;
import com.novapp.bclub.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ViewPagerFragment extends Fragment {

    private ViewPager2 viewPager;
    private MaterialButton loginButton;

    private static final float NEUTRAL_HEIGHT = 8f;
    private static final float ACTIVE_HEIGHT = 15f;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.pager);
        loginButton = view.findViewById(R.id.login_button);
        loginButton.setVisibility(View.GONE);
        FloatingActionButton nextScreenButton = view.findViewById(R.id.next_button);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                togglePointer(position);
                if (position == Constants.NUM_PAGES-1) {
                    loginButton.setVisibility(View.VISIBLE);
                    //toggleVisibility(View.VISIBLE, loginButton);
                    nextScreenButton.setVisibility(View.GONE);
                } else {
                    //toggleVisibility(View.GONE, loginButton);
                    loginButton.setVisibility(View.GONE);
                    nextScreenButton.setVisibility(View.VISIBLE);
                }
            }
        });

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        loginButton.setOnClickListener(v -> MainActivity.getNavController().navigate(R.id.action_onboarding_to_login));
        nextScreenButton.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true));
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

    private void setPointerSizeAndColor(View pointer, float height, int colorAttrId) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        int newHeightInPixels = (int) (height * density);
        ViewGroup.LayoutParams layoutParams = pointer.getLayoutParams();
        layoutParams.height = newHeightInPixels;
        pointer.setLayoutParams(layoutParams);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = requireContext().getTheme();
        theme.resolveAttribute(colorAttrId, typedValue, true);
        int color = typedValue.data;
        pointer.setBackgroundColor(color);
    }

    private void toggleNeutralPointer(View pointer) {
        setPointerSizeAndColor(pointer, NEUTRAL_HEIGHT, androidx.transition.R.attr.colorAccent);
    }

    private void toggleActivePointer(View pointer) {
        setPointerSizeAndColor(pointer, ACTIVE_HEIGHT, androidx.transition.R.attr.colorPrimary);
    }


    private void toggleVisibility(int status, MaterialButton button) {
        float from, to;
        if (status == View.VISIBLE) {
            from = 0f;
            to = 1f;
        } else {
            from = 1f;
            to = 0f;
        }
        ObjectAnimator fade = ObjectAnimator.ofFloat(button, "alpha", from, to);
        fade.setDuration(100);
        fade.start();

        if (status == View.GONE) {
            fade.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    // Set the visibility after the animation ends
                    button.setVisibility(status);
                }
            });
        } else {
            button.setVisibility(status);
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