package com.example.administrator.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by pj on 2016/10/9.
 */
public class MyTabLayout extends HorizontalScrollView {

    private static final int TAB_VIEW_TEXT_COLOR = 0xFC000000;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private static final boolean DEFAULT_DISTRIBUTE_EVENLY = false;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TITLE_OFFSET_DIPS = 24;
    private static final String TAG = "MyTabLayout";

    private ViewPager viewPager;
    private MyTabProvider tabProvider;
    private MyTabStrip tabStrip;
    private ColorStateList tabViewTextColors;
    private float tabViewTextSize;
    private boolean distributeEvenly;
    private int tabViewTextHorizontalPadding;
    private int titleOffset;

    public MyTabLayout(Context context) {
        this(context, null);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final float density = dm.density;
        this.tabViewTextColors = ColorStateList.valueOf(TAB_VIEW_TEXT_COLOR);
        float textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP, dm);
        boolean distributeEvenly = DEFAULT_DISTRIBUTE_EVENLY;
        this.tabViewTextSize = textSize;
        this.tabStrip = new MyTabStrip(context, attrs);
        int textHorizontalPadding = (int) (TAB_VIEW_PADDING_DIPS * density);
        this.tabViewTextHorizontalPadding = textHorizontalPadding;
        this.titleOffset = (int) (TITLE_OFFSET_DIPS * density);

        addView(tabStrip, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    public void setViewPager(ViewPager viewPager) {
        tabStrip.removeAllViews();

        this.viewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }


    /**
     * 填充tabStrip(两种模式)
     * 1.采用default模式填充View
     * 2.采用自定的View填充
     */
    private void populateTabStrip() {
        PagerAdapter adapter = viewPager.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {
            final View tabView = (tabProvider == null) ? createDefaultTabView(adapter.getPageTitle(i)) : tabProvider.createTabView(tabStrip, i, adapter);
            if (tabView == null) {
                throw new IllegalStateException("tabView is null!");
            }
            /**
             * tabStrip的子孩子有两种分布模式
             * 1.evenly  均匀分布
             * 2.正常分布
             */
            if (distributeEvenly) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            tabStrip.addView(tabView);

            if (i == viewPager.getCurrentItem()) {
                tabView.setSelected(true);
            }

        }

    }

    /**
     * 创建默认的TextView来作为TavView
     *
     * @param pageTitle
     * @return
     */
    private View createDefaultTabView(CharSequence pageTitle) {
        /**
         * 可以由用户指定的一些参数
         * 1.背景
         */
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(pageTitle);
        textView.setTextColor(tabViewTextColors);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabViewTextSize);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        textView.setPadding(
                tabViewTextHorizontalPadding, 0,
                tabViewTextHorizontalPadding, 0);

        return textView;
    }

    public interface MyTabProvider {

        /**
         * @return Return the View of {@code position} for the Tabs
         */
        View createTabView(ViewGroup container, int position, PagerAdapter adapter);

    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

        private int scrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = tabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            tabStrip.onViewPagerPageChanged(position, positionOffset);

            scrollToTab(position, positionOffset);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageSelected(int position) {
        }

    }

    /**
     * 由于tabStrip在滑动的时候有几种模式可选
     * 1.indicatorAlwaysInCenter   tab始终在中间显示
     * 如果要求tab始终在中间,那么势必要在每次滑动后
     * 2.普通模式
     * 注:滑动的方向会影响这两个参数
     * 右->左   tabIndex为上一个(左边的)的position
     * 左->右   tabIndex为当前显示的position(tabIndex=0除外)
     *
     * @param tabIndex
     * @param positionOffset [0-1]
     */
    private void scrollToTab(int tabIndex, float positionOffset) {
        int tabStripChildCount = tabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }
        /**
         * 拿到当前选中的tab
         */
        View selectedTab = tabStrip.getChildAt(tabIndex);

        int widthPlusMargin = Utils.getWidth(selectedTab) + Utils.getMarginHorizontally(selectedTab);

        int extraOffset = (int) (positionOffset * widthPlusMargin);
        /**
         * indicator始终在中间
         */
        if (tabStrip.isIndicatorAlwaysInCenter()) {
            if (0f < positionOffset && positionOffset < 1f) {
                View nextTab = tabStrip.getChildAt(tabIndex + 1);
                /**
                 * 计算selectTab的后半段和nextTab的前半段
                 */
                int selectHalfWidth = Utils.getWidth(selectedTab) / 2 + Utils.getMarginEnd(selectedTab);
                int nextHalfWidth = Utils.getWidth(nextTab) / 2 + Utils.getMarginStart(nextTab);
                extraOffset = Math.round(positionOffset * (selectHalfWidth + nextHalfWidth));
            }

            View firstTab = tabStrip.getChildAt(0);
            int x;
            /**
             * 计算出在x轴滑动的距离
             */
            int first = Utils.getWidth(firstTab) + Utils.getMarginStart(firstTab);
            int selected = Utils.getWidth(selectedTab) + Utils.getMarginStart(selectedTab);

            x = Utils.getStart(selectedTab) - Utils.getMarginStart(selectedTab) + extraOffset;
            x -= (first - selected) / 2;
            Log.i(TAG, "x:" + x);

            scrollTo(x, 0);

            return;
        }
        /**
         * 正常的滑动模式
         */
        int x;
        x = (tabIndex > 0 || positionOffset > 0) ? -titleOffset : 0;
        int start = Utils.getStart(selectedTab);
        int startMargin = Utils.getMarginStart(selectedTab);
        x += start - startMargin + extraOffset;
        Log.i(TAG, "tabIndex:" + tabIndex);
        scrollTo(x, 0);
    }

}
