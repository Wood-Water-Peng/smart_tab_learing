package com.example.administrator.myapplication;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by pj on 2016/10/9.
 */
public abstract class MyTabIndicatorInterpolator {

    public static final MyTabIndicatorInterpolator SMART = new MySmartIndicatorInterpolator();
    public static final MyTabIndicatorInterpolator LINEAR = new MyLinearIndicationInterpolator();
    static final int ID_SMART = 0;
    static final int ID_LINEAR = 1;

    public static MyTabIndicatorInterpolator of(int id) {
        switch (id) {
            case ID_SMART:
                return SMART;
            case ID_LINEAR:
                return LINEAR;
            default:
                throw new IllegalArgumentException("Unknown id: " + id);
        }
    }

    public abstract float getLeftEdge(float offset);

    public abstract float getRightEdge(float offset);

    public float getThickness(float offset) {
        return 1f; //Always the same thickness by default
    }

    public static class MySmartIndicatorInterpolator extends MyTabIndicatorInterpolator {
        private static final float DEFAULT_INDICATOR_INTERPOLATION_FACTOR = 3.0f;
        private final Interpolator leftEdgeInterpolator;
        private final Interpolator rightEdgeInterpolator;

        public MySmartIndicatorInterpolator() {
            this(DEFAULT_INDICATOR_INTERPOLATION_FACTOR);
        }

        public MySmartIndicatorInterpolator(float factor) {
            leftEdgeInterpolator = new AccelerateInterpolator(factor);
            rightEdgeInterpolator = new DecelerateInterpolator(factor);
        }

        @Override
        public float getLeftEdge(float offset) {
            return leftEdgeInterpolator.getInterpolation(offset);
        }

        @Override
        public float getRightEdge(float offset) {
            return rightEdgeInterpolator.getInterpolation(offset);
        }
    }

    public static class MyLinearIndicationInterpolator extends MyTabIndicatorInterpolator {

        @Override
        public float getLeftEdge(float offset) {
            return offset;
        }

        @Override
        public float getRightEdge(float offset) {
            return offset;
        }

    }


}
