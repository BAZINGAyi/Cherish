package com.example.cherishnewszyw109.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cherishnewszyw109.R;
import com.example.cherishnewszyw109.activity.NewsDetailActivity;
import com.example.cherishnewszyw109.adapter.NewsAdapter;
import com.example.cherishnewszyw109.api.ApiService;
import com.example.cherishnewszyw109.bean.LocalNews;
import com.example.cherishnewszyw109.bean.News;
import com.example.cherishnewszyw109.bean.NewsGson;
import com.example.cherishnewszyw109.bean.NewsUri;
import com.example.cherishnewszyw109.help.HelpFormatData;
import com.example.cherishnewszyw109.help.HelpHttp;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yuwei on 2016/12/4.
 */
public class RecommendFragment extends Fragment {
    @Nullable
    private int page = 1;
    private  int currentPage = 1;
    //加载数据相关
    private  NewsAdapter adapter;
    private EasyRecyclerView recyclerView;
    private LayoutInflater layoutInflater ;
    //加载控件
    private static String URLNEWS;
    private static int urlType;
    private static  int flag_News = 0 ; //用于标记是开始显示的新闻页还是用户点击新闻类型后调回的新闻页 0默认是新闻开始页
    private LayoutInflater inflater ;   //用于加载footer

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_recommend,null);
        initView(view);
        flashView();
        getLocalData();
        onClick();
        return view;
    }

    private void initView(View view) {
        layoutInflater = LayoutInflater.from(getActivity());
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter = new NewsAdapter(getActivity()));
        inflater = LayoutInflater.from(getActivity());
    }

    private void flashView() {
        //配置RecyclerView
        configRecyclerView();
        //配置NewsAdapter
        configNewsadapter();
    }

    private void configRecyclerView() {
        //new LinearLayoutManager(getActivity())
        //new GridLayoutManager(getActivity(),2)
        //new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        if(getActivity().getResources().getConfiguration().orientation==1) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
            gridLayoutManager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(2));
            recyclerView.setLayoutManager(gridLayoutManager);
        } else{
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
            gridLayoutManager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(3));
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        //设置没有网络的状态
        recyclerView.setEmptyView(R.layout.view_empty);
        recyclerView.setProgressView(R.layout.view_complete);
        //写刷新事件
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        currentPage = 1;
                        page = 1;
                        //getData();
                        getLocalData();
                    }
                }, 1000);
            }
        });
    }

    private void configNewsadapter() {
        //更多加载
        adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                //getData();
                getLocalData();
            }

            @Override
            public void onMoreClick() {

            }
        });
        //加载已满
        adapter.setNoMore(R.layout.view_complete, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {

            }

            @Override
            public void onNoMoreClick() {

            }
        });

       // 设置footer
//        adapter.addFooter(new RecyclerArrayAdapter.ItemView(){
//
//            @Override
//            public View onCreateView(ViewGroup parent) {
//                return inflater.inflate(R.layout.view_complete,parent,false);
//            }
//
//            @Override
//            public void onBindView(View headerView) {
//
//            }
//        } );
    }

    private void onClick() {
            //点击事件
            adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("picurl",adapter.getAllData().get(position).getPicUrl());
//                    intent.putExtra("url",adapter.getAllData().get(position).getUrl());
                    intent.putExtra("content",adapter.getAllData().get(position).getContent());
                    intent.putExtra("title",adapter.getAllData().get(position).getTitle());
                    intent.putExtra("time",adapter.getAllData().get(position).getCtime());
                    startActivity(intent);
                }
            });
        }

    private void getLocalData() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            //URLNEWS = NewsUri.nUri[0];
           // URLNEWS = URLNEWS.trim();
            flag_News = 0;
            //System.out.println(URLNEWS);
        } else {
          //  System.out.println("Grid的位置为" + bundle.getInt("uri"));
            urlType = NewsUri.nUri_Tid[bundle.getInt("uri")];
            flag_News = 1;
        }
        if ( page<=currentPage )
        {
            //已经加载所有内容
            if (page == currentPage && page!=1){
                adapter.stopMore();
            }
            //System.out.println("TotalCount" + currentPage);
            HelpHttp help = new HelpHttp();
            ApiService apiService = help.newsTypeHttp();
            if (flag_News!=0) {
                apiService.getNewsData(urlType,page)
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<LocalNews, List<News>>() {
                            @Override
                            public List<News> call(LocalNews newsgson) {
                                currentPage = newsgson.getValidPage();
                                if(currentPage == 1){
                                    adapter.stopMore();
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
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<News> newses) {
                                adapter.addAll(newses);
                            }
                        });

            }else{
                apiService.getNewsData(page)
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<LocalNews, List<News>>() {
                            @Override
                            public List<News> call(LocalNews newsgson) {
                                currentPage = newsgson.getValidPage();
                                if(currentPage == 1){
                                    adapter.stopMore();
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
                                            e.printStackTrace();
                                       }

                                       @Override
                                       public void onNext(List<News> newses) {
                                           adapter.addAll(newses);
                                       }
                                   }
                        );
            }

            page = page + 1;
        }

    }

    //取消加载网络数据的方法，使用本地数据库
    private void getData() {
        Bundle bundle = getArguments();
        if (bundle == null){
            URLNEWS = NewsUri.nUri[0];
            URLNEWS = URLNEWS.trim();
          //  System.out.println(URLNEWS);
        }else {
           // System.out.println("Grid的位置为"+bundle.getInt("uri"));
            URLNEWS = NewsUri.nUri[bundle.getInt("uri")];
        }

        Log.d("page", page + "");
        //tem.out.println("TotalCount"+currentPage);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.tianapi.com/")
                //String
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())//添加 json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加 RxJava 适配器
                .build();
        ApiService apiManager = retrofit.create(ApiService.class);//这里采用的是Java的动态代理模式
        apiManager.getNewsData(URLNEWS,"a442f2e7383713a977ad2f557593f0ba", "10", page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<NewsGson, List<News>>() {
                    @Override
                    public List<News> call(NewsGson newsgson) {
                        currentPage = newsgson.getCode();
                        List<News> newsList = new ArrayList<News>();
                        for (NewsGson.NewslistBean newslistBean : newsgson.getNewslist()) {
                            News new1 = new News();
                            new1.setTitle(newslistBean.getTitle());
                            new1.setCtime(newslistBean.getCtime());
                            new1.setDescription(newslistBean.getDescription());
                            new1.setPicUrl(newslistBean.getPicUrl());
                            new1.setUrl(newslistBean.getUrl());
                            //  System.out.println(newslistBean.getUrl()+"--");
                            newsList.add(new1);
                        }
                        return newsList; // 返回类型
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onNext(List<News> newsList) {
                        adapter.addAll(newsList);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(),
                                "网络连接失败", Toast.LENGTH_LONG).show();
                    }
                });
        page = page + 1;
    }


}
