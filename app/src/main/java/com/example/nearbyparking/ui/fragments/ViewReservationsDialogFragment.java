package com.example.nearbyparking.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nearbyparking.R;
import com.google.gson.Gson;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import database.DatabaseHelper;
import database.entities.CarUser;
import database.entities.Parking;
import database.entities.Reservation;


public class ViewReservationsDialogFragment extends DialogFragment {

    private Boolean isOwner;
    private CarUser user;
    private Parking parkingOwner;
    private RecyclerView reservationsRecyclerview;
    private ViewReservationsAdapter rViewAdapter;
    private ImageView noResults;
    private static final String USER_TYPE = "type";
    private static final String USER_OBJECT = "object";
    private static final String TIME_MILLIS = "millis";
    private Long timeMillis;
    private DatabaseHelper databaseHelper;
    private Context context;
    // TODO: Rename and change types of parameters


    public ViewReservationsDialogFragment() {
        // Required empty public constructor
    }


    public static ViewReservationsDialogFragment newInstance(Boolean type, Object obj, Long millis) {
        ViewReservationsDialogFragment fragment = new ViewReservationsDialogFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String usrString = null;

        if (obj instanceof CarUser) {
            CarUser user = (CarUser) obj;
            usrString = gson.toJson(user);
        } else {
            Parking parking = (Parking) obj;
            usrString = gson.toJson(parking);
        }

        args.putBoolean(USER_TYPE, type);
        args.putString(USER_OBJECT, usrString);
        args.putLong(TIME_MILLIS, millis);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();

        if (getArguments() != null) {
            isOwner = getArguments().getBoolean(USER_TYPE);
            timeMillis = getArguments().getLong(TIME_MILLIS);
            String usrObj = getArguments().getString(USER_OBJECT);

            if (isOwner) {
                parkingOwner = gson.fromJson(usrObj, Parking.class);
            } else {
                user = gson.fromJson(usrObj, CarUser.class);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_reservations, container, false);
        // Inflate the layout for this fragment
        reservationsRecyclerview = view.findViewById(R.id.reservations_recyclerview);
        noResults = view.findViewById(R.id.no_results_placeholder);

        context = getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(reservationsRecyclerview.getContext(), linearLayoutManager.getOrientation());
        reservationsRecyclerview.addItemDecoration(dividerItemDecoration);
        databaseHelper = new DatabaseHelper();
        reservationsRecyclerview.setLayoutManager(linearLayoutManager);
        rViewAdapter = new ViewReservationsAdapter(getContext());

        reservationsRecyclerview.setAdapter(rViewAdapter);

        getResults();
        return view;
    }


    private void getResults() {
        java.sql.Date startDate = null;
        java.sql.Date endDate = null;
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String dateString = formatterDate.format(new Date(timeMillis));
        java.util.Date startDay = null;

        try {
            startDay = formatterDateTime.parse(dateString + " 00:00:00.0");
            java.util.Date endDay = formatterDateTime.parse(dateString + " 23:59:59");
            startDate = new java.sql.Date(startDay.getTime());
            endDate = new java.sql.Date(endDay.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (!isOwner) {
            new DatabaseHelper.GetReservationPerUser(user.id, startDate, endDate, databaseHelper.getReservationDAO(), new DatabaseHelper.ReservationsPerParkingDBListener() {
                @Override
                public void onSuccess(List<Reservation> reservations) {
                    rViewAdapter.setList(reservations, isOwner);
                    if (reservations.size() == 0)
                        noResults.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure() {

                }
            }).execute();
        } else {


            new DatabaseHelper.GetReservedByParkings(parkingOwner.id, startDate, endDate, databaseHelper.getReservationDAO(), new DatabaseHelper.ReservationsPerParkingDBListener() {
                @Override
                public void onSuccess(List<Reservation> reservations) {
                    rViewAdapter.setList(reservations, isOwner);
                    if (reservations.size() == 0)
                        noResults.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure() {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }).execute();
        }

    }
}
