package com.lw.wechatplugin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.lw.wechatplugin.vo.MessageReplyVo;

/**
 * Created by admin on 2017/10/26.
 */

public class CommonUtils {
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
}
