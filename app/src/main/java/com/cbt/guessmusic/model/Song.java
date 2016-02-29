package com.cbt.guessmusic.model;

/**
 * Created by caobotao on 16/2/28.
 *
 * 歌曲bean类
 */
public class Song {
    //歌曲名
    private String songName;
    //歌曲文件名
    private String songFileName;

    public Song() {

    }

    public Song(String songName, String songFileName) {
        this.songName = songName;
        this.songFileName = songFileName;
    }

    //获取歌曲名长度
    public int getSongNameLength() {
        return songName.length();
    }

    //获取歌曲名的单个汉字的数组
    public char[] getSongNameChars() {
        return songName.toCharArray();
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongFileName() {
        return songFileName;
    }

    public void setSongFileName(String songFileName) {
        this.songFileName = songFileName;
    }
}
