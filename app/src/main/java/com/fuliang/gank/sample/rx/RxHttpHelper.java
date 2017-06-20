package com.fuliang.gank.sample.rx;


import com.fuliang.gank.sample.http.HttpErrorInfo;
import com.fuliang.gank.sample.http.RequestData;

import java.io.IOException;
import java.net.UnknownHostException;

/*
 * 请求，响应辅助类
 */
public class RxHttpHelper {

    /*
     * 将Request对象转换为JSON字符串
     *
     * @param request 请求对象
     * @return json字符串
     */
    public static String convertRequestToJson(RequestData request) {
        if (request == null) {
            return null;
        }
        String json = GsonHelper.getGson().toJson(request);
//        LogUtils.i("request--->", request.getClass().getName() + ":" + json);
        return json;
    }


    /*
     * 将请求失败的错误throwable对象转化为应错误的RequestErrorThrowable（主要是网络连接失败或解析接口返回的JSON失败）
     *
     * @param throwable 原始错误信息
     * @return 处理后的错误类
     */
    public static RequestErrorThrowable convertIOEError(Throwable throwable) {
        //解析相应的JSON失败
        String errorCode = HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE;
        String errorMsg = HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE;
        //无网络
        if (throwable instanceof UnknownHostException) {
            errorCode = HttpErrorInfo.CODE_OF_NO_NETWORK;
            errorMsg = HttpErrorInfo.MSG_OF_NO_NETWORK;
        }
        //连接失败，如超时
        if (throwable instanceof IOException) {
            errorCode = HttpErrorInfo.CODE_OF_CAN_NOT_CONNECT;
            errorMsg = HttpErrorInfo.MSG_OF_CAN_NOT_CONNECT;
        }
        return new RequestErrorThrowable(errorCode, errorMsg);
    }

}
