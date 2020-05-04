package com.example.nearbyparking.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nearbyparking.R;
import com.google.gson.Gson;

import Helpers.Constants;
import Helpers.Utilities;
import database.DatabaseHelper;
import database.entities.CarUser;
import database.entities.Parking;

import static Helpers.Utilities.PREF_USER_TYPE_KEY;

public class RegisterActivity extends AppCompatActivity {
    private boolean isOwner;
    private Context context;
    private EditText parkingName, parkingPassword, parkingUserName, parkingCapacity, parkingAdress, userUsername, userPassword, carPlateNumber, carDescription;
    private LinearLayout parkingLayout, userLayout;
    private Spinner ownerSpinner;
    private Button registerUser, registerOwner;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        parkingLayout = findViewById(R.id.parkingOwnerView);
        userLayout = findViewById(R.id.parkingUserView);
        databaseHelper = new DatabaseHelper();
        context = this;
        isOwner = getIntent().getExtras().getBoolean("isOwner");

        if (isOwner) {
            userLayout.setVisibility(View.GONE);
            parkingLayout.setVisibility(View.VISIBLE);
            setupParkingOwnerView();

        } else {
            parkingLayout.setVisibility(View.GONE);
            userLayout.setVisibility(View.VISIBLE);
            setupUserView();
        }
    }

    private void setupParkingOwnerView() {
        parkingName = findViewById(R.id.parkingName);
        parkingPassword = findViewById(R.id.parkinPassword);
        parkingUserName = findViewById(R.id.username);
        parkingAdress = findViewById(R.id.parkingAddress);
        ownerSpinner = findViewById(R.id.parkingOwnerSpinner);
        registerOwner = findViewById(R.id.registerOwnerBtn);
        parkingCapacity = findViewById(R.id.parkingCapacity);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Constants.areas);
        ownerSpinner.setAdapter(adapter);


        registerOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Parking parkingOwner = new Parking();

                parkingOwner.areaName = ownerSpinner.getSelectedItem().toString();
                parkingOwner.capacity = Integer.parseInt(parkingCapacity.getText().toString());
                parkingOwner.parkingAddress = parkingAdress.getText().toString();
                parkingOwner.password = parkingPassword.getText().toString();
                parkingOwner.userName = parkingUserName.getText().toString();
                parkingOwner.parkingName = parkingName.getText().toString();

                new DatabaseHelper.InsertOwner(parkingOwner, databaseHelper.getParkingDAO(), new DatabaseHelper.ParkingOwnerDBListener() {
                    @Override
                    public void onSuccess(Parking parkingOwner, long id) {
                        saveOwner(parkingOwner);
                        Gson gson = new Gson();
                        String usrString = gson.toJson(parkingOwner);
                        Intent i = new Intent(context, ParkingOwnerHomeActivity.class);
                        i.putExtra("user", usrString);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        finish();

                        startActivity(i);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(context, "Username already exists!", Toast.LENGTH_LONG).show();
                    }
                }).execute();


            }
        });

//        dropdown.getSelectedItem();
    }

    private void setupUserView() {
        userUsername = findViewById(R.id.username_user);
        userPassword = findViewById(R.id.userPassword);
        carDescription = findViewById(R.id.carDescription);
        carPlateNumber = findViewById(R.id.carPlatenumber);
        registerUser = findViewById(R.id.registerUserBtn);


        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CarUser carUser = new CarUser();
                carUser.userName = userUsername.getText().toString();
                carUser.password = userPassword.getText().toString();
                carUser.plateNumber = carPlateNumber.getText().toString();
                carUser.carType = carDescription.getText().toString();

                new DatabaseHelper.InsertUser(carUser, databaseHelper.getCarUserDAO(), new DatabaseHelper.UserDBListener() {
                    @Override
                    public void onSuccess(CarUser user, long id) {
                        saveUser(user);

                        Gson gson = new Gson();
                        String usrString = gson.toJson(user);
                        Intent i = new Intent(context, UserHomeActivity.class);
                        i.putExtra("user", usrString);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(context, "Username already exists!", Toast.LENGTH_LONG).show();
                    }
                }).execute();

            }
        });

    }


    private void saveUser(CarUser object) {
        Utilities.saveUserToSharedPref(object, context);
        Utilities.saveBooleanToSharedPrefs(false, PREF_USER_TYPE_KEY, context);
    }

    private void saveOwner(Parking object) {
        Utilities.saveUserToSharedPref(object, context);
        Utilities.saveBooleanToSharedPrefs(true, PREF_USER_TYPE_KEY, context);
    }
}
