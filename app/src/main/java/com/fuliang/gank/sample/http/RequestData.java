package com.fuliang.gank.sample.http;
/*
 * Created by lfu on 2017/2/22.
 */

import com.fuliang.gank.sample.helper.URLHelper;

public abstract class RequestData {


    public String getUrl() {
        return URLHelper.getInstance().URL;
    }

    /*
     * 对应的接口名称
     *
     * @return
     */
    public abstract String getInterfaceName();


    /*
     * 是否需要缓存该Request对应的响应数据
     *
     * @return
     */
    public abstract boolean isNeedCache();

    /*
     * 缓存该Request对应的响应数据的时间有效期
     *
     * @return 缓存周期（毫秒）
     */
    public abstract long getCachePeriod();

    /*
     * Request数据拼接的字符串
     *
     * @return
     */
    public abstract String getRequestKey();
}
