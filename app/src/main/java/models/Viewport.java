package models;

import com.google.gson.annotations.SerializedName;

public class Viewport {

    @SerializedName("lat")
    private Northeast northeast;

    @SerializedName("northeast")
    private Southwest southwest;

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Northeast getNortheast() {
        return this.northeast;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

    public Southwest getSouthwest() {
        return this.southwest;
    }
}

