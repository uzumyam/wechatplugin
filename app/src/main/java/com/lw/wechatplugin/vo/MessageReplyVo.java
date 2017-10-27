package com.lw.wechatplugin.vo;

import com.lw.wechatplugin.VersionParam;

/**
 * Created by admin on 2017/10/26.
 */

public class MessageReplyVo {
    private String key;
    private String userid;
    private String info;

    public MessageReplyVo(){

    }

    public MessageReplyVo(String info){
        this.key = VersionParam.TULING_APPKEY;
        this.userid = VersionParam.TULING_UID;
        this.info = info;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
