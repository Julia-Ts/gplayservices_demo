package com.yschool.gplayservices;

import android.app.Application;

import com.jt.maps.manager.ApiManager;

import timber.log.Timber;

public class App extends Application {

    public static final ApiManager apiManager = new ApiManager();

    @Override
    public void onCreate() {
        super.onCreate();
        apiManager.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

}
