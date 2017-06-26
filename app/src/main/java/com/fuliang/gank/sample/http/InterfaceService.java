package com.fuliang.gank.sample.http;

import com.fuliang.gank.sample.model.ResponseInfo;
import com.fuliang.gank.sample.model.WeatherResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/*
 * Created by lfu on 2017/2/21.
 */

public interface InterfaceService {

    @GET("{type}/{pageSize}/{page}")
    Observable<ResponseInfo> getAllInfo(@Path("type") String type, @Path("pageSize")String pageSize, @Path("page")String page);

    @GET("v3/weather/now.json")
    Observable<WeatherResponse> getWeather(@QueryMap Map<String ,String> map);
}
