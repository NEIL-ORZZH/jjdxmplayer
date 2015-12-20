//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qcloud.player;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class VideoInfo implements Parcelable {
    private String videoName;
    private String fileId;
    private String[] streamNames;
    private String[] streamUrls;
    private int defaultStream;
    private int currentStream;
    private int currentPosition;
    private int durationAllow;
    public static Creator<VideoInfo> CREATOR = new Creator() {
        public VideoInfo createFromParcel(Parcel source) {
            String fileId = source.readString();
            String[] streamNames = source.createStringArray();
            String[] streamUrls = source.createStringArray();
            int defaultStream = source.readInt();
            VideoInfo info = new VideoInfo(fileId, streamNames, streamUrls, defaultStream);
            info.setCurrentStream(source.readInt());
            info.setCurrentPosition(source.readInt());
            info.setDurationAllow(source.readInt());
            return info;
        }

        public VideoInfo[] newArray(int size) {
            return null;
        }
    };

    public VideoInfo(String fileId, String[] streamNames, String[] streamUrls) {
        this(fileId, streamNames, streamUrls, 0);
    }

    public VideoInfo(String fileId, String[] streamNames, String[] streamUrls, int defaultStream) {
        this(fileId, streamNames, streamUrls, defaultStream, -1);
    }

    public VideoInfo(String fileId, String[] streamNames, String[] streamUrls, int defaultStream, int durationAllow) {
        this.durationAllow = -1;
        this.fileId = fileId;
        this.streamNames = streamNames;
        this.streamUrls = streamUrls;
        this.defaultStream = defaultStream;
        this.currentStream = defaultStream;
        this.currentPosition = 0;
        this.durationAllow = durationAllow;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getFileId() {
        return this.fileId;
    }

    public String[] getStreamNames() {
        return this.streamNames;
    }

    public String[] getStreamUrls() {
        return this.streamUrls;
    }

    public int getDefaultStream() {
        return this.defaultStream;
    }

    public String getDefaultStreamUrl() {
        return this.streamUrls != null && this.streamUrls.length >= this.defaultStream && this.defaultStream >= 0?this.streamUrls[this.defaultStream]:null;
    }

    public String getDefaultStreamName() {
        return this.streamNames != null && this.streamNames.length >= this.defaultStream && this.defaultStream >= 0?this.streamNames[this.defaultStream]:null;
    }

    public int getCurrentStream() {
        return this.currentStream;
    }

    public void setCurrentStream(int currentStream) {
        this.currentStream = currentStream;
    }

    public String getCurrentStreamUrl() {
        return this.streamUrls[this.currentStream];
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public static void validate(VideoInfo videoInfo) throws IllegalArgumentException {
        if(videoInfo == null) {
            throw new IllegalArgumentException("videoInfo is null");
        } else if(TextUtils.isEmpty(videoInfo.fileId)) {
            throw new IllegalArgumentException("videoInfo.fileId is empty");
        } else if(videoInfo.streamNames != null && videoInfo.streamUrls != null && videoInfo.streamNames.length != 0 && videoInfo.streamUrls.length != 0 && videoInfo.streamNames.length == videoInfo.streamUrls.length) {
            if(videoInfo.defaultStream < 0 || videoInfo.defaultStream >= videoInfo.streamNames.length) {
                throw new IllegalArgumentException("default stream index out of bound");
            }
        } else {
            throw new IllegalArgumentException("Illegal streamNames or streamUrls");
        }
    }

    public int getDurationAllow() {
        return this.durationAllow;
    }

    public void setDurationAllow(int durationAllow) {
        this.durationAllow = durationAllow;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileId);
        dest.writeStringArray(this.streamNames);
        dest.writeStringArray(this.streamUrls);
        dest.writeInt(this.defaultStream);
        dest.writeInt(this.currentStream);
        dest.writeInt(this.currentPosition);
        dest.writeInt(this.durationAllow);
    }
}
