package com.example.cherishnewszyw109.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cherishnewszyw109.R;
import com.example.cherishnewszyw109.bean.NewsUri;
import com.example.cherishnewszyw109.help.HelpRetrofit;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yuwei on 2016/12/17.
 */
public class LoginFragment extends Fragment {
    SharedPreferences saveUserlogin;
    private Button startQr;
    private TextView disResult;
    private String username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_replace_qr, container, false);
        initView(view,savedInstanceState);
        initOnclick();
        getUsername();
        return view;
    }

    private void initOnclick() {
        startQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
            }
        });
    }

    private void initView(View view,Bundle savedInstanceState) {
        startQr = (Button) view.findViewById(R.id.QrButon);
        disResult = (TextView)view.findViewById(R.id.dis_QrButon);
        if (savedInstanceState!=null){
            startQr.setVisibility(View.GONE);
            disResult.setVisibility(View.VISIBLE);
            disResult.setText(savedInstanceState.getString("loginweb"));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String result = bundle.getString("result");
            final Snackbar snackbar = Snackbar.make(getView(), "确认登录?", Snackbar.LENGTH_SHORT);
            snackbar.show();
            snackbar.setAction("是", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                    doRequestLogin(result);
                }
            });
        }
    }

    private void doRequestLogin( String result) {
        final HelpRetrofit helpRetrofit = new HelpRetrofit(NewsUri.BaseUrl);
        Call<String> call = helpRetrofit.connectHttpLogin(username,result);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = helpRetrofit.formatReturnData(response);
                if (res.equals("success")){
                    startQr.setVisibility(View.GONE);
                    disResult.setVisibility(View.VISIBLE);
                }else{
                 //   passET.setError(getString(R.string.error_incorrect_password));
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getUsername() {
        saveUserlogin = getActivity().getSharedPreferences("LoginInfo",getActivity().MODE_PRIVATE); //保存到名为LoginInfo的文件下，模式为私有
        username = saveUserlogin.getString("name",null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String res = disResult.getText().toString();
        if (res!=null)
        outState.putString("loginweb",res);
        super.onSaveInstanceState(outState);
    }
}
