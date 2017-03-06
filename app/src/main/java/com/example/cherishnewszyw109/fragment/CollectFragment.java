package com.example.cherishnewszyw109.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cherishnewszyw109.MainActivity;
import com.example.cherishnewszyw109.R;
import com.example.cherishnewszyw109.activity.NewsDetailActivity;
import com.example.cherishnewszyw109.adapter.CollectAdapter;
import com.example.cherishnewszyw109.db.NewsDBAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuwei on 2016/12/19.
 */
public class CollectFragment extends Fragment implements CollectAdapter.OnViewClickListener{
    RecyclerView recyclerView;
    CollectAdapter adapter;
    List<Map> datas;
    private static NewsDBAdapter dbAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_collect, container, false);
        initDb();
        initView(view);
        getData();
        return view;
    }


    private void getData() {
                Observable.create(new Observable.OnSubscribe<List<Map>>() {
                    @Override
                    public void call(Subscriber<? super List<Map>> subscriber) {
                        List<Map> datas = dbAdapter.queryAll();
                        if(datas != null)
                            subscriber.onNext(datas);
                        System.out.println(Thread.currentThread().getName());
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<Map>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<Map> maps) {
                                System.out.println(Thread.currentThread().getName());
                                datas = maps;
                                adapter = new CollectAdapter(getActivity(),datas,CollectFragment.this);
                                recyclerView.setAdapter(adapter);
                                initOnclick();
                            }
                        });

    }

    private void initOnclick() {
        adapter.setOnItemClickListener(new CollectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                System.out.println("当前的Item搜"+position);
                Map map = adapter.getDetailinfo(position);
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("picurl",map.get("pic").toString());
                intent.putExtra("content",map.get("content").toString());
                intent.putExtra("title",map.get("title").toString());
                intent.putExtra("time",map.get("time").toString());
                startActivity(intent);
            }
        });
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_collect);
        if(getActivity().getResources().getConfiguration().orientation==1)
             recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        else{
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    @Override
    public void onViewClick(final int position, int viewtype) {
        final Snackbar snackbar = Snackbar.make(getView(), "删除?", Snackbar.LENGTH_SHORT);
        snackbar.show();
        snackbar.setAction("是", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
              doDelete(position);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.closeDB();
    }

    private void initDb() {
        dbAdapter = NewsDBAdapter.getInstance(getActivity());
        dbAdapter.openDB();
    }

    private void doDelete(final int position) {
        Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                String id = adapter.getid(position);
                if (dbAdapter!=null){
                    long res = dbAdapter.deleteById(id);
                    subscriber.onNext(res);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long res) {
                        if (res == 1){
                            final Snackbar snackbar = Snackbar.make(getView(), "删除成功", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            adapter.delete(position);
                        }else{
                            final Snackbar snackbar = Snackbar.make(getView(), "删除失败", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }


                });

    }

}
