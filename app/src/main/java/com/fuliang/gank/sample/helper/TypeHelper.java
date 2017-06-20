package com.fuliang.gank.sample.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.fuliang.gank.sample.R;

/**
 * Created by lfu on 2017/6/20.
 */

public class TypeHelper {

    public static String getType(String type){
        switch (type){
            case "福利":
                return "welfare";
            case "前端":
                return "web";
            case "休息视频":
                return "video";
            case "拓展资源":
                return "expand";
            case "我的收藏":
                return "collect";
            default:
                return type;
        }
    }

    public static Drawable getTypeDrawable(Context context,String type){
        switch (type){
            case "福利":
                return ContextCompat.getDrawable(context, R.mipmap.welfare_icon);
            case "前端":
                return ContextCompat.getDrawable(context, R.mipmap.web_icon);
            case "休息视频":
                return ContextCompat.getDrawable(context, R.mipmap.video_icon);
            case "拓展资源":
                return ContextCompat.getDrawable(context, R.mipmap.expand_icon);
            case "我的收藏":
                return ContextCompat.getDrawable(context, R.mipmap.expand_icon);
            case "Android":
                return ContextCompat.getDrawable(context,R.mipmap.android_icon);
            case "iOS":
                return ContextCompat.getDrawable(context,R.mipmap.ios_icon);
            default:
                return ContextCompat.getDrawable(context, R.mipmap.expand_icon);
        }

    }
}
