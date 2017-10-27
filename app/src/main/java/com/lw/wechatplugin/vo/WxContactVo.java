package com.lw.wechatplugin.vo;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class WxContactVo {

    private String username;

    private String alias;

    private String conRemark;

    private String nickname;

    private Integer type;

    private String encryptUsername;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getConRemark() {
        return conRemark;
    }

    public void setConRemark(String conRemark) {
        this.conRemark = conRemark;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEncryptUsername() {
        return encryptUsername;
    }

    public void setEncryptUsername(String encryptUsername) {
        this.encryptUsername = encryptUsername;
    }

    @Override
    public String toString() {
        return "WxContactVo{" +
                "username='" + username + '\'' +
                ", alias='" + alias + '\'' +
                ", conRemark='" + conRemark + '\'' +
                ", nickname='" + nickname + '\'' +
                ", type=" + type +
                ", encryptUsername='" + encryptUsername + '\'' +
                '}';
    }
}
