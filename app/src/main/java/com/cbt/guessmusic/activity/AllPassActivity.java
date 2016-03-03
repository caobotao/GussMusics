package com.cbt.guessmusic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.cbt.guessmusic.R;
import com.cbt.guessmusic.data.Const;
import com.cbt.guessmusic.util.Util;

/**
 * Created by caobotao on 16/3/2.
 */
public class AllPassActivity extends Activity {
    private FrameLayout mViewAddCoins;
    private ImageButton mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pass_activity);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mBtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCoins = Util.getInstance().readGameData(AllPassActivity.this)[Const.INDEX_LOAD_DATA_COINS];
                Util.getInstance().saveGameData(AllPassActivity.this,0,currentCoins);
                Util.getInstance().startActivityWithoutData(AllPassActivity.this,MainActivity.class);
            }
        });
    }

    private void initData() {
        mViewAddCoins.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        mViewAddCoins = (FrameLayout) findViewById(R.id.fl_add_coins);
        mBtnBack = (ImageButton) findViewById(R.id.btn_bar_back);
    }
}
