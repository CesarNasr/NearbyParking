package com.example.nearbyparking.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.nearbyparking.R;
import com.example.nearbyparking.ui.fragments.ViewReservationCalanderFragment;

public class DetailedReservationsAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_reservations_acitivity);
        startMyReservationsFragment();
    }

    void startMyReservationsFragment() {
        Fragment fragment = new ViewReservationCalanderFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, fragment);
        ft.commit();
    }
}
