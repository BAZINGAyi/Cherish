package com.example.cherishnewszyw109;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Toast;

import com.example.cherishnewszyw109.activity.BaseActivity;
import com.example.cherishnewszyw109.adapter.MyPagerAdapter;
import com.example.cherishnewszyw109.db.NewsDBAdapter;
import com.example.cherishnewszyw109.fragment.CollectFragment;
import com.example.cherishnewszyw109.fragment.NewsTypeFragment;
import com.example.cherishnewszyw109.fragment.QrFragment;
import com.example.cherishnewszyw109.fragment.RecommendFragment;
import com.example.cherishnewszyw109.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabLayout tableLayout;
    public static SearchView mSearchView;
    private List<Fragment> mfragments = new ArrayList<>();
    private PagerAdapter madapter;
    private String[] titles = new String[]{"专题资讯","新闻推荐","懂你的搜索","登录","我的收藏"};
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setMyActionbar();
        initViewPager();
        initOnclick();
    }



    private void initOnclick() {
        mNavigationView.setNavigationItemSelectedListener(this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = Snackbar.make(mDrawerLayout, "确认退出吗?", Snackbar.LENGTH_SHORT);
                snackbar.show();
                snackbar.setAction("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        finish();
                    }
                });
            }
        });
    }

    private void initViewPager() {
        Fragment fragment = new NewsTypeFragment();
        mfragments.add(fragment);
        Fragment fragment1 = new RecommendFragment();
        mfragments.add(fragment1);
        Fragment fragment2 = new SearchFragment();
        mfragments.add(fragment2);
        Fragment fragment3 = new QrFragment();
        mfragments.add(fragment3);
        Fragment fragment4 = new CollectFragment();
        mfragments.add(fragment4);

        madapter = new MyPagerAdapter(getSupportFragmentManager(),mfragments,titles);
        viewPager.setAdapter(madapter);
        tableLayout.setupWithViewPager(viewPager);

    }

    private void initView() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        tableLayout = (TabLayout) findViewById(R.id.tabs);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
    }

    private void doSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewPager.setCurrentItem(2);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void setMyActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        //设置home点击事件
        ab.setHomeAsUpIndicator(R.mipmap.home);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.ab_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        doSearchView();
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
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_type) {
            item.setChecked(true);
            viewPager.setCurrentItem(0);

        } else if (id == R.id.nav_news) {
            item.setChecked(true);
            viewPager.setCurrentItem(1);

        } else if (id == R.id.nav_search) {
            item.setChecked(true);
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_login) {
            item.setChecked(true);
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_collect) {
            viewPager.setCurrentItem(4);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        int pos = tableLayout.getSelectedTabPosition();
        //System.out.println("pos"+"退出");
        if(pos == 0){
            NewsTypeFragment.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

}
