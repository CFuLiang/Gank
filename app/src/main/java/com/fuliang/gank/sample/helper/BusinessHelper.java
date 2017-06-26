package com.fuliang.gank.sample.helper;

import com.fuliang.gank.sample.http.InterfaceAPI;
import com.fuliang.gank.sample.model.ResponseInfo;
import com.fuliang.gank.sample.model.WeatherResponse;

import java.util.Map;

import rx.Observable;

/**
 * Created by lfu on 2017/6/8.
 */

public class BusinessHelper {

    public static Observable<ResponseInfo> getAllData(String type,String pageSize ,String page){
        return new InterfaceAPI().getAllData(type,pageSize,page);
    }

    public static Observable<WeatherResponse> getWeather(Map map){
        return new InterfaceAPI().getWeather(map);
    }
}
