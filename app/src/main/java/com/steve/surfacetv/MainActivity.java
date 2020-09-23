package com.steve.surfacetv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.steve.surfacetv.adapter.DramaListAdapter;
import com.steve.surfacetv.datasource.MediaDataProvider;
import com.steve.surfacetv.datasource.util.ApplicationContextUtil;
import com.steve.surfacetv.datasource.util.SharedPreferenceUtil;
import com.steve.surfacetv.datasource.vo.DramaVo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends FragmentActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;

    private DramaListAdapter adapterDramaList;
    private List<DramaVo> dramaVoListForDisplay;
    private List<DramaVo> dramaVoListForSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.main_swipe);
        dramaVoListForDisplay = new ArrayList<>();

        adapterDramaList = new DramaListAdapter(this, this.dramaVoListForDisplay);
        adapterDramaList.setItemClickListener(new DramaListAdapter.OnDramaListItemClickListener() {
                @Override
                public void onItemClickListener(DramaVo dataBean) {
                    Log.d(ApplicationContextUtil.APP_LOG_TAG, "drama id click:" + dataBean.getDramaId());

                    Intent intent = new Intent(MainActivity.this, DramaDetailActivity.class);
                    intent.putExtra(DramaDetailActivity.BUNDLE_KEY_DRAMA_NAME, dataBean.getName());
                    intent.putExtra(DramaDetailActivity.BUNDLE_KEY_DRAMA_TOTAL_VIEWS, String.valueOf(dataBean.getTotalViews()));
                    intent.putExtra(DramaDetailActivity.BUNDLE_KEY_DRAMA_RATING, String.valueOf(dataBean.getRating()));
                    intent.putExtra(DramaDetailActivity.BUNDLE_KEY_DRAMA_CREATED_AT, dataBean.getCreatedAt());
                    intent.putExtra(DramaDetailActivity.BUNDLE_KEY_DRAMA_THUMB, dataBean.getThumb());
                    startActivity(intent);
                }
            });

        initSearchView();
        initDramaList();
        initSwipe();

        showDramaListForInit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initDramaList() {
        RecyclerView recyclerViewDramaList = (RecyclerView) this.findViewById(R.id.main_dramalist);
        recyclerViewDramaList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDramaList.setAdapter(adapterDramaList);
        recyclerViewDramaList.addItemDecoration(new DividerItemDecoration(ApplicationContextUtil.getAppContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void initSearchView() {
        searchView = findViewById(R.id.main_search);
        final ImageView imageSearchViewClose = searchView.findViewById(R.id.search_close_btn);
        final TextView textViewTitle = findViewById(R.id.main_subtitle);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageSearchViewClose.setVisibility(View.VISIBLE);
                    textViewTitle.setVisibility(View.INVISIBLE);
                }
            });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    textViewTitle.setVisibility(View.VISIBLE);
                    restore();
                    return false;
                }
            });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    imageSearchViewClose.setVisibility(View.VISIBLE);
                    if (newText.isEmpty())
                        restore();
                    else
                        search(newText);
                    return false;
                }
            });
    }

    private void initSwipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDramaListFromApi();
                }
            });
    }

    private void search(String text) {
        SharedPreferenceUtil.getInstance().setSearchInput(text);
        this.dramaVoListForDisplay.clear();
        for (DramaVo dramaVo : this.dramaVoListForSearch) {
            if (dramaVo.getName().contains(text))
                this.dramaVoListForDisplay.add(dramaVo);
        }
        this.adapterDramaList.notifyDataSetChanged();
    }

    private void restore() {
        SharedPreferenceUtil.getInstance().setSearchInput("");
        this.dramaVoListForDisplay.clear();
        this.dramaVoListForDisplay.addAll(this.dramaVoListForSearch);
        this.adapterDramaList.notifyDataSetChanged();
    }

    private void showDramaListForInit() {
        Log.d(ApplicationContextUtil.APP_LOG_TAG, "showDramaListInDb");

        MainActivity.this.swipeRefreshLayout.setRefreshing(true);
        MediaDataProvider.getInstance().getObservableForGetDramaListInDb()
                .subscribe(new Observer<List<DramaVo>>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - showDramaListForInit subscribe onSubscribe");
                        this.d = d;
                    }

                    @Override
                    public void onNext(List<DramaVo> dramaVoList) {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - showDramaListForInit subscribe onNext");
                        refreshRecycleView(dramaVoList);

                        if(this.d != null && !this.d.isDisposed())
                            this.d.dispose();

                        requestDramaListFromApi();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - showDramaListForInit subscribe onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - showDramaListForInit subscribe onComplete");
                    }
                });
    }

    private void requestDramaListFromApi() {
        Log.d(ApplicationContextUtil.APP_LOG_TAG, "requestDramaListFromApi");

        if (!ApplicationContextUtil.isNetworkAvailable()) {
            MainActivity.this.swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, "目前無網路，請稍候重試下拉更新", Toast.LENGTH_LONG).show();
            return;
        }

        MainActivity.this.swipeRefreshLayout.setRefreshing(true);
        MediaDataProvider.getInstance().getObservableForGetDramaListFromApi()
                .subscribe(new Observer<List<DramaVo>>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - requestDramaListFromApi subscribe onSubscribe");
                        this.d = d;
                    }

                    @Override
                    public void onNext(List<DramaVo> dramaVoList) {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - requestDramaListFromApi subscribe onNext");
                        refreshRecycleView(dramaVoList);
                        MainActivity.this.swipeRefreshLayout.setRefreshing(false);

                        if(this.d != null && !this.d.isDisposed())
                            this.d.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - requestDramaListFromApi subscribe onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ApplicationContextUtil.APP_LOG_TAG, "    - requestDramaListFromApi subscribe onComplete");
                    }
                });
    }

    private void refreshRecycleView(List<DramaVo> dramaVoList) {
        this.dramaVoListForDisplay.clear();
        this.dramaVoListForDisplay.addAll(dramaVoList);
        this.dramaVoListForSearch = dramaVoList;

        String prevSearchInput = SharedPreferenceUtil.getInstance().getSearchInput();
        if (!prevSearchInput.isEmpty()) {
            search(prevSearchInput);
            this.searchView.onActionViewExpanded();
            this.searchView.setQuery(prevSearchInput, false);
            return;
        }

        MainActivity.this.adapterDramaList.notifyDataSetChanged();
    }
}