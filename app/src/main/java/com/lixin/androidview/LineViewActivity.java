package com.lixin.androidview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lixin.customview.LineView;


public class LineViewActivity extends AppCompatActivity {


    LineView lineView;


    public static void start(Context context){
        context.startActivity(new Intent(context, LineViewActivity.class));
    }


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_view);
        lineView = findViewById(R.id.line_view);

        initData();
    }

    private void initData() {
        int[] data = new int[]{0,3,12,15,3,13,6};
         String[] labels = new String[]{"4.22","4.4","4.5","4.6","4.7","4.8","今天"};

        initView(data,labels);

    }

    private void initView(int[] data, String[] labels) {
        lineView.setLables(labels);
        lineView.setData(data);
    }
}
