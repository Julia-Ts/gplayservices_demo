package com.yschool.gplayservices.api;

import com.yschool.gplayservices.response.PlacesResponse;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by jtsym on 11/4/2016.
 */

public interface ApiService {

    //Google places Web api key
    String API_KEY = "AIzaSyCird2WO6ilUwE5sxky4eUiF0T-MwwhA9U";

    String KEY_PARAM = "key";
    String INPUT_PARAM = "input";

    @GET(ApiSettings.PATH_PLACES)
    Single<PlacesResponse> getPlaces(@QueryMap Map<String, String> parameters);

}
