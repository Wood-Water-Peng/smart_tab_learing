package com.example.administrator.myapplication;

import android.view.View;

/**
 * Created by pj on 2016/10/9.
 */
final class Utils {
    static int getStart(View v) {
        if (v == null) {
            return 0;
        }
        return v.getLeft();
    }

    static int getEnd(View v) {
        if (v == null) {
            return 0;
        }
        return v.getRight();
    }
}
