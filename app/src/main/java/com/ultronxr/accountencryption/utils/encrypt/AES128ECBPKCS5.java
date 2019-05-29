package com.ultronxr.accountencryption.utils.encrypt;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES128ECBPKCS5 {

    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static Cipher cipherForEncrypt;
    private static Cipher cipherForDecrypt;

    /**
     * 设置用来加密的密码
     * @param myKey 指定的密码
     */
    public static void setSecretKey(String myKey){

        MessageDigest sha = null;
        try{
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");

            cipherForEncrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherForEncrypt.init(Cipher.ENCRYPT_MODE, secretKey);
            cipherForDecrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherForDecrypt.init(Cipher.DECRYPT_MODE, secretKey);
        }catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * 对字符串进行加密
     * @param strToEncrypt 待加密的字符串
     * @return 加密后的结果，以字符串返回
     */
    public static String encryptString(String strToEncrypt){

        try{ return Base64.getEncoder().encodeToString(cipherForEncrypt.doFinal(strToEncrypt.getBytes("UTF-8"))); }
        catch(Exception e) { e.printStackTrace(); }
        return "";
    }

    /**
     * 对字节数组中的数据进行加密
     * @param bytesToEncrypt 待加密的字节数组
     * @return 加密后的结果，字节数组返回
     */
    private static byte[] encryptBytes(byte[] bytesToEncrypt){

        try{ return cipherForEncrypt.doFinal(bytesToEncrypt); }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }


    /**
     * 对字符串进行解密
     * @param strToDecrypt 待解密的字符串
     * @return 解密后的结果，以字符串返回
     */
    public static String decryptString(String strToDecrypt){

        try{ return new String(cipherForDecrypt.doFinal(Base64.getDecoder().decode(strToDecrypt))); }
        catch (Exception e) { e.printStackTrace(); }
        return "";
    }

    /**
     * 对字节数组进行解密
     * @param bytesToDecrypt 待解密的字节数组
     * @return 解密后的结果，以字节数组返回
     */
    private static byte[] decryptBytes(byte[] bytesToDecrypt){

        try { return cipherForDecrypt.doFinal(bytesToDecrypt); }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

}
