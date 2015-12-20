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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.SurfaceHolder.Callback;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.qcloud.player.CallBack;
import com.qcloud.player.PlayerConfig;
import com.qcloud.player.VideoInfo;
import com.qcloud.player.ui.VideoControllerView.MediaPlayerControl;
import com.qcloud.player.util.PlayerUtils;

import java.io.IOException;
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
 * 创建日期：2015/12/15 11:16
 * <p/>
 * 描 述：网络播放器
 * 1当前播放器还没有设置为收费播放器
 * 2当前播放器可以根据网络类型设置是否可观看（2/3/4G下询问播放，WiFi下自动播放）
 * 3.当前控制面板为（①上面titilebar 返回 三个自定义按钮 分辨率 全屏 菜单②右上全屏③中间播放④下面播放工具条 播放 播放进度 全屏）
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class QCloudVideoView extends FrameLayout implements android.media.MediaPlayer.OnPreparedListener, android.media.MediaPlayer.OnBufferingUpdateListener, android.media.MediaPlayer.OnCompletionListener, android.media.MediaPlayer.OnInfoListener, android.media.MediaPlayer.OnErrorListener, android.media.MediaPlayer.OnVideoSizeChangedListener, android.media.MediaPlayer.OnSeekCompleteListener {
    private Context context;
    private Activity outActivity;
    /** 视频外层布局 */
    private View rootView;
    /** 视频播放view */
    private SurfaceView videoSurface;
    /** MediaPlayer */
    private MediaPlayer player;
    /** 自定义控制面板 */
    private VideoControllerView controller;
    /** 分辨率view */
    private View streamSelectView;
    /** 分辨率列表 */
    private ListView streamSelectListView;
    private QCloudVideoView.StreamSelectAdapter streamSelectAdapter;
    private FrameLayout videoSurfaceContainer;
    private SeekBar volumeController;
    private View brightnessControllerContainer;
    private SeekBar brightnessController;
    private View titleBar;
    private View backButton;
    private TextView titleName;
    private View settingView;
    /** 分辨率按钮 */
    private TextView mStreamButton;
    /** 播放按钮 */
    private ImageView playView;
    /** 播放按钮 */
    private ImageView fullView;
    /** 加载中布局 */
    private View loadingView;
    /** 加载失败布局 */
    private View loadErrorView;
    /** 重新连接 */
    private View retryView;
    private View llaction;
    /** 网络提示布局 */
    private View netTieView;
    /** 继续观看 */
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
    private QCloudVideoView.OnToggleFullscreenListener onToggleFullscreenListener;
    private OnSeekBarChangeListener onBrightnessControllerChangeListener;
    private OnSeekBarChangeListener onVolumeControllerChangeListener;
    private OnClickListener onStreamSelectButtonClickListener;
    private Callback surfaceCallback;
    private GestureDetector mGestureDetector;
    private QCloudVideoView.OnKeyDownListener onKeyDownListener;
    private QCloudVideoView.OnKeyLongPressListener onKeyLongPressListener;
    private QCloudVideoView.OnKeyMultipleListener onKeyMultipleListener;
    private QCloudVideoView.OnKeyUpListener onKeyUpListener;
    private QCloudVideoView.OnPreparedListener onPreparedListener;
    private QCloudVideoView.OnCompletionListener onCompletionListener;
    private QCloudVideoView.OnVideoSizeChangedListener onVideoSizeChangedListener;
    private QCloudVideoView.OnBufferingUpdateListener onBufferingUpdateListener;
    private QCloudVideoView.OnErrorListener onErrorListener;
    private QCloudVideoView.OnInfoListener onInfoListener;
    private QCloudVideoView.OnSeekCompleteListener onSeekCompleteListener;
    private MediaPlayerControl mediaPlayerControl;
    private Handler handler;

    public QCloudVideoView(Context context) {
        this(context, (AttributeSet)null, 0);
    }

    public QCloudVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QCloudVideoView(Context context, AttributeSet attrs, int defStyle) {
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
                QCloudVideoView.this.setBrightness(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        this.onVolumeControllerChangeListener = new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                QCloudVideoView.this.currentVolume = seekBar.getProgress();
                QCloudVideoView.this.audioManager.setStreamVolume(3, progress, 0);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        this.onStreamSelectButtonClickListener = new OnClickListener() {
            public void onClick(View v) {
                QCloudVideoView.this.controller.hide();
                QCloudVideoView.this.showStreamSelectView();
            }
        };
        this.surfaceCallback = new Callback() {
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                PlayerUtils.log(3, "QCloudVideoView", "surfaceChanged");
            }

            public void surfaceCreated(SurfaceHolder holder) {
                PlayerUtils.log(3, "QCloudVideoView", "surfaceCreated");
                QCloudVideoView.this.isSurfaceCreated = true;
                if(QCloudVideoView.this.videoInfo != null) {
                    PlayerUtils.log(3, "QCloudVideoView", "surfaceCreated: videoInfo != null");
                    QCloudVideoView.this.play(holder);
                }

            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                PlayerUtils.log(3, "QCloudVideoView", "surfaceDestroyed");
                if(QCloudVideoView.this.player != null) {
                    if(QCloudVideoView.this.player.isPlaying()) {
                        QCloudVideoView.this.videoInfo.setCurrentPosition(QCloudVideoView.this.player.getCurrentPosition());
                    }

                    QCloudVideoView.this.stopPlayback();
                }

            }
        };
        this.mGestureDetector = new GestureDetector(this.getContext(), new OnGestureListener() {
            public boolean onDown(MotionEvent e) {
                return true;
            }

            public void onShowPress(MotionEvent e) {
                if(QCloudVideoView.this.enableGesture) {
                    ;
                }
            }

            public boolean onSingleTapUp(MotionEvent e) {

                QCloudVideoView.this.toggleController();
                QCloudVideoView.this.toggleTopBar();
                clockManage();
                if(QCloudVideoView.this.isStreamSelectViewShown) {
                    QCloudVideoView.this.hideStreamSelectView();
                }

                return false;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if(!QCloudVideoView.this.enableGesture) {
                    return false;
                } else {
                    int left = QCloudVideoView.this.rootView.getLeft();
                    int right = QCloudVideoView.this.rootView.getRight();
                    int middleLeft = left + QCloudVideoView.this.rootView.getWidth() / 3;
                    int middleRight = right - QCloudVideoView.this.rootView.getWidth() / 3;
                    float x1 = e1.getX();
                    float x2 = e2.getX();
                    if(x1 >= (float)left && x1 < (float)right && x2 >= (float)left && x2 < (float)right && e2.getY() >= (float)QCloudVideoView.this.rootView.getTop() && e2.getY() < (float)QCloudVideoView.this.rootView.getBottom()) {
                        if(x1 < (float)middleLeft && x2 < (float)middleLeft) {
                            QCloudVideoView.this.handleScrollOnLeftArea(e1, e2, distanceX, distanceY);
                            return true;
                        } else if(x1 >= (float)middleLeft && x1 < (float)middleRight && x2 >= (float)middleLeft && x2 < (float)middleRight) {
                            QCloudVideoView.this.handleScrollOnMiddleArea(e1, e2, distanceX, distanceY);
                            return true;
                        } else if(x1 >= (float)middleRight && x2 >= (float)middleRight) {
                            QCloudVideoView.this.handleScrollOnRightArea(e1, e2, distanceX, distanceY);
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
                if(QCloudVideoView.this.enableGesture) {
                    ;
                }
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return !QCloudVideoView.this.enableGesture?false:false;
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
                return QCloudVideoView.this.bufferPercent;
            }

            public int getCurrentPosition() {
                int pos;
                try {
                    pos = QCloudVideoView.this.player.getCurrentPosition();
                } catch (Exception var3) {
                    pos = 0;
                }

                return pos;
            }

            public int getDuration() {
                return QCloudVideoView.this.player != null && QCloudVideoView.this.mediaPlayerIsPrepared?QCloudVideoView.this.player.getDuration():0;
            }

            public boolean isPlaying() {
                return QCloudVideoView.this.player != null && QCloudVideoView.this.player.isPlaying();
            }

            public void pause() {
                PlayerUtils.log(3, "QCloudVideoView", "video pause");
                if(QCloudVideoView.this.player != null && QCloudVideoView.this.player.isPlaying()) {
                    QCloudVideoView.this.videoInfo.setCurrentPosition(QCloudVideoView.this.player.getCurrentPosition());
                    QCloudVideoView.this.player.pause();
                    if(QCloudVideoView.this.handler != null) {
                        QCloudVideoView.this.handler.removeMessages(2);
                    }

                    CallBack callBack = PlayerConfig.g().getCallBack();
                    if(callBack != null) {
                        callBack.onEvent(CallBack.EVENT_PLAY_PAUSE, "video paused", QCloudVideoView.this.videoInfo);
                    }
                }

            }

            public void seekTo(int i) {
                if(QCloudVideoView.this.videoInfo != null && QCloudVideoView.this.player != null) {
                    if(i < 0) {
                        i = 0;
                    }

                    int duration = this.getDuration();
                    if(i > duration) {
                        i = duration;
                    }

                    int durationAllow = QCloudVideoView.this.videoInfo.getDurationAllow();
                    if(durationAllow >= 0 && durationAllow < i) {
                        PlayerUtils.log(3, "QCloudVideoView", "seekTo: position out of bound");
                        this.seekTo(0);
                        this.start();
                        this.pause();
                        if(QCloudVideoView.this.controller != null) {
                            QCloudVideoView.this.controller.setProgress();
                            QCloudVideoView.this.controller.updatePausePlay();
                        }

                        CallBack callBack = PlayerConfig.g().getCallBack();
                        if(callBack != null) {
                            callBack.onEvent(CallBack.EVENT_PLAY_POSITION_OUT_OF_BOUND, "position out of bound", QCloudVideoView.this.videoInfo);
                        }
                    } else {
                        QCloudVideoView.this.player.seekTo(i);
                    }

                }
            }

            public void start() {
                CallBack callBack = PlayerConfig.g().getCallBack();
                if(callBack != null) {
                    callBack.onEvent(CallBack.EVENT_PLAY_RESUME, "video resumed", QCloudVideoView.this.videoInfo);
                }

                QCloudVideoView.this.player.start();
                if(QCloudVideoView.this.handler != null) {
                    QCloudVideoView.this.handler.sendEmptyMessage(2);
                }

            }

            public boolean isFullScreen() {
                return QCloudVideoView.this.isFullscreen;
            }

            public void toggleFullScreen() {
                if(QCloudVideoView.this.isFullscreen) {
                    QCloudVideoView.this.isFullscreen = false;
                } else {
                    QCloudVideoView.this.isFullscreen = true;
                }

                if(QCloudVideoView.this.onToggleFullscreenListener != null) {
                    QCloudVideoView.this.onToggleFullscreenListener.onToggleFullscreen(QCloudVideoView.this.isFullscreen);
                }

            }

            public boolean isLoading() {
                return QCloudVideoView.this.isloading;
            }

            public boolean enableToggleFullScreen() {
                return true;
            }

            public void hideTopBar() {
                QCloudVideoView.this.hideTopBar();
            }

            @Override
            public void hideCenterPlay() {
                QCloudVideoView.this.hideCenterPlay();
            }

            @Override
            public void hideRightTopFull() {
                QCloudVideoView.this.hideRightTopFull();
            }
        };
        this.handler = new Handler() {
            public void handleMessage(Message msg) {
                CallBack callBack = PlayerConfig.g().getCallBack();
                switch(msg.what) {
                    case 1:
                        if(callBack != null) {
                            callBack.onEvent(CallBack.EVENT_LOAD_TIME_OUT, "time out", QCloudVideoView.this.videoInfo);
                        }
                        break;
                    case 2:
                        if(QCloudVideoView.this.mediaPlayerControl != null) {
                            int pos = QCloudVideoView.this.mediaPlayerControl.getCurrentPosition();
                            if(QCloudVideoView.this.videoInfo == null || QCloudVideoView.this.videoInfo.getDurationAllow() >= 0 && QCloudVideoView.this.videoInfo.getDurationAllow() <= pos) {
                                QCloudVideoView.this.mediaPlayerControl.seekTo(0);
                                QCloudVideoView.this.mediaPlayerControl.start();
                                QCloudVideoView.this.mediaPlayerControl.pause();
                                if(QCloudVideoView.this.controller != null) {
                                    QCloudVideoView.this.controller.setProgress();
                                    QCloudVideoView.this.controller.updatePausePlay();
                                }

                                if(callBack != null) {
                                    callBack.onEvent(CallBack.EVENT_PLAY_POSITION_OUT_OF_BOUND, "position out of bound", QCloudVideoView.this.videoInfo);
                                }
                            } else if(QCloudVideoView.this.mediaPlayerControl.isPlaying()) {
                                QCloudVideoView.this.handler.sendEmptyMessageDelayed(2, 200L);
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
        this.rootView = inflater.inflate(PlayerUtils.getResourceIdByName("layout", "qcloud_player_video_root"), (ViewGroup)null);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        this.addView(this.rootView, layoutParams);
        this.videoSurfaceContainer = (FrameLayout)this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_video_surface_container"));
        this.videoSurface = (SurfaceView)this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_video_surface"));
        SurfaceHolder videoHolder = this.videoSurface.getHolder();
        videoHolder.addCallback(this.surfaceCallback);
        this.controller = new VideoControllerView(this.context);
        this.controller.setStreamSelectListener(this.onStreamSelectButtonClickListener);
        this.streamSelectView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_select_stream_container"));
        this.streamSelectListView = (ListView)this.streamSelectView.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_select_streams_list"));
        this.streamSelectAdapter = new QCloudVideoView.StreamSelectAdapter(this.context);
        this.streamSelectListView.setAdapter(this.streamSelectAdapter);
        this.streamSelectListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QCloudVideoView.this.hideStreamSelectView();
                QCloudVideoView.this.switchStream(position);
            }
        });

        /** 新加入功能 */
        this.llaction = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_ll_action"));
        this.mStreamButton = (TextView) this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_select_streams_btn"));
        this.playView = (ImageView)this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_btn_play"));
        this.fullView = (ImageView)this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_btn_full"));
        this.loadingView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_ll_loading"));
        this.loadErrorView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_ll_error"));
        this.retryView = this.loadErrorView.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_btn_retry"));
        this.netTieView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_ll_tie"));
        this.contineView = this.netTieView.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_btn_contine"));
        showLoading();
        retryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                QCloudVideoView.this.showLoading();
                QCloudVideoView.this.playView.setVisibility(View.GONE);
                mPlayErrorListener.onPlayError();
            }
        });
        contineView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                QCloudVideoView.this.showLoading();
                QCloudVideoView.this.playView.setVisibility(View.GONE);
                mPlayContineListener.onPlayContine();
            }
        });
        this.controller.setStreamButton(mStreamButton);
        this.controller.setPlayView(playView);
        this.controller.setFullView(fullView);

        /** 新加入功能 */

        this.audioManager = (AudioManager)this.context.getSystemService(Context.AUDIO_SERVICE);
        this.maxVolume = this.audioManager.getStreamMaxVolume(3);
        this.volumeController = (SeekBar)this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_volume_controller"));
        this.volumeController.setMax(this.maxVolume);
        this.volumeOffset = this.maxVolume < 20?1:this.maxVolume / 20;
        this.updateVolumeController();
        this.volumeController.setOnSeekBarChangeListener(this.onVolumeControllerChangeListener);
        this.brightnessControllerContainer = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_brightness_controller_container"));
        this.brightnessControllerContainer.setVisibility(View.GONE);
        this.brightnessController = (SeekBar)this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_brightness_controller"));
        this.brightnessController.setMax(100);
        float brightness = 0.01F;

        try {
            int e = System.getInt(this.context.getContentResolver(), "screen_brightness");
            brightness = 1.0F * (float)e / 255.0F;
        } catch (SettingNotFoundException var7) {
            var7.printStackTrace();
        }

        this.brightnessController.setProgress((int) (brightness * 100.0F));
        this.brightnessController.setOnSeekBarChangeListener(this.onBrightnessControllerChangeListener);
        this.titleBar = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_title_bar"));
        this.settingView = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_settings_container"));
        this.llaction.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                QCloudVideoView.this.controller.hide();
                QCloudVideoView.this.titleBar.setVisibility(View.GONE);
                QCloudVideoView.this.playView.setVisibility(View.GONE);
                QCloudVideoView.this.settingView.setVisibility(View.VISIBLE);
            }
        });
        this.backButton = this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_back_text"));
        this.titleName = (TextView)this.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_title_text"));
        this.backButton.setVisibility(this.enableBackButton ? View.VISIBLE : View.GONE);
        this.backButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (QCloudVideoView.this.isFullscreen) {
                    QCloudVideoView.this.toggleFullscreen();
                } else if (QCloudVideoView.this.outActivity != null) {
                    QCloudVideoView.this.outActivity.finish();
                } else if (QCloudVideoView.this.context instanceof Activity) {
                    ((Activity) QCloudVideoView.this.context).finish();
                }

            }
        });
        this.requestFocus();
        this.setLongClickable(true);


    }


    /**----------------------------------新加入功能-----------------------------------------------*/
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
                QCloudVideoView.this.toggleTopBar();
                QCloudVideoView.this.toggleController();
                QCloudVideoView.this.hideCenterPlay();
                QCloudVideoView.this.hideRightTopFull();
                stop();
            }
        }
    }

    /** 三秒无操作，收起控制面板 */
    private void clockManage(){
        if(this.titleBar.getVisibility() == View.VISIBLE){
            mAutoPlayRunnable.start();
        } else {
            mAutoPlayRunnable.stop();
        }
    }

    /** 显示加载中 */
    public void showLoading() {
        this.loadingView.setVisibility(View.VISIBLE);
        this.loadErrorView.setVisibility(View.GONE);
        this.netTieView.setVisibility(View.GONE);
    }

    /** 显示失败 */
    public void showError() {
        this.loadingView.setVisibility(View.GONE);
        this.loadErrorView.setVisibility(View.VISIBLE);
        this.netTieView.setVisibility(View.GONE);
    }

    /** 显示提示 */
    public void showNetTie() {
        this.loadingView.setVisibility(View.GONE);
        this.loadErrorView.setVisibility(View.GONE);
        this.netTieView.setVisibility(View.VISIBLE);
    }

    /** 显示视频 */
    public void showPlayer() {
        this.loadingView.setVisibility(View.GONE);
        this.loadErrorView.setVisibility(View.GONE);
        this.netTieView.setVisibility(View.GONE);
    }

    /** 播放出错监听 */
    public void setPleyErrorListener(PlayErrorListener l){
        this.mPlayErrorListener = l;
    }

    /** 播放出错 */
    public interface PlayErrorListener {
        void onPlayError();
    }

    /** 播放网络提示监听 */
    public void setPleyContineLIstener(PlayContineListener l){
        this.mPlayContineListener = l;
    }

    /** 播放网络提示 */
    public interface PlayContineListener {
        void onPlayContine();
    }

    /** 隐藏分辨率按钮 */
    public void hideStreamButton(){
        this.mStreamButton.setVisibility(View.GONE);
    }

    /** 隐藏返回按钮 */
    public void hideBack(){
        this.backButton.setVisibility(View.GONE);
    }


    /** 隐藏菜单按钮 */
    public void hideMenu(){
        this.llaction.setVisibility(View.GONE);
    }

    /** 隐藏自定义播放按钮 */
    public void hideCustomControllPlay(){
        this.playView.setVisibility(View.GONE);
    }


    /** 隐藏播放按钮 */
    public void hideControllPlay(){
        this.controller.hideControllPlay();
    }

    /** 隐藏全屏按钮 */
    public void hideControllFullscreen(){
        this.controller.hideControllFullscreen();
    }


    /** 显示返回按钮 */
    public void showBack(){
        this.backButton.setVisibility(View.VISIBLE);
    }


    /** 显示菜单按钮 */
    public void showMenu(){
        this.llaction.setVisibility(View.VISIBLE);
    }

    /** 显示自定义播放按钮 */
    public void showCustomControllPlay(){
        this.playView.setVisibility(View.VISIBLE);
    }


    /** 显示播放按钮 */
    public void showControllPlay(){
        this.controller.showControllPlay();
    }

    /** 显示全屏按钮 */
    public void showControllFullscreen(){
        this.controller.showControllFullscreen();
    }

    /**----------------------------------新加入功能-----------------------------------------------*/

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(this.mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
        if(this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            if(this.mVideoWidth * height > width * this.mVideoHeight) {
                height = width * this.mVideoHeight / this.mVideoWidth;
            } else if(this.mVideoWidth * height < width * this.mVideoHeight) {
                width = height * this.mVideoWidth / this.mVideoHeight;
            }

            android.view.ViewGroup.LayoutParams layoutParams = this.rootView.getLayoutParams();
            layoutParams.height = -1;
            layoutParams.width = -1;
            this.rootView.setLayoutParams(layoutParams);
            android.view.ViewGroup.LayoutParams lp = this.videoSurfaceContainer.getLayoutParams();
            lp.height = height;
            lp.width = width;
            this.videoSurfaceContainer.setLayoutParams(lp);
        }

        this.setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setVideoInfo(VideoInfo info ,boolean autoStart) {

        if(this.playView != null) {
            this.playView.setVisibility(View.GONE);
        }
        if(this.controller != null) {
            this.controller.hide();
        }
        if(this.videoInfo != null) {
            this.stopPlayback();
        }

        this.videoInfo = info;
        this.autoStart = autoStart;

        try {
            VideoInfo.validate(this.videoInfo);
        } catch (IllegalArgumentException var5) {
            PlayerUtils.log(6, "QCloudVideoView", "Illegal video info : " + var5.getMessage());
            CallBack callBack = PlayerConfig.g().getCallBack();
            if(callBack != null) {
                callBack.onEvent(CallBack.EVENT_PLAY_ERROR, var5.getMessage(), this.videoInfo);
            }

            return;
        }
        if(this.titleName != null && videoInfo!= null) {
            this.titleName.setText(info.getVideoName()==null?"":info.getVideoName());
        }

        this.controller.setCurrentStreamName(this.videoInfo.getDefaultStreamName());
        this.controller.setSeekBarrier(this.videoInfo.getDurationAllow());
        PlayerUtils.log(3, "QCloudVideoView", "setVideoInfo: isSurfaceCreated=" + this.isSurfaceCreated);
        if(this.isSurfaceCreated) {
            this.play(this.videoSurface.getHolder());
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
        if(this.backButton != null) {
            this.backButton.setVisibility(enableBackButton?View.VISIBLE:View.GONE);
        }

    }

    public int getCurrentPosition() {
        return this.mediaPlayerControl == null?0:this.mediaPlayerControl.getCurrentPosition();
    }

    public void puase() {
        if(this.mediaPlayerControl != null) {
            this.mediaPlayerControl.pause();
        }

    }

    public void resume() {
        if(this.mediaPlayerControl != null) {
            this.mediaPlayerControl.start();
        }

    }

    public int getDuration() {
        return this.mediaPlayerControl == null?0:this.mediaPlayerControl.getDuration();
    }

    public void seekTo(int position) {
        if(this.mediaPlayerControl != null) {
            this.mediaPlayerControl.seekTo(position);
        }

    }

    public void togglePlay() {
        if(this.mediaPlayerControl != null) {
            if(this.mediaPlayerControl.isPlaying()) {
                this.mediaPlayerControl.pause();
            } else {
                this.mediaPlayerControl.start();
            }

        }
    }

    public void showController(int timeout) {
        if(this.controller != null) {
            this.controller.show(timeout);
        }

    }

    public void hideController() {
        if(this.controller != null) {
            this.controller.hide();
        }

    }

    public void toggleController() {
        if(this.controller != null) {
            if(this.controller.isShowing()) {
                this.controller.hide();
                QCloudVideoView.this.playView.setVisibility(View.GONE);
            } else {
                this.controller.show(0);
                QCloudVideoView.this.playView.setVisibility(View.VISIBLE);
            }

        }
    }

    public void showTopBar() {
        if(this.enableTopBar && this.titleBar != null) {
            this.titleBar.setVisibility(View.VISIBLE);
            if(this.settingView != null) {
                this.settingView.setVisibility(View.GONE);
            }
        }

    }

    public void hideTopBar() {
        if(this.titleBar != null) {
            this.titleBar.setVisibility(View.GONE);
            if(this.settingView != null) {
                this.settingView.setVisibility(View.GONE);
            }
        }

    }

    /** 隐藏右上全屏按钮 */
    public void hideRightTopFull() {
        if(this.playView != null) {
       this.playView.setVisibility(View.GONE);}
    }

    /** 隐藏中间播放按钮 */
    public void hideCenterPlay() {
        if(this.playView != null) {
            this.playView.setVisibility(View.GONE);}
    }

    public void toggleTopBar() {
        if(this.titleBar != null) {
            if(this.titleBar.getVisibility() == View.VISIBLE) {
                this.titleBar.setVisibility(View.GONE);
            } else {
                this.titleBar.setVisibility(View.VISIBLE);
            }
            if(this.settingView != null) {
                this.settingView.setVisibility(View.GONE);
            }

        }
    }

    public void forward(int millisecond) {
        if(this.mediaPlayerControl != null) {
            int pos = this.mediaPlayerControl.getCurrentPosition();
            this.mediaPlayerControl.seekTo(pos + millisecond);
        }
    }

    public void backward(int milisecond) {
        if(this.mediaPlayerControl != null) {
            int pos = this.mediaPlayerControl.getCurrentPosition();
            this.mediaPlayerControl.seekTo(pos - milisecond);
        }
    }

    public void toggleFullscreen() {
        if(this.mediaPlayerControl != null) {
            this.mediaPlayerControl.toggleFullScreen();
        }
    }

    public boolean isFullscreen() {
        return this.isFullscreen;
    }

    public void increaseVolume() {
        if(this.audioManager != null) {
            this.updateCurrentVolume();
            PlayerUtils.log(3, "QCloudVideoView", "currentVolume=" + this.currentVolume);
            this.audioManager.setStreamVolume(3, this.currentVolume + this.volumeOffset, 0);
            this.updateVolumeController();
        }
    }

    public void decreaseVolume() {
        if(this.audioManager != null) {
            this.updateCurrentVolume();
            PlayerUtils.log(3, "QCloudVideoView", "currentVolume=" + this.currentVolume);
            this.audioManager.setStreamVolume(3, this.currentVolume - this.volumeOffset, 0);
            this.updateVolumeController();
        }
    }

    public void setEnableBrightnessControll(Activity activity, boolean enabled) {
        this.outActivity = activity;
        this.enableBrightnessControll = enabled;
        if(enabled) {
            this.brightnessControllerContainer.setVisibility(View.VISIBLE);
        } else {
            this.brightnessControllerContainer.setVisibility(View.GONE);
        }

    }

    public void increaseBrightness() {
        if(this.enableBrightnessControll && this.outActivity != null) {
            int brightness = this.getBrightness();
            this.setBrightness(brightness + 5);
        }
    }

    public void decreaseBrightness() {
        if(this.enableBrightnessControll && this.outActivity != null) {
            int brightness = this.getBrightness();
            this.setBrightness(brightness - 5);
        }
    }

    public void setBrightness(int brightness) {
        if(this.enableBrightnessControll && this.outActivity != null) {
            PlayerUtils.log(3, "QCloudVideoView", "setBrightness: brightness=" + brightness);
            android.view.WindowManager.LayoutParams layout = this.outActivity.getWindow().getAttributes();
            if(brightness < 1) {
                brightness = 1;
            }

            if(brightness > 100) {
                brightness = 100;
            }

            layout.screenBrightness = 1.0F * (float)brightness / 100.0F;
            this.outActivity.getWindow().setAttributes(layout);
            if(this.brightnessController != null) {
                this.brightnessController.setProgress(brightness);
            }

        }
    }

    public int getBrightness() {
        if(this.enableBrightnessControll && this.outActivity != null) {
            float brightness = this.outActivity.getWindow().getAttributes().screenBrightness;
            if(brightness < 0.0F) {
                try {
                    int e = System.getInt(this.context.getContentResolver(), "screen_brightness");
                    brightness = 1.0F * (float)e / 255.0F;
                } catch (SettingNotFoundException var3) {
                    var3.printStackTrace();
                }
            }

            return (int)(brightness * 100.0F);
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
        if(!PlayerUtils.isNetworkAvailable()) {
            Toast.makeText(this.context, PlayerUtils.getResourceIdByName("string", "qcloud_player_network_connection_failed"), Toast.LENGTH_LONG).show();
        } else {
            this.videoSizeIsSet = false;
            this.mediaPlayerIsPrepared = false;
            if(this.controller != null) {
                this.controller.setEnabled(false);
                this.controller.setProgress();
            }

            this.isloading = true;
            this.player = new MediaPlayer();
            this.player.setAudioStreamType(3);

            try {
                this.player.setDataSource(this.context, Uri.parse(this.videoInfo.getCurrentStreamUrl()));
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            this.player.setOnPreparedListener(this);
            this.player.setOnVideoSizeChangedListener(this);
            this.player.setOnBufferingUpdateListener(this);
            this.player.setOnErrorListener(this);
            this.player.setOnCompletionListener(this);
            this.player.setOnInfoListener(this);
            this.player.setOnSeekCompleteListener(this);
            this.player.setDisplay(holder);
            this.player.prepareAsync();
            this.handler.sendEmptyMessageDelayed(1, 30000L);
        }
    }

    public boolean isPlaying() {
        return this.mediaPlayerControl == null?false:this.mediaPlayerControl.isPlaying();
    }

    public void stopPlayback() {
        if(this.player != null) {
            int pos = this.mediaPlayerControl.getCurrentPosition();
            this.player.stop();
            this.player.reset();
            this.player.release();
            this.player = null;
            if(this.handler != null) {
                this.handler.removeMessages(2);
            }

            CallBack callBack = PlayerConfig.g().getCallBack();
            if(callBack != null) {
                this.videoInfo.setCurrentPosition(pos);
                callBack.onEvent(CallBack.EVENT_PLAY_STOP, "video stopped", this.videoInfo);
            }
        }

    }

    private void updateCurrentVolume() {
        this.currentVolume = this.audioManager.getStreamVolume(3);
    }

    private void updateVolumeController() {
        this.updateCurrentVolume();
        if(this.volumeController != null) {
            this.volumeController.setProgress(this.currentVolume);
        }

    }

    public void switchStream(int streamNum) {
        PlayerUtils.log(4, "QCloudVideoView", "switchStream: stream=" + streamNum);
        if(streamNum >= 0 && this.videoInfo != null && streamNum < this.videoInfo.getStreamUrls().length) {
            if(streamNum != this.videoInfo.getCurrentStream()) {
                this.videoInfo.setCurrentStream(streamNum);
                this.videoInfo.setCurrentPosition(this.player.getCurrentPosition());
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
        this.playView.setVisibility(View.GONE);
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
        if(distanceY > 0.0F) {
            this.increaseBrightness();
        } else {
            this.decreaseBrightness();
        }

    }

    private void handleScrollOnRightArea(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(distanceY > 0.0F) {
            this.increaseVolume();
        } else {
            this.decreaseVolume();
        }

    }

    private void handleScrollOnMiddleArea(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(distanceX < 0.0F) {
            this.forward(10000);
        } else {
            this.backward(10000);
        }

    }

    public void setOnKeyDownListener(QCloudVideoView.OnKeyDownListener listener) {
        this.onKeyDownListener = listener;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PlayerUtils.log(3, "QCloudVideoView", "onKeyDown keyCode=" + keyCode);
        switch(keyCode) {
            case 24:
                this.increaseVolume();
                return true;
            case 25:
                this.decreaseVolume();
                return true;
            default:
                return this.onKeyDownListener != null?this.onKeyDownListener.onKeyDown(keyCode, event):super.onKeyDown(keyCode, event);
        }
    }

    public void setOnKeyLongPressListener(QCloudVideoView.OnKeyLongPressListener listener) {
        this.onKeyLongPressListener = listener;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return this.onKeyLongPressListener != null?this.onKeyLongPressListener.onKeyLongPress(keyCode, event):super.onKeyLongPress(keyCode, event);
    }

    public void setOnKeyMultipleListener(QCloudVideoView.OnKeyMultipleListener listener) {
        this.onKeyMultipleListener = listener;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return this.onKeyMultipleListener != null?this.onKeyMultipleListener.onKeyMultiple(keyCode, repeatCount, event):super.onKeyMultiple(keyCode, repeatCount, event);
    }

    public void setOnKeyUpListener(QCloudVideoView.OnKeyUpListener listener) {
        this.onKeyUpListener = listener;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return this.onKeyUpListener != null?this.onKeyUpListener.onKeyUp(keyCode, event):super.onKeyUp(keyCode, event);
    }

    public void setOnPreparedListener(QCloudVideoView.OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    public void onPrepared(MediaPlayer mp) {
        PlayerUtils.log(3, "QCloudVideoView", "onPrepared is called");
        this.handler.removeMessages(1);
        this.mediaPlayerIsPrepared = true;
        this.isloading = false;
        this.controller.setMediaPlayer(this.mediaPlayerControl);
        this.controller.setAnchorView((RelativeLayout)this.rootView);
        this.controller.setEnabled(true);
        this.player.start();
        PlayerUtils.log(3, "QCloudVideoView", "player.start()");
        if(this.videoSizeIsSet) {
            this.player.seekTo(this.videoInfo.getCurrentPosition());
        }

        if(!this.autoStart) {
            this.player.pause();
            this.autoStart = true;
        } else if(this.handler != null) {
            this.handler.sendEmptyMessage(2);
        }

        this.controller.setProgress();
        this.controller.updatePausePlay();
        this.controller.updateFullScreen();
        this.controller.hide();
        this.hideTopBar();
        this.playView.setVisibility(View.GONE);
        CallBack callBack = PlayerConfig.g().getCallBack();
        if(callBack != null) {
            callBack.onEvent(CallBack.EVENT_PLAY_START, "video start", this.videoInfo);
        }

        if(this.onPreparedListener != null) {
            this.onPreparedListener.onPrepared(mp);
        }

    }

    public void setOnCompletionListener(QCloudVideoView.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public void onCompletion(MediaPlayer mp) {
        CallBack callBack = PlayerConfig.g().getCallBack();
        if(callBack != null) {
            callBack.onEvent(CallBack.EVENT_PLAY_COMPLETE, "play complete", this.videoInfo);
        }

        if(this.onCompletionListener != null) {
            this.onCompletionListener.onCompletion(mp);
        }

    }

    public void setOnVideoSizeChangedListener(QCloudVideoView.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.onVideoSizeChangedListener = onVideoSizeChangedListener;
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        PlayerUtils.log(3, "QCloudVideoView", "onVideoSizeChanged called");
        this.mVideoWidth = mp.getVideoWidth();
        this.mVideoHeight = mp.getVideoHeight();
        if(width != 0 && height != 0) {
            this.videoSizeIsSet = true;
            if(this.mediaPlayerIsPrepared) {
                this.player.seekTo(this.videoInfo.getCurrentPosition());
            }
        } else {
            PlayerUtils.log(3, "QCloudVideoView", "invalid video width(" + width + ") or height(" + height + ")");
        }

        if(this.onVideoSizeChangedListener != null) {
            this.onVideoSizeChangedListener.onVideoSizeChanged(mp, width, height);
        }

    }

    public void setOnBufferingUpdateListener(QCloudVideoView.OnBufferingUpdateListener onBufferingUpdateListener) {
        this.onBufferingUpdateListener = onBufferingUpdateListener;
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.bufferPercent = percent;
        if(this.onBufferingUpdateListener != null) {
            this.onBufferingUpdateListener.onBufferingUpdate(mp, percent);
        }

    }

    public void setOnErrorListener(QCloudVideoView.OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        String errorWhat;
        switch(what) {
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
        switch(extra) {
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
        if(callBack != null) {
            callBack.onEvent(CallBack.EVENT_PLAY_ERROR, msg, this.videoInfo);
        }

        return this.onErrorListener == null || this.onErrorListener.onError(mp, what, extra);
    }

    public void setOnInfoListener(QCloudVideoView.OnInfoListener onInfoListener) {
        this.onInfoListener = onInfoListener;
    }

    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        PlayerUtils.log(4, "QCloudVideoView", "onInfo: what=" + what + " extra=" + extra);
        return this.onInfoListener != null && this.onInfoListener.onInfo(mp, what, extra);
    }

    public void setOnSeekCompleteListener(QCloudVideoView.OnSeekCompleteListener onSeekCompleteListener) {
        this.onSeekCompleteListener = onSeekCompleteListener;
    }

    public void onSeekComplete(MediaPlayer mp) {
        if(this.onSeekCompleteListener != null) {
            this.onSeekCompleteListener.onSeekComplete(mp);
        }

    }

    public void setOnToggleFullscreenListener(QCloudVideoView.OnToggleFullscreenListener listener) {
        this.onToggleFullscreenListener = listener;
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(MediaPlayer var1, int var2);
    }

    public interface OnCompletionListener {
        void onCompletion(MediaPlayer var1);
    }

    public interface OnErrorListener {
        boolean onError(MediaPlayer var1, int var2, int var3);
    }

    public interface OnInfoListener {
        boolean onInfo(MediaPlayer var1, int var2, int var3);
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
        void onPrepared(MediaPlayer var1);
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
            return QCloudVideoView.this.videoInfo == null?0:QCloudVideoView.this.videoInfo.getStreamNames().length;
        }

        public Object getItem(int position) {
            return QCloudVideoView.this.videoInfo.getStreamNames()[position];
        }

        public long getItemId(int position) {
            return (long)position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            QCloudVideoView.StreamSelectAdapter.ViewHolder holder;
            if(convertView == null) {
                convertView = this.layoutInflater.inflate(PlayerUtils.getResourceIdByName("layout", "qcloud_player_select_streams_list_item"), (ViewGroup)null);
                holder = new QCloudVideoView.StreamSelectAdapter.ViewHolder();
                holder.streamName = (TextView)convertView.findViewById(PlayerUtils.getResourceIdByName("id", "qcloud_player_stream_name"));
                convertView.setTag(holder);
            } else {
                holder = (QCloudVideoView.StreamSelectAdapter.ViewHolder)convertView.getTag();
            }

            String streamName = (String)this.getItem(position);
            holder.streamName.setText(streamName);
            if(position == QCloudVideoView.this.videoInfo.getCurrentStream()) {
                holder.streamName.setTextColor(QCloudVideoView.this.getResources().getColor(PlayerUtils.getResourceIdByName("color", "qcloud_player_stream_name_playing")));
            } else {
                holder.streamName.setTextColor(QCloudVideoView.this.getResources().getColor(PlayerUtils.getResourceIdByName("color", "qcloud_player_stream_name_normal")));
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
