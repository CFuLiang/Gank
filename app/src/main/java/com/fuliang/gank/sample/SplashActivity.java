package com.fuliang.gank.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by lfu on 2017/6/14.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toMain();
                finish();
            }
        },2000);
    }

    private void toMain(){
        startActivity(new Intent(SplashActivity.this,SampleActivity.class));
    }
}
