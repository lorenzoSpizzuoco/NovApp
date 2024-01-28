package com.example.novapp2.ui.home;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novapp2.R;
import com.example.novapp2.databinding.FragmentHomeBinding;
import com.example.novapp2.ui.ad.Ad;
import com.example.novapp2.ui.ad.AdViewAdapter;
import com.example.novapp2.ui.ad.AdViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AdViewModel adViewModel;

    private List<Ad> ads;


    @Override
    public void onCreate(Bundle SavedInstaceStace) {

        super.onCreate(SavedInstaceStace);

        adViewModel = new ViewModelProvider(this).get(AdViewModel.class);

        ads = new ArrayList<>();

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerViewAd = view.findViewById(R.id.AdRecyclerView);
        FloatingActionButton newAdButton = view.findViewById(R.id.newAdButton);

        int white = ContextCompat.getColor(this.getContext(), android.R.color.background_light);
        newAdButton.setColorFilter(white);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);



        newAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(view, "new ad button", Snackbar.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_newAdFragment);
            }
        });


        AdViewAdapter adRecyclerViewAdapter = new AdViewAdapter(ads,

                // navigation to ad details
                new AdViewAdapter.OnItemClickListener() {
                    @Override
                    public void onAdItemClick(Ad ad) {
                        HomeFragmentDirections.ActionNavigationHomeToAdDetailsFragment action =
                                HomeFragmentDirections.actionNavigationHomeToAdDetailsFragment(ad);
                        Navigation.findNavController(view).navigate(action);
                    }

                });

        recyclerViewAd.setLayoutManager(layoutManager);
        recyclerViewAd.setAdapter(adRecyclerViewAdapter);

        adViewModel.getAllAd().observe(getViewLifecycleOwner(), ads -> {
            this.ads.addAll(ads);
            adRecyclerViewAdapter.notifyItemChanged(0, ads.size());
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}