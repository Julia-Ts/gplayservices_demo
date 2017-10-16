package com.yschool.gplayservices.base;

import android.support.annotation.NonNull;

import com.yschool.gplayservices.api.ApiManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseMvpPresenterImpl<V extends BaseView> implements BasePresenter<V> {

    protected V view;
    protected ApiManager apiManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * Attach view to presenter, also here we have subscription
     * for destroy event. On destroy event we should detach view
     * and destroy presenter
     *
     * @param view extend BaseMvpView
     */
    @Override
    public void attachView(@NonNull V view) {
        this.view = view;
        apiManager = ApiManager.getInstance();
    }

    /**
     * This method adds given rx subscription to the [.compositeDisposable]
     * which is unsubscribed [.detachView]
     *
     * @param subscription - rx subscription that must be unsubscribed [.detachView]
     */
    protected void addDisposable(@NonNull Disposable subscription) {
        compositeDisposable.add(subscription);
    }

    /**
     * Here we are detaching view and removing and
     * unsubscribing all subscriptions
     */
    @Override
    public void detachView() {
        compositeDisposable.dispose();
        view = null;
    }

}