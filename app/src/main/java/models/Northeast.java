package models;

import com.google.gson.annotations.SerializedName;

public class Northeast {
    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return this.lng;
    }
}


