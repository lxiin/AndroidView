package com.lixin.androidview;

import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lixin.customview.dialog.LoadingDialog;
import com.lixin.customview.statelayout.StateFrameLayout;

public class MainActivity extends AppCompatActivity {



    //1

    //22222



    //3333

    StateFrameLayout stateFrameLayout;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateFrameLayout = findViewById(R.id.state_layout);
        showViewState();

        stateFrameLayout.setOnReloadListener(new StateFrameLayout.OnReloadListener() {
            @Override
            public void onReload(View view) {
                i++;
                showViewState();
            }
        });


    }

    private void showViewState() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (i == 0){
//                    stateFrameLayout.showErrorView();
//                }else if (i == 1){
//                    stateFrameLayout.showNetErrorView();
//                }else if (i == 2){
//                    stateFrameLayout.showEmptyView();
//                }else{
//                    stateFrameLayout.showContentView();
//                }
//            }
//        },800);
        stateFrameLayout.showContentView();
    }


    public void showLoadingClick(View view){
        final LoadingDialog loadingDialog = LoadingDialog.show(this,"加载中...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        },1500);
    }

    public void showLineViewClick(View view){
        LineViewActivity.start(MainActivity.this);
    }

}
