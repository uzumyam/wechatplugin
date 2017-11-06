package com.lw.wechatplugin.hook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import com.lw.wechatplugin.db.WechatDbHelper;
import com.lw.wechatplugin.utils.CommonUtils;
import com.lw.wechatplugin.utils.PreferencesUtils;
import com.lw.wechatplugin.VersionParam;
import com.lw.wechatplugin.vo.WxMessageVo;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

/**
 * 微信hook关键类
 * Created by Administrator on 2017/10/19 0019.
 */

public class WxHook {

    public static final int MSG_CODE = 0x101;

    Context context;

    private WechatDbHelper wechatDbHelper;

    private LinkedBlockingDeque<WxMessageVo> blockingDeque = new LinkedBlockingDeque<>(500);

    private LoadPackageParam loadPackageParam;

    public WxHook(LoadPackageParam loadPackageParam, Context context, String versionName) {
        this.loadPackageParam = loadPackageParam;
        startMessageLooperThread();
    }

    public void startMessageLooperThread(){
       new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        WxMessageVo messageVo = blockingDeque.take();
                        messageHandler.sendMessage(messageHandler.obtainMessage(MSG_CODE, messageVo));
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public Handler messageHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_CODE:
                    WxMessageVo messageVo = (WxMessageVo) msg.obj;
                    if(messageVo.getMessageType().intValue() == 436207665 || messageVo.getMessageType().intValue() == 469762097){   //收到红包
                        if(PreferencesUtils.luckyMoneySet()){   //开启了自动收红包
                            log("收到红包，跳转到红包页面=============== ");
                            Toast.makeText(context, "收到红包，跳转到红包页面", Toast.LENGTH_SHORT).show();
                            int i;
                            String a = c(messageVo.getContent());
                            Intent intent = new Intent();
                            intent.setClassName(VersionParam.WECHAT_PACKAGE_NAME, "com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f");
                            if (messageVo.getTalker().endsWith("@chatroom")) {
                                i = 0;
                            } else {
                                i = 1;
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("key_way", i);
                            intent.putExtra("key_native_url", a);
                            intent.putExtra("key_username", messageVo.getTalker());
                            context.startActivity(intent);
                        }
                    }else if(messageVo.getMessageType().intValue() == 419430449) {  //收到转账
                        if(PreferencesUtils.autoReceiptSet()){  //开启了自动收账
                            log("收到转账，跳转到转账页面=============== ");
                            Toast.makeText(context, "收到转账，跳转到转账页面", Toast.LENGTH_SHORT).show();
                            String invalidTime = getFromXml(messageVo.getContent(), "invalidtime");
                            String transactionId = getFromXml(messageVo.getContent(), "transcationid");
                            String transferid = getFromXml(messageVo.getContent(), "transferid");
                            String money = getFromXml(messageVo.getContent(), "feedesc");
                            Intent intent = new Intent();
                            intent.setClassName(VersionParam.WECHAT_PACKAGE_NAME, "com.tencent.mm.plugin.remittance.ui.RemittanceDetailUI");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("invalid_time", invalidTime);
                            intent.putExtra("transaction_id", transactionId);
                            intent.putExtra("transfer_id", transferid);
                            intent.putExtra("sender_name", messageVo.getTalker());
                            Toast.makeText(context, "转账金额为 " + money, Toast.LENGTH_SHORT).show();
                            context.startActivity(intent);
                        }
                    }else if(messageVo.getMessageType().intValue() == 1){   //文字聊天
                        if(PreferencesUtils.autoReplySet()){    //开启了文字聊天自动回复
                            log("收到文字聊天消息，跳转到聊天页面");
                            Intent intent = new Intent();
                            intent.setClassName(VersionParam.WECHAT_PACKAGE_NAME, "com.tencent.mm.ui.chatting.En_5b8fbb1e");
                            intent.putExtra("nofification_type", "new_msg_nofification");
                            intent.putExtra("MainUI_User_Last_Msg_Type", messageVo.getMessageType());
                            intent.putExtra("Intro_Is_Muti_Talker", false);
                            intent.putExtra("Chat_User", messageVo.getTalker());
                            intent.putExtra("Msg_Content", messageVo.getContent());   //自己添加的，用于回复的聊天内容
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            context.startActivity(intent);
                        }
                    }else if(messageVo.getMessageType().intValue() == 3){   //图片聊天，转账
                        if(PreferencesUtils.autoTransferSet()){     //开启了自动转账
                            Intent intent = new Intent();
                            intent.setClassName(VersionParam.WECHAT_PACKAGE_NAME, "com.tencent.mm.plugin.remittance.ui.RemittanceUI");
                            intent.putExtra("pay_scene", 31);
                            intent.putExtra("pay_channel", 11);
                            intent.putExtra("receiver_name", messageVo.getTalker());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 微信红包hook逻辑
     *
     * @param loadPackageParam
     */
    public void luckyMoneyHook(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.tencent.mm.booter.notification.b", loadPackageParam.classLoader, "a", new Object[]{String.class, String.class, Integer.TYPE, Integer.TYPE, Boolean.TYPE, new XC_MethodHook() {

            protected void beforeHookedMethod(MethodHookParam methodHookParam) {

            }

            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                super.afterHookedMethod(methodHookParam);
                l(loadPackageParam);
                log(" talker=============== " + methodHookParam.args[0].toString());
                log(" content=============== " + methodHookParam.args[1].toString());
                log(" messageType=============== " + methodHookParam.args[2].toString());
                //消息为自己发出的isSender为true,消息为别人发出的isSender为false
                log(" isSender=============== " + methodHookParam.args[4].toString());

                log(" nickname=============== " + wechatDbHelper.getNickname(methodHookParam.args[0].toString()));

                WxMessageVo wxMessageVo = new WxMessageVo();
                wxMessageVo.setTalker(methodHookParam.args[0].toString());
                wxMessageVo.setContent(methodHookParam.args[1].toString());
                wxMessageVo.setMessageType(Integer.parseInt(methodHookParam.args[2].toString()));
                wxMessageVo.setOther(Integer.parseInt(methodHookParam.args[3].toString()));
                wxMessageVo.setSenderType(Boolean.parseBoolean(methodHookParam.args[4].toString()));

                blockingDeque.put(wxMessageVo);

//                if (methodHookParam.args[2].toString().equals("436207665") || methodHookParam.args[2].toString().equals("469762097")) {   //接收到红包
//                    if(PreferencesUtils.luckyMoneySet()){   //开启了自动收红包
//                        log("收到红包，跳转到红包页面=============== ");
//                        Toast.makeText(context, "收到红包，跳转到红包页面", Toast.LENGTH_SHORT).show();
//                        int i;
//                        String a = c(methodHookParam.args[1].toString());
//                        Intent intent = new Intent();
//                        intent.setClassName(VersionParam.WECHAT_PACKAGE_NAME, "com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f");
//                        if (methodHookParam.args[0].toString().endsWith("@chatroom")) {
//                            i = 0;
//                        } else {
//                            i = 1;
//                        }
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("key_way", i);
//                        intent.putExtra("key_native_url", a);
//                        intent.putExtra("key_username", methodHookParam.args[0].toString());
//                        context.startActivity(intent);
//                    }
//                } else if (methodHookParam.args[2].toString().equals("419430449")) {   //收到转账
//                    if(PreferencesUtils.autoReceiptSet()){  //开启了自动收账
//                        log("收到转账，跳转到转账页面=============== ");
//                        Toast.makeText(context, "收到转账，跳转到转账页面", Toast.LENGTH_SHORT).show();
//                        String invalidTime = getFromXml(methodHookParam.args[1].toString(), "invalidtime");
//                        String transactionId = getFromXml(methodHookParam.args[1].toString(), "transcationid");
//                        String transferid = getFromXml(methodHookParam.args[1].toString(), "transferid");
//                        String money = getFromXml(methodHookParam.args[1].toString(), "feedesc");
//                        Intent intent = new Intent();
//                        intent.setClassName(VersionParam.WECHAT_PACKAGE_NAME, "com.tencent.mm.plugin.remittance.ui.RemittanceDetailUI");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("invalid_time", invalidTime);
//                        intent.putExtra("transaction_id", transactionId);
//                        intent.putExtra("transfer_id", transferid);
//                        intent.putExtra("sender_name", methodHookParam.args[0].toString());
//                        Toast.makeText(context, "转账金额为 " + money, Toast.LENGTH_SHORT).show();
//                        context.startActivity(intent);
//                    }
//                } else if (methodHookParam.args[2].toString().equals("1")) {   //收到文字消息
//                    if(PreferencesUtils.autoReplySet()){    //开启了文字聊天自动回复
//                        log("收到文字聊天消息，跳转到聊天页面");
//                        Intent intent = new Intent();
//                        intent.setClassName(VersionParam.WECHAT_PACKAGE_NAME, "com.tencent.mm.ui.chatting.En_5b8fbb1e");
//                        intent.putExtra("nofification_type", "new_msg_nofification");
////					intent.putExtra("Intro_Bottle_unread_count", 0);
//                        intent.putExtra("MainUI_User_Last_Msg_Type", methodHookParam.args[2].toString());
//                        intent.putExtra("Intro_Is_Muti_Talker", false);
//                        intent.putExtra("Chat_User", methodHookParam.args[0].toString());
//                        intent.putExtra("Msg_Content", methodHookParam.args[1].toString());   //自己添加的，用于回复的聊天内容
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        context.startActivity(intent);
//                    }
////                    exec(new String[]{"am", "start", "-n", "com.tencent.mm/.ui.chatting.En_5b8fbb1e ", "-e", "Chat_User", methodHookParam.args[0].toString(), "Msg_Content", methodHookParam.args[1].toString(),
////                            "--ei", "_auto_finish", "2000", "--user", String.valueOf(android.os.Process.myUserHandle().hashCode())});
//
//                            //直接回复消息
////                    Object requestCaller = callStaticMethod(findClass(VersionParam.networkRequest, loadPackageParam.classLoader), VersionParam.getNetworkByModelMethod);
////                    callMethod(requestCaller, "a", newInstance(findClass("com.tencent.mm.aj.a", loadPackageParam.classLoader), methodHookParam.args[0].toString(), "direct reply"),0);
//                } else if (methodHookParam.args[2].toString().equals("3")) {   //收到图片消息
//                    if(PreferencesUtils.autoTransferSet()){     //开启了自动转账
//                        Intent intent = new Intent();
//                        intent.setClassName(VersionParam.WECHAT_PACKAGE_NAME, "com.tencent.mm.plugin.remittance.ui.RemittanceUI");
//                        intent.putExtra("pay_scene", 31);
//                        intent.putExtra("pay_channel", 11);
//                        intent.putExtra("receiver_name", methodHookParam.args[0].toString());
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent);
//                    }
//                } else if (methodHookParam.args[2].toString().equals("34")) {  //收到语音消息
//
//                }
            }
        }});

