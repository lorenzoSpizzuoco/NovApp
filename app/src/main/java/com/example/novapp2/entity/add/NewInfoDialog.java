package com.example.novapp2.entity.add;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.novapp2.R;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.service.UserService;
import com.example.novapp2.ui.home.HomeFragment;
import com.example.novapp2.utils.Constants;
import com.example.novapp2.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NewInfoDialog extends DialogFragment {

    private static final String TAG = NewEventDialog.class.getSimpleName();
    private Toolbar toolbar;
    private DatePicker eventDatePicker;

    private final UserService userService = new UserService();

    private TextInputLayout eventDateText;

    private ImageView eventImage;

    private Bitmap eventPhoto;

    private TextInputEditText eventDateTextInner;

    private MaterialButton photoButton;

    private FloatingActionButton delPhoto;

    private Button saveEvent;

    private TextInputEditText infoTitle;
    private TextInputEditText infoPlace;
    private TextInputEditText infoDesc;



    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public NewInfoDialog() {
        // Required empty public constructor
    }



    public static NewInfoDialog newInstance(String param1, String param2) {
        NewInfoDialog fragment = new NewInfoDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        infoTitle = view.findViewById(R.id.new_info_title_inner);
        infoDesc = view.findViewById(R.id.new_info_desc_inner);
        infoPlace = view.findViewById(R.id.new_info_place_inner);
        saveEvent = view.findViewById(R.id.save_button_info);
        toolbar = view.findViewById(R.id.toolbar_info);
        eventDateText = view.findViewById(R.id.date_picker_input_text_info);
        eventDateTextInner = view.findViewById(R.id.date_input_text_inner_info);
        eventDateTextInner.setInputType(InputType.TYPE_NULL);

        InputFilter[] filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_SMALL_TEXT, requireView(), requireContext());
        infoTitle.setFilters(filters);
        infoPlace.setFilters(filters);
        filters = Utils.setMaxCharFilter(Constants.MAX_NUM_CHAR_LONG_TEXT, requireView(), requireContext());
        infoDesc.setFilters(filters);

        eventDateTextInner.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean sel) {

                if(v.getId() == R.id.date_input_text_inner_info  && sel) {

                    CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                            .setValidator(DateValidatorPointForward.now());

                    MaterialDatePicker<Long> dp = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Seleziona la data dell'evento")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .setCalendarConstraints(constraintsBuilder.build())
                            .build();

                    dp.show(getChildFragmentManager(), "TAG");

                    dp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @Override
                        public void onPositiveButtonClick(Long selection) {
                            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(selection));
                            eventDateTextInner.setText(date);
                        }
                    });
                }
            }
        });

        toolbar.setNavigationOnClickListener(v -> dismiss());

        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });

        saveEvent.setOnClickListener(v -> {
            checkModal();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.Theme_NovApp2_Slide);
        }
    }

    private void checkModal() {
        boolean valid = true;

        // checking event modal
        if (eventDateTextInner.getText().toString().compareTo("") == 0) {
            valid = false;
            eventDateTextInner.setError(ContextCompat.getString(getContext(), R.string.date_error));
        }
        else{
            eventDateTextInner.setError(null);
        }

        if (infoTitle.getText().toString().compareTo("") == 0){
            valid = false;
            infoTitle.setError(ContextCompat.getString(getContext(), R.string.title_error));
        }
        else{
            infoTitle.setError(null);
        }

        if(infoPlace.getText().toString().compareTo("") == 0) {
            valid = false;
            infoPlace.setError(ContextCompat.getString(getContext(), R.string.place_error));
        }
        else{
            infoPlace.setError(null);
        }

        if(infoDesc.getText().toString().compareTo("") == 0) {
            valid = false;
            infoDesc.setError(ContextCompat.getString(getContext(), R.string.desc_error));
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
                    R.drawable.analisi,
                    null,
                    infoDesc.getText().toString(),
                    2,
                    eventDateTextInner.getText().toString(),
                    infoPlace.getText().toString(),
                    0 ));


            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_navigation_add_to_loadingFragment, b);

        }

    }


}




