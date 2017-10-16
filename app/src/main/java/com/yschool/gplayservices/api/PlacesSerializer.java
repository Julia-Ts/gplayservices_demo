package com.yschool.gplayservices.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.yschool.gplayservices.model.Place;
import com.yschool.gplayservices.response.PlacesResponse;

import java.lang.reflect.Type;

/**
 * Created by jtsym on 11/4/2016.
 */

public class PlacesSerializer implements JsonSerializer<PlacesResponse> {

    @Override
    public JsonElement serialize(PlacesResponse src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(PlacesResponse.STATUS, src.getStatus());

        JsonArray jsonPlaces = new JsonArray();
        for (Place place : src.getPlaces()) {
            JsonObject jsonPlace = new JsonObject();
            jsonPlace.addProperty(Place.ID, place.getId());
            jsonPlace.addProperty(Place.DESCRIPTION, place.getDescription());
            jsonPlace.addProperty(Place.PLACE_ID, place.getPlaceId());
            jsonPlaces.add(jsonPlace);
        }

        jsonObject.add(PlacesResponse.PLACES, jsonPlaces);

        return jsonObject;
    }

}
