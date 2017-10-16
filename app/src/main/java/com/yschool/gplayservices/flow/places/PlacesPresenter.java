package com.yschool.gplayservices.flow.places;

import com.yschool.gplayservices.R;
import com.yschool.gplayservices.base.BaseMvpPresenterImpl;
import com.yschool.gplayservices.model.Place;
import com.yschool.gplayservices.model.Term;
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

    private static final String COUNTRY = "country";
    private static final String CITY = "locality";
    private static final String RESULT_OK = "OK";

    @Override
    public void loadPlaces(String input) {
        addDisposable(apiManager.getPlaces(input)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlacesResponse>() {
                    @Override
                    public void accept(PlacesResponse placesResponse) throws Exception {
                        if (placesResponse.getStatus().equals(RESULT_OK)) {
                            List<Place> places = placesResponse.getPlaces();
                            List<String> results = new ArrayList<>();
                            for (Place place : places) {
                                String countryCity = extractCountryAndCity(place);
                                if (countryCity != null) {
                                    results.add(countryCity);
                                }
                            }
                            view.onPlacesLoaded(results);
                        } else {
                            view.showMessage("Failed to load places, error: " + placesResponse.getStatus());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showMessage("Error loading places");
                    }
                }));
    }

    private String extractCountryAndCity(Place place) {
        String city = extractCity(place);
        String country = extractCountry(place);
        if (city != null && country != null) {
            return String.format(view.getContext().getString(R.string.country_and_city), city, country);
        } else if (country != null) {
            return country;
        } else if (city != null) {
            return city;
        } else {
            return null;
        }
    }

    private String extractCity(Place place) {
        return isTypeMatched(place, CITY) ? place.getFormatting().getMainText() : null;
    }

    private String extractCountry(Place place) {
        if (isTypeMatched(place, COUNTRY)) {
            return place.getFormatting().getMainText();
        }
        if (isTypeMatched(place, CITY)) {
            List<Term> terms = place.getTerms();
            if (terms != null) {
                if (terms.size() == 3) {//In case we have region in terms
                    return terms.get(2).getValue();
                } else if (terms.size() == 2) {//In case we have no region in terms
                    return terms.get(1).getValue();
                } else if (terms.size() == 1) {//In case we have country == city
                    return terms.get(0).getValue();
                }
            }
        }
        return null;
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
