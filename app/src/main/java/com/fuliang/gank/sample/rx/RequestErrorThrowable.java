package com.fuliang.gank.sample.rx;

/*
 * . 请求失败时的错误对象
 */
public class RequestErrorThrowable extends Throwable {

    private String errorCode;  //错误代码

    public RequestErrorThrowable(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
