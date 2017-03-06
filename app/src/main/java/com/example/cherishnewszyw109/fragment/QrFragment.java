package com.example.cherishnewszyw109.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.cherishnewszyw109.R;
import com.example.cherishnewszyw109.bean.NewsUri;
import com.example.cherishnewszyw109.help.HelpRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yuwei on 2016/12/5.
 */
public class QrFragment extends Fragment implements View.OnClickListener{
    private Button loginButton ;
    private EditText nameET;
    private EditText passET;
    private Switch rememberSW;
    SharedPreferences saveUserlogin;
    private FrameLayout switchFragment;
    private View view;
    private LinearLayout fg_qr_login;
    private  int islogin = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null ) {
            view = inflater.inflate(R.layout.fg_qr, container, false);
            initView(view);
            setCheckName();
            readSp();
            if (savedInstanceState!=null){
                int islogin = savedInstanceState.getInt("currentposition");
                if (islogin == 1)
                    switchFragment();
                //System.out.println(savedInstanceState.getInt("currentposition")+"重构的值");
            }

        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup viewGroup = (ViewGroup)view.getParent();
        if (viewGroup != null){
            viewGroup.removeView(view);
        }
        return view;
    }

    private void setCheckName() {
        nameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String  name = nameET.getText().toString();
                    name = name.trim();
                    //检查名字合法性
                    if (TextUtils.isEmpty(name)) {
                        nameET.setError(getString(R.string.error_field_required));
                    } else if (isNameValid(name)) {
                        nameET.setError(getString(R.string.error_invalid_name));
                    }else {
                        requestCheckUsername(name);
                    }
                }

            }
        });
    }

    private void initView(View view) {
        loginButton = (Button)view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        nameET = (EditText)view.findViewById(R.id.uname);
        passET = (EditText)view.findViewById(R.id.upassword);
        rememberSW = (Switch)view.findViewById(R.id.switch_save);
        switchFragment = (FrameLayout)view.findViewById(R.id.fragment_container);
        fg_qr_login = (LinearLayout)view.findViewById(R.id.fg_qr_login);
    }



    private void readSp() {
        saveUserlogin = getActivity().getSharedPreferences("LoginInfo",getActivity().MODE_PRIVATE); //保存到名为LoginInfo的文件下，模式为私有
        String strname = saveUserlogin.getString("name",null);
        String strpass = saveUserlogin.getString("password",null);
        Boolean bool   = saveUserlogin.getBoolean("ischecked",false);
        if(bool == false){
        }else {
            nameET.setText(strname);
            passET.setText(strpass);
            rememberSW.setChecked(bool);
        }
    }




    private void checkUser() {
        String name = nameET.getText().toString() ;
        name = name.trim();
        String  pass =  passET.getText().toString() ;
        pass = pass.trim();
        Boolean flag = true;   //为true的时候才检查账号和密码 这步请求网络

        //检查密码合法性
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            passET.setError(getString(R.string.error_invalid_password));
            flag = false;
        }
        //检查名字合法性
        if (TextUtils.isEmpty(name)) {
            nameET.setError(getString(R.string.error_field_required));
            flag = false;
        } else if (isNameValid(name)) {
            nameET.setError(getString(R.string.error_invalid_name));
            flag = false;
        }
        //如果用户输入合法
        if (flag == true){
            requestCheckUserLogin( name , pass);
        }

    }

    //判断用户密码输入
    private boolean isPasswordValid(String password) {
        return password.length() >=3 ;
    }
    private boolean isNameValid(String name) {
        return name.length() > 8;
    }

    private void requestCheckUserLogin(String name ,String pass) {
        final HelpRetrofit helpRetrofit = new HelpRetrofit(NewsUri.BaseUrl);
        Call<String> call = helpRetrofit.connectHttp(name,pass);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = helpRetrofit.formatReturnData(response);
                if (res.equals("success")){
                      saveSp();
                      switchFragment();
                }else{
                    passET.setError(getString(R.string.error_incorrect_password));
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void requestCheckUsername(String name) {
        final HelpRetrofit helpRetrofit = new HelpRetrofit(NewsUri.BaseUrl);
        Call<String> call = helpRetrofit.connectHttpUsername(name);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = helpRetrofit.formatReturnData(response);
                if (res.equals("success")){

                }else{
                    nameET.setError("没有此用户");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void switchFragment() {
        switchFragment.setVisibility(View.VISIBLE);
        fg_qr_login.setVisibility(View.GONE);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.replace(R.id.fragment_container,loginFragment);
        islogin = 1;
        ft.commit();
    }

    private void saveSp( ){
        if(rememberSW.isChecked()) {
            SharedPreferences.Editor editor = saveUserlogin.edit();
            String name = nameET.getText().toString();
            name = name.trim();
            String pass =passET.getText().toString();
            pass = pass.trim();
            editor.putString("name",name);
            editor.putString("password", pass);
            editor.putBoolean("ischecked", rememberSW.isChecked());
            editor.commit();
        }else{
            SharedPreferences.Editor editor = saveUserlogin.edit();
            editor.clear();
            editor.commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
             case R.id.login_button:
                 checkUser();
                 break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentposition",islogin);
        super.onSaveInstanceState(outState);
    }
}
