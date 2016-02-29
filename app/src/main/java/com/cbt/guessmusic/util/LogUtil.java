package com.cbt.guessmusic.util;

import android.util.Log;

/**
 * Created by caobotao on 16/2/29.
 */
public class LogUtil {
    public static final boolean IS_DEBUG = true;

    public static void d(String tag,String messageName,String message) {
        if (IS_DEBUG) {
            Log.d(tag, messageName + " --> " + message);
        }
    }

    public static void w(String tag,String messageName,String message) {
        if (IS_DEBUG) {
            Log.w(tag, messageName + " --> " + message);
        }
    }

    public static void i(String tag,String messageName,String message) {
        if (IS_DEBUG) {
            Log.i(tag, messageName + " --> " + message);
        }
    }

    public static void e(String tag,String messageName,String message) {
        if (IS_DEBUG) {
            Log.e(tag, messageName + " --> " + message);
        }
    }

}
