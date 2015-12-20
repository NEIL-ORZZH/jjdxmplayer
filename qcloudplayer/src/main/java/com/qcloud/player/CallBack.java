//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qcloud.player;

import com.qcloud.player.VideoInfo;

public abstract class CallBack {
    private static int eventId = 0;
    public static int EVENT_MIN;
    public static int EVENT_PLAY_START;
    public static int EVENT_PLAY_STOP;
    public static int EVENT_PLAY_COMPLETE;
    public static int EVENT_PLAY_PAUSE;
    public static int EVENT_PLAY_RESUME;
    public static int EVENT_PLAY_ERROR;
    public static int EVENT_PLAY_EXIT;
    public static int EVENT_PLAY_POSITION_OUT_OF_BOUND;
    public static int EVENT_LOAD_TIME_OUT;
    public static int EVENT_MAX;

    static {
        EVENT_MIN = eventId++;
        EVENT_PLAY_START = eventId++;
        EVENT_PLAY_STOP = eventId++;
        EVENT_PLAY_COMPLETE = eventId++;
        EVENT_PLAY_PAUSE = eventId++;
        EVENT_PLAY_RESUME = eventId++;
        EVENT_PLAY_ERROR = eventId++;
        EVENT_PLAY_EXIT = eventId++;
        EVENT_PLAY_POSITION_OUT_OF_BOUND = eventId++;
        EVENT_LOAD_TIME_OUT = eventId++;
        EVENT_MAX = eventId;
    }

    public CallBack() {
    }

    public abstract void onEvent(int var1, String var2, VideoInfo var3);
}
