package com.example.cherishnewszyw109.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cherishnewszyw109.R;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

/**
 * Created by yuwei on 2016/12/4.
 */
public class RollPagerAdapter extends StaticPagerAdapter {
    public static final int[] imgs = {
            R.mipmap.news1,
            R.mipmap.news2,
            R.mipmap.news3
    };


    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }
}
