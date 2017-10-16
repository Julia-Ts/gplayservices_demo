package com.yschool.gplayservices.interfaces;

import android.support.annotation.StringRes;

/**
 * Created by Dmitriy-Dovbnya on 02.09.2015.
 */
public interface BaseActivityCallback {

    void showError(@StringRes int strResId);

    void showError(String message);

    void hideKeyboard();

    void showMessage(String message);

    void showMessage(@StringRes int srtResId);

    void showProgress();

    void hideProgress();

}
