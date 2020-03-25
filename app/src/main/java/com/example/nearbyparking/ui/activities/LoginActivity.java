package com.example.nearbyparking.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearbyparking.R;

import database.DatabaseHelper;
import database.entities.CarUser;
import database.entities.Parking;

public class LoginActivity extends AppCompatActivity {
    private boolean isOwner;
    private Button loginBtn;
    private TextView signupText;
    private EditText username, password;
    private DatabaseHelper databaseHelper;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isOwner = getIntent().getExtras().getBoolean("isOwner");
        loginBtn = findViewById(R.id.loginBtn);
        signupText = findViewById(R.id.registerText);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        databaseHelper = new DatabaseHelper();
        context = this;

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginPressed();
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignupPressed();

            }
        });

    }


    void onLoginPressed() {
        if (isOwner) {
            new DatabaseHelper.LoginParkingOwner(username.getText().toString(), password.getText().toString(), databaseHelper.getParkingDAO(), new DatabaseHelper.ParkingOwnerDBListener() {
                @Override
                public void onSuccess(Parking parkingOwner, long id) {
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure() {


                    Toast.makeText(context, "Wrong Username or Password", Toast.LENGTH_LONG).show();
                }
            }).execute();


        } else {
            new DatabaseHelper.LoginUser(username.getText().toString(), password.getText().toString(), databaseHelper.getCarUserDAO(), new DatabaseHelper.UserDBListener() {


                @Override
                public void onSuccess(CarUser user, long id) {
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure() {
                    Toast.makeText(context, "Wrong Username or Password", Toast.LENGTH_LONG).show();
                }
            }).execute();

        }
    }

    void onSignupPressed() {
        Intent myIntent = new Intent(this, RegisterActivity.class);

        if (isOwner) {
            myIntent.putExtra("isOwner", true); //Optional parameters
        } else {
            myIntent.putExtra("isOwner", false); //Optional parameters
        }
        this.startActivity(myIntent);
    }
}
