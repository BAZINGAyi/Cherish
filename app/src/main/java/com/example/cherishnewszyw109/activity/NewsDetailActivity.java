package com.example.cherishnewszyw109.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cherishnewszyw109.R;
import com.example.cherishnewszyw109.adapter.CollectAdapter;
import com.example.cherishnewszyw109.db.NewsDBAdapter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuwei on 2016/12/5.
 */
public class NewsDetailActivity extends BaseActivity {
    private String url;
    private WebView webView;
    //原加载网络相关
    private ImageView ivImage;
    private CollapsingToolbarLayout colooapsingtoolbarlayout;
    private Button change_TextSize;
    SeekBar mRadiusSeekBar;
    private TextView news_content;
    private int currentseek = 0;
    //数据库相关
    FloatingActionButton collectButton;
    NewsDBAdapter dbAdapter;
    private String content;
    private String picUrl;
    private String news_titile;
    private String time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOverflowMenu();
        setContentView(R.layout.textviewdisplay);
        initDb();
        setMyActionbar();
        hideTile();
        initView();
        initData(savedInstanceState);
        initOnClick();
        //bindWebView();
    }


    private void initOnClick() {
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = Snackbar.make(colooapsingtoolbarlayout, "收藏?", Snackbar.LENGTH_SHORT);
                snackbar.show();
                snackbar.setAction("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        querydata();
                    }
                });
            }
        });
    }

    private void hideTile() {
        colooapsingtoolbarlayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar) ;
        colooapsingtoolbarlayout.setExpandedTitleColor(Color.parseColor("#ffffff"));//设置还没收缩时状态下字体颜色
        colooapsingtoolbarlayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }
    private void initData(Bundle s) {
        Intent intent = getIntent();
        content  = intent.getStringExtra("content");
        picUrl = intent.getStringExtra("picurl");
        news_titile = intent.getStringExtra("title");
        time = intent.getStringExtra("time");
        doStringText(content);
        doSeekBar();
        if(s!=null){
            int contentfont = s.getInt("contentfont",20);
            news_content.setTextSize(contentfont);
        }
    }
    private void initView() {
       // webView = (WebView)findViewById(R.id.webView);
        ivImage = (ImageView)findViewById(R.id.ivImage);
        collectButton = (FloatingActionButton)findViewById(R.id.fab_collect);
        news_content = (TextView)findViewById(R.id.news_content);
        mRadiusSeekBar = (SeekBar) findViewById(R.id.cardview_radius_seekbar);
    }

    private void doSeekBar() {
        news_content.setTextSize(20);
        mRadiusSeekBar.setProgress(20);
        mRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentseek = mRadiusSeekBar.getProgress();

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                news_content.setTextSize(currentseek);
            }
        });
    }

    private void doStringText(String content) {
        content = "\t\t"+content;
        content = content.replace("\n","\n\t\t");
        news_content.setText(content);
        Glide.with(this)
                .load(picUrl).error(R.mipmap.ic_launcher)
                .fitCenter().into(ivImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_news1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    private void setMyActionbar() {
        final ActionBar ab = getSupportActionBar();
        //设置home点击事件
        ab.setHomeAsUpIndicator(R.mipmap.returnhome);
        ab.setDisplayHomeAsUpEnabled(true);
    }
    private void initDb() {
        dbAdapter = NewsDBAdapter.getInstance(NewsDetailActivity.this);
        dbAdapter.openDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.closeDB();
    }
    //加载js和网络布局
    private void bindWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        //  System.out.println(url+"--");
        webView.loadUrl(url);

        Glide.with(this)
                .load(picUrl).error(R.mipmap.ic_launcher)
                .fitCenter().into(ivImage);
    }

    private void querydata() {
        Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                Integer res= dbAdapter.queryBytitle(news_titile);
                if(res != null){
                    if (res == -1){
                        final Snackbar snackbar = Snackbar.make(ivImage, "您已经收藏过了", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }else{
                        Map map = new HashMap();
                        map.put("content",content);
                        map.put("picurl",picUrl);
                        map.put("title",news_titile);
                        map.put("time",time);
                        long id = dbAdapter.insert(map);
                        subscriber.onNext(id);
                    }
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
                        if (res == -1){
                            final Snackbar snackbar = Snackbar.make(ivImage, "收藏失败", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else{
                            final Snackbar snackbar = Snackbar.make(ivImage, "收藏成功", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentseek!=0)
            outState.putInt("contentfont",currentseek);
    }
}