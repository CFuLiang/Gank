package com.fuliang.gank.sample.http;

import com.fuliang.gank.sample.model.ResponseInfo;
import com.fuliang.gank.sample.rx.RequestErrorThrowable;
import com.fuliang.gank.sample.rx.RxHttpHelper;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/*
 * Created by lfu on 2017/2/21.
 */

public class InterfaceAPI {

    private InterfaceService interfaceService;

    public InterfaceAPI() {
        interfaceService = new RetrofitClient().getInterfaceService();
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
