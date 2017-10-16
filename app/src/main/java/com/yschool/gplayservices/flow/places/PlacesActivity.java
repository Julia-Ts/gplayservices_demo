package com.yschool.gplayservices.flow.places;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
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

    @BindView(R.id.cityInput)
    AutoCompleteTextView cityInput;

    private List<String> suggestionList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        cityInput.setThreshold(5);
        cityInput.addTextChangedListener(new AfterTextChangedWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                presenter.loadCities(s.toString());
            }

        });

        cityInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cityInput.dismissDropDown();
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestionList);
        adapter.setNotifyOnChange(true);
        cityInput.setAdapter(adapter);
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
    public void onCitiesLoaded(List<String> cities) {
        adapter.clear();
        adapter.addAll(cities);
        cityInput.showDropDown();
    }

}
