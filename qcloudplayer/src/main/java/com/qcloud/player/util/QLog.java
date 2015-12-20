//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qcloud.player.util;

public interface QLog {
    int VERBOSE = 2;
    int DEBUG = 3;
    int INFO = 4;
    int WARN = 5;
    int ERROR = 6;
    int ASSERT = 7;

    int v(String var1, String var2);

    int v(String var1, String var2, Throwable var3);

    int d(String var1, String var2);

    int d(String var1, String var2, Throwable var3);

    int i(String var1, String var2);

    int i(String var1, String var2, Throwable var3);

    int w(String var1, String var2);

    int w(String var1, String var2, Throwable var3);

    int w(String var1, Throwable var2);

    int e(String var1, String var2);

    int e(String var1, String var2, Throwable var3);
}
