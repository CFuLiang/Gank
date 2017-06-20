package com.fuliang.gank.sample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuliang.gank.sample.R;
import com.fuliang.gank.sample.model.ResponseInfo;
import com.fuliang.gank.sample.model.ResultsList;
import com.fuliang.gank.sample.widget.LoadingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lfu on 2017/6/12.
 */

public class WelfareAdapter extends RecyclerView.Adapter<WelfareAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<ResultsList> list;
    private ItemClickListener listener;


    public WelfareAdapter(Context context){
        this.context = context;
    }

    @Override
    public WelfareAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view;
        if (viewType == 0){
            view = LayoutInflater.from(context).inflate(R.layout.welfare_fragment_item,parent,false);
        }else {
            view = LayoutInflater.from(context).inflate((R.layout.item_loading_layout),parent,false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WelfareAdapter.ViewHolder holder, int position) {
        if (position != list.size()){
            Picasso.with(context)
                    .load(list.get(position).url)
                    .placeholder(R.mipmap.xx)
                    .into(holder.welfareImage);
            holder.welfareImage.setTag(position);
            holder.welfareImage.setOnClickListener(this);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public void setData(ArrayList<ResultsList> data){
        list = data;
    }

    public void addData(ArrayList<ResultsList> data){
        list.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onItemClick((int)v.getTag());
        }
    }


    public void setOnItemClickListener(ItemClickListener onItemClick) {
        this.listener = onItemClick;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView welfareImage;
        private LoadingView loadingView;

        public ViewHolder(View itemView) {
            super(itemView);
            welfareImage = (ImageView)itemView.findViewById(R.id.image_view);
        }
    }

}
