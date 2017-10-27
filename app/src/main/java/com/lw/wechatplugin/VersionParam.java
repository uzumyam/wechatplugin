package com.lw.wechatplugin;


public class VersionParam {

    public static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";

    public static final String WX_ROOT_PATH = "/data/data/com.tencent.mm/";
    private static final String WX_DB_DIR_PATH = WX_ROOT_PATH + "MicroMsg";

    private static final String WX_DB_FILE_NAME = "EnMicroMsg.db";

    private static final String WX_SP_UIN_PATH = WX_ROOT_PATH + "shared_prefs/auth_info_key_prefs.xml";

    private static final String localDbPath = WX_DB_DIR_PATH + "EnMicroMsg.db";

    public static final String TULING_API_URL = "http://www.tuling123.com/openapi/api";
    public static final String TULING_APPKEY = "7e918415cdfd4bcab042dd73a550c663";
    public static final String TULING_UID = "161409";

    public static String receiveUIFunctionName = "d";

    /**
     * last param of receiveUIFunctionName for class luckyMoneyReceiveUI
     */
    public static String receiveUIParamName = "com.tencent.mm.ac.k";

    /**
     * Search MMCore has not been initialize ?
     */
    public static String networkRequest = "com.tencent.mm.x.ap";

    /**
     * Search MMCore has not been initialize ? next function of networkRequest
     */
    public static String getNetworkByModelMethod = "wT";

    /**
     * Search get value failed, %s", e.getMessage(), having field_talker
     */
    public static String getMessageClass = "com.tencent.mm.g.b.ce";

    public static String luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.En_fba4b94f";

    /**
     * Search jSONObject.optString("timingIdentifier")
     */
    public static String receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.b.ae";


    /**
     * Search hashMap.put("timingIdentifier", str
     */
    public static String luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.b.ab";


    public static boolean hasTimingIdentifier = false;


    public static void init(String version) {
        switch (version) {
            case "6.3.22":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.t.j";
                networkRequest = "com.tencent.mm.model.ah";
                getNetworkByModelMethod = "tF";
                getMessageClass = "com.tencent.mm.e.b.bj";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                break;
            case "6.3.23":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.t.j";
                networkRequest = "com.tencent.mm.model.ah";
                getNetworkByModelMethod = "vE";
                getMessageClass = "com.tencent.mm.e.b.bl";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                break;
            case "6.3.25":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.t.j";
                networkRequest = "com.tencent.mm.model.ah";
                getNetworkByModelMethod = "vF";
                getMessageClass = "com.tencent.mm.e.b.bl";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                break;
            case "6.3.27":
                receiveUIFunctionName = "e";
                receiveUIParamName = "com.tencent.mm.u.k";
                networkRequest = "com.tencent.mm.model.ah";
                getNetworkByModelMethod = "yj";
                getMessageClass = "com.tencent.mm.e.b.br";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                break;
            case "6.3.28":
                receiveUIFunctionName = "c";
                receiveUIParamName = "com.tencent.mm.v.k";
                networkRequest = "com.tencent.mm.model.ah";
                getNetworkByModelMethod = "vP";
                getMessageClass = "com.tencent.mm.e.b.bu";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                break;
            case "6.3.30":
            case "6.3.31":
                receiveUIFunctionName = "c";
                receiveUIParamName = "com.tencent.mm.v.k";
                networkRequest = "com.tencent.mm.model.ah";
                getNetworkByModelMethod = "vS";
                getMessageClass = "com.tencent.mm.e.b.bv";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                break;
            case "6.3.32":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.v.k";
                networkRequest = "com.tencent.mm.model.ak";
                getNetworkByModelMethod = "vw";
                getMessageClass = "com.tencent.mm.e.b.by";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                break;
            case "6.5.3":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.v.k";
                networkRequest = "com.tencent.mm.model.ak";
                getNetworkByModelMethod = "vy";
                getMessageClass = "com.tencent.mm.e.b.bx";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
                break;
            case "6.5.4":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.u.k";
                networkRequest = "com.tencent.mm.model.ak";
                getNetworkByModelMethod = "vy";
                getMessageClass = "com.tencent.mm.e.b.by";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                hasTimingIdentifier = true;
                break;
            case "6.5.6":
            case "6.5.7":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.u.k";
                networkRequest = "com.tencent.mm.model.al";
                getNetworkByModelMethod = "vM";
                getMessageClass = "com.tencent.mm.e.b.by";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.En_fba4b94f";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                hasTimingIdentifier = true;
                break;
            case "6.5.8":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.w.k";
                networkRequest = "com.tencent.mm.model.an";
                getNetworkByModelMethod = "uC";
                getMessageClass = "com.tencent.mm.e.b.ca";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.En_fba4b94f";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                hasTimingIdentifier = true;
                break;
            case "6.5.10":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.w.k";
                networkRequest = "com.tencent.mm.s.ao";
                getNetworkByModelMethod = "uH";
                getMessageClass = "com.tencent.mm.e.b.cd";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.En_fba4b94f";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.c.ab";
                hasTimingIdentifier = true;
                break;
            case "6.5.13":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.y.k";
                networkRequest = "com.tencent.mm.u.ap";
                getNetworkByModelMethod = "vd";
                getMessageClass = "com.tencent.mm.e.b.ce";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.b.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.En_fba4b94f";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.b.ab";
                hasTimingIdentifier = true;
                break;
            case "6.5.16":
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.ac.k";
                networkRequest = "com.tencent.mm.x.ap";
                getNetworkByModelMethod = "wT";
                getMessageClass = "com.tencent.mm.g.b.ce";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.b.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.En_fba4b94f";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.b.ab";
                hasTimingIdentifier = true;
                break;
            default:
                receiveUIFunctionName = "d";
                receiveUIParamName = "com.tencent.mm.ac.k";
                networkRequest = "com.tencent.mm.x.ap";
                getNetworkByModelMethod = "wT";
                getMessageClass = "com.tencent.mm.g.b.ce";
                receiveLuckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.b.ae";
                luckyMoneyReceiveUI = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.En_fba4b94f";
                luckyMoneyRequest = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.b.ab";
                hasTimingIdentifier = true;
        }
    }
}
