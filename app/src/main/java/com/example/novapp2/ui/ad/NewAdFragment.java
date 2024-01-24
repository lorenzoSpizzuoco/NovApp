package com.example.novapp2.ui.ad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.novapp2.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;


public class NewAdFragment extends Fragment {

    private final String TAG = NewAdFragment.class.getSimpleName();

    // new ad text fields
    private TextInputEditText textInputLayoutAdContent;

    private TextInputEditText textInputLayoutAdTitle;

    // view model
    private AdViewModel adViewModel;

    private View loadingView;

    public NewAdFragment() {
        // Required empty public constructor
    }


    public static NewAdFragment newInstance(String param1, String param2) {
        NewAdFragment fragment = new NewAdFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        adViewModel = new ViewModelProvider(this).get(AdViewModel.class);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_ad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstaceState) {

        final Button newAdButton = view.findViewById(R.id.button_add_new_ad);
        textInputLayoutAdContent = view.findViewById(R.id.text_input_ad_content);
        textInputLayoutAdTitle = view.findViewById(R.id.text_input_ad_title);

        newAdButton.setOnClickListener(v -> {


            String content = textInputLayoutAdContent.getText().toString();
            String title = textInputLayoutAdTitle.getText().toString();
            String author = "Lorenzo";


            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String date = sdf.format(new Date());

            // navigating to loading fragment
            Ad newAd = new Ad(author, title, content, R.drawable.analisi, date);
            Bundle b = new Bundle();
            b.putParcelable("adobj", newAd);
            Navigation.findNavController(requireView()).navigate(R.id.action_newAdFragment_to_loadingFragment, b);
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                MenuItem menuItem = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "new ad");
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); // Imposta il modo di visualizzazione

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == android.R.id.home) {
                    Navigation.findNavController(requireView()).navigateUp();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
}