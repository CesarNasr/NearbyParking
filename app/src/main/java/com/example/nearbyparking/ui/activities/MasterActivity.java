package com.example.nearbyparking.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nearbyparking.R;
import com.google.gson.Gson;

import Helpers.Utilities;
import database.entities.CarUser;
import database.entities.Parking;

import static Helpers.Utilities.PREF_USER_TYPE_KEY;

public class MasterActivity extends AppCompatActivity {
    private Button ownerBtn, userBtn;
    private Context context;
    private Object user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        ownerBtn = findViewById(R.id.owner_btn);
        userBtn = findViewById(R.id.user_btn);
        context = this;
        if (isLoggedIn()) {

            Gson gson = new Gson();
            String usrString = gson.toJson(user);

            if (user instanceof CarUser) {
//finish();
                Intent i = new Intent(context, UserHomeActivity.class);
                i.putExtra("user", usrString);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            } else if (user instanceof Parking) {
//                finish();
                Intent i = new Intent(context, ParkingOwnerHomeActivity.class);
                i.putExtra("user", usrString);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
            } else {}
        } else {
            final Intent intent = new Intent(context, LoginActivity.class);

            ownerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("isOwner", true);
                    startActivity(intent);
                }
            });

            userBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("isOwner", false);
                    startActivity(intent);

                }
            });


        }

//        final Intent intent = new Intent(this, UserHomeActivity.class);
//        startActivity(intent);
//        finish();


    }


    private boolean isLoggedIn() {
        user = Utilities.getCurrentUserFromSharedPrefs(Utilities.getBooleanFromSharedPrefs(PREF_USER_TYPE_KEY, context), context);

        if (user == null)
            return false;
        else return true;
//        return Utilities.getBooleanFromSharedPrefs(PREF_USER_TYPE_KEY, context);
    }

//    private Object getLoggedInUser() {
//        if (isLoggedIn())
//            if (user instanceof CarUser)
//                return
//    }
}


//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.8938,33.5018&radius=10000&types=parking&sensor=false&key=AIzaSyBF4Kf_L3gu19X0rdcSyigj1KEDENiVNNY

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.8938,35.5018&radius=1000&types=parking&sensor=false&key=AIzaSyAZevZYKP_nnsPunE5ViboeLNfEJT6Vwy0
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?&location=33.8938,35.5018&radius=1000&types=parking&sensor=false&key=AIzaSyAZevZYKP_nnsPunE5ViboeLNfEJT6Vwy0
