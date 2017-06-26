package com.fuliang.gank.sample;

import android.app.Application;
import android.util.Log;

import com.fuliang.gank.sample.stroage.DataCache;

/**
 * Created by lfu on 2017/6/16.
 */

public class MyApplication extends Application{

    private String [] title;

    @Override
    public void onCreate() {
        super.onCreate();
        DataCache.instance.init(MyApplication.this);
        Long saveTime = DataCache.instance.getCacheData("fuliang","limitTime");
        Long nowTime = System.currentTimeMillis();
        if (saveTime == null){
            DataCache.instance.saveCacheData("fuliang","limitTime",nowTime);
        }else {
            title = getResources().getStringArray(R.array.title_list);
            int i = 0;
            if (nowTime - saveTime > 12*60*60*1000){
                while (i<title.length-1){
                    DataCache.instance.clearCacheData("fuliang",title[i]);
                    i++;
                }
            }
        }
    }
}
