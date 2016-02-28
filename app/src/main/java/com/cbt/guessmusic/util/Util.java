package com.cbt.guessmusic.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by caobotao on 16/2/28.
 */
public class Util {
    public static View getView(Context context, int layoutId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, null);
        return view;
    }
}
