package com.example.cherishnewszyw109.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by yuwei on 2016/11/7.
 */
public class MyViewPaperAdapter extends PagerAdapter {
    private ArrayList<View> viewLists;

    public MyViewPaperAdapter(ArrayList<View> viewLists) {
        super();
        this.viewLists = viewLists;
    }

    @Override
    public int getCount() {
        return viewLists.size();
    }       //得到view的总数

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }                                                           //判断当前的对象和view是不是一个

    @Override
    public Object instantiateItem(ViewGroup container, int position) {     //显示view
        container.addView(viewLists.get(position));
        return viewLists.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) { //销毁view
        container.removeView(viewLists.get(position));
    }
}
