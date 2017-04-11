package com.kk.taurus.playerbase.setting;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.callback.OnPlayerGestureListener;

/**
 * Created by Taurus on 2017/3/26.
 */

public class PlayerGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private int mWidth;
    private int mHeight;

    private OnPlayerGestureListener mOnPlayerGestureListener;
    private boolean firstTouch;
    private boolean horizontalSlide;
    private boolean rightVerticalSlide;

    public PlayerGestureDetector(int width, int height){
        this.mWidth = width;
        this.mHeight = height;
    }

    public void updateWH(int width, int height){
        this.mWidth = width;
        this.mHeight = height;
    }

    public void setOnPlayerGestureListener(OnPlayerGestureListener onPlayerGestureListener) {
        this.mOnPlayerGestureListener = onPlayerGestureListener;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(mOnPlayerGestureListener!=null){
            mOnPlayerGestureListener.onSingleTapUp(e);
        }
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if(mOnPlayerGestureListener!=null){
            mOnPlayerGestureListener.onDoubleTap(e);
        }
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        firstTouch = true;
        return super.onDown(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mOnPlayerGestureListener!=null){
            mOnPlayerGestureListener.onScroll(e1, e2, distanceX, distanceY);
        }
        float mOldX = e1.getX(), mOldY = e1.getY();
        float deltaY = mOldY - e2.getY();
        float deltaX = mOldX - e2.getX();
        if (firstTouch) {
            horizontalSlide = Math.abs(distanceX) >= Math.abs(distanceY);
            rightVerticalSlide = mOldX > mWidth * 0.5f;
            firstTouch = false;
        }

        if(horizontalSlide){
            if(mOnPlayerGestureListener!=null){
                mOnPlayerGestureListener.onHorizontalSlide(-deltaX / mWidth);
            }
        }else{
            if(rightVerticalSlide){
                if(mOnPlayerGestureListener!=null){
                    mOnPlayerGestureListener.onRightVerticalSlide(deltaY / mHeight);
                }
            }else{
                if(mOnPlayerGestureListener!=null){
                    mOnPlayerGestureListener.onLeftVerticalSlide(deltaY / mHeight);
                }
            }
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }
}