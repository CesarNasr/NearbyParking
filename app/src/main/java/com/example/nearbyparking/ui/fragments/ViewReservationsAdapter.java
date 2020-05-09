package com.example.nearbyparking.ui.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyparking.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import database.entities.Parking;
import database.entities.Reservation;

public class ViewReservationsAdapter extends RecyclerView.Adapter<ViewReservationsAdapter.ViewHolder> {

    private List<Reservation> reservationList = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Boolean isOwner;
    private SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

    // data is passed into the constructor
    ViewReservationsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        if (isOwner) {
//
//        } else {
//
//
//        }

        String reservationNumber = "Reservation-ID :" + reservationList.get(position).id;
        String parkingName = reservationList.get(position).parkingOwner.parkingName;
        String userName = reservationList.get(position).carUser.userName;
        String carType = reservationList.get(position).carUser.carType;
        String carPlate = reservationList.get(position).carUser.plateNumber;
        Long reservationTimeInMillis = reservationList.get(position).fromTime.getTime();
        String dateString = formatterDateTime.format(new Date(reservationTimeInMillis));

        holder.parkingName.setText("Venue: " + parkingName);
        holder.userName.setText("Username: " + userName);
        holder.carPlate.setText("Car plate: " + carPlate);
        holder.carName.setText("Car type: " + carType);
        holder.reservationTime.setText(dateString);
        holder.reservationNumber.setText(reservationNumber);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return reservationList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView parkingName, carName, carPlate, reservationTime, userName, reservationNumber;

        LinearLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            parkingName = itemView.findViewById(R.id.parkingNameText);
            carName = itemView.findViewById(R.id.carType);
            carPlate = itemView.findViewById(R.id.carPlate);
            userName = itemView.findViewById(R.id.username);
            reservationTime = itemView.findViewById(R.id.time);
            reservationNumber = itemView.findViewById(R.id.reservationNumber);
            parentLayout = itemView.findViewById(R.id.item_parent);


//            parentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Reservation getItem(int id) {
        return reservationList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    void setList(List<Reservation> reservations, Boolean isOwner) {
        this.reservationList = reservations;
        this.isOwner = isOwner;
        notifyDataSetChanged();
    }
}