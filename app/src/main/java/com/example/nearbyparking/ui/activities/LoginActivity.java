package com.example.nearbyparking.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nearbyparking.R;

public class LoginActivity extends AppCompatActivity {
    private boolean isOwner;
    private Button loginBtn;
    private TextView signupText;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isOwner = getIntent().getExtras().getBoolean("isOwner");
        loginBtn = findViewById(R.id.loginBtn);
        signupText = findViewById(R.id.registerText);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

    }


    void onLoginPressed() {

    }

    void onSignupPressed() {
        if (isOwner) {

        } else {

        }
    }
}
