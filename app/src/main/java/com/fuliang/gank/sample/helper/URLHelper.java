package com.fuliang.gank.sample.helper;

/*
 * Created by lfu on 2017/2/21.
 */

public class URLHelper {

    public String URL = "http://gank.io/api/data/";

    public static URLHelper getInstance() {
        return Singleton.instance;
    }

    private static final class Singleton {
        public static final URLHelper instance = new URLHelper();
    }

}
