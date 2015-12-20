//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qcloud.player.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.qcloud.player.PlayerConfig;
import com.qcloud.player.util.QLog;

public class PlayerUtils {
    private static final String TAG = "PlayerUtils";

    public PlayerUtils() {
    }

    /**
     * 获取资源id
     * @param className 资源类名称如（layout,drawable,mipmap,id）
     * @param name 资源名称如（tab_layout.xml 名称为tab_layout ）
     * @return 资源id
     */
    public static int getResourceIdByName(String className, String name) {
        String packageName = PlayerConfig.g().getAppContext().getPackageName();
        int id = 0;

        try {
            String e = packageName + ".R$" + className;
            Class desireClass = Class.forName(e);
            if(desireClass != null) {
                id = desireClass.getField(name).getInt(desireClass);
            }
        } catch (ClassNotFoundException var6) {
            log(6, "PlayerUtils", "ClassNotFoundException: class=" + className + " fieldname=" + name);
        } catch (IllegalArgumentException var7) {
            log(6, "PlayerUtils", "IllegalArgumentException: class=" + className + " fieldname=" + name);
        } catch (SecurityException var8) {
            log(6, "PlayerUtils", "SecurityException: class=" + className + " fieldname=" + name);
        } catch (IllegalAccessException var9) {
            log(6, "PlayerUtils", "IllegalAccessException: class=" + className + " fieldname=" + name);
        } catch (NoSuchFieldException var10) {
            log(6, "PlayerUtils", "NoSuchFieldException: class=" + className + " fieldname=" + name);
        }

        return id;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)PlayerConfig.g().getAppContext().getSystemService("connectivity");
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void log(int level, String tag, String msg) {
        QLog logger = PlayerConfig.g().getLogger();
        if(logger == null) {
            switch(level) {
                case 2:
                    Log.v(tag, msg);
                    break;
                case 3:
                    Log.d(tag, msg);
                    break;
                case 4:
                    Log.i(tag, msg);
                case 5:
                default:
                    break;
                case 6:
                    Log.e(tag, msg);
            }
        } else {
            switch(level) {
                case 2:
                    logger.v(tag, msg);
                    break;
                case 3:
                    logger.d(tag, msg);
                    break;
                case 4:
                    logger.i(tag, msg);
                case 5:
                default:
                    break;
                case 6:
                    logger.e(tag, msg);
            }
        }

    }
}
