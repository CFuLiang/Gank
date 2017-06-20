package com.fuliang.gank.sample.stroage;

import android.content.Context;

import java.io.File;

public class DataCache {

    private String cacheDir;

    private static final String CACHE_ROOT = "responses";

    public static String APPCONFIG = "APPCONFIG";

    public static final DataCache instance = new DataCache();

    private DataCache() {
    }

    public void init(Context context) {
        cacheDir = context.getCacheDir().getAbsolutePath()
                + File.separator + CACHE_ROOT;
    }

    private String getHashData(String key) {
        return HashUtil.GetHashCode(key, HashUtil.HashType.MD5.toString());
    }

    /*
     * 将对象序列化缓存到本地
     * @return 成功返回true，否则false
     */
    public boolean saveCacheData(String loginAccount, String key, Object data) {
        key = getHashData(loginAccount + key);
        return CacheDataUtils.writeCacheData(cacheDir, key, data);
    }

    /*
     * 删除本地缓存数据
     * @return 成功返回true，否则false
     */
    public boolean clearCacheData(String loginAccount, String key) {
        key = getHashData(loginAccount + key);
        return CacheDataUtils.clearCacheData(cacheDir, key);
    }

    /*
     * 获取缓存数据并返反序列化
     * @return 成功返回对应对象实例，否则返回null
     */
    public <T> T getCacheData(String loginAccount, String key) {
        key = getHashData(loginAccount + key);
        Object obj = CacheDataUtils.readCacheData(cacheDir, key);
        if (obj == null) {
            return null;
        }

        return (T) obj;
    }

    public boolean isCacheExist(String loginAccount, String key) {
        key = getHashData(loginAccount + key);
        return CacheDataUtils.isCacheDataExist(cacheDir, key);
    }

    public String getImageFile(String loginAccount, String key) {
        try {
            key = getHashData(loginAccount + key);
            File target = new File(cacheDir + File.separator + key);
            return target.getCanonicalPath();
        } catch (Throwable throwable) {

        }
        return null;
    }

    public Object getAppServiceConfigData() {
        return getCacheData("", APPCONFIG);
    }
}
