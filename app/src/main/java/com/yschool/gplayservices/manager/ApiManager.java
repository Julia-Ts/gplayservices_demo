package com.yschool.gplayservices.manager;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.yschool.gplayservices.interfaces.Manager;

import java.io.IOException;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager implements Manager {

    private Retrofit retrofit;
    private ApiService apiService;

    @Override
    public void init(Context context) {
        initRetrofit(context);
        initServices();
    }

    private void initRetrofit(Context context) {
        //Interceptor for logging
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        okhttp3.Request original = chain.request();
                        okhttp3.Request.Builder requestBuilder = original.newBuilder()
                                .method(original.method(), original.body());

                        return chain.proceed(requestBuilder.build());
                    }
                })
                .addInterceptor(logInterceptor)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(ApiSettings.BASE_URL)
                .addConverterFactory(createGsonConverter())
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        try {
            builder.registerTypeAdapter(Class.forName("com.jt.maps.model.PlacesResponse"), new PlacesSerializer());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return GsonConverterFactory.create(builder.create());
    }

    private void initServices() {
        apiService = retrofit.create(ApiService.class);
    }

    public Single<PlacesResponse> getPlaces(Map<String, String> params) {
        return apiService.getPlaces(params);
    }

    @Override
    public void clear() {
    }

}
