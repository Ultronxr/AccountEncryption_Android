package com.ultronxr.accountencryption.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {

    public static String stringToMd5LowerCase(String str){

        String resString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] inputBytes = str.getBytes("UTF-8");
            byte[] buffBytes = md.digest(inputBytes);

            resString = bytesToHex(buffBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return resString;

    }

    public static String stringToMd5UpperCase(String str){
        return stringToMd5LowerCase(str).toUpperCase();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toLowerCase();
    }

}
