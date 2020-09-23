package com.steve.surfacetv.datasource.api;

import com.steve.surfacetv.datasource.api.ro.DramaRoList;
import com.steve.surfacetv.datasource.api.rest.DramaApi;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaApiClient {
    public static final String BASE_URL = "https://static.linetv.tw";
    private static MediaApiClient mediaApiClient;

    private DramaApi dramaApi;

    public static MediaApiClient getInstance() {
        if (mediaApiClient == null)
            mediaApiClient = new MediaApiClient();
        return mediaApiClient;
    }

    public Observable<DramaRoList> getDramaList() {
        return this.dramaApi.getDramas();
    }

    private MediaApiClient() {
        //.addCallAdapterFactory(CoroutineCallAdapterFactory())
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createOkHttpClient())
                //.addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                .build();

        this.dramaApi = retrofit.create(DramaApi.class);
    }

    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }
}
