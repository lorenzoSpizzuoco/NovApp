package com.example.novapp2.ui.onboarding;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.novapp2.ui.onboarding.pages.FirstPageOnboarding;
import com.example.novapp2.ui.onboarding.pages.SecondPageOnboarding;
import com.example.novapp2.ui.onboarding.pages.ThirdPageOnboarding;
import com.example.novapp2.utils.Constants;


public class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    public ScreenSlidePagerAdapter(ViewPagerFragment viewPagerFragment) {
        super(viewPagerFragment);
    }


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
                return null;
        }
    }

    public int getItemCount() {
        return Constants.NUM_PAGES;
    }
}
