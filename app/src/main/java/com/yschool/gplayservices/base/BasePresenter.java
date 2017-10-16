package com.yschool.gplayservices.base;

public interface BasePresenter<V> {

    void attachView(V view);

    void detachView();

}
