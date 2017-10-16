package com.yschool.gplayservices.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenterInstance();
        presenter.attachView(this);
    }

    @NonNull
    protected abstract T getPresenterInstance();

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
