package com.lw.wechatplugin.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lw.wechatplugin.vo.WxContactVo;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class WxUtils {
    public static final String TAG = "WeChatData";

    public static final String WX_ROOT_PATH = "/data/data/com.tencent.mm/";

    private static final String WX_DB_DIR_PATH = WX_ROOT_PATH + "MicroMsg";

    private static final String WX_DB_FILE_NAME = "EnMicroMsg.db";

    private static final String WX_SP_UIN_PATH = WX_ROOT_PATH + "shared_prefs/auth_info_key_prefs.xml";

    private static final String LOCAL_DB_PATH = WX_DB_DIR_PATH + "/" + "EnMicroMsg.db";

    private List<File> mWxDbPathList = new ArrayList<File>();

    List<WxContactVo> contactVoList = new ArrayList<>();

    private static WxUtils instance;

    private WxUtils(){
    }

    public static WxUtils getInstance(){
        synchronized (WxUtils.class){
            if(instance == null){
                synchronized (WxUtils.class){
                    instance = new WxUtils();

                }
            }
            return instance;
        }
    }


    public String getUIN() {
        String mCurrWxUin = "";

        CommonUtils.commandCommonSU("chmod 777 -R " + WX_ROOT_PATH);

        File descFile = new File(WX_SP_UIN_PATH);
        XmlPullParserFactory factory;
        if (descFile.exists()) {
            try {
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new FileInputStream(descFile));
                Element root = document.getRootElement();
                List<Element> elements = root.elements();
                for (Element element : elements) {
                    if ("_auth_uin".equals(element.attributeValue("name"))) {
                        mCurrWxUin = element.attributeValue("value");
                        break;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }  catch (DocumentException e) {
                e.printStackTrace();
                Log.i(TAG, "获取微信uid失败，请检查auth_info_key_prefs文件权限");
            }
        }
        return mCurrWxUin;
    }

    private void searchDBFile(String path){
        File wxDataDir = new File(path);
        mWxDbPathList.clear();
        searchFile(wxDataDir, WX_DB_FILE_NAME);
    }

    private void searchFile(File file, String fileName) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    searchFile(childFile, fileName);
                }
            }
        } else {
            if (fileName.equals(file.getName())) {
                mWxDbPathList.add(file);
            }
        }
    }

    public String getDbPassword(String imei, String uin) {
        if (TextUtils.isEmpty(imei) || TextUtils.isEmpty(uin)) {
            Log.i(TAG, "初始化数据库密码失败：imei或uid为空");
            return "";
        }
        String md5 = CommonUtils.md5(imei + uin);
        if(TextUtils.isEmpty(md5)){
            return "";
        }
        String password = md5.substring(0, 7).toLowerCase();
        Log.d(TAG, "password ==========" + password);
        return password;
    }

    public void fetchPublicAccountList(Context context){

        String password = getDbPassword(CommonUtils.getDeviceId(context), getUIN());
        Log.i("wechat", "======== password = " + password);

        searchDBFile(WX_DB_DIR_PATH);

        if(mWxDbPathList.isEmpty()) {
            return ;
        }

        File databaseFile = mWxDbPathList.get(0);
        SQLiteDatabase.loadLibs(context);
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook(){
            public void preKey(SQLiteDatabase database){
            }
            public void postKey(SQLiteDatabase database){
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(databaseFile, password, null, hook);
            cursor = db.query("rcontact", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));

                if(!TextUtils.isEmpty(username) && username.startsWith("gh_") && type!=0){
                    WxContactVo item = new WxContactVo();
                    item.setUsername(username);
                    item.setAlias(cursor.getString(cursor.getColumnIndex("alias")));
                    item.setConRemark(cursor.getString(cursor.getColumnIndex("conRemark")));
                    item.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
                    item.setType(cursor.getInt(cursor.getColumnIndex("type")));

                    contactVoList.add(item);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "exception : " + e.getLocalizedMessage());
        }finally {
            if(db!=null){
                db.close();
            }
            if(cursor!=null){
                cursor.close();
            }
        }
    }

    public List<WxContactVo> getContactVoList(){
        return contactVoList;
    }

}
