package com.fuliang.gank.sample.http;

import com.fuliang.gank.sample.model.ResponseInfo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/*
 * Created by lfu on 2017/2/21.
 */

public interface InterfaceService {

    @GET("{type}/{pageSize}/{page}")
    Observable<ResponseInfo> getAllInfo(@Path("type") String type,@Path("pageSize")String pageSize, @Path("page")String page);
}
