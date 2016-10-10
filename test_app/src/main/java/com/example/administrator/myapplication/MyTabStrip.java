package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by pj on 2016/10/9.
 */
public class MyTabStrip extends LinearLayout {

    private static final int GRAVITY_BOTTOM = 0;

    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 8;
    private static final int AUTO_WIDTH = -1;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;
    private static final int DEFAULT_INDICATOR_GRAVITY = GRAVITY_BOTTOM;
    private static final boolean DEFAULT_INDICATOR_IN_CENTER = true;
    private static final String TAG = "MyTabStrip";

    private float selectionOffset;
    private final Paint indicatorPaint;
    private int selectedPosition;
    private TabColorizer customTabColorizer;
    private SimpleTabColorizer defaultTabColorizer;
    private MyTabIndicatorInterpolator indicatorInterpolator;
    private int indicatorThickness;
    private int indicatorWidth;
    private int indicatorGravity;
    private final RectF indicatorRectF = new RectF();
    private boolean indicatorAlwaysInCenter;

    public MyTabStrip(Context context) {
        this(context, null);
    }

    public MyTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        final float density = getResources().getDisplayMetrics().density;

        this.indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int indicatorColor = DEFAULT_SELECTED_INDICATOR_COLOR;
        this.defaultTabColorizer = new SimpleTabColorizer();
        this.defaultTabColorizer.setIndicatorColors(indicatorColor);
        this.indicatorInterpolator = MyTabIndicatorInterpolator.of(MyTabIndicatorInterpolator.ID_SMART);
        this.indicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        this.indicatorWidth = AUTO_WIDTH;
        this.indicatorGravity = GRAVITY_BOTTOM;
        this.indicatorAlwaysInCenter = DEFAULT_INDICATOR_IN_CENTER;
    }
    /**
     * 如果我需要画一个tab，那么我肯定需要给他定义颜色，所以，我需要一个TabColorizer
     */


    /**
     * 该控件的滑动是基于ViewPager的，所以，他必须监听ViewPager的滑动，并获得两个重要参数
     *
     * @param position       要滑动的page
     * @param positionOffset 滑动的距离
     */
    void onViewPagerPageChanged(int position, float positionOffset) {
        selectedPosition = position;
        selectionOffset = positionOffset;
        /**
         * 立刻进行重绘,进而调用onDraw()方法
         */
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDecoration(canvas);
    }

    private void drawDecoration(Canvas canvas) {
        /**
         * 拿到该控件的宽高、子孩子个数、tabColorizer
         */
        int height = getHeight();
        int width = getWidth();
        int tabCount = getChildCount();
        TabColorizer tabColorizer = getTabColorizer();

        /**
         * 当tabCount满足条件时，我们将其画出来
         */
        if (tabCount > 0) {
            /**
             * 拿到当前选中的tab
             */
            View selectedTab = getChildAt(selectedPosition);
            /**
             * 拿到selectedTab的起始位置
             */
            int selectedStart = Utils.getStart(selectedTab);
            int selectedEnd = Utils.getEnd(selectedTab);

            int left;
            int right;

            left = selectedStart;
            right = selectedEnd;

            /**
             * 根据选中的位置拿到indicator的颜色
             */
            int color = tabColorizer.getIndicatorColor(selectedPosition);
            float thickness = indicatorThickness;
            if (selectionOffset > 0f && selectedPosition < (getChildCount() - 1)) {
                /**
                 * 根据当前tab对应的color，下一个tab对应的color和滑动的距离selectedPosition
                 * 混合出一个当前显示的color
                 */
                int nextColor = tabColorizer.getIndicatorColor(selectedPosition + 1);
                if (color != nextColor) {
                    color = blendColors(nextColor, color, selectionOffset);
                }
                Log.i(TAG, "color:" + color);
                /**
                 * 根据滑动的距离，动态的确定出要画的indicator的首尾
                 * 左右的offSet由不同的interpolator根据selectionOff计算出
                 */
                float startOffset = indicatorInterpolator.getLeftEdge(selectionOffset);
                float endOffset = indicatorInterpolator.getRightEdge(selectionOffset);
                float thicknessOffset = indicatorInterpolator.getThickness(selectionOffset);

                /**
                 * 计算出要滑动到的tab的起始坐标
                 */
                View nextTab = getChildAt(selectedPosition + 1);
//                Log.i(TAG, "selectedPosition + 1:" + (selectedPosition + 1));
                Log.i(TAG, "startOffset:" + startOffset);
                int nextStart = Utils.getStart(nextTab);
                int nextEnd = Utils.getEnd(nextTab);

                /**
                 * 当pager滑动时，selectionPosition改变，此时的left，right是会动态改变的
                 */
                left = (int) (startOffset * nextStart + (1.0f - startOffset) * left);
                right = (int) (endOffset * nextEnd + (1.0f - endOffset) * right);

                thickness = thickness * thicknessOffset;

            }
            drawIndicator(canvas, left, right, height, thickness, color);
        }

    }

    private void drawIndicator(Canvas canvas, int left, int right, int height, float thickness, int color) {

        if (indicatorThickness <= 0 || indicatorWidth == 0) {
            return;
        }
        /**
         * 确定要画的矩形的top，bottom
         */
        float top;
        float bottom;
        float center;

        switch (indicatorGravity) {
            case GRAVITY_BOTTOM:
                center = height - (indicatorThickness / 2f);
                top = center - (thickness / 2f);
                bottom = center + (thickness / 2f);
                break;
            default:
                center = height - (indicatorThickness / 2f);
                top = center - (thickness / 2f);
                bottom = center + (thickness / 2f);
        }
        indicatorPaint.setColor(Color.RED);
//        Log.i(TAG, "left:" + left + "---top:" + top + "---right:" + right + "---bottom:" + bottom);
        indicatorRectF.set(left, top, right, bottom);

        canvas.drawRect(indicatorRectF, indicatorPaint);
    }

    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    private TabColorizer getTabColorizer() {
        return (customTabColorizer != null) ? customTabColorizer : defaultTabColorizer;
    }

    boolean isIndicatorAlwaysInCenter() {
        return indicatorAlwaysInCenter;
    }


    public interface TabColorizer {
        int getIndicatorColor(int position);

        int getDividerColor(int position);
    }

    private static class SimpleTabColorizer implements TabColorizer {
        private int[] indicatorColors;
        private int[] dividerColors;

        @Override
        public int getIndicatorColor(int position) {
            return indicatorColors[position % indicatorColors.length];
        }

        @Override
        public int getDividerColor(int position) {
            return dividerColors[position % dividerColors.length];
        }

        void setIndicatorColors(int... colors) {
            indicatorColors = colors;
        }

        void setDividerColors(int... colors) {
            dividerColors = colors;
        }

    }

}
