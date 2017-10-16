package com.yschool.gplayservices.base;

interface BasePresenter<V> {

    void attachView(V view);

    void detachView();

}
