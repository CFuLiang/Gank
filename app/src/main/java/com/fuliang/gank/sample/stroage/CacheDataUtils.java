package com.fuliang.gank.sample.stroage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CacheDataUtils {
    /*
     * @param  dir 要保存的目录
     * @param  filename 要保存的文件名
     * @param data 要保存的数据
     * */
    public static boolean writeCacheData(String dir, String filename, Object data){
        File f = new File(dir);
        if (!f.exists()){
            f.mkdirs();
        }
        File target = new File(dir + File.separator + filename);
        return writeCacheData(target, data);
    }

    public static boolean clearCacheData(String dir, String filename){
        File target = new File(dir + File.separator + filename);
        if (target.exists()){
            target.delete();
        }
        return true;
    }

    /*
     * @param  file 要保存的文件
     * @param data 要保存的数据
     * */
    public static boolean writeCacheData(File file, Object data){
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;

        byte[] result = null;

        try {
            objectOutputStream = new ObjectOutputStream(output);
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
            result = output.toByteArray();
            objectOutputStream.close();
            output.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (result == null){
            return false;
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(result);
            outputStream.close();
//            AppCacheMgr.instance.appendCacheFileSize(file);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /*
     * 从指定文件读取数据，并反序列化对象
     * @param  dir 要读取的目录
     * @param  filename 要读取的文件名
     * @return 反序列化后的对象
     * */
    public static Object readCacheData(String dir, String filename){
        File target = new File(dir + File.separator + filename);
        return readCacheData(target);
    }
    public static boolean isCacheDataExist(String dir, String filename){
        File target = new File(dir + File.separator + filename);
        return target.exists();
    }
    /*
     * 从指定文件读取数据，并反序列化对象
     * @param  file 要读取的文件
     * @return 反序列化后的对象
     * */
    public static Object readCacheData(File file){
        if (!file.exists()){
            return null;
        }

        Object obj = null;
        FileInputStream fileinput = null;
        try {
            fileinput = new FileInputStream(file);
            byte[] data = new byte[fileinput.available()];
            fileinput.read(data);
            fileinput.close();

            ByteArrayInputStream input = new ByteArrayInputStream(data);

            ObjectInputStream objectInputStream = new ObjectInputStream(input);
            obj = objectInputStream.readObject();
            objectInputStream.close();
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return obj;
    }
}
