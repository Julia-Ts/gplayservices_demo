package com.yschool.gplayservices.flow.places;

import com.yschool.gplayservices.base.BasePresenter;
import com.yschool.gplayservices.base.BaseView;

import java.util.List;

/**
 * Created by jtsym on 10/16/2017.
 */

public final class PlacesContract {

    public interface Presenter extends BasePresenter<View> {

        void loadCities(String input);

    }

    public interface View extends BaseView {

        void onCitiesLoaded(List<String> cities);

    }

}
