package com.example.cherishnewszyw109.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cherishnewszyw109.MainActivity;
import com.example.cherishnewszyw109.R;
import com.example.cherishnewszyw109.activity.NewsDetailActivity;
import com.example.cherishnewszyw109.adapter.NewsAdapter;
import com.example.cherishnewszyw109.api.ApiService;
import com.example.cherishnewszyw109.bean.LocalNews;
import com.example.cherishnewszyw109.bean.News;
import com.example.cherishnewszyw109.help.HelpFormatData;
import com.example.cherishnewszyw109.help.HelpHttp;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yuwei on 2016/12/5.
 */
public class SearchFragment extends Fragment {
    private EasyRecyclerView search_recyclerView;
    private EditText search_Edit;
    private NewsAdapter newsAdapter;
    private int page = 1;
    private  int currentPage = 1;
    private Button searchButton;
    //加载数据相关
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_search,container,false);
        initView(view);
        initOnClick();
        configRecyclerView();
        configNewsadapter();
        initSearch(savedInstanceState);
        onClick();
        return view;
    }

    private void configRecyclerView() {
        if(getActivity().getResources().getConfiguration().orientation==1) {
            LinearLayoutManager linear = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            search_recyclerView.setLayoutManager(linear);
        } else{
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
            gridLayoutManager.setSpanSizeLookup(newsAdapter.obtainGridSpanSizeLookUp(3));
            search_recyclerView.setLayoutManager(gridLayoutManager);
        }

        search_recyclerView.setEmptyView(R.layout.view_nodata);
    }

    private void initView(View view) {
        search_recyclerView = (EasyRecyclerView) view.findViewById(R.id.search_recyclerView);
        search_Edit = (EditText)view.findViewById(R.id.search_Edit);
        search_recyclerView.setAdapter(newsAdapter = new NewsAdapter(getActivity()));
        searchButton = (Button)view.findViewById(R.id.search_button);
    }
    private void initOnClick(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = search_Edit.getText().toString();
                title = title.trim();
                if(!title.equals("")){
                    page = 1;
                    currentPage = 1;
                    newsAdapter.clear();
                    conectData(title);
                }else {
                    search_Edit.setError("搜索不能为空");
                }

            }
        });
    }
//当屏幕切换的时候重新查询数据
    private void initSearch(Bundle savedInstanceState) {
        String search = String.valueOf(MainActivity.mSearchView.getQuery());
        if (savedInstanceState!=null){
            String s = savedInstanceState.getString("search",null);
            if(s != null)
                conectData(s);
        }else if(!search.equals("")){
            conectData(search);
        }
    }
    //用于加载下一页的请求数据方法
    private void requestData(String search) {
        if ( page<=currentPage ) {
            //已经加载所有内容
            if (page == currentPage && page!=1){
                newsAdapter.stopMore();
            }
            conectData(search);
        }
    }
    //用于用户重新点击按钮后的请求数据的方法
    private void conectData(String search) {
        HelpHttp help = new HelpHttp();
        ApiService apiService = help.newsTypeHttp();
        apiService.getSearchData(search,page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<LocalNews, List<News>>() {
                    @Override
                    public List<News> call(LocalNews newsgson) {
                        currentPage = newsgson.getValidPage();
                        if(currentPage == 1){
                            newsAdapter.stopMore();
                        }
                        HelpFormatData helpFormatData = new HelpFormatData();
                        List<News> newsList =  helpFormatData.formatData(newsgson);
                        return newsList; // 返回类型
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        newsAdapter.clear();
                        search_recyclerView.showEmpty();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<News> newses) {
                        newsAdapter.addAll(newses);
                    }
                });
        page = page + 1;
    }

    private void configNewsadapter() {
        //更多加载
        newsAdapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                String title = search_Edit.getText().toString();
                title = title.trim();
                requestData(title);
            }

            @Override
            public void onMoreClick() {

            }
        });
        //加载已满
        newsAdapter.setNoMore(R.layout.view_complete, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {

            }

            @Override
            public void onNoMoreClick() {

            }
        });

    }

    private void onClick() {
        //点击事件
        newsAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("picurl",newsAdapter.getAllData().get(position).getPicUrl());
                intent.putExtra("title",newsAdapter.getAllData().get(position).getTitle());
                intent.putExtra("content",newsAdapter.getAllData().get(position).getContent());
                intent.putExtra("time",newsAdapter.getAllData().get(position).getCtime());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search",search_Edit.getText().toString());
    }
}