        //红包hook
        Class class1 = findClass("com.tencent.mm.ac.k", loadPackageParam.classLoader);
        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f", loadPackageParam.classLoader, "d", new Object[]{Integer.TYPE, Integer.TYPE, String.class, class1, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws InterruptedException {
                if (PreferencesUtils.luckyMoneySet()) {
                    l(loadPackageParam);
                    Button button = (Button) getObjectField(methodHookParam.thisObject, "nzF");
                    if (button.isShown()) {
                        Thread.sleep((long) new Random().nextInt(500));
                        button.performClick();
                    } else {
                        callMethod(methodHookParam.thisObject, "finish", new Object[0]);
                        Toast.makeText(context, "(^O^)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }});
        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI", loadPackageParam.classLoader, "onResume", new XC_MethodHook() {

            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                if (PreferencesUtils.luckyMoneySet()) {
                    try {
                        super.afterHookedMethod(methodHookParam);
                        log("======LuckyMoneyDetailUI========");
                        TextView moneyTxt = (TextView) getObjectField(methodHookParam.thisObject, "nAx");
                        Toast.makeText(context, "你所领取的红包金额为 " + moneyTxt.getText().toString(), Toast.LENGTH_SHORT).show();
                        log("=====money======= " + moneyTxt.getText().toString());
                        //todo 红包金额上传逻辑处理

                        callMethod(methodHookParam.thisObject, "finish", new Object[0]);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        });

        //转账hook
        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.remittance.ui.RemittanceDetailUI", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws InterruptedException {
                Activity activity = (Activity) methodHookParam.thisObject;
                log("appmsg_type==========" + activity.getIntent().getIntExtra("appmsg_type", 0));
                log("invalid_time==========" + activity.getIntent().getIntExtra("invalid_time", 0));
                log("transaction_id==========" + activity.getIntent().getStringExtra("transaction_id"));
                log("bill_id==========" + activity.getIntent().getStringExtra("bill_id"));
                log("transfer_id==========" + activity.getIntent().getStringExtra("transfer_id"));
                log("sender_name==========" + activity.getIntent().getStringExtra("sender_name"));
                log("effective_date==========" + activity.getIntent().getIntExtra("effective_date", 3));
                log("is_sender==========" + activity.getIntent().getBooleanExtra("is_sender", false));
                log("total_fee==========" + activity.getIntent().getIntExtra("total_fee", 0));
            }
        });
        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.remittance.ui.RemittanceDetailUI", loadPackageParam.classLoader, "d", new Object[]{Integer.TYPE, Integer.TYPE, String.class, class1, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws InterruptedException {
                if (PreferencesUtils.autoReceiptSet()) {
                    l(loadPackageParam);
                    Button button = (Button) getObjectField(methodHookParam.thisObject, "oYL");
                    if (button.isShown()) {
                        Thread.sleep((long) new Random().nextInt(500));
                        button.performClick();
                    } else {
                        callMethod(methodHookParam.thisObject, "finish", new Object[0]);
                    }
                }
            }
        }});

        //自动转账hook
        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.remittance.ui.RemittanceUI", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws InterruptedException {
                if (PreferencesUtils.autoTransferSet()) {
                    Activity activity = (Activity) methodHookParam.thisObject;
                    log("pay_scene==========" + activity.getIntent().getIntExtra("pay_scene", 31));
                    log("scan_remittance_id==========" + activity.getIntent().getStringExtra("scan_remittance_id"));
                    log("fee==========" + activity.getIntent().getDoubleExtra("fee", 0.0d));
                    log("receiver_name==========" + activity.getIntent().getStringExtra("receiver_name"));
                    log("receiver_nick_name==========" + activity.getIntent().getStringExtra("receiver_nick_name"));
                    log("receiver_true_name==========" + activity.getIntent().getStringExtra("receiver_true_name"));
                    log("pay_channel==========" + activity.getIntent().getIntExtra("pay_channel", 0));

                    log("开始准备转账++++++++++++");
                    Object object = getObjectField(methodHookParam.thisObject, "kNI");
                    callMethod(object, "setText", "0.02");
                    Button transfer = (Button) getObjectField(methodHookParam.thisObject, "lsT");
                    if (transfer != null) {
                        transfer.performClick();
                    }
                    log("跳转输入密码界面++++++++++++");
                }
            }
        });

        XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.wallet.pay.ui.WalletPayUI", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws InterruptedException {
                Activity activity = (Activity) methodHookParam.thisObject;
                Bundle bundle = activity.getIntent().getExtras();
                for (String key : bundle.keySet()) {
                    log(key + "==========" + bundle.get(key).toString());
                }
                XposedHelpers.findAndHookMethod("com.tenpay.android.wechat.TenpaySecureEditText", loadPackageParam.classLoader, "init", Context.class, AttributeSet.class, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                if (PreferencesUtils.autoTransferSet()) {
                                    log("开始输入密码++++++++++++");
                                    final View object = (View) param.thisObject;
                                    object.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String pwd = PreferencesUtils.transferPwd();
                                            callMethod(object, "setText", pwd);
                                            log("begin to transfer money+++++++++++++");
                                            object.performClick();
                                            log("end of transfer money+++++++++++++");
                                        }
                                    }, 2000);
                                    log("输入密码完毕++++++++++++");
                                }
                            }
                        }
                );

            }
        });

        //文字聊天
        XposedHelpers.findAndHookMethod("com.tencent.mm.ui.chatting.En_5b8fbb1e", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) throws InterruptedException {
                if (PreferencesUtils.autoReplySet()) {
                    Activity activity = (Activity) methodHookParam.thisObject;
                    final String content = activity.getIntent().getStringExtra("Msg_Content");
                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    Object sendObject = getObjectField(methodHookParam.thisObject, "vUO");
                    String replyMessage = "";

                    if (PreferencesUtils.robotReplySet()) {   //机器人回复优先级最高
                        //todo 回复机器人聊天语句
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
                        StrictMode.setThreadPolicy(policy);
                        replyMessage = CommonUtils.getTulingReply(content);
                    } else {
                        if (PreferencesUtils.fixedReplySet()) {
                            replyMessage = PreferencesUtils.fixedMsgSet();
                        }
                    }

                    if (!TextUtils.isEmpty(replyMessage)) {
                        XposedBridge.log("需要回复的聊天内容为=========" + replyMessage);
                        XposedBridge.log("开始发送消息=========");
                        try {
                            callMethod(sendObject, "Vo", replyMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        XposedBridge.log("开始关闭聊天页面=========");
                        callMethod(sendObject, "finish", new Object[0]);
                        XposedBridge.log("发送消息结束=========");
                    }
                }
            }
        });

        XposedHelpers.findAndHookConstructor("com.tencent.mm.storage.ad", loadPackageParam.classLoader, "com.tencent.mm.bt.g", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (null == wechatDbHelper)
                    wechatDbHelper = new WechatDbHelper(param.args[0]);
                }
         });

    }

