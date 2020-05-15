package com.example.nearbyparking.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nearbyparking.R;

import java.util.List;

import database.DatabaseHelper;
import database.entities.Parking;


public class ParkingPickerFragment extends Fragment {
    private static final String ARG_PARAM1 = "area";

    // TODO: Rename and change types of parameters
    private String area;
    private DatabaseHelper databaseHelper;
    private RecyclerView parkingRecyclerView;
    private ParkingRecyclerViewAdapter adapter;
    private Context context;
    private List<Parking> parkingList;
    private ImageView emptyPlaceholder;

    public ParkingPickerFragment() {
        // Required empty public constructor
    }


    public static ParkingPickerFragment newInstance(String area) {
        ParkingPickerFragment fragment = new ParkingPickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, area);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            area = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_parking_picker, container, false);
        parkingRecyclerView = view.findViewById(R.id.rvParkings);
        emptyPlaceholder = view.findViewById(R.id.no_parking_placeholder);
        context = getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(parkingRecyclerView.getContext(), linearLayoutManager.getOrientation());

        parkingRecyclerView.addItemDecoration(dividerItemDecoration);
        databaseHelper = new DatabaseHelper();
        parkingRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ParkingRecyclerViewAdapter(getContext());
        parkingRecyclerView.setAdapter(adapter);


        new DatabaseHelper.GetParkingsByArea(area, databaseHelper.getParkingDAO(), new DatabaseHelper.ParkingsDBListener() {
            @Override
            public void onSuccess(final List<Parking> parkings) {

                if (parkings.size() == 0)
                    emptyPlaceholder.setVisibility(View.VISIBLE);

                parkingList = parkings;
                adapter.setList(parkings);
            }

            @Override
            public void onFailure() {

            }
        }).execute();

        adapter.setClickListener(new ParkingRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (parkingList != null)
                    startTimePickerFragment(parkingList.get(position));
            }
        });

        return view;
    }

    private void startTimePickerFragment(Parking parking) {
        ReserveFragment reserveFragment = new ReserveFragment().newInstance(parking);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.mainFrame, reserveFragment, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }
}
