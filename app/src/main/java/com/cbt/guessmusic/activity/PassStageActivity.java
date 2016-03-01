package com.cbt.guessmusic.activity;

import android.app.Activity;
import android.os.Bundle;

import com.cbt.guessmusic.R;

/**
 * Created by caobotao on 16/3/1.
 */
public class PassStageActivity extends Activity {
    public static final String SONG_NAME_STR = "song name";
    public static final String CURRENT_STAGE_STR = "current stage";
    public static final String REWARD_COINS_STR = "reward coins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_stage);
    }
}
