package com.anki.airquality.utils;

import android.util.Log;
/*
* Logger class
* */
public final class DebugUtils {

    public static void debug(Class caller, String message) {
        Log.d(caller.getSimpleName(), message);
    }

    public static void warn(Class caller, String message) {
        Log.w(caller.getSimpleName(), message);
    }

    public static void error(Class caller, String message) {
        Log.e(caller.getSimpleName(), message);
    }
}
