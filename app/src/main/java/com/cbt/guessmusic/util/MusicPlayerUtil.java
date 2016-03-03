package com.cbt.guessmusic.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.IOException;

/**
 * Created by caobotao on 16/3/2.
 * 音乐播放器工具类
 */
public class MusicPlayerUtil {
    //音乐的播放器
    private static MediaPlayer mMediaPlayer;

    //音效文件名
    private static String[] mToneNames = new String[]{"enter.mp3","cancel.mp3","coin.mp3"};

    //音效文件索引
    public static final int INDEX_ENTER = 0;
    public static final int INDEX_CANCEL = 1;
    public static final int INDEX_COIN = 2;

    //音效的播放器
    private static MediaPlayer[] mToneMediaPlayer = new MediaPlayer[mToneNames.length];

    /**
     * 播放音效
     */
    public static void playTone(Context context, int toneFileIndex) {
        MediaPlayer mediaPlayer = mToneMediaPlayer[toneFileIndex];
        mediaPlayer = initMediaPlayer(context,mediaPlayer, mToneNames[toneFileIndex]);
        //播放音乐
        mediaPlayer.start();
    }

    /**
     * 播放声音
     */
    public static void playSong(Context context, String fileName,OnCompletionListener listener) {
        mMediaPlayer = initMediaPlayer(context,mMediaPlayer, fileName);
        //播放音乐
        mMediaPlayer.start();
        //设置歌曲播放完成的回调
        mMediaPlayer.setOnCompletionListener(listener);
    }

    private static MediaPlayer initMediaPlayer(Context context,MediaPlayer mediaPlayer, String fileName) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        //强制重置,在非首次播放时需要强制重置
        mediaPlayer.reset();
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            mediaPlayer.setDataSource( fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }
    /**
     * 获取声音的播放时长
     */
    public static int getMusicDuration(Context context, String fileName) {
        mMediaPlayer = initMediaPlayer(context,mMediaPlayer, fileName);
        return mMediaPlayer.getDuration();
    }
    /**
     * 停止声音
     */
    public static void stopSong() {
        if (mMediaPlayer != null) {
            //如果正在播放,需要先暂停再停止
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
            //停止声音
            mMediaPlayer.stop();
        }
    }
}