//        XposedHelpers.findAndHookMethod(this.q.bh, loadPackageParam.classLoader, this.q.aG, new Object[]{Boolean.TYPE, new XC_MethodHook() {
//
//            protected void afterHookedMethod(MethodHookParam methodHookParam) {
//
//                XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI", loadPackageParam.classLoader, "onCreate", new Object[]{Bundle.class, new XC_MethodHook() {
//
//                    protected void afterHookedMethod(MethodHookParam methodHookParam) {
//                        try {
//                            super.afterHookedMethod(methodHookParam);
//                            XposedBridge.log("======LuckyMoneyDetailUI========");
//                        } catch (Throwable throwable) {
//                            throwable.printStackTrace();
//                        }
//                    }
//                }});
//                Class findClass = XposedHelpers.findClass("com.tencent.mm.ac.k", loadPackageParam.classLoader);
//                XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f", loadPackageParam.classLoader, "d", new Object[]{Integer.TYPE, Integer.TYPE, String.class, findClass, new XC_MethodHook() {
//                    protected void afterHookedMethod(MethodHookParam methodHookParam) throws InterruptedException {
//                        l(loadPackageParam);
//                        Button button = (Button) XposedHelpers.getObjectField(methodHookParam.thisObject, "nzF");
//                        if (button.isShown()) {
//                            Thread.sleep((long) new Random().nextInt(500));
//                            button.performClick();
//                        } else {
//                            XposedHelpers.callMethod(methodHookParam.thisObject, "finish", new Object[0]);
//                            Toast.makeText(context, "(^O^)", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }});
//            }
//        }});

    private void l(LoadPackageParam loadPackageParam) {
        if (this.context == null) {
            this.context = (Context) callStaticMethod(findClass("com.tencent.mm.sdk.platformtools.ab", loadPackageParam.classLoader), "getContext", new Object[0]);
            if (this.context != null) {
                this.context = this.context.getApplicationContext();
            }
        }
    }

    private String c(String str) {
        String substring = str.substring(str.indexOf("<msg>"));
        String str2 = "";
        try {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            XmlPullParser newPullParser = newInstance.newPullParser();
            newPullParser.setInput(new StringReader(substring));
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType == 2 && newPullParser.getName().equals("nativeurl")) {
                    newPullParser.nextToken();
                    return newPullParser.getText();
                }
            }
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    private String getFromXml(String xmlmsg, String node) {
        String xl = xmlmsg.substring(xmlmsg.indexOf("<msg>"));
        String result = "";
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser pz = factory.newPullParser();
            pz.setInput(new StringReader(xl));
            int v = pz.getEventType();
            while (v != XmlPullParser.END_DOCUMENT) {
                if (v == XmlPullParser.START_TAG) {
                    if (pz.getName().equals(node)) {
                        pz.nextToken();
                        return pz.getText();
                    }
                }
                v = pz.next();
            }
            return result;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }


}
