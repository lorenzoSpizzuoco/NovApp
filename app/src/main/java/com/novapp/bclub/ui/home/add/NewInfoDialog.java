package com.novapp.bclub.ui.home.add;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.novapp.bclub.R;
import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.service.nativeapi.UserService;
import com.novapp.bclub.utils.Constants;
import com.novapp.bclub.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class NewInfoDialog extends DialogFragment {

    private final UserService userService = new UserService();

    private TextInputEditText eventDateTextInner;

    private TextInputEditText infoTitle;
    private TextInputEditText infoPlace;
    private TextInputEditText infoDesc;


    public NewInfoDialog() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_NovApp2_Slide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_info_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        infoTitle = view.findViewById(R.id.new_info_title_inner);
        infoDesc = view.findViewById(R.id.new_info_desc_inner);
        infoPlace = view.findViewById(R.id.new_info_place_inner);
        Button saveEvent = view.findViewById(R.id.save_button_info);
        Toolbar toolbar = view.findViewById(R.id.toolbar_info);
        eventDateTextInner = view.findViewById(R.id.date_input_text_inner_info);
        eventDateTextInner.setInputType(InputType.TYPE_NULL);

        InputFilter[] filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_SMALL_TEXT, requireView(), requireContext());
        infoTitle.setFilters(filters);
        infoPlace.setFilters(filters);
        filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_LONG_TEXT, requireView(), requireContext());
        infoDesc.setFilters(filters);

        eventDateTextInner.setOnFocusChangeListener((v, sel) -> {

            if(v.getId() == R.id.date_input_text_inner_info  && sel) {

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now());

                MaterialDatePicker<Long> dp = MaterialDatePicker.Builder.datePicker()
                        .setTitleText(getString(R.string.event_data_pick))
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();

                dp.show(getChildFragmentManager(), getString(R.string.tag));

                dp.addOnPositiveButtonClickListener(selection -> {
                    String date = new SimpleDateFormat(Constants.DATA_PATTERN, Locale.getDefault()).format(new Date(selection));
                    eventDateTextInner.setText(date);
                });
            }
        });

        toolbar.setNavigationOnClickListener(v -> dismiss());

        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });

        saveEvent.setOnClickListener(v -> checkModal());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.Theme_NovApp2_Slide);
        }
    }

    private void checkModal() {
        boolean valid = true;

        // checking event modal
        if (Objects.requireNonNull(eventDateTextInner.getText()).toString().compareTo("") == 0) {
            valid = false;
            eventDateTextInner.setError(ContextCompat.getString(requireContext(), R.string.date_error));
        }
        else{
            eventDateTextInner.setError(null);
        }

        if (Objects.requireNonNull(infoTitle.getText()).toString().compareTo("") == 0){
            valid = false;
            infoTitle.setError(ContextCompat.getString(requireContext(), R.string.title_error));
        }
        else{
            infoTitle.setError(null);
        }

        if(Objects.requireNonNull(infoPlace.getText()).toString().compareTo("") == 0) {
            valid = false;
            infoPlace.setError(ContextCompat.getString(requireContext(), R.string.place_error));
        }
        else{
            infoPlace.setError(null);
        }

        if(Objects.requireNonNull(infoDesc.getText()).toString().compareTo("") == 0) {
            valid = false;
            infoDesc.setError(ContextCompat.getString(requireContext(), R.string.desc_error));
        }
        else{
            infoDesc.setError(null);
        }

        if (valid) {
            Bundle b = new Bundle();
            b.putParcelable("post", new Post(
                    infoTitle.getText().toString(),
                    null,
                    userService.getCurrentUser().getEmail(),
                    R.mipmap.ic_launcher,
                    null,
                    infoDesc.getText().toString(),
                    2,
                    eventDateTextInner.getText().toString(),
                    infoPlace.getText().toString(),
                    0 ));


            Navigation.findNavController(requireParentFragment().requireView()).navigate(R.id.action_navigation_add_to_loadingFragment, b);

        }

    }


}




