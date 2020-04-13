package com.example.nearbyparking.ui.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.nearbyparking.R;

import java.util.List;

import Helpers.Constants;
import database.DatabaseHelper;
import database.entities.Parking;

public class AreaPickerFragment extends Fragment {
    private Spinner areaSpinner;
    private Context context;
    private Button areaPickerBtn;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area_picker, container, false);
        context = getContext();
        areaPickerBtn = view.findViewById(R.id.continue_btn);
        areaSpinner  = view.findViewById(R.id.choose_area_spinner);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Constants.areas);
        areaSpinner.setAdapter(adapter);

        databaseHelper = new DatabaseHelper();


        areaPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startParkingPickerFragment(areaSpinner.getSelectedItem().toString());

            }
        });


        return view;
    }


    private void startParkingPickerFragment(String area) {
        ParkingPickerFragment parkingPickerFragment = new ParkingPickerFragment().newInstance(area);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrame, parkingPickerFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }
}
