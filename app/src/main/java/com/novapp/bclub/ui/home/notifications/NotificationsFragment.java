package com.novapp.bclub.ui.home.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.novapp.bclub.utils.ESaveStateKey;
import com.novapp.bclub.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        // Recover the instance state.
        if (savedInstanceState != null) {
            String test = savedInstanceState.getString(ESaveStateKey.USER_PROFILE_STATE.getKey());
        }

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Invoked when the activity might be temporarily destroyed; save the instance state here.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ESaveStateKey.USER_PROFILE_STATE.getKey(), "ATTIVO");

        // Call superclass to save any view hierarchy.
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //TODO aggiungere prima della chiusura della view il salvataggio dello stato del bottone
}
