package com.djxf.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djxf.bubblepopup.BubblePopupWindowCustom;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup root = findViewById(R.id.rootView);
        findViewById(R.id.bt_pup).setOnClickListener(view -> {
            View view1 = View.inflate(getBaseContext(), R.layout.test_1, null);
            TextView textView = view1.findViewById(R.id.tv_content);
            textView.setText("fdasfewfsdafaewfwe");
            BubblePopupWindowCustom bubblePopupWindowCustom = new BubblePopupWindowCustom.Builder(getApplicationContext(), view1, view)
                                                                .bubbleOffset(100f)
                                                                .gravity(Gravity.BOTTOM)
                                                                .build();
            bubblePopupWindowCustom.show();
        });
    }
}