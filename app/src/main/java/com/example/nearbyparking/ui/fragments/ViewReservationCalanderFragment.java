package com.example.nearbyparking.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.nearbyparking.R;

import java.util.GregorianCalendar;
import java.util.Objects;

import Helpers.Utilities;
import database.entities.CarUser;
import database.entities.Parking;

import static Helpers.Utilities.PREF_USER_TYPE_KEY;


public class ViewReservationCalanderFragment extends Fragment {
    private CalendarView calendarView;
    private Context context;
    private CarUser user;
    private Parking parking;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_reservation_calander, container, false);
        context = getContext();
        calendarView = view.findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
                long millis = cal.getTimeInMillis();
                showDialog(millis);

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    void showDialog(Long millis) {

        Boolean isOwner = Utilities.getBooleanFromSharedPrefs(PREF_USER_TYPE_KEY, context);
        DialogFragment newFragment = null;
        if (isOwner) {
            parking = (Parking) Utilities.getCurrentUserFromSharedPrefs(isOwner, context);
            newFragment = ViewReservationsDialogFragment.newInstance(isOwner, parking, millis);

        } else {
            user = (CarUser) Utilities.getCurrentUserFromSharedPrefs(isOwner, context);
            newFragment = ViewReservationsDialogFragment.newInstance(isOwner, user, millis);

        }

//        Gson gson = new Gson();
//        String objStr = gson.toJson(user);


        Objects.requireNonNull(newFragment).show(getFragmentManager(), "dialog");
    }
}
