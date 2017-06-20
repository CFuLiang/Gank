package com.fuliang.gank.sample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lfu on 2017/6/8.
 */

public class ResponseInfo implements Serializable{

    @Expose
    @SerializedName("error")
    public boolean isError;

    @Expose
    @SerializedName("results")
    public ArrayList<ResultsList> results;

}
