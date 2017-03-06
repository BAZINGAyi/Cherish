package com.example.cherishnewszyw109.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.cherishnewszyw109.MainActivity;
import com.example.cherishnewszyw109.R;
import com.example.cherishnewszyw109.db.NewsDBAdapter;


public class WelcomeActivity extends BaseActivity {
    private static final int GO_HOME = 1001;
    private static final int GO_GUIDE = 1000;
    private static final int TIME = 2000;
    private static  Boolean isFrist = false;
    NewsDBAdapter dbAdapter;
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg.what+"--");
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
        setContentView(R.layout.welcome_layout);
        init();
        initDb();
    }

    private void init() {
        SharedPreferences s = getSharedPreferences("welcome",MODE_PRIVATE);
        isFrist = s.getBoolean("isFirstIn",true);
        if(!isFrist){
            mhandler.sendEmptyMessageDelayed(GO_HOME,TIME);
        }else {
            mhandler.sendEmptyMessageDelayed(GO_GUIDE,TIME);
            SharedPreferences.Editor editor = s.edit();
            editor.putBoolean("isFirstIn",false);
            editor.commit();
        }
    }

    private void goGuide() {
        Intent intent = new Intent(WelcomeActivity.this , GuidActivity.class);
        startActivity(intent);
        finish();
    }
    private void goHome() {
        Intent intent = new Intent(WelcomeActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void initDb() {
        dbAdapter = NewsDBAdapter.getInstance(WelcomeActivity.this);
        dbAdapter.openDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.closeDB();
    }

}
