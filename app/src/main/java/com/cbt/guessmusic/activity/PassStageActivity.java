package com.cbt.guessmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cbt.guessmusic.R;
import com.cbt.guessmusic.data.Const;
import com.cbt.guessmusic.util.Util;

/**
 * Created by caobotao on 16/3/1.
 */
public class PassStageActivity extends Activity {
    public static final String SONG_NAME_STR = "song name";
    public static final String CURRENT_STAGE_STR = "current stage";
    public static final String REWARD_COINS_STR = "reward coins";

    private TextView mViewBitPercent;
    private TextView mViewPassStage;
    private TextView mViewPassSongName;
    private TextView mViewRewardCoins;

    private ImageButton mViewNextStage;
    private ImageButton mViewShareToWeiXin;

    private int mPassStage;
    private int mRewardCoins;
    private String mPassSongName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_stage);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mViewNextStage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTheLastStage()) {
                    Util.getInstance().startActivityWithoutData(PassStageActivity.this,AllPassActivity.class);
                } else {
                    Intent intent = new Intent(PassStageActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.DELIVERED_STAGE, mPassStage);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean isTheLastStage() {
        return mPassStage == Const.SONGS_INFO.length;
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mPassStage = bundle.getInt(CURRENT_STAGE_STR);
        mRewardCoins = bundle.getInt(REWARD_COINS_STR);
        mPassSongName = bundle.getString(SONG_NAME_STR);

        mViewPassStage.setText(mPassStage + "");
        mViewPassSongName.setText(mPassSongName);
        mViewRewardCoins.setText(mRewardCoins + "");
    }

    private void initView() {
        mViewBitPercent = (TextView) findViewById(R.id.bit_percent);
        mViewPassStage = (TextView) findViewById(R.id.pass_stage);
        mViewPassSongName = (TextView) findViewById(R.id.pass_song_name);
        mViewRewardCoins = (TextView) findViewById(R.id.reward_coins);
        mViewNextStage = (ImageButton) findViewById(R.id.next_stage);
        mViewShareToWeiXin = (ImageButton) findViewById(R.id.share_to_wx);
    }
}
