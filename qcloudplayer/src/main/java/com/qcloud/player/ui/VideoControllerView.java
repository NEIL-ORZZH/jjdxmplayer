//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qcloud.player.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.qcloud.player.util.PlayerUtils;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

/**
 * ========================================
 * <p/>
 * 版 权：dou361 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2015/12/15 12:00
 * <p/>
 * 描 述：播放器工具条
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class VideoControllerView extends FrameLayout {
    private static final String TAG = "VideoControllerView";
    private VideoControllerView.MediaPlayerControl mPlayer;
    private Context mContext;
    private ViewGroup mAnchor;
    private View mRoot;
    private ProgressBar mProgress;
    private TextView mDuration;
    private TextView mCurrentTime;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private boolean mUseFastForward;
    private boolean mFromXml;
    private boolean mListenersSet;
    private OnClickListener mNextListener;
    private OnClickListener mPrevListener;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private TextView mStreamButton;
    private ImageButton mPauseButton;
    private OnClickListener mStreamSelectListener;
    private String currentStringName;
    private ImageButton mFullscreenButton;
    private Handler mHandler;
    private int seekBarrier;
    private OnClickListener mPauseListener;
    private OnClickListener mFullscreenListener;
    private OnSeekBarChangeListener mSeekListener;
    private OnClickListener mRewListener;
    private OnClickListener mFfwdListener;

    public VideoControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHandler = new VideoControllerView.MessageHandler(this);
        this.seekBarrier = -1;
        this.mPauseListener = new OnClickListener() {
            public void onClick(View v) {
                VideoControllerView.this.doPauseResume();
                VideoControllerView.this.show(3000);
            }
        };
        this.mFullscreenListener = new OnClickListener() {
            public void onClick(View v) {
                VideoControllerView.this.doToggleFullscreen();
                VideoControllerView.this.show(3000);
            }
        };
        this.mSeekListener = new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar bar) {
                VideoControllerView.this.show(3600000);
                VideoControllerView.this.mDragging = true;
                VideoControllerView.this.mHandler.removeMessages(2);
            }

            public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
                if (VideoControllerView.this.mPlayer != null) {
                    if (fromuser) {
                        long duration = (long) VideoControllerView.this.mPlayer.getDuration();
                        long newposition = duration * (long) progress / 1000L;
                        if (VideoControllerView.this.seekBarrier >= 0 && newposition >= (long) VideoControllerView.this.seekBarrier) {
                            VideoControllerView.this.mPlayer.seekTo(0);
                            VideoControllerView.this.mPlayer.pause();
                        } else {
                            VideoControllerView.this.mPlayer.seekTo((int) newposition);
                            if (VideoControllerView.this.mCurrentTime != null) {
                                VideoControllerView.this.mCurrentTime.setText(VideoControllerView.this.stringForTime((int) newposition));
                            }
                        }

                    }
                }
            }

            public void onStopTrackingTouch(SeekBar bar) {
                VideoControllerView.this.mDragging = false;
                VideoControllerView.this.setProgress();
                VideoControllerView.this.updatePausePlay();
                VideoControllerView.this.show(3000);
                VideoControllerView.this.mHandler.sendEmptyMessage(2);
            }
        };
        this.mRewListener = new OnClickListener() {
            public void onClick(View v) {
                if (VideoControllerView.this.mPlayer != null) {
                    int pos = VideoControllerView.this.mPlayer.getCurrentPosition();
                    pos -= 5000;
                    VideoControllerView.this.mPlayer.seekTo(pos);
                    VideoControllerView.this.setProgress();
                    VideoControllerView.this.show(3000);
                }
            }
        };
        this.mFfwdListener = new OnClickListener() {
            public void onClick(View v) {
                if (VideoControllerView.this.mPlayer != null) {
                    int pos = VideoControllerView.this.mPlayer.getCurrentPosition();
                    pos += 15000;
                    VideoControllerView.this.mPlayer.seekTo(pos);
                    VideoControllerView.this.setProgress();
                    VideoControllerView.this.show(3000);
                }
            }
        };
        this.mRoot = null;
        this.mContext = context;
        this.mUseFastForward = true;
        this.mFromXml = true;
        PlayerUtils.log(4, "VideoControllerView", "VideoControllerView");
    }

    public VideoControllerView(Context context, boolean useFastForward) {
        super(context);
        this.mHandler = new VideoControllerView.MessageHandler(this);
        this.seekBarrier = -1;
        this.mPauseListener = new OnClickListener() {
            public void onClick(View v) {
                VideoControllerView.this.doPauseResume();
                VideoControllerView.this.show(3000);
            }
        };
        this.mFullscreenListener = new OnClickListener() {
            public void onClick(View v) {
                VideoControllerView.this.doToggleFullscreen();
                VideoControllerView.this.show(3000);
            }
        };
        this.mSeekListener = new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar bar) {
                VideoControllerView.this.show(3600000);
                VideoControllerView.this.mDragging = true;
                VideoControllerView.this.mHandler.removeMessages(2);
            }

            public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
                if (VideoControllerView.this.mPlayer != null) {
                    if (fromuser) {
                        long duration = (long) VideoControllerView.this.mPlayer.getDuration();
                        long newposition = duration * (long) progress / 1000L;
                        if (VideoControllerView.this.seekBarrier >= 0 && newposition >= (long) VideoControllerView.this.seekBarrier) {
                            VideoControllerView.this.mPlayer.seekTo(0);
                            VideoControllerView.this.mPlayer.pause();
                        } else {
                            VideoControllerView.this.mPlayer.seekTo((int) newposition);
                            if (VideoControllerView.this.mCurrentTime != null) {
                                VideoControllerView.this.mCurrentTime.setText(VideoControllerView.this.stringForTime((int) newposition));
                            }
                        }

                    }
                }
            }

            public void onStopTrackingTouch(SeekBar bar) {
                VideoControllerView.this.mDragging = false;
                VideoControllerView.this.setProgress();
                VideoControllerView.this.updatePausePlay();
                VideoControllerView.this.show(3000);
                VideoControllerView.this.mHandler.sendEmptyMessage(2);
            }
        };
        this.mRewListener = new OnClickListener() {
            public void onClick(View v) {
                if (VideoControllerView.this.mPlayer != null) {
                    int pos = VideoControllerView.this.mPlayer.getCurrentPosition();
                    pos -= 5000;
                    VideoControllerView.this.mPlayer.seekTo(pos);
                    VideoControllerView.this.setProgress();
                    VideoControllerView.this.show(3000);
                }
            }
        };
        this.mFfwdListener = new OnClickListener() {
            public void onClick(View v) {
                if (VideoControllerView.this.mPlayer != null) {
                    int pos = VideoControllerView.this.mPlayer.getCurrentPosition();
                    pos += 15000;
                    VideoControllerView.this.mPlayer.seekTo(pos);
                    VideoControllerView.this.setProgress();
                    VideoControllerView.this.show(3000);
                }
            }
        };
        this.mContext = context;
        this.mUseFastForward = useFastForward;
        PlayerUtils.log(4, "VideoControllerView", "VideoControllerView");
    }

    public VideoControllerView(Context context) {
        this(context, true);
        PlayerUtils.log(4, "VideoControllerView", "VideoControllerView");
    }

    @Override
    public void onFinishInflate() {
        if (this.mRoot != null) {
            this.initControllerView(this.mRoot);
        }
        super.onFinishInflate();
    }

    public void setMediaPlayer(VideoControllerView.MediaPlayerControl player) {
        this.mPlayer = player;
        this.updatePausePlay();
        this.updateFullScreen();
    }

    public void setAnchorView(ViewGroup view) {
        this.mAnchor = view;
        LayoutParams frameParams = new LayoutParams(-1, -1);
        this.removeAllViews();
        View v = this.makeControllerView();
        this.addView(v, frameParams);
    }

    public View getRootView() {
        return this.mRoot;
    }

    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mRoot = inflate.inflate(PlayerUtils.getResourceIdByName("layout", "qcloud_player_media_controller"), (ViewGroup) null);
        this.initControllerView(this.mRoot);
        return this.mRoot;
    }

    private void initControllerView(View v) {
        this.mPauseButton = (ImageButton) v.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_pause_btn"));
        if (this.mPauseButton != null) {
            this.mPauseButton.requestFocus();
            this.mPauseButton.setOnClickListener(this.mPauseListener);
            if (mPlayView != null) {
                this.mPlayView.setOnClickListener(this.mPauseListener);
            }
        }

        this.mFullscreenButton = (ImageButton) v.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_full_screen_btn"));
        if (this.mFullscreenButton != null) {
            this.mFullscreenButton.requestFocus();
            this.mFullscreenButton.setOnClickListener(this.mFullscreenListener);
            if (mFullPlay != null) {
                this.mFullPlay.setOnClickListener(this.mFullscreenListener);
            }
        }

        this.mProgress = (ProgressBar) v.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_mediacontroller_progress"));
        if (this.mProgress != null) {
            if (this.mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) this.mProgress;
                seeker.setOnSeekBarChangeListener(this.mSeekListener);
            }

            this.mProgress.setMax(1000);
        }
        this.mDuration = (TextView) v.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_duration"));
        this.mCurrentTime = (TextView) v.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_time_current"));
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        this.installPrevNextListeners();
    }

    public void show() {
        this.show(3000);
    }

    private void disableUnsupportedButtons() {
        if (this.mPlayer != null) {
            try {
                if (this.mPauseButton != null && !this.mPlayer.canPause()) {
                    this.mPauseButton.setEnabled(false);
                }

                if (this.mFullscreenButton != null && !this.mPlayer.enableToggleFullScreen()) {
                    this.mFullscreenButton.setEnabled(false);
                }
            } catch (IncompatibleClassChangeError var2) {
                ;
            }

        }
    }

    public void show(int timeout) {
        if (!this.mShowing && this.mAnchor != null) {
            this.setProgress();
            if (this.mPauseButton != null) {
                this.mPauseButton.requestFocus();
            }

            this.disableUnsupportedButtons();
            Object msg;
            if (this.mAnchor instanceof RelativeLayout) {
                msg = new android.widget.RelativeLayout.LayoutParams(-1, -2);
                ((android.widget.RelativeLayout.LayoutParams) msg).addRule(12);
            } else {
                msg = new LayoutParams(-1, -2, 80);
            }

            this.mAnchor.addView(this, (android.view.ViewGroup.LayoutParams) msg);
            this.mShowing = true;
        }

        this.updatePausePlay();
        this.updateFullScreen();
        this.updateStreamNameButton();
        this.mHandler.sendEmptyMessage(2);
        Message msg1 = this.mHandler.obtainMessage(1);
        if (timeout != 0) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(msg1, (long) timeout);
        }

    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void hide() {
        if (this.mAnchor != null) {
            try {
                this.mAnchor.removeView(this);
                this.mHandler.removeMessages(2);
            } catch (IllegalArgumentException var2) {
                PlayerUtils.log(3, "MediaController", "already removed");
            }

            this.mShowing = false;
        }
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60 % 60;
        int hours = totalSeconds / 3600;
        this.mFormatBuilder.setLength(0);
        return hours > 0 ? this.mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString() : this.mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
    }

    public int setProgress() {
        if (this.mPlayer != null && !this.mDragging && !this.mPlayer.isLoading()) {
            int position = this.mPlayer.getCurrentPosition();
            if (this.seekBarrier >= 0 && position >= this.seekBarrier) {
                position = 0;
            }

            int duration = this.mPlayer.getDuration();
            if (this.mProgress != null) {
                if (duration > 0) {
                    long str = 1000L * (long) position / (long) duration;
                    this.mProgress.setProgress((int) str);
                }

                int str1 = this.mPlayer.getBufferPercentage();
                this.mProgress.setSecondaryProgress(str1 * 10);
            }

            if (this.mDuration != null) {
                String str2 = this.stringForTime(duration);
                this.mDuration.setText(str2);
            }

            if (this.mCurrentTime != null) {
                this.mCurrentTime.setText(this.stringForTime(position));
            }

            this.updatePausePlay();
            return position;
        } else {
            return 0;
        }
    }

    public void setCurrentStreamName(String streamName) {
        this.currentStringName = streamName;
    }

    private void updateStreamNameButton() {
        if (this.mStreamButton != null) {
            this.mStreamButton.setText(this.currentStringName);
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        PlayerUtils.log(3, "Controller", "onTouchEvent");
        this.show(3000);
        return true;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        this.show(3000);
        return false;
    }

    public void updatePausePlay() {
        if (this.mRoot != null && this.mPauseButton != null && this.mPlayer != null) {
            if (this.mPlayer.isPlaying()) {
                if (this.mPlayView != null) {
                    this.mPlayView.setImageResource(PlayerUtils.getResourceIdByName("drawable", "qcloud_play_pause_01"));
                }
                this.mPauseButton.setImageResource(PlayerUtils.getResourceIdByName("drawable", "qcloud_player_icon_media_pause"));
            } else {
                if (this.mPlayView != null) {
                    this.mPlayView.setImageResource(PlayerUtils.getResourceIdByName("drawable", "qcloud_play_play_01"));
                }
                this.mPauseButton.setImageResource(PlayerUtils.getResourceIdByName("drawable", "qcloud_player_icon_media_play"));
            }

        }
    }

    /**
     * ----------------------------新增功能----------------------------------
     */
    private ImageView mPlayView;
    private ImageView mFullPlay;

    /**
     * 设置自定义播放按钮
     */
    public void setStreamButton(TextView streamButton) {
        this.mStreamButton = streamButton;
        if (this.mStreamSelectListener != null && mStreamButton != null) {
            this.mStreamButton.setOnClickListener(this.mStreamSelectListener);
        }
    }

    /**
     * 设置是否可以拖动
     */
    public void setEnableBannedMove(boolean bannedMove) {
        if (mProgress != null) {
            MySeekBar ms = (MySeekBar) this.mProgress;
            ms.setEnableBannedMove(bannedMove);
        }
    }

    /**
     * 设置自定义播放按钮
     */
    public void setPlayView(ImageView playView) {
        this.mPlayView = playView;
    }

    /**
     * 设置自定义全屏按钮
     */
    public void setFullView(ImageView fullPlay) {
        this.mFullPlay = fullPlay;
    }

    /**
     * 隐藏播放按钮
     */
    public void hideControllPlay() {
        if (this.mPauseButton != null) {
            this.mPauseButton.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏全屏按钮
     */
    public void hideControllFullscreen() {
        if (this.mFullscreenButton != null) {
            this.mFullscreenButton.setVisibility(View.GONE);
        }
    }

    /**
     * 显示播放按钮
     */
    public void showControllPlay() {
        if (this.mPauseButton != null) {
            this.mPauseButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示全屏按钮
     */
    public void showControllFullscreen() {
        if (this.mFullscreenButton != null) {
            this.mFullscreenButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ----------------------------新增功能----------------------------------
     */


    public void updateFullScreen() {
        if (this.mRoot != null && this.mFullscreenButton != null && this.mPlayer != null) {
            if (!this.mPlayer.enableToggleFullScreen()) {
                this.mFullscreenButton.setVisibility(View.GONE);
                if (mFullPlay != null) {
                    this.mFullPlay.setVisibility(View.GONE);
                }
            }

            if (this.mPlayer.isFullScreen()) {
                if (mFullPlay != null) {
                    this.mFullPlay.setImageResource(PlayerUtils.getResourceIdByName("drawable", "qcloud_player_icon_fullscreen_shrink"));
                }
                this.mFullscreenButton.setImageResource(PlayerUtils.getResourceIdByName("drawable", "qcloud_player_icon_fullscreen_shrink"));
            } else {
                if (mFullPlay != null) {
                    this.mFullPlay.setImageResource(PlayerUtils.getResourceIdByName("drawable", "qcloud_player_icon_fullscreen_stretch"));
                }
                this.mFullscreenButton.setImageResource(PlayerUtils.getResourceIdByName("drawable", "qcloud_player_icon_fullscreen_stretch"));
            }

        }
    }

    private void doPauseResume() {
        if (this.mPlayer != null) {
            if (this.mPlayer.isPlaying()) {
                this.mPlayer.pause();
            } else {
                this.mPlayer.start();
            }

            this.updatePausePlay();
        }
    }

    private void doToggleFullscreen() {
        if (this.mPlayer != null) {
            this.mPlayer.toggleFullScreen();
        }
    }

    public void setEnabled(boolean enabled) {
        if (this.mPauseButton != null) {
            this.mPauseButton.setEnabled(enabled);
        }

        if (this.mProgress != null) {
            this.mProgress.setEnabled(enabled);
        }

        if (this.mFullscreenButton != null) {
            this.mFullscreenButton.setEnabled(enabled);
        }

        this.disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    private void installPrevNextListeners() {
    }

    public void setPrevNextListeners(OnClickListener next, OnClickListener prev) {
        this.mNextListener = next;
        this.mPrevListener = prev;
        this.mListenersSet = true;
        if (this.mRoot != null) {
            this.installPrevNextListeners();
        }

    }

    public void setStreamSelectListener(OnClickListener listener) {
        this.mStreamSelectListener = listener;
        if (this.mStreamButton != null) {
            this.mStreamButton.setOnClickListener(listener);
        }

    }

    public void setSeekBarrier(int barrier) {
        this.seekBarrier = barrier;
    }

    public interface MediaPlayerControl {

        /**
         * 开始
         */
        void start();

        /**
         * 暂停
         */
        void pause();

        /**
         * 获取播放时长
         */
        int getDuration();

        /**
         * 获取当前播放位置
         */
        int getCurrentPosition();

        /**
         * 设置播放位置
         */
        void seekTo(int var1);

        /**
         * 判断是否在播放
         */
        boolean isPlaying();

        /**
         * 获取缓存
         */
        int getBufferPercentage();

        /**
         * 判断是否可以暂停
         */
        boolean canPause();

        /**
         * 判断是否可以后退
         */
        boolean canSeekBackward();

        /**
         * 判断是否可以向前
         */
        boolean canSeekForward();

        /**
         * 判断是否全屏
         */
        boolean isFullScreen();

        /***/
        void toggleFullScreen();

        /**
         * 判断是否在加载
         */
        boolean isLoading();

        /**
         * 判断全屏转换是否可用
         */
        boolean enableToggleFullScreen();

        /**
         * 隐藏顶部栏
         */
        void hideTopBar();

        /**
         * 隐藏中间暂停按钮
         */
        void hideCenterPlay();

        /**
         * 隐藏右上全屏按钮
         */
        void hideRightTopFull();
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<VideoControllerView> mView;

        MessageHandler(VideoControllerView view) {
            this.mView = new WeakReference(view);
        }

        public void handleMessage(Message msg) {
            VideoControllerView view = (VideoControllerView) this.mView.get();
            if (view != null && view.mPlayer != null) {
                switch (msg.what) {
                    case 1:
                        view.hide();
                        view.mPlayer.hideTopBar();
                        view.mPlayer.hideCenterPlay();
                        view.mPlayer.hideRightTopFull();
                        break;
                    case 2:
                        int pos = view.setProgress();
                        if (!view.mDragging && view.mShowing && view.mPlayer.isPlaying()) {
                            msg = this.obtainMessage(2);
                            this.sendMessageDelayed(msg, (long) (1000 - pos % 1000));
                        }
                }

            }
        }
    }
}
