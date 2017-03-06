package com.example.cherishnewszyw109.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import com.example.cherishnewszyw109.MainActivity;
import com.example.cherishnewszyw109.R;
import com.example.cherishnewszyw109.adapter.MyViewPaperAdapter;

import java.util.ArrayList;


public class GuidActivity extends BaseActivity {
    private ViewPager vpager_one;
    private ArrayList<View> aList;
    private MyViewPaperAdapter mAdapter;
    private Button button_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_start);
        BindView();
        initData();
        vpager_one.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == aList.size()-1){
                    button_start = (Button)findViewById(R.id.start_app_Button);
                    button_start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(GuidActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        aList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        aList.add(li.inflate(R.layout.guid1,null,false));
        aList.add(li.inflate(R.layout.guid2,null,false));
        aList.add(li.inflate(R.layout.guid3,null,false));
        mAdapter = new MyViewPaperAdapter(aList);
        vpager_one.setAdapter(mAdapter);
    }

    private void BindView() {
        vpager_one = (ViewPager)findViewById(R.id.ViewPaper_Guid);
    }
}
