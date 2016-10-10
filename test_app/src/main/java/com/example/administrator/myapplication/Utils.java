package com.example.administrator.myapplication;

import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pj on 2016/10/9.
 */
final class Utils {
    static int getStart(View v) {
        return getStart(v, false);
    }

    static int getStart(View v, boolean withoutPadding) {
        if (v == null) {
            return 0;
        }
        return (withoutPadding) ? v.getLeft() + getPaddingStart(v) : v.getLeft();
    }

    static int getPaddingStart(View v) {
        if (v == null) {
            return 0;
        }
        return ViewCompat.getPaddingStart(v);
    }

    static int getPaddingEnd(View v) {
        if (v == null) {
            return 0;
        }
        return ViewCompat.getPaddingEnd(v);
    }

    static int getPaddingHorizontally(View v) {
        if (v == null) {
            return 0;
        }
        return v.getPaddingLeft() + v.getPaddingRight();
    }


    static int getEnd(View v) {
        if (v == null) {
            return 0;
        }
        return v.getRight();
    }

    static int getMarginHorizontally(View v) {
        if (v == null) {
            return 0;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return lp.leftMargin + lp.rightMargin;
    }

    static int getWidth(View v) {
        return (v == null) ? 0 : v.getWidth();
    }

    static int getMarginStart(View v) {
        if (v == null) {
            return 0;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(lp);
    }

    static int getMarginEnd(View v) {
        if (v == null) {
            return 0;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginEnd(lp);
    }


}
