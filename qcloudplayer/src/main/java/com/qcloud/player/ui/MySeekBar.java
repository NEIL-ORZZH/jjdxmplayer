package com.qcloud.player.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

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
 * 创建日期：2015/12/16
 * <p/>
 * 描 述：可动态设置是否可以拖到进度条
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class MySeekBar extends SeekBar {


    public MySeekBar(Context context) {
        super(context);
// TODO Auto-generated constructor stub
    }


    public MySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }


    public MySeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * onTouchEvent 是在 SeekBar 继承的抽象类 AbsSeekBar 里 你可以看下他们的继承关系
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mBannedMove){
            return false;
        } else {
            return super.onTouchEvent(event);
        }

    }

    /** 是否禁止移动true为禁止false为可移动 */
    private boolean mBannedMove;

    public void setEnableBannedMove(boolean bannedMove){
        this.mBannedMove = bannedMove;
    }

}