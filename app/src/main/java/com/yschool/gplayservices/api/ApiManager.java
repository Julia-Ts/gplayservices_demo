package com.yschool.gplayservices.api;

import com.google.gson.GsonBuilder;
import com.yschool.gplayservices.interfaces.Manager;
import com.yschool.gplayservices.response.PlacesResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
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
    private volatile static ApiManager instance;

    private ApiManager() {
        initRetrofit();
        initServices();
    }

    public synchronized static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    private void initRetrofit() {
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
            builder.registerTypeAdapter(Class.forName("com.yschool.gplayservices.response.PlacesResponse"), new PlacesSerializer());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return GsonConverterFactory.create(builder.create());
    }

    private void initServices() {
        apiService = retrofit.create(ApiService.class);
    }

    public Single<PlacesResponse> getPlaces(String input) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiService.KEY_PARAM, ApiService.API_KEY);
        params.put(ApiService.INPUT_PARAM, input);
        params.put("types", "(regions)");
        params.put("language", "en");
        return apiService.getPlaces(params);
    }

    @Override
    public void clear() {
    }

}
