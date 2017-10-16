package com.yschool.gplayservices.flow.places;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.yschool.gplayservices.R;
import com.yschool.gplayservices.base.BaseMvpActivity;
import com.yschool.gplayservices.interfaces.AfterTextChangedWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jtsym on 10/16/2017.
 */

public class PlacesActivity extends BaseMvpActivity<PlacesContract.Presenter> implements PlacesContract.View {

    @BindView(R.id.placesInput)
    AutoCompleteTextView placesInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesInput.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.loadPlaces(s.toString());
            }
        });

        placesInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                placesInput.dismissDropDown();
            }
        });
    }

    @NonNull
    @Override
    protected PlacesContract.Presenter getPresenterInstance() {
        return new PlacesPresenter();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_places;
    }

    @Override
    public void onPlacesLoaded(List<String> places) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, places);
        placesInput.setAdapter(adapter);
        placesInput.showDropDown();
    }

}
