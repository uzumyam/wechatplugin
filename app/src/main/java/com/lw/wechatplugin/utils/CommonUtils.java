package com.lw.wechatplugin.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lw.wechatplugin.VersionParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by admin on 2017/10/26.
 */

public class CommonUtils {

    public static final String TAG = "CommonUtils";

    /**
     * post网络请求
     * @param urlpost
     * @param postJson
     * @return
     */
    public static String httpPost(String urlpost, String postJson){

        OutputStreamWriter streamWriter = null;
        BufferedReader bufferedReader = null;
        String result = "";
        try {
            URL url = new URL(urlpost);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");  //设置发送的数据格式
            connection.setRequestProperty("Accept", "application/json");    //设置接收的数据格式
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.connect();
            streamWriter = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
            streamWriter.write(postJson);
            streamWriter.flush();

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(streamWriter != null){
                try {
                    streamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 图灵机器人回复
     * @param info
     * @return
     */
    public static String getTulingReply(String info){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", VersionParam.TULING_APPKEY);
            jsonObject.put("userid", VersionParam.TULING_UID);
            jsonObject.put("info", info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String postJson = jsonObject.toString();
        String resultStr = httpPost(VersionParam.TULING_API_URL, postJson);
        try {
            JSONObject resultJson = new JSONObject(resultStr);
            String code = resultJson.optString("code");
            String result = resultJson.optString("text");
            if("100000".equals(code)){
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "抱歉，暂时不能回复您";
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

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static void commandCommonSU(final String command) {
        boolean ret = false;
        Process process = null;
        DataOutputStream os = null;
        BufferedReader is = null;
        try {
            Log.i(TAG, "start su");
            process = Runtime.getRuntime().exec("su");

            os = new DataOutputStream(process.getOutputStream());
            Log.i(TAG, "command = " + command);
            os.writeBytes(command + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            ret = (process.waitFor() == 0);
        } catch (Exception e) {
            Log.w(TAG, e.getLocalizedMessage(), e);
            ret = false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    Log.w(TAG, e.getLocalizedMessage(), e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    Log.w(TAG, e.getLocalizedMessage(), e);
                }
            }
            try {
                process.destroy();
            } catch (Exception e) {
                Log.w(TAG, e.getLocalizedMessage(), e);
            }
        }
        if (ret) {
            Log.d(TAG, "Root SUC success!");
        } else {
            Log.d(TAG, "Root SUC fail!");
        }
    }
}
