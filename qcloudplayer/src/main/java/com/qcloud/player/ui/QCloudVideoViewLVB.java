//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qcloud.player.ui;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.widget.VideoView;
import com.qcloud.player.CallBack;
import com.qcloud.player.PlayerConfig;
import com.qcloud.player.VideoInfo;
import com.qcloud.player.ui.VideoControllerView.MediaPlayerControl;
import com.qcloud.player.util.PlayerUtils;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;

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
 * 创建日期：2015/12/15 10:55
 * <p/>
 * 描 述：直播播放器
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class QCloudVideoViewLVB extends FrameLayout implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnInfoListener {
    private static final String LOG_TAG = "QCloudVideoView";
    private Context context;
    private Activity outActivity;
    public static final int BRIGHTNESS_MAX = 100;
    public static final int BRIGHTNESS_MIN = 1;
    private static final int BRIGHTNESS_DELTA = 5;
    private static final int MSG_LOAD_TIME_OUT = 1;
    private static final int MSG_UPDATE_POSITION = 2;
    private static final int LOAD_TIME_LIMIT = 30000;
    private static final int TIME_OFFSET = 10000;
    private View rootView;
    private VideoView videoSurface;
    private MediaPlayer player;
    private VideoControllerView controller;
    private View streamSelectView;
    private ListView streamSelectListView;
    private QCloudVideoViewLVB.StreamSelectAdapter streamSelectAdapter;
    private FrameLayout videoSurfaceContainer;
    private SeekBar volumeController;
    private View brightnessControllerContainer;
    private SeekBar brightnessController;
    private View titleBar;
    private View backButton;
    private TextView titleName;
    private View settingView;
    private View llaction;
    /**
     * 播放按钮
     */
    private ImageView fullView;
    private TextView mStreamButton;
    /**
     * 加载中布局
     */
    private View loadingView;
    /**
     * 加载失败布局
     */
    private View loadErrorView;
    /**
     * 重新连接
     */
    private View retryView;
    /**
     * 网络提示布局
     */
    private View netTieView;
    /**
     * 继续观看
     */
    private View contineView;
    private PlayErrorListener mPlayErrorListener;
    private PlayContineListener mPlayContineListener;

    private VideoInfo videoInfo;
    private boolean videoSizeIsSet;
    private boolean mediaPlayerIsPrepared;
    private AudioManager audioManager;
    private int currentVolume;
    private boolean isShowingView;
    private int bufferPercent;
    private boolean isloading;
    private int mVideoWidth;
    private int mVideoHeight;
    private boolean isFullscreen;
    private boolean enableBrightnessControll;
    private boolean isStreamSelectViewShown;
    private boolean autoStart;
    private int maxVolume;
    private int volumeOffset;
    private boolean enableGesture;
    private boolean enableTopBar;
    private boolean enableBackButton;
    private boolean isSurfaceCreated;
    private QCloudVideoViewLVB.OnToggleFullscreenListener onToggleFullscreenListener;
    private OnSeekBarChangeListener onBrightnessControllerChangeListener;
    private OnSeekBarChangeListener onVolumeControllerChangeListener;
    private OnClickListener onStreamSelectButtonClickListener;
    private Callback surfaceCallback;
    private GestureDetector mGestureDetector;
    private QCloudVideoViewLVB.OnKeyDownListener onKeyDownListener;
    private QCloudVideoViewLVB.OnKeyLongPressListener onKeyLongPressListener;
    private QCloudVideoViewLVB.OnKeyMultipleListener onKeyMultipleListener;
    private QCloudVideoViewLVB.OnKeyUpListener onKeyUpListener;
    private QCloudVideoViewLVB.OnPreparedListener onPreparedListener;
    private QCloudVideoViewLVB.OnCompletionListener onCompletionListener;
    private QCloudVideoViewLVB.OnVideoSizeChangedListener onVideoSizeChangedListener;
    private QCloudVideoViewLVB.OnBufferingUpdateListener onBufferingUpdateListener;
    private QCloudVideoViewLVB.OnErrorListener onErrorListener;
    private QCloudVideoViewLVB.OnInfoListener onInfoListener;
    private QCloudVideoViewLVB.OnSeekCompleteListener onSeekCompleteListener;
    private MediaPlayerControl mediaPlayerControl;
    private Handler handler;

    public QCloudVideoViewLVB(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public QCloudVideoViewLVB(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QCloudVideoViewLVB(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.videoSizeIsSet = false;
        this.mediaPlayerIsPrepared = false;
        this.isShowingView = false;
        this.isloading = false;
        this.enableBrightnessControll = false;
        this.isStreamSelectViewShown = false;
        this.enableGesture = false;
        this.enableTopBar = false;
        this.enableBackButton = false;
        this.isSurfaceCreated = false;
        this.onBrightnessControllerChangeListener = new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QCloudVideoViewLVB.this.setBrightness(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        this.onVolumeControllerChangeListener = new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QCloudVideoViewLVB.this.currentVolume = seekBar.getProgress();
                QCloudVideoViewLVB.this.audioManager.setStreamVolume(3, progress, 0);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        this.onStreamSelectButtonClickListener = new OnClickListener() {
            public void onClick(View v) {
                QCloudVideoViewLVB.this.controller.hide();
                QCloudVideoViewLVB.this.showStreamSelectView();
            }
        };
        this.surfaceCallback = new Callback() {
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                PlayerUtils.log(3, "QCloudVideoView", "surfaceChanged");
            }

            public void surfaceCreated(SurfaceHolder holder) {
                PlayerUtils.log(3, "QCloudVideoView", "surfaceCreated");
                QCloudVideoViewLVB.this.isSurfaceCreated = true;
                if (QCloudVideoViewLVB.this.videoInfo != null) {
                    PlayerUtils.log(3, "QCloudVideoView", "surfaceCreated: videoInfo != null");
                    QCloudVideoViewLVB.this.play(holder);
                }

            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                PlayerUtils.log(3, "QCloudVideoView", "surfaceDestroyed");
                if (QCloudVideoViewLVB.this.videoSurface != null) {
                    if (QCloudVideoViewLVB.this.videoSurface.isPlaying()) {
                        QCloudVideoViewLVB.this.videoInfo.setCurrentPosition((int) QCloudVideoViewLVB.this.videoSurface.getCurrentPosition());
                    }

                    QCloudVideoViewLVB.this.stopPlayback();
                }

            }
        };
        this.mGestureDetector = new GestureDetector(this.getContext(), new OnGestureListener() {
            public boolean onDown(MotionEvent e) {
                return true;
            }

            public void onShowPress(MotionEvent e) {
                if (QCloudVideoViewLVB.this.enableGesture) {
                    ;
                }
            }

            public boolean onSingleTapUp(MotionEvent e) {
                QCloudVideoViewLVB.this.toggleController();
                QCloudVideoViewLVB.this.toggleTopBar();
                clockManage();
                if (QCloudVideoViewLVB.this.isStreamSelectViewShown) {
                    QCloudVideoViewLVB.this.hideStreamSelectView();
                }

                return false;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!QCloudVideoViewLVB.this.enableGesture) {
                    return false;
                } else {
                    int left = QCloudVideoViewLVB.this.rootView.getLeft();
                    int right = QCloudVideoViewLVB.this.rootView.getRight();
                    int middleLeft = left + QCloudVideoViewLVB.this.rootView.getWidth() / 3;
                    int middleRight = right - QCloudVideoViewLVB.this.rootView.getWidth() / 3;
                    float x1 = e1.getX();
                    float x2 = e2.getX();
                    if (x1 >= (float) left && x1 < (float) right && x2 >= (float) left && x2 < (float) right && e2.getY() >= (float) QCloudVideoViewLVB.this.rootView.getTop() && e2.getY() < (float) QCloudVideoViewLVB.this.rootView.getBottom()) {
                        if (x1 < (float) middleLeft && x2 < (float) middleLeft) {
                            QCloudVideoViewLVB.this.handleScrollOnLeftArea(e1, e2, distanceX, distanceY);
                            return true;
                        } else if (x1 >= (float) middleLeft && x1 < (float) middleRight && x2 >= (float) middleLeft && x2 < (float) middleRight) {
                            QCloudVideoViewLVB.this.handleScrollOnMiddleArea(e1, e2, distanceX, distanceY);
                            return true;
                        } else if (x1 >= (float) middleRight && x2 >= (float) middleRight) {
                            QCloudVideoViewLVB.this.handleScrollOnRightArea(e1, e2, distanceX, distanceY);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }

            public void onLongPress(MotionEvent e) {
                if (QCloudVideoViewLVB.this.enableGesture) {
                    ;
                }
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return !QCloudVideoViewLVB.this.enableGesture ? false : false;
            }
        });
        this.mediaPlayerControl = new MediaPlayerControl() {
            public boolean canPause() {
                return true;
            }

            public boolean canSeekBackward() {
                return true;
            }

            public boolean canSeekForward() {
                return true;
            }

            public int getBufferPercentage() {
                return QCloudVideoViewLVB.this.bufferPercent;
            }

            public int getCurrentPosition() {
                int pos;
                try {
                    pos = (int) QCloudVideoViewLVB.this.videoSurface.getCurrentPosition();
                } catch (Exception var3) {
                    pos = 0;
                }

                return pos;
            }

            public int getDuration() {
                return QCloudVideoViewLVB.this.videoSurface != null && QCloudVideoViewLVB.this.mediaPlayerIsPrepared ? (int) QCloudVideoViewLVB.this.videoSurface.getDuration() : 0;
            }

            public boolean isPlaying() {
                return QCloudVideoViewLVB.this.videoSurface != null && QCloudVideoViewLVB.this.videoSurface.isPlaying();
            }

            public void pause() {
                PlayerUtils.log(3, "QCloudVideoView", "video pause");
                if (QCloudVideoViewLVB.this.videoSurface != null && QCloudVideoViewLVB.this.videoSurface.isPlaying()) {
                    QCloudVideoViewLVB.this.videoInfo.setCurrentPosition((int) QCloudVideoViewLVB.this.videoSurface.getCurrentPosition());
                    QCloudVideoViewLVB.this.videoSurface.pause();
                    if (QCloudVideoViewLVB.this.handler != null) {
                        QCloudVideoViewLVB.this.handler.removeMessages(2);
                    }

                    CallBack callBack = PlayerConfig.g().getCallBack();
                    if (callBack != null) {
                        callBack.onEvent(CallBack.EVENT_PLAY_PAUSE, "video paused", QCloudVideoViewLVB.this.videoInfo);
                    }
                }

            }

            public void seekTo(int i) {
                if (QCloudVideoViewLVB.this.videoInfo != null && QCloudVideoViewLVB.this.videoSurface != null) {
                    if (i < 0) {
                        i = 0;
                    }

                    int duration = this.getDuration();
                    if (i > duration) {
                        i = duration;
                    }

                    int durationAllow = QCloudVideoViewLVB.this.videoInfo.getDurationAllow();
                    if (durationAllow >= 0 && durationAllow < i) {
                        PlayerUtils.log(3, "QCloudVideoView", "seekTo: position out of bound");
                        this.seekTo(0);
                        this.start();
                        this.pause();
                        if (QCloudVideoViewLVB.this.controller != null) {
                            QCloudVideoViewLVB.this.controller.setProgress();
                            QCloudVideoViewLVB.this.controller.updatePausePlay();
                        }

                        CallBack callBack = PlayerConfig.g().getCallBack();
                        if (callBack != null) {
                            callBack.onEvent(CallBack.EVENT_PLAY_POSITION_OUT_OF_BOUND, "position out of bound", QCloudVideoViewLVB.this.videoInfo);
                        }
                    } else {
                        QCloudVideoViewLVB.this.videoSurface.seekTo(i);
                    }

                }
            }

            public void start() {
                CallBack callBack = PlayerConfig.g().getCallBack();
                if (callBack != null) {
                    callBack.onEvent(CallBack.EVENT_PLAY_RESUME, "video resumed", QCloudVideoViewLVB.this.videoInfo);
                }

                QCloudVideoViewLVB.this.videoSurface.start();
                if (QCloudVideoViewLVB.this.handler != null) {
                    QCloudVideoViewLVB.this.handler.sendEmptyMessage(2);
                }

            }

            public boolean isFullScreen() {
                return QCloudVideoViewLVB.this.isFullscreen;
            }

            public void toggleFullScreen() {
                if (QCloudVideoViewLVB.this.isFullscreen) {
                    QCloudVideoViewLVB.this.isFullscreen = false;
                } else {
                    QCloudVideoViewLVB.this.isFullscreen = true;
                }

                if (QCloudVideoViewLVB.this.onToggleFullscreenListener != null) {
                    QCloudVideoViewLVB.this.onToggleFullscreenListener.onToggleFullscreen(QCloudVideoViewLVB.this.isFullscreen);
                }

            }

            public boolean isLoading() {
                return QCloudVideoViewLVB.this.isloading;
            }

            public boolean enableToggleFullScreen() {
                return true;
            }

            public void hideTopBar() {
                QCloudVideoViewLVB.this.hideTopBar();
            }

            @Override
            public void hideCenterPlay() {

            }

            @Override
            public void hideRightTopFull() {

            }
        };
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                CallBack callBack = PlayerConfig.g().getCallBack();
                switch (msg.what) {
                    case 1:
                        if (callBack != null) {
                            callBack.onEvent(CallBack.EVENT_LOAD_TIME_OUT, "time out", QCloudVideoViewLVB.this.videoInfo);
                        }
                        break;
                    case 2:
                        if (QCloudVideoViewLVB.this.mediaPlayerControl != null) {
                            int pos = QCloudVideoViewLVB.this.mediaPlayerControl.getCurrentPosition();
                            if (QCloudVideoViewLVB.this.videoInfo == null || QCloudVideoViewLVB.this.videoInfo.getDurationAllow() >= 0 && QCloudVideoViewLVB.this.videoInfo.getDurationAllow() <= pos) {
                                QCloudVideoViewLVB.this.mediaPlayerControl.seekTo(0);
                                QCloudVideoViewLVB.this.mediaPlayerControl.start();
                                QCloudVideoViewLVB.this.mediaPlayerControl.pause();
                                if (QCloudVideoViewLVB.this.controller != null) {
                                    QCloudVideoViewLVB.this.controller.setProgress();
                                    QCloudVideoViewLVB.this.controller.updatePausePlay();
                                }

                                if (callBack != null) {
                                    callBack.onEvent(CallBack.EVENT_PLAY_POSITION_OUT_OF_BOUND, "position out of bound", QCloudVideoViewLVB.this.videoInfo);
                                }
                            } else if (QCloudVideoViewLVB.this.mediaPlayerControl.isPlaying()) {
                                QCloudVideoViewLVB.this.handler.sendEmptyMessageDelayed(2, 200L);
                            }
                        }
                }

            }
        };
        this.init(context);
    }

    private void init(Context ctx) {
        this.context = ctx;
        this.isFullscreen = false;
        this.mVideoHeight = 0;
        this.mVideoWidth = 0;
        LayoutInflater inflater = LayoutInflater.from(this.context);
        this.rootView = inflater.inflate(PlayerUtils.getResourceIdByName("layout", "qcloud_player_video_root_lvb"), (ViewGroup) null);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        this.addView(this.rootView, layoutParams);
        this.videoSurfaceContainer = (FrameLayout) this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_video_surface_container"));
        this.videoSurface = (VideoView) this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_video_surface"));
        SurfaceHolder videoHolder = this.videoSurface.getHolder();
        videoHolder.addCallback(this.surfaceCallback);
        this.controller = new VideoControllerView(this.context);
        this.controller.setStreamSelectListener(this.onStreamSelectButtonClickListener);
        this.streamSelectView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_select_stream_container"));
        this.streamSelectListView = (ListView) this.streamSelectView.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_select_streams_list"));
        this.streamSelectAdapter = new QCloudVideoViewLVB.StreamSelectAdapter(this.context);
        this.streamSelectListView.setAdapter(this.streamSelectAdapter);
        this.streamSelectListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QCloudVideoViewLVB.this.hideStreamSelectView();
                QCloudVideoViewLVB.this.switchStream(position);
            }
        });

        /** 新加入功能 */
        this.mStreamButton = (TextView) this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_select_streams_btn"));
        this.fullView = (ImageView) this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_btn_full"));
        this.loadingView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_ll_loading"));
        this.loadErrorView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_ll_error"));
        this.retryView = this.loadErrorView.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_btn_retry"));
        this.netTieView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_ll_tie"));
        this.contineView = this.netTieView.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_btn_contine"));
        showLoading();
        retryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                QCloudVideoViewLVB.this.showLoading();
                mPlayErrorListener.onPlayError();
            }
        });
        contineView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                QCloudVideoViewLVB.this.showLoading();
                mPlayContineListener.onPlayContine();
            }
        });
        this.controller.setStreamButton(mStreamButton);
        this.controller.setFullView(fullView);
        /** 新加入功能 */
        this.llaction = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_ll_action"));
        this.audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        this.maxVolume = this.audioManager.getStreamMaxVolume(3);
        this.volumeController = (SeekBar) this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_volume_controller"));
        this.volumeController.setMax(this.maxVolume);
        this.volumeOffset = this.maxVolume < 20 ? 1 : this.maxVolume / 20;
        this.updateVolumeController();
        this.volumeController.setOnSeekBarChangeListener(this.onVolumeControllerChangeListener);
        this.brightnessControllerContainer = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_brightness_controller_container"));
        this.brightnessControllerContainer.setVisibility(View.GONE);
        this.brightnessController = (SeekBar) this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_brightness_controller"));
        this.brightnessController.setMax(100);
        float brightness = 0.01F;

        try {
            int e = System.getInt(this.context.getContentResolver(), "screen_brightness");
            brightness = 1.0F * (float) e / 255.0F;
        } catch (SettingNotFoundException var7) {
            var7.printStackTrace();
        }

        this.brightnessController.setProgress((int) (brightness * 100.0F));
        this.brightnessController.setOnSeekBarChangeListener(this.onBrightnessControllerChangeListener);
        this.titleBar = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_title_bar"));
        this.settingView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_settings_container"));
        this.llaction.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                QCloudVideoViewLVB.this.controller.hide();
                QCloudVideoViewLVB.this.titleBar.setVisibility(View.GONE);
                QCloudVideoViewLVB.this.settingView.setVisibility(View.VISIBLE);
            }
        });
        this.backButton = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_back_text"));
        this.titleName = (TextView) this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_title_text"));
        this.backButton.setVisibility(this.enableBackButton ? View.VISIBLE : View.GONE);
        this.backButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (QCloudVideoViewLVB.this.isFullscreen) {
                    QCloudVideoViewLVB.this.toggleFullscreen();
                } else if (QCloudVideoViewLVB.this.outActivity != null) {
                    QCloudVideoViewLVB.this.outActivity.finish();
                } else if (QCloudVideoViewLVB.this.context instanceof Activity) {
                    ((Activity) QCloudVideoViewLVB.this.context).finish();
                }

            }
        });
        this.requestFocus();
        this.setLongClickable(true);


    }

    /**
     * ----------------------------------新加入功能-----------------------------------------------
     */
    private AutoPlayRunnable mAutoPlayRunnable = new AutoPlayRunnable();
    private Handler mHandler = new Handler();

    private class AutoPlayRunnable implements Runnable {
        private int AUTO_PLAY_INTERVAL = 3000;
        private boolean mShouldAutoPlay;

        public AutoPlayRunnable() {
            mShouldAutoPlay = false;
        }

        public void start() {
            if (!mShouldAutoPlay) {
                mShouldAutoPlay = true;
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, AUTO_PLAY_INTERVAL);
            }
        }

        public void stop() {
            if (mShouldAutoPlay) {
                mHandler.removeCallbacks(this);
                mShouldAutoPlay = false;
            }
        }

        @Override
        public void run() {
            if (mShouldAutoPlay) {
                mHandler.removeCallbacks(this);
                QCloudVideoViewLVB.this.toggleTopBar();
                QCloudVideoViewLVB.this.toggleController();
                stop();
            }
        }
    }

    /**
     * 三秒无操作，收起控制面板
     */
    private void clockManage() {
        if (this.titleBar.getVisibility() == View.VISIBLE) {
            this.controller.setEnableBannedMove(true);
            mAutoPlayRunnable.start();
        } else {
            mAutoPlayRunnable.stop();
        }
    }


    /**
     * 显示加载中
     */
    public void showLoading() {
        this.loadingView.setVisibility(View.VISIBLE);
        this.loadErrorView.setVisibility(View.GONE);
        this.netTieView.setVisibility(View.GONE);
    }

    /**
     * 显示失败
     */
    public void showError() {
        this.loadingView.setVisibility(View.GONE);
        this.loadErrorView.setVisibility(View.VISIBLE);
        this.netTieView.setVisibility(View.GONE);
    }

    /**
     * 显示提示
     */
    public void showNetTie() {
        this.loadingView.setVisibility(View.GONE);
        this.loadErrorView.setVisibility(View.GONE);
        this.netTieView.setVisibility(View.VISIBLE);
    }

    /**
     * 显示视频
     */
    public void showPlayer() {
        this.loadingView.setVisibility(View.GONE);
        this.loadErrorView.setVisibility(View.GONE);
        this.netTieView.setVisibility(View.GONE);
    }

    /**
     * 播放出错监听
     */
    public void setPleyErrorListener(PlayErrorListener l) {
        this.mPlayErrorListener = l;
    }

    /**
     * 播放出错
     */
    public interface PlayErrorListener {
        void onPlayError();
    }

    /**
     * 播放网络提示监听
     */
    public void setPleyContineLIstener(PlayContineListener l) {
        this.mPlayContineListener = l;
    }

    /**
     * 播放网络提示
     */
    public interface PlayContineListener {
        void onPlayContine();
    }

    /**
     * 隐藏返回按钮
     */
    public void hideBack() {
        this.backButton.setVisibility(View.GONE);
    }


    /**
     * 隐藏菜单按钮
     */
    public void hideMenu() {
        if (llaction != null) {
            this.llaction.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏菜单按钮
     */
    public void hideStream() {
        if (mStreamButton != null) {
            this.mStreamButton.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏自定义播放按钮
     */
    public void hideCustomControllFull() {
        this.fullView.setVisibility(View.GONE);
    }


    /**
     * 隐藏播放按钮
     */
    public void hideControllPlay() {
        this.controller.hideControllPlay();
    }

    /**
     * 隐藏全屏按钮
     */
    public void hideControllFullscreen() {
        this.controller.hideControllFullscreen();
    }


    /**
     * 显示返回按钮
     */
    public void showBack() {
        this.backButton.setVisibility(View.VISIBLE);
    }


    /**
     * 显示菜单按钮
     */
    public void showMenu() {
        this.llaction.setVisibility(View.VISIBLE);
    }

    /**
     * 显示自定义播放按钮
     */
    public void showCustomControllFull() {
        this.fullView.setVisibility(View.VISIBLE);
    }


    /**
     * 显示播放按钮
     */
    public void showControllPlay() {
        this.controller.showControllPlay();
    }

    /**
     * 显示全屏按钮
     */
    public void showControllFullscreen() {
        this.controller.showControllFullscreen();
    }

    /**
     * ----------------------------------新加入功能-----------------------------------------------
     */

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            if (this.mVideoWidth * height > width * this.mVideoHeight) {
                height = width * this.mVideoHeight / this.mVideoWidth;
            } else if (this.mVideoWidth * height < width * this.mVideoHeight) {
                width = height * this.mVideoWidth / this.mVideoHeight;
            }

            ViewGroup.LayoutParams layoutParams = this.rootView.getLayoutParams();
            layoutParams.height = -1;
            layoutParams.width = -1;
            this.rootView.setLayoutParams(layoutParams);
            ViewGroup.LayoutParams lp = this.videoSurfaceContainer.getLayoutParams();
            lp.height = height;
            lp.width = width;
            this.videoSurfaceContainer.setLayoutParams(lp);
        }

        this.setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setVideoInfo(VideoInfo info, boolean autoStart) {

        if (this.controller != null) {
            this.controller.hide();
        }
        if (this.videoInfo != null) {
            this.stopPlayback();
        }

        this.videoInfo = info;
        this.autoStart = autoStart;

        try {
            VideoInfo.validate(this.videoInfo);
        } catch (IllegalArgumentException var5) {
            PlayerUtils.log(6, "QCloudVideoView", "Illegal video info : " + var5.getMessage());
            CallBack callBack = PlayerConfig.g().getCallBack();
            if (callBack != null) {
                callBack.onEvent(CallBack.EVENT_PLAY_ERROR, var5.getMessage(), this.videoInfo);
            }
            return;
        }

        if (this.titleName != null && videoInfo != null) {
            this.titleName.setText(info.getVideoName() == null ? "" : info.getVideoName());
        }
        this.controller.setCurrentStreamName(this.videoInfo.getDefaultStreamName());
        this.controller.setSeekBarrier(this.videoInfo.getDurationAllow());
        PlayerUtils.log(3, "QCloudVideoView", "setVideoInfo: isSurfaceCreated=" + this.isSurfaceCreated);
        if (this.isSurfaceCreated) {
            if (this.videoSurface != null) {
                this.play(this.videoSurface.getHolder());
            }
        }

    }

    public boolean isEnableTopBar() {
        return this.enableTopBar;
    }

    public void setEnableTopBar(boolean enableTopBar) {
        this.enableTopBar = enableTopBar;
    }

    public boolean isEnableBackButton() {
        return this.enableBackButton;
    }

    public void setEnableBackButton(Activity outActivity, boolean enableBackButton) {
        this.outActivity = outActivity;
        this.enableBackButton = enableBackButton;
        if (this.backButton != null) {
            this.backButton.setVisibility(enableBackButton ? View.VISIBLE : View.GONE);
        }

    }

    public int getCurrentPosition() {
        return this.mediaPlayerControl == null ? 0 : this.mediaPlayerControl.getCurrentPosition();
    }

    public void puase() {
        if (this.mediaPlayerControl != null) {
            this.mediaPlayerControl.pause();
        }

    }

    public void resume() {
        if (this.mediaPlayerControl != null) {
            this.mediaPlayerControl.start();
        }

    }

    public int getDuration() {
        return this.mediaPlayerControl == null ? 0 : this.mediaPlayerControl.getDuration();
    }

    public void seekTo(int position) {
        if (this.mediaPlayerControl != null) {
            this.mediaPlayerControl.seekTo(position);
        }

    }

    public void togglePlay() {
        if (this.mediaPlayerControl != null) {
            if (this.mediaPlayerControl.isPlaying()) {
                this.mediaPlayerControl.pause();
            } else {
                this.mediaPlayerControl.start();
            }

        }
    }

    public void showController(int timeout) {
        if (this.controller != null) {
            this.controller.show(timeout);
        }

    }

    public void hideController() {
        if (this.controller != null) {
            this.controller.hide();
        }

    }

    public void toggleController() {
        if (this.controller != null) {
            if (this.controller.isShowing()) {
                this.controller.hide();
            } else {
                this.controller.show(0);
            }

        }
    }

    public void showTopBar() {
        if (this.enableTopBar && this.titleBar != null) {
            this.titleBar.setVisibility(View.VISIBLE);
            if (this.settingView != null) {
                this.settingView.setVisibility(View.GONE);
            }
        }

    }

    public void hideTopBar() {
        if (this.titleBar != null) {
            this.titleBar.setVisibility(View.GONE);
            if (this.settingView != null) {
                this.settingView.setVisibility(View.GONE);
            }
        }

    }

    public void toggleTopBar() {
        if (this.titleBar != null) {
            if (this.titleBar.getVisibility() == View.VISIBLE) {
                this.titleBar.setVisibility(View.GONE);
            } else {
                this.titleBar.setVisibility(View.VISIBLE);
            }
            if (this.settingView != null) {
                this.settingView.setVisibility(View.GONE);
            }

        }
    }

    public void forward(int millisecond) {
        if (this.mediaPlayerControl != null) {
            int pos = this.mediaPlayerControl.getCurrentPosition();
            this.mediaPlayerControl.seekTo(pos + millisecond);
        }
    }

    public void backward(int milisecond) {
        if (this.mediaPlayerControl != null) {
            int pos = this.mediaPlayerControl.getCurrentPosition();
            this.mediaPlayerControl.seekTo(pos - milisecond);
        }
    }

    public void toggleFullscreen() {
        if (this.mediaPlayerControl != null) {
            this.mediaPlayerControl.toggleFullScreen();
        }
    }

    public boolean isFullscreen() {
        return this.isFullscreen;
    }

    public void increaseVolume() {
        if (this.audioManager != null) {
            this.updateCurrentVolume();
            PlayerUtils.log(3, "QCloudVideoView", "currentVolume=" + this.currentVolume);
            this.audioManager.setStreamVolume(3, this.currentVolume + this.volumeOffset, 0);
            this.updateVolumeController();
        }
    }

    public void decreaseVolume() {
        if (this.audioManager != null) {
            this.updateCurrentVolume();
            PlayerUtils.log(3, "QCloudVideoView", "currentVolume=" + this.currentVolume);
            this.audioManager.setStreamVolume(3, this.currentVolume - this.volumeOffset, 0);
            this.updateVolumeController();
        }
    }

    public void setEnableBrightnessControll(Activity activity, boolean enabled) {
        this.outActivity = activity;
        this.enableBrightnessControll = enabled;
        if (enabled) {
            this.brightnessControllerContainer.setVisibility(View.VISIBLE);
        } else {
            this.brightnessControllerContainer.setVisibility(View.GONE);
        }

    }

    public void increaseBrightness() {
        if (this.enableBrightnessControll && this.outActivity != null) {
            int brightness = this.getBrightness();
            this.setBrightness(brightness + 5);
        }
    }

    public void decreaseBrightness() {
        if (this.enableBrightnessControll && this.outActivity != null) {
            int brightness = this.getBrightness();
            this.setBrightness(brightness - 5);
        }
    }

    public void setBrightness(int brightness) {
        if (this.enableBrightnessControll && this.outActivity != null) {
            PlayerUtils.log(3, "QCloudVideoView", "setBrightness: brightness=" + brightness);
            android.view.WindowManager.LayoutParams layout = this.outActivity.getWindow().getAttributes();
            if (brightness < 1) {
                brightness = 1;
            }

            if (brightness > 100) {
                brightness = 100;
            }

            layout.screenBrightness = 1.0F * (float) brightness / 100.0F;
            this.outActivity.getWindow().setAttributes(layout);
            if (this.brightnessController != null) {
                this.brightnessController.setProgress(brightness);
            }

        }
    }

    public int getBrightness() {
        if (this.enableBrightnessControll && this.outActivity != null) {
            float brightness = this.outActivity.getWindow().getAttributes().screenBrightness;
            if (brightness < 0.0F) {
                try {
                    int e = System.getInt(this.context.getContentResolver(), "screen_brightness");
                    brightness = 1.0F * (float) e / 255.0F;
                } catch (SettingNotFoundException var3) {
                    var3.printStackTrace();
                }
            }

            return (int) (brightness * 100.0F);
        } else {
            return 0;
        }
    }

    public void setEnableGesture(boolean enableGesture) {
        this.enableGesture = enableGesture;
    }

    public boolean getEnableGesture() {
        return this.enableGesture;
    }

    private void play(SurfaceHolder holder) {
        if (!PlayerUtils.isNetworkAvailable()) {
            Toast.makeText(this.context, PlayerUtils.getResourceIdByName("string", "qcloud_player_network_connection_failed"), Toast.LENGTH_LONG).show();
        } else {
            this.videoSizeIsSet = false;
            this.mediaPlayerIsPrepared = false;
            if (this.controller != null) {
                this.controller.setEnabled(false);
                this.controller.setProgress();
            }

            this.isloading = true;
//            this.player = new MediaPlayer();
//            this.player.setAudioStreamType(3);
//
//            try {
//                this.player.setDataSource(this.context, Uri.parse(this.videoInfo.getCurrentStreamUrl()));
//            } catch (IOException var3) {
//                var3.printStackTrace();
//            }


            //http://9890.vod.myqcloud.com/9890_2c5731709d7911e581efe1226474f963.f30.mp4

//            this.videoSurface.setVideoPath("http://9890.vod.myqcloud.com/9890_2c5731709d7911e581efe1226474f963.f30.mp4");
            this.videoSurface.setVideoPath(this.videoInfo.getCurrentStreamUrl());
            Log.e("dou361", "------------his.videoInfo.getCurrentStreamUrl()------------" + this.videoInfo.getCurrentStreamUrl());
            this.videoSurface.setOnPreparedListener(this);
            this.videoSurface.setOnErrorListener(this);
            this.videoSurface.setOnCompletionListener(this);
            this.videoSurface.setOnInfoListener(this);

//            this.player.setOnVideoSizeChangedListener(this);
//            this.player.setOnBufferingUpdateListener(this);
//            this.player.setOnPreparedListener(this);
//            this.player.setOnErrorListener(this);
//            this.player.setOnCompletionListener(this);
//            this.player.setOnInfoListener(this);
//            this.player.setOnSeekCompleteListener(this);
//            this.player.setDisplay(holder);
//            this.player.prepareAsync();
            this.handler.sendEmptyMessageDelayed(1, 30000L);
        }
    }

    public boolean isPlaying() {
        return this.mediaPlayerControl == null ? false : this.mediaPlayerControl.isPlaying();
    }

    public void stopPlayback() {
        if (this.videoSurface != null) {
            int pos = this.mediaPlayerControl.getCurrentPosition();
            this.videoSurface.stopPlayback();
            this.videoSurface = null;
            if (this.handler != null) {
                this.handler.removeMessages(2);
            }

            CallBack callBack = PlayerConfig.g().getCallBack();
            if (callBack != null) {
                if (this.videoInfo != null) {
                    this.videoInfo.setCurrentPosition(pos);
                    callBack.onEvent(CallBack.EVENT_PLAY_STOP, "video stopped", this.videoInfo);
                }
            }
        }

    }

    private void updateCurrentVolume() {
        this.currentVolume = this.audioManager.getStreamVolume(3);
    }

    private void updateVolumeController() {
        this.updateCurrentVolume();
        if (this.volumeController != null) {
            this.volumeController.setProgress(this.currentVolume);
        }

    }

    public void switchStream(int streamNum) {
        PlayerUtils.log(4, "QCloudVideoView", "switchStream: stream=" + streamNum);
        if (streamNum >= 0 && this.videoInfo != null && streamNum < this.videoInfo.getStreamUrls().length) {
            if (streamNum != this.videoInfo.getCurrentStream()) {
                this.videoInfo.setCurrentStream(streamNum);
                this.videoInfo.setCurrentPosition((int) this.videoSurface.getCurrentPosition());
                this.controller.setCurrentStreamName(this.videoInfo.getStreamNames()[streamNum]);
                this.streamSelectAdapter.notifyDataSetChanged();
                this.stopPlayback();
                this.play(this.videoSurface.getHolder());
            }
        }
    }

    private void showStreamSelectView() {
        this.streamSelectView.setVisibility(View.VISIBLE);
        this.titleBar.setVisibility(View.GONE);
        this.streamSelectListView.setItemsCanFocus(true);
        this.isStreamSelectViewShown = true;
    }

    private void hideStreamSelectView() {
        this.streamSelectView.setVisibility(View.GONE);
        this.isStreamSelectViewShown = false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.mGestureDetector.onTouchEvent(event);
    }

    private void handleScrollOnLeftArea(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (distanceY > 0.0F) {
            this.increaseBrightness();
        } else {
            this.decreaseBrightness();
        }

    }

    private void handleScrollOnRightArea(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (distanceY > 0.0F) {
            this.increaseVolume();
        } else {
            this.decreaseVolume();
        }

    }

    private void handleScrollOnMiddleArea(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (distanceX < 0.0F) {
            this.forward(10000);
        } else {
            this.backward(10000);
        }

    }

    public void setOnKeyDownListener(QCloudVideoViewLVB.OnKeyDownListener listener) {
        this.onKeyDownListener = listener;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PlayerUtils.log(3, "QCloudVideoView", "onKeyDown keyCode=" + keyCode);
        switch (keyCode) {
            case 24:
                this.increaseVolume();
                return true;
            case 25:
                this.decreaseVolume();
                return true;
            default:
                return this.onKeyDownListener != null ? this.onKeyDownListener.onKeyDown(keyCode, event) : super.onKeyDown(keyCode, event);
        }
    }

    public void setOnKeyLongPressListener(QCloudVideoViewLVB.OnKeyLongPressListener listener) {
        this.onKeyLongPressListener = listener;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return this.onKeyLongPressListener != null ? this.onKeyLongPressListener.onKeyLongPress(keyCode, event) : super.onKeyLongPress(keyCode, event);
    }

    public void setOnKeyMultipleListener(QCloudVideoViewLVB.OnKeyMultipleListener listener) {
        this.onKeyMultipleListener = listener;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return this.onKeyMultipleListener != null ? this.onKeyMultipleListener.onKeyMultiple(keyCode, repeatCount, event) : super.onKeyMultiple(keyCode, repeatCount, event);
    }

    public void setOnKeyUpListener(QCloudVideoViewLVB.OnKeyUpListener listener) {
        this.onKeyUpListener = listener;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return this.onKeyUpListener != null ? this.onKeyUpListener.onKeyUp(keyCode, event) : super.onKeyUp(keyCode, event);
    }

    public void setOnPreparedListener(QCloudVideoViewLVB.OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        PlayerUtils.log(3, "QCloudVideoView", "onPrepared is called");
        this.handler.removeMessages(1);
        this.mediaPlayerIsPrepared = true;
        this.isloading = false;
        this.controller.setMediaPlayer(this.mediaPlayerControl);
        this.controller.setAnchorView((RelativeLayout) this.rootView);
        this.controller.setEnabled(true);
        this.videoSurface.start();
        PlayerUtils.log(3, "QCloudVideoView", "player.start()");
        if (this.videoSizeIsSet) {
            this.videoSurface.seekTo(this.videoInfo.getCurrentPosition());
        }

        if (!this.autoStart) {
            this.videoSurface.pause();
            this.autoStart = true;
        } else if (this.handler != null) {
            this.handler.sendEmptyMessage(2);
        }

        this.controller.setProgress();
        this.controller.updatePausePlay();
        this.controller.updateFullScreen();
        this.controller.hide();
        this.hideTopBar();
        CallBack callBack = PlayerConfig.g().getCallBack();
        if (callBack != null) {
            callBack.onEvent(CallBack.EVENT_PLAY_START, "video start", this.videoInfo);
        }

        if (this.onPreparedListener != null) {
            this.onPreparedListener.onPrepared(mp);
        }

    }

    public void setOnCompletionListener(QCloudVideoViewLVB.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public void onCompletion(IMediaPlayer mp) {
        CallBack callBack = PlayerConfig.g().getCallBack();
        if (callBack != null) {
            callBack.onEvent(CallBack.EVENT_PLAY_COMPLETE, "play complete", this.videoInfo);
        }

        if (this.onCompletionListener != null) {
            this.onCompletionListener.onCompletion(mp);
        }

    }

    public void setOnVideoSizeChangedListener(QCloudVideoViewLVB.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.onVideoSizeChangedListener = onVideoSizeChangedListener;
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        PlayerUtils.log(3, "QCloudVideoView", "onVideoSizeChanged called");
        this.mVideoWidth = mp.getVideoWidth();
        this.mVideoHeight = mp.getVideoHeight();
        if (width != 0 && height != 0) {
            this.videoSizeIsSet = true;
            if (this.mediaPlayerIsPrepared) {
                this.videoSurface.seekTo(this.videoInfo.getCurrentPosition());
            }
        } else {
            PlayerUtils.log(3, "QCloudVideoView", "invalid video width(" + width + ") or height(" + height + ")");
        }

        if (this.onVideoSizeChangedListener != null) {
            this.onVideoSizeChangedListener.onVideoSizeChanged(mp, width, height);
        }

    }

    public void setOnBufferingUpdateListener(QCloudVideoViewLVB.OnBufferingUpdateListener onBufferingUpdateListener) {
        this.onBufferingUpdateListener = onBufferingUpdateListener;
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.bufferPercent = percent;
        if (this.onBufferingUpdateListener != null) {
            this.onBufferingUpdateListener.onBufferingUpdate(mp, percent);
        }

    }

    public void setOnErrorListener(QCloudVideoViewLVB.OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        String errorWhat;
        switch (what) {
            case 1:
                errorWhat = "MEDIA_ERROR_UNKNOWN";
                break;
            case 100:
                errorWhat = "MEDIA_ERROR_SERVER_DIED";
                break;
            default:
                errorWhat = "!";
        }

        String errorExtra;
        switch (extra) {
            case -1010:
                errorExtra = "MEDIA_ERROR_UNSUPPORTED";
                break;
            case -1007:
                errorExtra = "MEDIA_ERROR_MALFORMED";
                break;
            case -1004:
                errorExtra = "MEDIA_ERROR_IO";
                break;
            case -110:
                errorExtra = "MEDIA_ERROR_TIMED_OUT";
                break;
            default:
                errorExtra = "!";
        }

        String msg = String.format("what:%d,%s extra:%d,%s", new Object[]{Integer.valueOf(what), errorWhat, Integer.valueOf(extra), errorExtra});
        PlayerUtils.log(4, "QCloudVideoView", "onErrorListener " + msg);
        CallBack callBack = PlayerConfig.g().getCallBack();
        if (callBack != null) {
            callBack.onEvent(CallBack.EVENT_PLAY_ERROR, msg, this.videoInfo);
        }

        return this.onErrorListener == null || this.onErrorListener.onError(mp, what, extra);
    }

    public void setOnInfoListener(QCloudVideoViewLVB.OnInfoListener onInfoListener) {
        this.onInfoListener = onInfoListener;
    }

    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        PlayerUtils.log(4, "QCloudVideoView", "onInfo: what=" + what + " extra=" + extra);
        return this.onInfoListener != null && this.onInfoListener.onInfo(mp, what, extra);
    }

    public void setOnSeekCompleteListener(QCloudVideoViewLVB.OnSeekCompleteListener onSeekCompleteListener) {
        this.onSeekCompleteListener = onSeekCompleteListener;
    }

    public void onSeekComplete(MediaPlayer mp) {
        if (this.onSeekCompleteListener != null) {
            this.onSeekCompleteListener.onSeekComplete(mp);
        }

    }

    public void setOnToggleFullscreenListener(QCloudVideoViewLVB.OnToggleFullscreenListener listener) {
        this.onToggleFullscreenListener = listener;
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(MediaPlayer var1, int var2);
    }

    public interface OnCompletionListener {
        void onCompletion(IMediaPlayer var1);
    }

    public interface OnErrorListener {
        boolean onError(IMediaPlayer var1, int var2, int var3);
    }

    public interface OnInfoListener {
        boolean onInfo(IMediaPlayer var1, int var2, int var3);
    }

    public interface OnKeyDownListener {
        boolean onKeyDown(int var1, KeyEvent var2);
    }

    public interface OnKeyLongPressListener {
        boolean onKeyLongPress(int var1, KeyEvent var2);
    }

    public interface OnKeyMultipleListener {
        boolean onKeyMultiple(int var1, int var2, KeyEvent var3);
    }

    public interface OnKeyUpListener {
        boolean onKeyUp(int var1, KeyEvent var2);
    }

    public interface OnPreparedListener {
        void onPrepared(IMediaPlayer var1);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(MediaPlayer var1);
    }

    public interface OnToggleFullscreenListener {
        void onToggleFullscreen(boolean var1);
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(MediaPlayer var1, int var2, int var3);
    }

    class StreamSelectAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        public StreamSelectAdapter(Context context) {
            this.layoutInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return QCloudVideoViewLVB.this.videoInfo == null ? 0 : QCloudVideoViewLVB.this.videoInfo.getStreamNames().length;
        }

        public Object getItem(int position) {
            return QCloudVideoViewLVB.this.videoInfo.getStreamNames()[position];
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            QCloudVideoViewLVB.StreamSelectAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = this.layoutInflater.inflate(PlayerUtils.getResourceIdByName("layout", "qcloud_player_select_streams_list_item"), (ViewGroup) null);
                holder = new QCloudVideoViewLVB.StreamSelectAdapter.ViewHolder();
                holder.streamName = (TextView) convertView.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_stream_name"));
                convertView.setTag(holder);
            } else {
                holder = (QCloudVideoViewLVB.StreamSelectAdapter.ViewHolder) convertView.getTag();
            }

            String streamName = (String) this.getItem(position);
            holder.streamName.setText(streamName);
            if (position == QCloudVideoViewLVB.this.videoInfo.getCurrentStream()) {
                holder.streamName.setTextColor(QCloudVideoViewLVB.this.getResources().getColor(PlayerUtils.getResourceIdByName("color", "qcloud_player_stream_name_playing")));
            } else {
                holder.streamName.setTextColor(QCloudVideoViewLVB.this.getResources().getColor(PlayerUtils.getResourceIdByName("color", "qcloud_player_stream_name_normal")));
            }

            return convertView;
        }

        class ViewHolder {
            public TextView streamName;

            ViewHolder() {
            }
        }
    }
}
