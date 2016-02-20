package com.cbt.guessmusic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cbt.guessmusic.R;

/**
 * Created by caobotao on 16/1/30.
 */
public class MainActivity extends Activity {
    //声明动画相关的变量
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;


    //播放音乐的按钮,盘片图片,控制杆图片
    private ImageButton mIbtnPlayStart;
    private ImageView mViewPan;
    private ImageView mViewPanBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    //初始化事件
    private void initEvent() {
        mBarInAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            //当控制杆移入动画结束时开始盘片旋转动画
            public void onAnimationEnd(Animation animation) {
                mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mPanAnim.setAnimationListener(new AnimationListener() {
            @Override
            //当盘片动画开始时隐藏播放按钮
            public void onAnimationStart(Animation animation) {
                mIbtnPlayStart.setVisibility(View.GONE);
            }

            @Override
            //当盘片动画结束时开始控制杆移出动画
            public void onAnimationEnd(Animation animation) {
                mViewPanBar.startAnimation(mBarOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarOutAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            //当控制杆移出动画结束时设置播放按钮为可见
            public void onAnimationEnd(Animation animation) {
                mIbtnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mIbtnPlayStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayButton();
            }
        });
    }

    //处理点击播放按钮的逻辑
    private void handlePlayButton() {
        //开始控制杆移入的动画
        mViewPanBar.startAnimation(mBarInAnim);
    }

    //初始化控件
    private void initView() {
        mIbtnPlayStart = (ImageButton) findViewById(R.id.ibtn_play_start);
        mViewPan = (ImageView) findViewById(R.id.iv_game_disc);
        mViewPanBar = (ImageView) findViewById(R.id.iv_index_pin);
    }

    //初始化数据
    private void initData() {
        //初始化动画
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);

        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setInterpolator(mBarInLin);

        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setInterpolator(mBarOutLin);

    }

    @Override
    protected void onPause() {
        //当Activity被中断时清除动画
        mViewPan.clearAnimation();
        super.onPause();
    }
}
