package com.cbt.guessmusic.util;

import android.util.Log;

/**
 * Created by caobotao on 16/2/29.
 */
public class LogUtil {
    public static final boolean IS_DEBUG = true;

    public static void d(String tag,String message) {
        if (IS_DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void w(String tag,String message) {
        if (IS_DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void i(String tag,String message) {
        if (IS_DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void e(String tag,String message) {
        if (IS_DEBUG) {
            Log.e(tag, message);
        }
    }

}
