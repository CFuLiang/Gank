package com.fuliang.gank.sample.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuliang.gank.sample.R;
import com.fuliang.gank.sample.model.ResultsList;

/**
 * Created by lfu on 2017/6/14.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private String [] titleList;
    private Drawable [] iconNormal;
    private Drawable [] iconSelected;
    private int selected;

    private ItemClickListener listener;

    public MenuAdapter (Context context){
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_option,parent,false);
        titleList = context.getResources().getStringArray(R.array.ld_activityScreenTitles);
        iconNormal = getIcon(R.array.icon_list_normal);
        iconSelected = getIcon(R.array.icon_list);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.linearLayout.setTag(position);
        holder.linearLayout.setOnClickListener(this);
        holder.title.setText(titleList[position]);
        if (selected == position){
            holder.icon.setBackground(iconSelected[position]);
            holder.title.setTextColor(ContextCompat.getColor(context,R.color.blue));
        }else {
            holder.icon.setBackground(iconNormal[position]);
            holder.title.setTextColor(ContextCompat.getColor(context,R.color.text_title));
        }


    }

    @Override
    public int getItemCount() {
        return 8;
    }
    private Drawable[] getIcon(int resourcesId){
        TypedArray ta = context.getResources().obtainTypedArray(resourcesId);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(context, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onItemClick((int)v.getTag());
        }
    }

    public void setSelected(int position){
        selected = position;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemClickListener onItemClick) {
        this.listener = onItemClick;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout linearLayout;
        private ImageView icon;
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
            icon = (ImageView)itemView.findViewById(R.id.icon);
            title = (TextView)itemView.findViewById(R.id.title);
        }
    }
}
