package com.fuliang.gank.sample.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuliang.gank.sample.R;
import com.fuliang.gank.sample.helper.TypeHelper;
import com.fuliang.gank.sample.model.ResultsList;
import com.fuliang.gank.sample.widget.LoadingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lfu on 2017/6/8.
 */

public class AllDataAdapter extends RecyclerView.Adapter<AllDataAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;

    private ArrayList<ResultsList> list;

    private ItemClickListener listener;

    private ReloadListener reloadListener;

    private boolean isLoadMoreFail = false;

    public AllDataAdapter(Context context){
        this.context = context;
    }

    @Override
    public AllDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0){
            view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        }else if (viewType == 2){
            view = LayoutInflater.from(context).inflate((R.layout.welfare_item),parent,false);
        }else {
            view = LayoutInflater.from(context).inflate((R.layout.item_loading_layout),parent,false);
        }
        return new AllDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllDataAdapter.ViewHolder holder, int position) {
        if (position != list.size()){
            if (list.get(position).type.equals("福利")){
                Picasso.with(context)
                        .load(list.get(position).url)
                        .placeholder(R.mipmap.xx)
                        .into(holder.welfareImage);
            }else {
                holder.imageView.setBackground(TypeHelper.getTypeDrawable(context,list.get(position).type));
                holder.type.setText(list.get(position).type);
                holder.titleText .setText(list.get(position).desc);
                holder.writerText.setText(list.get(position).who);
                holder.createTime.setText(list.get(position).createdAt.substring(0,10));
                holder.cardView.setTag(list.get(position));
                holder.cardView.setOnClickListener(this);
            }
        }else {
            holder.errorText.setOnClickListener(this);
            if (isLoadMoreFail){
                holder.loadingView.setVisibility(View.GONE);
                holder.errorText.setVisibility(View.VISIBLE);
            }else {
                holder.loadingView.setVisibility(View.VISIBLE);
                holder.errorText.setVisibility(View.GONE);
            }
            holder.loadingView.setScrollBarSize(1);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()){
            return 1;
        }else if (list.get(position).type.equals("福利")){
            return 2;
        }else {
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_view:
                if (listener != null){
                    listener.onItemClick((ResultsList) v.getTag());
                }
                break;
            case R.id.error_text:
                if (reloadListener != null){
                    reloadListener.onReload();
                }
        }

    }

    @Override
    public int getItemCount() {
        if (list.size() == 0){
            return 0;
        }else {
            return list.size() + 1;
        }
    }

    public void loadMoreFail() {
        isLoadMoreFail = true;
        this.notifyDataSetChanged();
    }

    public void startReload(){
        isLoadMoreFail = false;
        this.notifyDataSetChanged();
    }


    public void setData(ArrayList<ResultsList> allDataList){
        list = allDataList;
        isLoadMoreFail = false;
    }

    public void addData(ArrayList<ResultsList> allDataList){
        list.addAll(allDataList);
        isLoadMoreFail =false;
    }

    public void setOnItemClickListener(ItemClickListener onItemClick) {
        this.listener = onItemClick;
    }

    public interface ItemClickListener {
        void onItemClick(ResultsList model);
    }

    public void setReloadListener(ReloadListener onReloda) {
        this.reloadListener = onReloda;
    }

    public interface ReloadListener {
        void onReload();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView imageView,welfareImage;
        private TextView titleText,writerText,createTime,type,errorText;
        private LoadingView loadingView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            welfareImage = (ImageView)itemView.findViewById(R.id.image_view);
            imageView = (ImageView)itemView.findViewById(R.id.type_image);
            type = (TextView)itemView.findViewById(R.id.type);
            titleText = (TextView) itemView.findViewById(R.id.title);
            writerText = (TextView) itemView.findViewById(R.id.writer_name);
            createTime = (TextView) itemView.findViewById(R.id.create_time);
            loadingView = (LoadingView)itemView.findViewById(R.id.item_loading);
            errorText = (TextView)itemView.findViewById(R.id.error_text);
        }
    }

}
