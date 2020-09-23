package com.steve.surfacetv.datasource.api.rest;

import com.steve.surfacetv.datasource.api.ro.DramaRoList;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface DramaApi {
    // 會返回一個 call 類別
    @GET("/interview/dramas-sample.json")
    Observable<DramaRoList> getDramas();
}
