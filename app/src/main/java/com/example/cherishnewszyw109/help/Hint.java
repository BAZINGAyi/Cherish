package com.example.cherishnewszyw109.help;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by yuwei on 2016/12/19.
 */
public class Hint {
    public void ToastMessage(View view, final Activity appCompatActivity,String string){
        final Snackbar snackbar = Snackbar.make(view, string, Snackbar.LENGTH_SHORT);
        snackbar.show();
        snackbar.setAction("是", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                appCompatActivity.finish();
            }
        });
    }
}
