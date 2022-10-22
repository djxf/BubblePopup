package com.djxf.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_pup).setOnClickListener(view -> {
            BubblePopupWindow leftTopWindow = new BubblePopupWindow(MainActivity.this);
            View bubbleView = View.inflate(MainActivity.this, R.layout.layout_popup, null);
            TextView tvContent = (TextView) bubbleView.findViewById(R.id.tv_content);
            tvContent.setText("你发大你发大大你发大你发大你发大你发大大你发大你发大你发大你发大");
            leftTopWindow.setBubbleView(bubbleView); // 设置气泡内容
            leftTopWindow.show(view, Gravity.BOTTOM); // 显示弹窗
        });
    }
}