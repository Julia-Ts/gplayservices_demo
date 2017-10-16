package com.yschool.gplayservices.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jtsym on 4/16/2017.
 */

public class Place {

    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String PLACE_ID = "place_id";

    @SerializedName("id")
    private String id;

    @SerializedName("description")
    private String description;

    @SerializedName("types")
    private List<String> types;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("structured_formatting")
    private Formatting formatting;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Formatting getFormatting() {
        return formatting;
    }

    public void setFormatting(Formatting formatting) {
        this.formatting = formatting;
    }

}
