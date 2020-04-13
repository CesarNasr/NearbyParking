package com.example.nearbyparking.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nearbyparking.R;
import com.google.gson.Gson;

import Helpers.Utilities;
import database.entities.CarUser;
import database.entities.Parking;

import static Helpers.Utilities.PREF_USER_TYPE_KEY;

public class ParkingOwnerHomeActivity extends AppCompatActivity {
    Boolean isOwner;
    Parking user = null;
    Context context;
    Gson gson = new Gson();
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_owner_home);
        context = this;
        getUser();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    private void getUser() {
        Intent intent = getIntent();
        String userString = intent.getExtras().getString("user");
        isOwner = Utilities.getBooleanFromSharedPrefs(PREF_USER_TYPE_KEY, context);
        user = gson.fromJson(userString, Parking.class);
        Toast.makeText(context, "Welcome " + user.userName, Toast.LENGTH_LONG).show();
    }

    private void logout() {
        Utilities.saveUserToSharedPref(null, context);
        Intent i = new Intent(context, MasterActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
