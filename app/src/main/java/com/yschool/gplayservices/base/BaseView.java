package com.yschool.gplayservices.base;

import android.content.Context;
import android.support.annotation.StringRes;

public interface BaseView {

    Context getContext();

    void showMessage(String message);

    void showMessage(@StringRes int error);

    void showProgress();

    void hideProgress();

}
