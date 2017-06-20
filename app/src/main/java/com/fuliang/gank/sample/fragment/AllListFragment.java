package com.fuliang.gank.sample.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuliang.gank.sample.helper.TypeHelper;
import com.fuliang.gank.sample.stroage.DataCache;
import com.fuliang.gank.sample.widget.EndLessOnScrollListener;
import com.fuliang.gank.sample.R;
import com.fuliang.gank.sample.WebActivity;
import com.fuliang.gank.sample.adapter.AllDataAdapter;
import com.fuliang.gank.sample.helper.BusinessHelper;
import com.fuliang.gank.sample.model.ResponseInfo;
import com.fuliang.gank.sample.model.ResultsList;
import com.fuliang.gank.sample.widget.MyLayoutManager;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;

import java.util.ArrayList;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import rx.functions.Action1;

/**
 * Created by lfu on 2017/6/8.
 */

public class AllListFragment extends Fragment implements WaveSwipeRefreshLayout.OnRefreshListener{

    private WaveSwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private AllDataAdapter allDataAdapter;
    private ArrayList<ResultsList> allDataList;
    private String itemType;
    private LoadingFragment loadingFragment;
    private Animator spruceAnimator;
    private int page = 1;
    private boolean isLoadMore = false;
    private boolean isHaveMore = true;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_fragment_layout,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new MyLayoutManager(getActivity()){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                spruceAnimator = new Spruce.SpruceBuilder(recyclerView)
                        .sortWith(new DefaultSort(100))
                        .animateWith(DefaultAnimations.shrinkAnimator(recyclerView, 800),
                                ObjectAnimator.ofFloat(recyclerView, "translationX", -recyclerView.getWidth(), 0f).setDuration(800))
                        .start();
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        swipeLayout = (WaveSwipeRefreshLayout)view.findViewById(R.id.swipe_layout);
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.white),ContextCompat.getColor(getActivity(),R.color.white));
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setWaveColor(ContextCompat.getColor(getActivity(),R.color.blue));
        allDataAdapter = new AllDataAdapter(getActivity());
        allDataAdapter.setReloadListener(new AllDataAdapter.ReloadListener() {
            @Override
            public void onReload() {
                page++;
                isLoadMore = true;
                getDataFromInternet();
                allDataAdapter.startReload();
            }
        });
        allDataAdapter.setOnItemClickListener(new AllDataAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ResultsList model) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("model",model);
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                if (isHaveMore){
                    page++;
                    isLoadMore = true;
                    getDataFromInternet();
                }
            }
        });
        itemType = getArguments().getString("type");
        setType(itemType);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getDataFromInternet(){
        BusinessHelper.getAllData(itemType,"30",String.valueOf(page)).subscribe(new Action1<ResponseInfo>() {
            @Override
            public void call(ResponseInfo responseInfo) {
                if (swipeLayout.isRefreshing()) {
                    swipeLayout.setRefreshing(false);
                }
                if (loadingFragment != null){
                    loadingFragment.removeSelf(getFragmentManager());
                }
                if (responseInfo.results.size() < 30){
                    isHaveMore = false;
                }

                if (page == 1){
                    DataCache.instance.saveCacheData("fuliang", TypeHelper.getType(itemType),responseInfo.results);
                }
                allDataList = responseInfo.results;
                setViewData(isLoadMore);

                if (spruceAnimator != null){
                    spruceAnimator.start();
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (loadingFragment != null){
                    loadingFragment.loadFail();
                }
                if (isLoadMore){
                    page--;
                    isHaveMore = true;
                    allDataAdapter.loadMoreFail();
                }
            }
        });
    }

    private void setViewData(boolean isLoadMore){
        if (isLoadMore){
            allDataAdapter.addData(allDataList);
        }else {
            allDataAdapter.setData(allDataList);
        }
        if (recyclerView.getAdapter() == null){
            recyclerView.setAdapter(allDataAdapter);
        }else {
            if (!isLoadMore){
                recyclerView.scrollTo(0,0);
            }
            allDataAdapter.notifyDataSetChanged();
        }

    }

    public void setType(String type){
        itemType = type;
        isLoadMore = false;
        page = 1;
        if (recyclerView != null){
            recyclerView.removeAllViews();
            if (allDataList != null){
                allDataList.clear();
                allDataAdapter.notifyDataSetChanged();
            }
        }
        if (loadingFragment != null){
            loadingFragment.removeSelf(getFragmentManager());
        }
        allDataList = DataCache.instance.getCacheData("fuliang",TypeHelper.getType(type));
        if (allDataList == null){
            if (!type.equals("我的收藏")){
                showFragment();
                getDataFromInternet();
            }else {
                showFragment();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingFragment.noData();
                    }
                },500);
            }
            return;
        }
        if (allDataList.size()>0){
            setViewData(isLoadMore);
            if (spruceAnimator != null){
                spruceAnimator.start();
            }
        }else {
            showFragment();
            getDataFromInternet();
        }
    }

    private void showFragment() {
        loadingFragment= new LoadingFragment();
        loadingFragment.setReloadListener(new LoadingFragment.ReloadData() {
            @Override
            public void reloadData() {
                loadingFragment.reloadData();
                page = 1;
                getDataFromInternet();
            }
        });
        getFragmentManager().beginTransaction()
                .replace(R.id.loading_layout, loadingFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getDataFromInternet();
    }
}
