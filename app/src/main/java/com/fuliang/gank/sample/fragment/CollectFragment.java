package com.fuliang.gank.sample.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuliang.gank.sample.R;
import com.fuliang.gank.sample.WebActivity;
import com.fuliang.gank.sample.adapter.CollectItemAdapter;
import com.fuliang.gank.sample.model.ResultsList;
import com.fuliang.gank.sample.stroage.DataCache;
import com.fuliang.gank.sample.widget.MyLayoutManager;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;

import java.util.ArrayList;

/**
 * Created by lfu on 2017/6/20.
 */

public class CollectFragment extends Fragment {

    private RecyclerView recyclerView;
    private Animator spruceAnimator;
    private CollectItemAdapter adapter;
    private ArrayList<ResultsList> list;
    private LoadingFragment loadingFragment;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
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
        adapter = new CollectItemAdapter(getActivity());
        adapter.setOnItemClickListener(new CollectItemAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ResultsList model) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("model",model);
                startActivity(intent);
            }
        });
        list = DataCache.instance.getCacheData("fuliang","collect");
        if (list == null){
            showFragment();
        }else {
            adapter.setData(list);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    private void showFragment() {
        loadingFragment= new LoadingFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.loading_layout, loadingFragment)
                .commitAllowingStateLoss();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingFragment.noData();
            }
        },500);

    }
}
