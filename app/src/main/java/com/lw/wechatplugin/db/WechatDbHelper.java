package com.lw.wechatplugin.db;

import android.database.Cursor;
import android.text.TextUtils;

import java.util.HashMap;

import static de.robv.android.xposed.XposedHelpers.callMethod;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class WechatDbHelper  {
    private Object SQLDB;

    private HashMap<String, String> nicknameCache;


    public WechatDbHelper(Object dbObject) {
        SQLDB = dbObject;
        nicknameCache = new HashMap<>();
    }


    public Cursor getLastMsg() {
        return rawQuery("SELECT * FROM message order by msgId desc limit 0,1");
    }

    public Cursor rawQuery(String query) {
        return rawQuery(query, null);
    }

    public Cursor rawQuery(String query, String[] args) {
        return (Cursor) callMethod(SQLDB, "rawQuery", query, args);
    }


    public String getNickname(String username) {
        if (nicknameCache.containsKey(username)) {
            return nicknameCache.get(username);
        }

        Cursor cursor = getContact(username);
        if (cursor == null || !cursor.moveToFirst())
            return username;

        String name = cursor.getString(cursor.getColumnIndex("conRemark"));
        if (TextUtils.isEmpty(name)) {
            name = cursor.getString(cursor.getColumnIndex("nickname"));
        }
        name = name.trim();
        cursor.close();
        nicknameCache.put(username, name);
        return name;
    }

    private Cursor getContact(String username) {
        String query = "SELECT * FROM rcontact WHERE username = ?";
        return rawQuery(query, new String[]{username});
    }


    private String getChatroomName(String username) {
        String name = getNickname(username);
        if (!TextUtils.isEmpty(name))
            return name;

        String query = "SELECT * FROM chatroom WHERE chatroomname = ?";
        Cursor cursor = rawQuery(query, new String[]{username});
        if (cursor == null || !cursor.moveToFirst())
            return null;

        name = cursor.getString(cursor.getColumnIndex("displayname"));
        cursor.close();
        return name;
    }
}
