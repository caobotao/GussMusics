package com.cbt.guessmusic.model;

import android.view.View;
import android.widget.Button;

/**
 * 文字按钮实体类
 * Created by caobotao on 16/2/20.
 */
public class WordButton {
    private int mIndex;
    private String mWordString;
    private boolean mIsVisible;
    private Button mButton;

    public WordButton() {
        mIsVisible = true;
        mWordString = "";
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public String getWordString() {
        return mWordString;
    }

    public void setWordString(String mWordString) {
        this.mWordString = mWordString;
    }

    public boolean getVisible() {
        return mIsVisible;
    }

    public void setVisible(boolean mIsVisible) {
        this.mIsVisible = mIsVisible;
    }

    public Button getButton() {
        return mButton;
    }

    public void setButton(Button mButton) {
        this.mButton = mButton;
    }

    @Override
    public String toString() {
        return "WordButton{" +
                "mIndex=" + mIndex +
                ", mWordString='" + mWordString + '\'' +
                ", mIsVisible=" + mIsVisible +
                ", mButton=" + mButton +
                '}';
    }


}
