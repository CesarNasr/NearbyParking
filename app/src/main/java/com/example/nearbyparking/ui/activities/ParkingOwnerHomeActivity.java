package com.example.nearbyparking.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearbyparking.R;
import com.example.nearbyparking.ui.fragments.ViewReservationCalanderFragment;
import com.example.nearbyparking.ui.fragments.ViewReservationsDialogFragment;
import com.google.gson.Gson;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Helpers.Constants;
import Helpers.Utilities;
import database.DatabaseHelper;
import database.entities.CarUser;
import database.entities.Converters;
import database.entities.Parking;
import database.entities.Reservation;

import static Helpers.Utilities.PREF_USER_TYPE_KEY;

public class ParkingOwnerHomeActivity extends AppCompatActivity {
    Boolean isOwner;
    Parking user = null;
    Context context;
    Gson gson = new Gson();
    private ImageView logout;
    private CalendarView calendarView;
    private DatabaseHelper databaseHelper;
    //    LinearLayout linearLayout;
    private Button fetchReservations;
    private TextView parkingName;
    private ImageView imageView;
    Map<Long, Integer> countMap;
    private Spinner timeSpinner;

    private List<Long> timestamps;

    void startDetailedReservationsActivity() {

        Intent i = new Intent(context, DetailedReservationsAcitivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_owner_home);
        context = this;
        timeSpinner = findViewById(R.id.times_spinner);
        fetchReservations = findViewById(R.id.fetch_reservations_btn);
//        linearLayout = findViewById(R.id.reservations_layout);
        imageView = findViewById(R.id.more_details);
        databaseHelper = new DatabaseHelper();
        parkingName = findViewById(R.id.parkingName);
        getUser();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailedReservationsActivity();
            }
        });
        parkingName.setText(user.parkingName + "");

        calendarView = findViewById(R.id.calendar);
//        calendarView.setMaxDate(System.currentTimeMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);
                long millis = cal.getTimeInMillis();
                setTimerSpinner(millis);

//                getReservations(user.id, millis);
            }
        });


        setTimerSpinner(calendarView.getDate());
// get reservations on same day when fragment start
//        getReservations(user.id, System.currentTimeMillis());


        fetchReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(timestamps.get(timeSpinner.getSelectedItemPosition()));

//                getReservations(user.id, timestamps.get(timeSpinner.getSelectedItemPosition()));
            }
        });
    }


    void showDialog(Long hourMillis) {
        DialogFragment newFragment = ViewReservationsDialogFragment.newInstance(hourMillis, true, user, null);


//        Gson gson = new Gson();
//        String objStr = gson.toJson(user);


        Objects.requireNonNull(newFragment).show(getSupportFragmentManager(), "dialog");
    }


    private void setTimerSpinner(Long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date));
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);


        GregorianCalendar newCal = new GregorianCalendar(year, month, day);
        long startMillis = newCal.getTimeInMillis();

        List<String> stringTimes = new ArrayList<>();

        SimpleDateFormat formatterDate = new SimpleDateFormat("hh:mm a");
//        SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");


//        java.util.Date startDay = formatterDateTime.parse(dateString + " 00:00:00.0");
//        java.util.Date endDay = formatterDateTime.parse(dateString + " 23:59:59");
//
//        java.sql.Date startDate = new java.sql.Date(startDay.getTime());
//        java.sql.Date endDate = new java.sql.Date(endDay.getTime());


        Long dateInMs = startMillis;
        timestamps = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            timestamps.add(dateInMs);

            String startDateString = formatterDate.format(new Date(dateInMs));


            String endDateString = formatterDate.format(new Date(dateInMs + Constants.millisToAdd));

            stringTimes.add(startDateString + " - " + endDateString);


            dateInMs = dateInMs + Constants.millisToAdd;

        }


        ArrayAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, stringTimes);
        timeSpinner.setAdapter(adapter);
    }





    private Map<Long, Integer> groupReservations(List<Reservation> reservations) {

        countMap = new HashMap<>();

        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            Long resStartTimeMillis = Converters.fromDate(reservation.fromTime);

            if (countMap.containsKey(resStartTimeMillis))
                countMap.put(resStartTimeMillis, countMap.get(resStartTimeMillis) + 1);
            else
                countMap.put(resStartTimeMillis, 1);
        }


        return countMap;
    }

//    private void updateReservationsUI(List<Reservation> reservations, Long date) {
//
//        groupReservations(reservations);
//
//
//        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
//
//        linearLayout.removeAllViews();
//
//        Iterator it = countMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry) it.next();
//
//            Long fromTimeMillis = (Long) pair.getKey();
//            Long toTimeMillis = (Long) pair.getKey() + Constants.millisToAdd;
//
//
////            if (toTimeMillis > System.currentTimeMillis()) {
//
//
//            String fromTime = formatter.format(new Date(fromTimeMillis));
//            String toTime = formatter.format(new Date(toTimeMillis));
//
//
//            TextView text = new TextView(context);
//            text.setText(fromTime + " - " + toTime + " : " + pair.getValue() + " Slot(s)");
//            text.setTextSize(20);
//            text.setGravity(Gravity.CENTER);
//            linearLayout.addView(text);
//
////            }
////            System.out.println(pair.getKey() + " = " + pair.getValue());
////            it.remove(); // avoids a ConcurrentModificationException
//        }

//
//            TextView text = new TextView(context);
////            text.setText(fromTime + " - " + toTime + " : " + reservation.);
//
////            linearLayout.addView(valueTV);
//        }

//    }


    private void getUser() {
        Intent intent = getIntent();
        String userString = intent.getExtras().getString("user");
        isOwner = Utilities.getBooleanFromSharedPrefs(PREF_USER_TYPE_KEY, context);
        user = gson.fromJson(userString, Parking.class);
        Toast.makeText(context, "Welcome " + user.userName, Toast.LENGTH_LONG).show();
    }

    private void logout() {
        Utilities.saveUserToSharedPref(null, context);
        Intent i = new Intent(context, MasterActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
