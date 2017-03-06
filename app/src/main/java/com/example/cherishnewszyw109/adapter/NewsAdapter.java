package com.example.cherishnewszyw109.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.cherishnewszyw109.bean.News;
import com.example.cherishnewszyw109.viewholder.NewsViewHolder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;


/**
 * Created by Mr.Jude on 2016/6/7.
 */
public class NewsAdapter extends RecyclerArrayAdapter<News> {
    public NewsAdapter(Context context) {
        super(context);
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(parent);
    }
}
