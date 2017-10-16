package com.yschool.gplayservices.response;

import com.google.gson.annotations.SerializedName;
import com.yschool.gplayservices.model.Place;

import java.util.List;

/**
 * Created by jtsym on 4/16/2017.
 */

public class PlacesResponse {

    public static final String STATUS = "status";
    public static final String PLACES = "predictions";

    @SerializedName("status")
    private String status;

    @SerializedName("predictions")
    private List<Place> places;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

}
