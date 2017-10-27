package com.lw.wechatplugin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import com.lw.wechatplugin.hook.WxHook;
import com.lw.wechatplugin.utils.WxUtils;
import com.lw.wechatplugin.vo.WxContactVo;

import java.util.List;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static com.lw.wechatplugin.VersionParam.WECHAT_PACKAGE_NAME;


public class Main implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) {
        if (lpparam.packageName.equals(WECHAT_PACKAGE_NAME)) {
            try {
                Context context = (Context) callMethod(callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
                String versionName = context.getPackageManager().getPackageInfo(lpparam.packageName, 0).versionName;
                log("Found wechat version:" + versionName);
//                WxUtils.getInstance().fetchPublicAccountList(context);
//                List<WxContactVo> wxContactVoList = WxUtils.getInstance().getContactVoList();
//                if(wxContactVoList != null && wxContactVoList.size() > 0){
//                    for(int i=0;i<wxContactVoList.size();i++){
//                        XposedBridge.log("Setting" + wxContactVoList.get(i).toString());
//                    }
//                }else{
//                    XposedBridge.log("Setting" + "wxContact read null=====================");
//                }
                WxHook wxHook = new WxHook(context, versionName);
                wxHook.luckyMoneyHook(lpparam);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            new HideModule().hide(lpparam);
        }
    }
}
