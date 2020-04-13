package com.example.nearbyparking.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.nearbyparking.R;
import com.google.gson.Gson;

import Helpers.Constants;
import database.DatabaseHelper;
import database.entities.Parking;


public class ReserveFragment extends Fragment {
    private CalendarView calendarView;
    private Context context;
    private Spinner timeSpinner;
    private static final String ARG_PARAM1 = "parking";
    private Parking parking;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private DatabaseHelper databaseHelper;

    private Gson gson;

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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve, container, false);
        context = getContext();
        gson = new Gson();
        parking = gson.fromJson(mParam1, Parking.class);


        // get time from database



        timeSpinner = view.findViewById(R.id.times_spinner);
        calendarView = view.findViewById(R.id.calendar);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Constants.times);
        timeSpinner.setAdapter(adapter);
        timeSpinner.getSelectedItem().toString();


        return view;
    }
}
