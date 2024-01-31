package com.example.novapp2.utils;

import android.database.CursorWindow;

import java.lang.reflect.Field;

public class FixCursorWindow {
    public static void fix() {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 102400 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
