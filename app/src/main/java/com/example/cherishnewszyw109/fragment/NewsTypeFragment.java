package com.example.cherishnewszyw109.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentFrameLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.example.cherishnewszyw109.R;
import com.example.cherishnewszyw109.adapter.MyGridView;
import com.example.cherishnewszyw109.adapter.RollPagerAdapter;
import com.example.cherishnewszyw109.bean.NewsUri;
import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuwei on 2016/12/4.
 */
public class NewsTypeFragment extends Fragment {
    RollPagerView mRollViewPager ;
    private MyGridView gridView ;
    private LayoutInflater inflater;
    private List<String> newsType = new ArrayList<>();
    private ArrayAdapter<String> madapter;
    private static FrameLayout frameLayout;
    private View view;
    private RollPagerAdapter rollPagerAdapter  = new RollPagerAdapter();
    private static int currentGridpos = -1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null ) {
            //初始化的程序卸载这里
            view = inflater.inflate(R.layout.fg_newstype, container, false);
            initView(view);
            setGardView();
            for (int i=0;i<NewsUri.nUri_Local.length;i++){
                newsType.add(NewsUri.nUri_Local[i]);
            }
            if (savedInstanceState!=null){
                //savedInstanceState.getInt的默认值是0
                int s = savedInstanceState.getInt("currentposition1",-1);
              //  System.out.println("重构+"+savedInstanceState.getInt("currentposition1",-1));
                if(s != -1){
                    switchFragment (s);
                }
            }
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup viewGroup = (ViewGroup)view.getParent();
        if (viewGroup != null){
            viewGroup.removeView(view);
        }
        return view;
    }

    private void initView(View view) {
        mRollViewPager = (RollPagerView)view.findViewById(R.id.rollPagerView);
        mRollViewPager.setPlayDelay(2000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(rollPagerAdapter);
        gridView = (MyGridView)view.findViewById(R.id.gv_newsType);
        frameLayout = (FrameLayout)view.findViewById(R.id.replace_Newstype);
    }

    private void setGardView() {
        inflater = LayoutInflater.from(getActivity());
        //-1代表自己设置布局
        gridView.setAdapter(madapter = new ArrayAdapter<String>(getActivity(),-1,newsType){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null){
                    convertView = inflater.inflate(R.layout.newstype_grid_item,parent,false);
                }
                TextView tv = (TextView)convertView.findViewById(R.id.newstype_name);
                tv.setText(getItem(position));
                return convertView;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("zhangyuwei--");;
                currentGridpos = position;
                frameLayout.setVisibility(View.VISIBLE);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                RecommendFragment recommendFragment = new RecommendFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("uri",position);
                recommendFragment.setArguments(bundle);
                ft.replace(R.id.replace_Newstype,recommendFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private void switchFragment(int position) {
        frameLayout.setVisibility(View.VISIBLE);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        RecommendFragment recommendFragment = new RecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("uri",position);
        recommendFragment.setArguments(bundle);
        ft.replace(R.id.replace_Newstype,recommendFragment);
        ft.commit();
    }

    //通过返回键判断是否处于TableLayout的第二个位置
    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            //用户按返回键,把frameLayout隐藏
            frameLayout.setVisibility(View.GONE);
            currentGridpos = -1;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (currentGridpos != -1)
            outState.putInt("currentposition1",currentGridpos);
        super.onSaveInstanceState(outState);
    }

}
