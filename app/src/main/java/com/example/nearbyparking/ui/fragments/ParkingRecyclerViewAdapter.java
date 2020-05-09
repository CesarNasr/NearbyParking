package com.example.nearbyparking.ui.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyparking.R;

import java.util.ArrayList;
import java.util.List;

import database.entities.Parking;

public class ParkingRecyclerViewAdapter extends RecyclerView.Adapter<ParkingRecyclerViewAdapter.ViewHolder> {

    private List<Parking> parkingList = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    ParkingRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.parking_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = parkingList.get(position).parkingName;
        String cap = String.valueOf(parkingList.get(position).capacity);
        holder.parkingName.setText(name);
        holder.parkingCapacity.setText("Capacity / Session: " + cap);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return parkingList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView parkingName;
        TextView parkingCapacity;
        LinearLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            parkingName = itemView.findViewById(R.id.parking_name);
            parkingCapacity = itemView.findViewById(R.id.parking_capacity);
            parentLayout = itemView.findViewById(R.id.item_parent);


            parentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Parking getItem(int id) {
        return parkingList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    void setList(List<Parking> parkings) {
        this.parkingList = parkings;
        notifyDataSetChanged();
    }
}