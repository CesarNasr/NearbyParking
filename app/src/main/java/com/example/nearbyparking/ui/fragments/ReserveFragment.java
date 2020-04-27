package com.example.nearbyparking.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nearbyparking.R;
import com.example.nearbyparking.ui.activities.UserHomeActivity;
import com.google.gson.Gson;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseHelper;
import database.entities.CarUser;
import database.entities.Parking;
import database.entities.Reservation;


public class ReserveFragment extends Fragment {
    private CalendarView calendarView;
    private Context context;
    private Spinner timeSpinner;
    private static final String ARG_PARAM1 = "parking";
    private Parking parking;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private DatabaseHelper databaseHelper;
    private CarUser carUser;
    private Gson gson;
    private Button reserve;
    private List stringTimes;
    private List timeStamps = new ArrayList<Long>();
    final long millisToAdd = 7_200_000; //two hours

    public ReserveFragment() {
    }


    public static ReserveFragment newInstance(Parking parking) {
        ReserveFragment fragment = new ReserveFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String usrString = gson.toJson(parking);
        args.putString(ARG_PARAM1, usrString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve, container, false);
        context = getContext();
        carUser = ((UserHomeActivity) getActivity()).user;
        gson = new Gson();
        parking = gson.fromJson(mParam1, Parking.class);

        reserve = view.findViewById(R.id.btn_reserve);


        // get time from database


        timeSpinner = view.findViewById(R.id.times_spinner);
        calendarView = view.findViewById(R.id.calendar);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

//              call  GetAvailableParkingTimes and pass it timestamp

                new DatabaseHelper.GetAvailableParkingTimes(parking.capacity, carUser.id, parking.id, calendarView.getDate(), databaseHelper.getReservationDAO(), new DatabaseHelper.EmptySlotsDBListener() {
                    @Override
                    public void onSuccess(List<Long> emptyReservationsTimeOnly) {

                        stringTimes = new ArrayList<String>();
                        timeStamps = new ArrayList<Long>(emptyReservationsTimeOnly);


                        for (int i = 0; i < emptyReservationsTimeOnly.size(); i++) {
                            Long fromMs = emptyReservationsTimeOnly.get(i);
                            Long toMs = emptyReservationsTimeOnly.get(i) + 7140000;

                            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                            String fromTime = formatter.format(new Date(fromMs));
                            String toTime = formatter.format(new Date(toMs));
                            stringTimes.add(fromTime + " - " + toTime);
//
//                            Reservation reservation = new Reservation();
//                            reservation.fromTime = Date.valueOf(fromTime);
//                            reservation.toTime = Date.valueOf(toTime);
                        }


//                        for (int i = 0; i < emptyReservationsTimeOnly.size(); i++) {
//                            String timePeriod = emptyReservationsTimeOnly.get(i).fromTime.toString() + emptyReservationsTimeOnly.get(i).toTime.toString();
//                            times.add(timePeriod);
//                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, stringTimes);
                        timeSpinner.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure() {

                    }
                }).execute();


            }
        });

//        timeSpinner.getSelectedItem().toString();

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedItemPosition = timeSpinner.getSelectedItemPosition();


                Reservation reservation = new Reservation();
                reservation.parkingId = parking.id;
                reservation.userId = carUser.id;


                java.sql.Date startReservation = new java.sql.Date((Long) timeStamps.get(selectedItemPosition));
                java.sql.Date endReservation = new java.sql.Date((Long) timeStamps.get(selectedItemPosition) + millisToAdd);

                reservation.fromTime = startReservation;
                reservation.toTime = endReservation;

                new DatabaseHelper.InsertReservation(reservation, databaseHelper.getReservationDAO(), new DatabaseHelper.ReserveDBListener() {
                    @Override
                    public void onSuccess(Long inserted) {
                        Toast.makeText(context, "Reservation Successful", Toast.LENGTH_LONG).show();
                        //TODO
                        // should refresh the list
                        // should fix issue related to get time from 12 t0 12 and make it get time from current time + x hours
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_LONG).show();

                    }
                }).execute();

            }
        });

        return view;
    }


//    private List<String> getAvailableTimes() {
//
//    }

}
