package com.fuliang.gank.sample.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fuliang.gank.sample.R;
import com.fuliang.gank.sample.adapter.WelfareAdapter;
import com.fuliang.gank.sample.helper.BusinessHelper;
import com.fuliang.gank.sample.model.ResponseInfo;
import com.fuliang.gank.sample.model.ResultsList;
import com.fuliang.gank.sample.widget.SpacesItemDecoration;

import java.util.ArrayList;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import rx.functions.Action1;

/**
 * Created by lfu on 2017/6/12.
 */

public class WelfareFragment extends Fragment implements WaveSwipeRefreshLayout.OnRefreshListener{

    private WaveSwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private WelfareAdapter adapter;
    private LoadingFragment loadingFragment;
    private WelfarePreview previewFragment;
    private ArrayList<ResultsList> list;
    private int page = 1;
    private boolean isLoadMore = false;
    private boolean isHaveMore = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.all_fragment_layout,container,false);
        final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view) ;
        recyclerView.setLayoutManager(manager);
        swipeLayout = (WaveSwipeRefreshLayout)view.findViewById(R.id.swipe_layout);
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.white),ContextCompat.getColor(getActivity(),R.color.white));
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setWaveColor(ContextCompat.getColor(getActivity(),R.color.blue));
        adapter = new WelfareAdapter(getActivity());
        SpacesItemDecoration decoration = new SpacesItemDecoration(dp2px(getActivity(),6));
        recyclerView.setPadding(dp2px(getActivity(),6),dp2px(getActivity(),6),dp2px(getActivity(),6),0);
        recyclerView.addItemDecoration(decoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition ;
                int[] lastPositions = new int[manager.getSpanCount()];
                manager.findLastVisibleItemPositions(lastPositions);
                lastPosition = findMax(lastPositions);
                if (lastPosition == recyclerView.getLayoutManager().getItemCount()-1){
                    if (isHaveMore){
                        page++;
                        isLoadMore = true;
                        getDataFromInternet();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        adapter.setOnItemClickListener(new WelfareAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showPreview(position);
            }
        });
        showFragment();
        getDataFromInternet();
        return view;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void getDataFromInternet(){
        BusinessHelper.getAllData("福利","30",String.valueOf(page)).subscribe(new Action1<ResponseInfo>() {
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
                list = responseInfo.results;
                setViewData(isLoadMore);

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setViewData(boolean isLoadMore){
        if (isLoadMore){
            adapter.addData(list);
        }else {
            adapter.setData(list);
        }
        if (recyclerView.getAdapter() != null){
            adapter.notifyDataSetChanged();
        }else {
            recyclerView.setAdapter(adapter);
        }
    }

    private void showFragment() {
        loadingFragment= new LoadingFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.loading_layout, loadingFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onRefresh() {
        getDataFromInternet();
    }

    private void showPreview(int position){
        previewFragment= new WelfarePreview();
        previewFragment.setData(list,position,getFragmentManager());
        getFragmentManager().beginTransaction()
                .replace(R.id.preview_layout, previewFragment)
                .commitAllowingStateLoss();
    }
}
