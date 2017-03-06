package com.example.cherishnewszyw109.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by yuwei on 2016/11/11.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragments ;
    String[] title ;
    public MyPagerAdapter(FragmentManager fm , List<Fragment> fragments ,  String[] title) {
        super(fm);
        this.fragments = fragments;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) { //返回fragment
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) { //返回title 为了给Tab使用
        return title[position];
    }
}
