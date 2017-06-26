package com.fuliang.gank.sample.http;

import com.fuliang.gank.sample.model.ResponseInfo;
import com.fuliang.gank.sample.model.WeatherResponse;
import com.fuliang.gank.sample.rx.RequestErrorThrowable;
import com.fuliang.gank.sample.rx.RxHttpHelper;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/*
 * Created by lfu on 2017/2/21.
 */

public class InterfaceAPI {

    private InterfaceService interfaceService;
    private InterfaceService interfaceServiceSecond;

    public InterfaceAPI() {
        interfaceService = new RetrofitClient().getInterfaceService();
        interfaceServiceSecond = new RetrofitClient(0).getInterfaceService();

    }

    public Observable<WeatherResponse> getWeather(Map<String, String> map){
        return interfaceServiceSecond.getWeather(map).onErrorResumeNext(new Func1<Throwable, Observable<? extends WeatherResponse>>() {
            @Override
            public Observable<? extends WeatherResponse> call(Throwable throwable) {
                return Observable.error(RxHttpHelper.convertIOEError(throwable));
            }
        }).flatMap(new Func1<WeatherResponse, Observable<WeatherResponse>>() {
            @Override
            public Observable<WeatherResponse> call(WeatherResponse responseInfo) {
                if (responseInfo == null) {
                    return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                            HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                }else {
                    return Observable.just(responseInfo);
//                    if (responseInfo.isError){
//                        return Observable.error(new RequestErrorThrowable("-1", "获取失败"));
//                    }else {
//                        return Observable.just(responseInfo);
//                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResponseInfo> getAllData(String type,String pageSize ,String page) {
        return interfaceService.getAllInfo(type,pageSize,page).onErrorResumeNext(new Func1<Throwable, Observable<? extends ResponseInfo>>() {
            @Override
            public Observable<? extends ResponseInfo> call(Throwable throwable) {
                return Observable.error(RxHttpHelper.convertIOEError(throwable));
            }
        }).flatMap(new Func1<ResponseInfo, Observable<ResponseInfo>>() {
            @Override
            public Observable<ResponseInfo> call(ResponseInfo responseInfo) {
                if (responseInfo == null) {
                    return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                            HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                }else {
                    if (responseInfo.isError){
                        return Observable.error(new RequestErrorThrowable("-1", "获取失败"));
                    }else {
                        return Observable.just(responseInfo);
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
