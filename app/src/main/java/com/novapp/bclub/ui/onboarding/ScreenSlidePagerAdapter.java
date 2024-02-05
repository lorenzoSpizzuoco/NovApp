package com.novapp.bclub.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.novapp.bclub.ui.onboarding.pages.FirstPageOnboarding;
import com.novapp.bclub.ui.onboarding.pages.SecondPageOnboarding;
import com.novapp.bclub.ui.onboarding.pages.ThirdPageOnboarding;
import com.novapp.bclub.utils.Constants;


public class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    public ScreenSlidePagerAdapter(ViewPagerFragment viewPagerFragment) {
        super(viewPagerFragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new FirstPageOnboarding();
            case 1:
                return new SecondPageOnboarding();
            case 2:
                return new ThirdPageOnboarding();
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getItemCount() {
        return Constants.NUM_PAGES;
    }
}
