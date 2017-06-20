package com.fuliang.gank.sample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuliang.gank.sample.R;
import com.fuliang.gank.sample.model.ResultsList;

import java.util.ArrayList;

/**
 * Created by lfu on 2017/6/20.
 */

public class CollectItemAdapter extends RecyclerView.Adapter<CollectItemAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<ResultsList> list;
    private ItemClickListener listener;

    public CollectItemAdapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.collect_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0){
            holder.lineView.setVisibility(View.GONE);
        }
        holder.rootLayout.setTag(list.get(position));
        holder.rootLayout.setOnClickListener(this);
        holder.type.setText(list.get(position).type);
        holder.titleText .setText(list.get(position).desc);
        holder.writerText.setText(list.get(position).who);
        holder.createTime.setText(list.get(position).createdAt.substring(0,10));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onItemClick((ResultsList) v.getTag());
        }
    }

    public void setData(ArrayList<ResultsList> data){
        list = data;
    }

    public void setOnItemClickListener(ItemClickListener onItemClick) {
        this.listener = onItemClick;
    }


    public interface ItemClickListener {
        void onItemClick(ResultsList model);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleText,writerText,createTime,type;
        private View lineView;
        private LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            lineView = (View)itemView.findViewById(R.id.line_view);
            type = (TextView)itemView.findViewById(R.id.type);
            titleText = (TextView) itemView.findViewById(R.id.title);
            writerText = (TextView) itemView.findViewById(R.id.writer_name);
            createTime = (TextView) itemView.findViewById(R.id.create_time);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.root_layout);
        }
    }
}
