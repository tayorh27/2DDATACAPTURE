package com.datacapture.a2ddatacapture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView tv1,tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tv1 = (TextView)findViewById(R.id.tv_header);
        tv2 = (TextView)findViewById(R.id.tv_footer);

        String text1 = "2D Indigenous Data Capture\nBuilt for Easy Capture of 2D Face\nand Respective Data";
        String text2 = "Created By:\nAdedoyin\nYakubu\nDominic\nShinna &\nMosad\nSupervisor: Dr. Popoola";

        tv1.setText(text1);
        tv2.setText(text2);
    }
}
