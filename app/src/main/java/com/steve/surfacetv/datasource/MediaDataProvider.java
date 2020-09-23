package com.steve.surfacetv.datasource;

import android.util.Log;

import com.steve.surfacetv.datasource.api.MediaApiClient;
import com.steve.surfacetv.datasource.api.ro.DramaRo;
import com.steve.surfacetv.datasource.api.ro.DramaRoList;
import com.steve.surfacetv.datasource.converter.DramaConverter;
import com.steve.surfacetv.datasource.db.MediaDatabase;
import com.steve.surfacetv.datasource.db.entity.DramaPo;
import com.steve.surfacetv.datasource.util.ApplicationContextUtil;
import com.steve.surfacetv.datasource.vo.DramaVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MediaDataProvider {
    private static MediaDataProvider instance;

    public static MediaDataProvider getInstance() {
        if (instance == null)
            instance = new MediaDataProvider();
        return instance;
    }

    private MediaDataProvider() {
    }

    public Observable<List<DramaVo>> getObservableForGetDramaListInDb() {
        return MediaDatabase.getInstance(ApplicationContextUtil.getAppContext()).getDramaDao().getAll()
                .map(getFuncForMapDramaPoListToDramaVoList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<DramaVo>> getObservableForGetDramaListFromApi() {
        //如果沒有網路：
        //    - 直接由 DB 取出 List<DramaPo> 後轉為 List<DramaVo>
        if (!ApplicationContextUtil.isNetworkAvailable())
            return getObservableForGetDramaListInDb();

        //如果沒有網路：
        //    - 先執行 API Request
        //    - 接著新增 DB
        //    - 接著由 DB 查詢後回傳
        return MediaApiClient.getInstance().getDramaList()
                .map(getFuncForMapDramaRoListToDramaPoList())
                .doOnNext(new Consumer<List<DramaPo>>() {
                    @Override
                    public void accept(List<DramaPo> dramaPoList) throws Exception {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - getObservableForGetDramaListFromApi doOnNext Consumer accept");
                        for (DramaPo dramaPo : dramaPoList)
                            MediaDatabase.getInstance(ApplicationContextUtil.getAppContext()).getDramaDao().insert(dramaPo);
                    }
                })
                .concatMap(new Function<List<DramaPo>, ObservableSource<List<DramaPo>>>() {
                    @Override
                    public ObservableSource<List<DramaPo>> apply(List<DramaPo> dramaPoList) throws Exception {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - getObservableForGetDramaListFromApi concatMap apply");
                        return (MediaDatabase.getInstance(ApplicationContextUtil.getAppContext()).getDramaDao().getAll());
                    }
                })
                .map(getFuncForMapDramaPoListToDramaVoList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //轉換： DramaRoList -> List<DramaPo>
    private Function<DramaRoList, List<DramaPo>> getFuncForMapDramaRoListToDramaPoList() {
        return new Function<DramaRoList, List<DramaPo>>() {
                @Override
                public List<DramaPo> apply(DramaRoList dramaRoList) throws Exception {
                    Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - getFuncForMapDramaRoListToDramaPoList apply");
                    List<DramaPo> dramaPoList = new ArrayList<>();
                    for (DramaRo dramaRO : dramaRoList.getData())
                        dramaPoList.add(DramaConverter.getInstance().convert(dramaRO));
                    return dramaPoList;
                }
            };
    }

    //連接： 將 List<DramaPo> 新增至 DB 的 Completable
    private Function<List<DramaPo>, CompletableSource> getFuncForConcatMapInsertDb() {
        return new Function<List<DramaPo>, CompletableSource>() {
                @Override
                public CompletableSource apply(final List<DramaPo> dramaPoList) throws Exception {
                    Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - getFuncForConcatMapInsertDb apply");
                    return Completable.fromAction(new Action() {
                            @Override
                            public void run() {
                                for (DramaPo dramaPo : dramaPoList)
                                    MediaDatabase.getInstance(ApplicationContextUtil.getAppContext()).getDramaDao().insert(dramaPo);
                            }
                        });
                }
            };
    }

    //轉換： List<DramaPo> -> List<DramaVo>
    private Function<List<DramaPo>, List<DramaVo>> getFuncForMapDramaPoListToDramaVoList() {
        return new Function<List<DramaPo>, List<DramaVo>>() {
                @Override
                public List<DramaVo> apply(List<DramaPo> dramaPoList) throws Exception {
                    Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - getFuncForMapDramaPoListToDramaVoList apply");
                    List<DramaVo> dramaVoList = new ArrayList<>();
                    for (DramaPo dramaPo: dramaPoList)
                        dramaVoList.add(DramaConverter.getInstance().convert(dramaPo));
                    return dramaVoList;
                }
            };
    }
}
