package com.example.cherishnewszyw109.help;

import com.example.cherishnewszyw109.bean.LocalNews;
import com.example.cherishnewszyw109.bean.News;
import com.example.cherishnewszyw109.bean.NewsUri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuwei on 2016/12/16.
 */
public class HelpFormatData {
    public List<News> formatData(LocalNews newsgson){
        List<News> newsList = new ArrayList<News>();
        for (LocalNews.ResultBean newslistBean : newsgson.getResult()) {
            News new1 = new News();
            new1.setTitle(newslistBean.getTitle());
            new1.setCtime(newslistBean.getPublishtime());
            new1.setContent(newslistBean.getContent());
            String mod = NewsUri.BaseUrl + NewsUri.localImgpic + newslistBean.getPic();
         //   System.out.println(mod);
            new1.setPicUrl(mod);
            new1.setUrl(newslistBean.getId());
            newsList.add(new1);
        }
        return newsList;
    }
}
