package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItems;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private EditText editText;
    private Button btn;
    public static final String ID_AUTH = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smartTabLayout = (SmartTabLayout) findViewById(R.id.smart_tab);
        viewPager = (ViewPager) findViewById(R.id.vp);
        editText = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
        init();
    }

    private void test() {
        String num = editText.getText().toString().trim();
        if (Pattern.matches(ID_AUTH, num)) {
            Toast.makeText(this, "有效", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "无效", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        ViewPagerItemAdapter adapter = new ViewPagerItemAdapter(ViewPagerItems.with(this)
                .add(R.string.title_01, R.layout.page_01)
                .add(R.string.title_02, R.layout.page_02)
                .add(R.string.title_03, R.layout.page_03)
                .add(R.string.title_03, R.layout.page_03)
                .add(R.string.title_03, R.layout.page_03)
                .add(R.string.title_03, R.layout.page_03)
                .create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
