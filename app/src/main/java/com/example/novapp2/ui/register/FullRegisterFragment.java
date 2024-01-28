package com.example.novapp2.ui.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.novapp2.R;


public class FullRegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_register, container, false);
    }

}

/*public class FullRegisterActivity extends AppCompatActivity {

    private Button end_flow_button;
    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        end_flow_button = this.findViewById(R.id.full_register_end_flow_button);
        activeUser = (User) getIntent().getParcelableExtra("activeUser");

        end_flow_button.setOnClickListener(v -> {
            updateUser();
            toMainPage();
        });
    }

    private void toMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateUser() {
        //Toast.makeText(this, activeUser.email, Toast.LENGTH_SHORT).show();
    }
}*/