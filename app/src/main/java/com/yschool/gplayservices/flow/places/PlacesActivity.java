package com.yschool.gplayservices.flow.places;

import android.support.annotation.NonNull;

import com.yschool.gplayservices.base.BaseMvpActivity;

/**
 * Created by jtsym on 10/16/2017.
 */

public class PlacesActivity extends BaseMvpActivity<PlacesContract.Presenter> implements PlacesContract.View {

    @NonNull
    @Override
    protected PlacesContract.Presenter getPresenterInstance() {
        return new PlacesPresenter();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_places;
    }

}
