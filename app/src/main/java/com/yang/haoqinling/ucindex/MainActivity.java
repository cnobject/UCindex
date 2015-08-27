package com.yang.haoqinling.ucindex;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private View headerView;
    private View headerUpView;
    private View headerSearchView;
    private ViewGroup headerDownView;
    private View voiceView;
    private ViewPager viewPager;
    private List<Fragment> datas;
    private int headerMaxHeight;
    private int headerMinHeight;
    private int headerUpLayoutHeight;
    private int headerSearchViewMargin;
    private int headerSearchWidth;
    private AFragment aFragment;
    private BFragment bFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setViewPager();
    }

    private void initViews() {
        headerView = findViewById(R.id.header);
        headerUpView = findViewById(R.id.header_up_layout);
        headerSearchView = findViewById(R.id.header_search_layout);
        headerDownView = (ViewGroup) findViewById(R.id.header_down_layout);
        voiceView = findViewById(R.id.voice);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        getHeaderHeight();
    }

    private void getHeaderHeight() {

        ViewTreeObserver vto2 = headerSearchView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headerSearchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                headerMaxHeight = headerView.getMeasuredHeight();
                headerMinHeight = headerSearchView.getMeasuredHeight();
                headerUpLayoutHeight = headerUpView.getMeasuredHeight();
                headerSearchWidth = headerSearchView.getMeasuredWidth();
                headerSearchViewMargin = (headerView.getMeasuredWidth() - headerSearchWidth) / 2;

                aFragment.setFragmentPaddingTop(headerMaxHeight);
                bFragment.setFragmentPaddingTop(headerMinHeight);
            }
        });

    }

    private void setViewPager() {
        datas = new ArrayList<>();
        aFragment = new AFragment();
        bFragment = new BFragment();
        datas.add(aFragment);
        datas.add(bFragment);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //从页面a滑向页面b positionOffset的变化范围为0到1
                //从页面b滑向页面a positionOffset的变化范围为1到0

                if(position == 1) {
                    voiceView.setVisibility(View.VISIBLE);
                } else {
                    voiceView.setVisibility(View.GONE);
                }
                if (positionOffset > 0) {
                    ViewGroup.LayoutParams lp = headerView.getLayoutParams();
                    lp.height = (int) (headerMaxHeight - (headerMaxHeight - headerMinHeight) * positionOffset);
                    headerView.setLayoutParams(lp);
                    //设置头布局的位移
                    int deltaY = (int) (headerUpLayoutHeight * positionOffset);
                    headerUpView.setTranslationY(-deltaY);
                    headerSearchView.setTranslationY(-deltaY);
                    headerDownView.setTranslationY(-deltaY);
                    //设置搜索栏下面布局中的View的透明度变化
                    float alpha = 1 - positionOffset * 2;
                    setChildAlpha(alpha);
                    //设置搜索栏的背景颜色变色
                    int color = getColor(positionOffset, Color.parseColor("#7CC1EA"), Color.parseColor("#50ABE2"));
                    headerSearchView.setBackgroundColor(color);
                    //设置搜索栏的大小
                    headerSearchView.setScaleX((headerSearchWidth + headerSearchViewMargin * positionOffset) / headerSearchWidth);
                }
            }

        });
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }

    private int getColor(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (startA + (int)(fraction * (endA - startA))) << 24 |
                ((startR + (int)(fraction * (endR - startR))) << 16) |
                ((startG + (int)(fraction * (endG - startG))) << 8) |
                ((startB + (int)(fraction * (endB - startB))));
    }

    private void setChildAlpha(float alpha) {
        int count = headerDownView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = headerDownView.getChildAt(i);
            child.setAlpha(alpha);
        }
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return datas.get(position);
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }

}
