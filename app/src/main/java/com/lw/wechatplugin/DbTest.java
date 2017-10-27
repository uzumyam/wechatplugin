package com.lw.wechatplugin;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by admin on 2017/10/18.
 */

public class DbTest {
    public static void main(String[] args){
        String imei = "864517030958738";
        String uin = "151471781";
        System.out.println(md5(imei + uin).substring(0, 7));
    }

    public static String md5(String content){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(content.getBytes("utf-8"));
            byte[] values = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<values.length;i++){
                String c = Integer.toHexString(0xff & values[i]);
                if(c.length() == 1){
                    sb.append("0").append(c);
                }else{
                    sb.append(c);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
