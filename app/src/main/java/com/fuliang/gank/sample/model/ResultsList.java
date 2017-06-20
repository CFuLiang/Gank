package com.fuliang.gank.sample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lfu on 2017/6/8.
 */

public class ResultsList implements Serializable {

    @Expose
    @SerializedName("_id")
    public String id;

    @Expose
    @SerializedName("createdAt")
    public String createdAt;

    @Expose
    @SerializedName("desc")
    public String desc;

    @Expose
    @SerializedName("publishedAt")
    public String publishedAt;

    @Expose
    @SerializedName("source")
    public String source;

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("url")
    public String url;

    @Expose
    @SerializedName("who")
    public String who;

    @Expose
    @SerializedName("used")
    public boolean used;

    @Expose
    @SerializedName("images")
    public String[] images = {""};
}
