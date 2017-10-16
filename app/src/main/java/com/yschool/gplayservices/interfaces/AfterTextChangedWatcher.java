package com.yschool.gplayservices.interfaces;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class AfterTextChangedWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //ignore
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //ignore
    }

    public abstract void afterTextChanged(Editable s);

}
