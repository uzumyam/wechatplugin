package com.lw.wechatplugin.vo;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class WxMessageVo {

    public String talker;
    public String content;
    public Integer messageType;
    public Integer other;
    public Boolean senderType;

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public Boolean getSenderType() {
        return senderType;
    }

    public void setSenderType(Boolean senderType) {
        this.senderType = senderType;
    }

    @Override
    public String toString() {
        return "WxMessageVo{" +
                "talker='" + talker + '\'' +
                ", content='" + content + '\'' +
                ", messageType=" + messageType +
                ", other=" + other +
                ", senderType=" + senderType +
                '}';
    }
}
