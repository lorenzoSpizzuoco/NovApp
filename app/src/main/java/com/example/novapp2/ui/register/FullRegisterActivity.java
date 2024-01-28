package com.example.novapp2.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.Toast;

import com.example.novapp2.MainActivity;
import com.example.novapp2.R;
import com.example.novapp2.entity.User;
import com.example.novapp2.service.UserService;

public class FullRegisterActivity extends AppCompatActivity {

    private Button end_flow_button;
    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_register);

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
}