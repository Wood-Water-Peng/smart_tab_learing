package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.ogaclejapan.smarttablayout.utils.ViewPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItems;

public class MainActivity extends AppCompatActivity {
    private MyTabLayout smartTabLayout;
    private ViewPager viewPager;
    public static final String ID_AUTH = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smartTabLayout = (MyTabLayout) findViewById(R.id.my_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.vp);
        init();
    }


    private void init() {
        ViewPagerItemAdapter adapter = new ViewPagerItemAdapter(ViewPagerItems.with(this)
                .add(R.string.title_01, R.layout.page_01)
                .add(R.string.title_02, R.layout.page_02)
                .add(R.string.title_03, R.layout.page_03)
                .add(R.string.title_04, R.layout.page_03)
                .add(R.string.title_05, R.layout.page_03)
                .add(R.string.title_06, R.layout.page_03)
                .create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
