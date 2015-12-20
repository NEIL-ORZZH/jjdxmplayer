//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qcloud.player;

import android.content.Context;
import com.qcloud.player.CallBack;
import com.qcloud.player.util.QLog;

public class PlayerConfig {
    private Context appContext;
    private String userKey;
    private CallBack callBack;
    private QLog logger;
    private static PlayerConfig instance;

    private PlayerConfig(Context appContext, String userKey) {
        this.appContext = appContext;
        this.userKey = userKey;
    }

    public static PlayerConfig g() {
        if(instance == null) {
            throw new RuntimeException("PlayerConfig not initialized!");
        } else {
            return instance;
        }
    }

    public static void init(Context appContext, String userKey) {
        instance = new PlayerConfig(appContext, userKey);
    }

    public Context getAppContext() {
        return this.appContext;
    }

    public String getUserKey() {
        return this.userKey;
    }

    public void registerCallback(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return this.callBack;
    }

    public void registerLogger(QLog logger) {
        this.logger = logger;
    }

    public QLog getLogger() {
        return this.logger;
    }
}
