package com.steve.surfacetv.datasource;

public interface MediaDataCallback<T> {
    void onDataCallback(T result);
    void onError(Throwable t);
}
