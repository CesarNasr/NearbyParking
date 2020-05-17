package com.example.nearbyparking.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nearbyparking.R;
import com.example.nearbyparking.ui.activities.UserHomeActivity;
import com.google.gson.Gson;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import Helpers.Constants;
import database.DatabaseHelper;
import database.entities.CarUser;
import database.entities.Parking;
import database.entities.Reservation;
import models.TimeReservationModel;

public class ReserveFragment extends Fragment {
    private CalendarView calendarView;
    private Context context;
    private Spinner timeSpinner;
    private static final String ARG_PARAM1 = "parking";
    private Parking parking;
    private String mParam1;
    private DatabaseHelper databaseHelper;
    private CarUser carUser;
    private Gson gson;
    private Button reserve;
    private List stringTimes;
    private List timeStamps = new ArrayList<Long>();
    private ArrayAdapter<String> adapter;
private Boolean canPressReserveButton = true;
    private Long onCalendarSelectMillis;

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
        onCalendarSelectMillis = System.currentTimeMillis();

        reserve = view.findViewById(R.id.btn_reserve);

        // get time from database
        getEmptyReservationsData(onCalendarSelectMillis);
        timeSpinner = view.findViewById(R.id.times_spinner);
        calendarView = view.findViewById(R.id.calendar);


        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
                onCalendarSelectMillis = cal.getTimeInMillis();
                getEmptyReservationsData(onCalendarSelectMillis);
            }
        });
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (timeSpinner.getSelectedItem() == null || !canPressReserveButton) {
                    Toast.makeText(context, "Oops! You missed your place for this day !", Toast.LENGTH_LONG).show();
                    return;
                }


                final int selectedItemPosition = timeSpinner.getSelectedItemPosition();

                Reservation reservation = new Reservation();
                reservation.parkingId = parking.id;
                reservation.userId = carUser.id;

                java.sql.Date startReservation = new java.sql.Date((Long) timeStamps.get(selectedItemPosition));
                java.sql.Date endReservation = new java.sql.Date((Long) timeStamps.get(selectedItemPosition) + Constants.millisToAdd);

                reservation.fromTime = startReservation;
                reservation.toTime = endReservation;

                new DatabaseHelper.InsertReservation(reservation, databaseHelper.getReservationDAO(), new DatabaseHelper.ReserveDBListener() {
                    @Override
                    public void onSuccess(Long inserted) {
                        getEmptyReservationsData(onCalendarSelectMillis);
                        Toast.makeText(context, "Reservation Successful", Toast.LENGTH_LONG).show();
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


    private void getEmptyReservationsData(Long millis) {
        new DatabaseHelper.GetAvailableParkingTimes(parking.capacity, carUser.id, parking.id, millis, databaseHelper.getReservationDAO(), new DatabaseHelper.EmptySlotsDBListener() {
            @Override
            public void onSuccess(final List<TimeReservationModel> emptyReservationsTimeOnly) {

                stringTimes = new ArrayList<String>();
                timeStamps = new ArrayList<Long>();
                Long currentTimeStamp = System.currentTimeMillis();
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mma");

                for (int i = 0; i < emptyReservationsTimeOnly.size(); i++) {


                    Long fromMs = emptyReservationsTimeOnly.get(i).getStartTime();
                    Long toMs = emptyReservationsTimeOnly.get(i).getStartTime() + 7140000;

//                    if (currentTimeStamp - fromMs <= 0) {
                        timeStamps.add(fromMs);
                        String fromTime = formatter.format(new Date(fromMs));
                        String toTime = formatter.format(new Date(toMs));
                        stringTimes.add(fromTime + " - " + toTime);
//                    }
                }

                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, stringTimes) {
                    @Override
                    public boolean isEnabled(int position) {

                        if (emptyReservationsTimeOnly.get(position).getCanReserve())
                            return true;

                        return false;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View mView = super.getDropDownView(position, convertView, parent);
                        TextView mTextView = (TextView) mView;

                        if (!emptyReservationsTimeOnly.get(position).getCanReserve()) {
                            mTextView.setTextColor(Color.GRAY);
                        } else {
                            mTextView.setTextColor(Color.BLACK);
                        }
                        return mView;
                    }
                };
                timeSpinner.setAdapter(adapter);
                timeSpinner.setSelection(getFirstSelectableIndex(emptyReservationsTimeOnly));
            }

            @Override
            public void onFailure() {
            }
        }).execute();
    }


    private int getFirstSelectableIndex(List<TimeReservationModel> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCanReserve()) {
                canPressReserveButton = true;

                return i;
            }
        }
        canPressReserveButton = false;
        return -1;
    }
}