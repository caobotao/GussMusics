package com.cbt.guessmusic.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by caobotao on 16/2/28.
 */
public class Util {
    private static Util util;

    private Util() {
    }

    public static Util getInstance() {
        if (util == null) {
            synchronized (Util.class) {
                if (util == null) {
                    util = new Util();
                }
            }
        }
        return util;
    }

    //根据布局id获取对应View
    public View getView(Context context, int layoutId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, null);
        return view;
    }

    //获取一个随机的汉字字符
    public char getRandomChineseChar() {
        String str = "";
        int highPos;
        int lowPos;
        Random random = new Random();
        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));
        byte[] bytes = new byte[2];
        bytes[0] = Integer.valueOf(highPos).byteValue();
        bytes[1] = Integer.valueOf(lowPos).byteValue();
        try {
            str = new String(bytes, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }
}
