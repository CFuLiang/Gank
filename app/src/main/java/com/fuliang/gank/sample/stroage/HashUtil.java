package com.fuliang.gank.sample.stroage;


import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    static String TYPE_MD5 = "MD5";
    static String TYPE_SHA1 = "SHA1";

    public enum HashType{
        MD5(TYPE_MD5),
        SHA1(TYPE_SHA1);

        HashType(String s){
            str = s;
        }

        private String str;

        @Override
        public String toString() {
            return str;
        }
    }

	// 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
    private static String byteToNum(byte bByte) {
        int iRet = bByte;

        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }
    
    private static String ConvertByteToString(byte[] var0) {
        StringBuilder var1 = new StringBuilder(var0.length * 2);

        for (int var2 = 0; var2 < var0.length; ++var2) {
            var1.append(Character.forDigit((var0[var2] & 240) >> 4, 16));
            var1.append(Character.forDigit(var0[var2] & 15, 16));
        }

        return var1.toString();
    }


    // 转换字节数组为16进制字串
//    private static String byteToString(byte[] bByte) {
//        StringBuffer sBuffer = new StringBuffer();
//        for (int i = 0; i < bByte.length; i++) {
//            sBuffer.append(byteToArrayString(bByte[i]));
//        }
//        return sBuffer.toString();
//    }

//    public static String GetMD5Code(String strObj) {
//        String resultString = null;
//        try {
//            resultString = new String(strObj);
//            MessageDigest md = MessageDigest.getInstance("SHA1");
//            md.update(strObj.getBytes());  
//            // md.digest() 该函数返回值为存放哈希值结果的byte数组
//            resultString = new String(md.digest());
//        } catch (NoSuchAlgorithmException ex) {
//            ex.printStackTrace();
//        }
//        return resultString.toLowerCase();
//    }
    
    public static String GetHashCode(byte[] strObj, HashType type) {
        return GetHashCode(strObj, type.toString());
    }

    public static String GetHashCode(byte[] strObj, String type) {
        byte[] resultString = null;
        // ZALog.d("GetMD5Code = " + new String(strObj));
        try {
            MessageDigest md = MessageDigest.getInstance(type.toString());
            md.update(strObj);
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = md.digest();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return ConvertByteToString(resultString);
    }

    public static String GetHashCode(String strObj, String type) {
        String result = GetHashCode(strObj.getBytes(Charset.forName("ASCII")), type);
        return result;
    }
}
