package com.example.nearbyparking.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nearbyparking.R;

import models.Root;
import network.GetDataService;
import network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static Helpers.Constants.API_KEY;

public class MasterActivity extends AppCompatActivity {
    private Button ownerBtn, userBtn;
private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        ownerBtn = findViewById(R.id.owner_btn);
        userBtn = findViewById(R.id.user_btn);
        context = this;
        if (isLoggedIn()) {

        } else {

            ownerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("isOwner", true);
                    startActivity(intent);
                }
            });

            userBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("isOwner", false);
                    startActivity(intent);

                }
            });


        }

//        final Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();


    }


    private boolean isLoggedIn() {
        return false;
    }
}


//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.8938,33.5018&radius=10000&types=parking&sensor=false&key=AIzaSyBF4Kf_L3gu19X0rdcSyigj1KEDENiVNNY

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.8938,35.5018&radius=1000&types=parking&sensor=false&key=AIzaSyAZevZYKP_nnsPunE5ViboeLNfEJT6Vwy0
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?&location=33.8938,35.5018&radius=1000&types=parking&sensor=false&key=AIzaSyAZevZYKP_nnsPunE5ViboeLNfEJT6Vwy0
