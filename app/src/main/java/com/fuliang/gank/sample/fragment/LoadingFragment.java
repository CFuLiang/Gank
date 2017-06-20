package com.fuliang.gank.sample.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuliang.gank.sample.R;
import com.fuliang.gank.sample.widget.LoadingView;

import static com.fuliang.gank.sample.widget.LoadingView.STATE_EMPTY_RESULT;
import static com.fuliang.gank.sample.widget.LoadingView.STATE_FAILED;
import static com.fuliang.gank.sample.widget.LoadingView.STATE_LOADING;

/**
 * Created by lfu on 2017/6/12.
 */

public class LoadingFragment extends Fragment implements View.OnClickListener{

    private LoadingView loadingView;
    private TextView errorTextView;
    private ReloadData listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading_layout,container,false);
        errorTextView = (TextView)view.findViewById(R.id.error_text);
        errorTextView.setOnClickListener(this);
        loadingView = (LoadingView)view.findViewById(R.id.loading_view);
        loadingView.setNormalColor(ContextCompat.getColor(getActivity(),R.color.blue));
        return view;
    }

    public void removeSelf(final FragmentManager fragmentManager){
        if(fragmentManager == null){
            return;
        }
        loadingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getActivity()!=null){
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(0,0)
                            .remove(LoadingFragment.this)
                            .commitAllowingStateLoss();
                }

            }
        },200);
    }

    public void loadFail(){
        errorTextView.setVisibility(View.VISIBLE);
        loadingView.setViewState(STATE_FAILED);
    }

    public void reloadData(){
        loadingView.setViewState(STATE_LOADING);
        errorTextView.setVisibility(View.GONE);
    }

    public void noData(){
        loadingView.setViewState(STATE_EMPTY_RESULT);
        errorTextView.setText("暂无数据");
        errorTextView.setVisibility(View.VISIBLE);
    }

    public void setReloadListener(ReloadData reloadData) {
        this.listener = reloadData;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.reloadData();
        }
    }

    public interface ReloadData {
        void reloadData();
    }


}
