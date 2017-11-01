package cn.laobancha.www.weixinhooker.core;

import android.content.Context;
import android.content.pm.PackageManager;

import com.lw.wechatplugin.BuildConfig;

import java.io.File;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class Sender {

    public static Context context;

    public static class SenderHolder {
        public static final Sender INSTANCE = new Sender(context);
    }

    private Sender(Context context){
        Context hookerPackageContext = null;
        try {
            hookerPackageContext = context.createPackageContext(BuildConfig.APPLICATION_ID, Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        System.load(new File(hookerPackageContext.getApplicationInfo().nativeLibraryDir, "libsmloader.so").getAbsolutePath());
        File file = new File(hookerPackageContext.getApplicationInfo().nativeLibraryDir, "libsmweixin.so");
//        File file2 = new File(hookerPackageContext.getApplicationInfo().nativeLibraryDir, "libsmsilk-codec.so");
//        File file3 = new File(hookerPackageContext.getApplicationInfo().nativeLibraryDir, "libsmamr-codec.so");
        System.load(file.getAbsolutePath());
//        System.load(file2.getAbsolutePath());
//        System.load(file3.getAbsolutePath());
    }

    public static Sender getInstance(Context contextRef){
        context = contextRef;
        return SenderHolder.INSTANCE;
    }

    public native int sendText(String str, String str2, int i);

}
