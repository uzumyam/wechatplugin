package com.lw.wechatplugin;

import com.lw.wechatplugin.utils.CommonUtils;
import com.lw.wechatplugin.vo.RechargeRspVo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by admin on 2017/10/18.
 */

public class DbTest {
    public static void main(String[] args){
//        String imei = "864517030958738";
//        String uin = "151471781";
//        System.out.println(md5(imei + uin).substring(0, 7));
        String content = CommonUtils.httpGet("http://123.207.29.254:8080/WS/ExchangeInterface.ashx?action=Recharge&nickName=%E5%B0%86%E5%86%9B&gameId=614283&rechargeMoney=&strInsurePass=21218CCA77804D2BA1922C33E0151105");
        RechargeRspVo rechargeRspVo = CommonUtils.getRechargeRsp(content);
        System.out.println(rechargeRspVo.toString());
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
