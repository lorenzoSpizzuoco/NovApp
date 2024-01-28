package com.example.novapp2.ui.home.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.novapp2.databinding.AdDetailsFragmentBinding;
import com.example.novapp2.entity.ad.Ad;
import com.example.novapp2.ui.home.feed.AdDetailsFragmentArgs;


public class AdDetailsFragment extends Fragment {

    private final String TAG = AdDetailsFragment.class.getSimpleName();
    private Ad ad;

    private AdDetailsFragmentBinding adDetailsFragmentBinding;
    public AdDetailsFragment() {
        // Required empty public constructor
    }

    public static AdDetailsFragment newInstance() {
        AdDetailsFragment fragment = new AdDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //this.getActivity().setTitle("Ad");
        adDetailsFragmentBinding = AdDetailsFragmentBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return adDetailsFragmentBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstaceState) {

        super.onViewCreated(view, savedInstaceState);


        // setting back button

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                //menu.add("Ad");
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    Navigation.findNavController(requireView()).navigateUp();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);


        // getting clicked ad

        Ad ad = AdDetailsFragmentArgs.fromBundle(getArguments()).getAdObject();

        // setting fragment elements with selected ad data

        adDetailsFragmentBinding.textviewNewsTitle.setText(ad.getTitle());
        adDetailsFragmentBinding.textviewNewsContent.setText(ad.getContent());
        adDetailsFragmentBinding.imageviewAd.setImageResource(ad.getImage());

    }
}