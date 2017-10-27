package com.lw.wechatplugin.utils;


import de.robv.android.xposed.XSharedPreferences;

public class PreferencesUtils {

    private static XSharedPreferences instance = null;

    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences(PreferencesUtils.class.getPackage().getName());
            instance.makeWorldReadable();
        } else {
            instance.reload();
        }
        return instance;
    }

    /**
     * 自动收红包开关项
     * @return
     */
    public static boolean luckyMoneySet() {
        return getInstance().getBoolean("open_lucky_money", true);
    }

    /**
     * 自动回复聊天开关项
     * @return
     */
    public static boolean autoReplySet() {
        return getInstance().getBoolean("auto_reply", true);
    }

    /**
     * 机器人回复聊天开关项
     * @return
     */
    public static boolean robotReplySet() {
        return getInstance().getBoolean("robot_reply", false);
    }

    /**
     * 固定语回复聊天开关项
     * @return
     */
    public static boolean fixedReplySet() {
        return getInstance().getBoolean("fixed_reply", true);
    }

    /**
     * 聊天固定语设置
     * @return
     */
    public static String fixedMsgSet() {
        return getInstance().getString("reply_msg", "");
    }

    /**
     * 自动收款开关项
     * @return
     */
    public static boolean autoReceiptSet() {
        return getInstance().getBoolean("auto_receipt", true);
    }

    /**
     * 自动转账开关项
     * @return
     */
    public static boolean autoTransferSet() {
        return getInstance().getBoolean("auto_transfer", true);
    }

    public static String transferPwd(){
        return getInstance().getString("transfer_setting", "");
    }


}


