package com.fuliang.gank.sample.http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by lfu on 2017/2/22.
 */

public class ResponseData implements Serializable{

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("result")
    public boolean result;

    @Expose
    @SerializedName("code")
    public String errorCode;

    @Expose
    @SerializedName("msg")
    public String errorMsg;

    public void clearData(){

    }
}
