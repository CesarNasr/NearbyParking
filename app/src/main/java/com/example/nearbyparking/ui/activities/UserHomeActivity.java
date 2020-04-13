package com.example.nearbyparking.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.nearbyparking.R;
import com.example.nearbyparking.ui.fragments.AreaPickerFragment;
import com.example.nearbyparking.ui.fragments.ContactUs;
import com.example.nearbyparking.ui.fragments.LicenseAgreementFrag;
import com.example.nearbyparking.ui.fragments.MyMapFragment;
import com.example.nearbyparking.ui.fragments.ReserveFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.Toast;

import Helpers.Utilities;
import database.entities.CarUser;
import database.entities.Parking;

import static Helpers.Utilities.PREF_USER_TYPE_KEY;

public class UserHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int visibleFragmentNb; // used to know which fragment is visible now
    Boolean isOwner;
    CarUser user = null;
    Context context;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        getUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        startMapsFragment();

    }

    private void getUser() {
        Intent intent = getIntent();
        String userString = intent.getExtras().getString("user");
        isOwner = Utilities.getBooleanFromSharedPrefs(PREF_USER_TYPE_KEY, context);
        user = gson.fromJson(userString, CarUser.class);
        Toast.makeText(context, "Welcome " + user.userName, Toast.LENGTH_LONG).show();
    }

    void startMapsFragment() {
        Fragment fragment = new MyMapFragment();
        if (visibleFragmentNb != 1) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
            visibleFragmentNb = 1;
        }
    }

    void startReserveFragment() {
        Fragment fragment = new AreaPickerFragment();
        if (visibleFragmentNb != 4) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
            visibleFragmentNb = 4;
        }
    }

    void startContactUsFrag() {
        Fragment fragment = new ContactUs();

        if (visibleFragmentNb != 2) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();

            visibleFragmentNb = 2;
        }
    }

    void startLicenseAgreementFrag() {
        Fragment fragment = new LicenseAgreementFrag();

        if (visibleFragmentNb != 3) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
            visibleFragmentNb = 3;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;


        if (id == R.id.nav_home) {
            startMapsFragment();

            // Handle the camera action
        } else if (id == R.id.nav_contact_us) {
            startContactUsFrag();
        } else if (id == R.id.nav_license_agreement) {
            startLicenseAgreementFrag();
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_reserve) {
            startReserveFragment();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment frg = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        if (frg != null) {
            frg.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void logout() {
        Utilities.saveUserToSharedPref(null, context);
        Intent i = new Intent(context, MasterActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);
    }
}
