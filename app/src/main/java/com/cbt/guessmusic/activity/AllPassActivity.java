package com.cbt.guessmusic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.cbt.guessmusic.R;

/**
 * Created by caobotao on 16/3/2.
 */
public class AllPassActivity extends Activity {
    private FrameLayout mViewAddCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pass_activity);
        initView();
        initData();
    }

    private void initData() {
        mViewAddCoins.setVisibility(View.INVISIBLE);
    }

    private void initView() {
        mViewAddCoins = (FrameLayout) findViewById(R.id.fl_add_coins);
    }
}
