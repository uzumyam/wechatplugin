package com.lw.wechatplugin.vo;

/**
 * Created by admin on 2017/11/8.
 */

public class RechargeRspVo {
    private boolean valid;
    private String content;
    private String entity;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "RechargeRspVo{" +
                "valid=" + valid +
                ", content='" + content + '\'' +
                ", entity='" + entity + '\'' +
                '}';
    }
}
