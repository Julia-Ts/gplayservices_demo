package com.yschool.gplayservices.flow.places;

import com.yschool.gplayservices.base.BaseMvpPresenterImpl;
import com.yschool.gplayservices.model.Place;
import com.yschool.gplayservices.response.PlacesResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jtsym on 10/16/2017.
 */

public class PlacesPresenter extends BaseMvpPresenterImpl<PlacesContract.View> implements PlacesContract.Presenter {

    private static final String CITY = "locality";

    @Override
    public void loadCities(String input) {
        view.showProgress();
        addDisposable(apiManager.getPlaces(input)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlacesResponse>() {
                    @Override
                    public void accept(PlacesResponse placesResponse) throws Exception {
                        view.showProgress();
                        if (placesResponse.getStatus().equals("OK")) {
                            List<Place> places = placesResponse.getPlaces();
                            List<String> cities = new ArrayList<>();
                            for (Place place : places) {
                                String city = extractCity(place);
                                if (city != null) {
                                    cities.add(city);
                                }
                            }
                            view.onCitiesLoaded(cities);
                        } else {
                            view.showMessage("Failed to load places, error: " + placesResponse.getStatus());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.hideProgress();
                        view.showMessage("Error loading places");
                    }
                }));
    }

    private String extractCity(Place place) {
        return isTypeMatched(place, CITY) ? place.getFormatting().getMainText() : null;
    }

    private boolean isTypeMatched(Place place, String type) {
        for (String t : place.getTypes()) {
            if (type.equals(t)) {
                return true;
            }
        }
        return false;
    }

}
