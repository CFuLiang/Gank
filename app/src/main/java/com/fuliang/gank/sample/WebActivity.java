package com.fuliang.gank.sample;

import android.content.ClipboardManager;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fuliang.gank.sample.model.ResultsList;
import com.fuliang.gank.sample.stroage.DataCache;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

/**
 * Created by lfu on 2017/6/13.
 */

public class WebActivity extends AppCompatActivity {

    private ResultsList model;
    private WebView webView;
    private String url;
    private ProgressBar myProgressBar;
    private ArrayList<ResultsList> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        webView = (WebView)findViewById(R.id.web_view);
        myProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        myProgressBar.setProgressDrawable(this.getDrawable(R.drawable.progress_style));
        model = (ResultsList) getIntent().getSerializableExtra("model");
        url = model.url;
        getUrl();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUrl() {
        WebSettings webSettings = webView.getSettings();
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //不使用cache 全部从网络上获取
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //无模式选择，通过setAppCacheEnabled(boolean flag)设置是否打开。默认关闭，即，H5的缓存无法使用。
        webSettings.setAppCacheEnabled(true);
        //设置支持HTML5本地存储
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);
        webView.loadUrl(url);
        //设置Web视图
        webView.setWebViewClient(new webViewClient());
        webView.setWebChromeClient(new webChromeClient());
        ImageView icon = new ImageView(this);
        icon.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.plane));
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this).setContentView(icon).build();
        SubActionButton.Builder itemBulider = new SubActionButton.Builder(this);

        ImageView share = new ImageView(this);
        share.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.share));
        SubActionButton shareButton = itemBulider.setContentView(share).build();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getWindow().getDecorView().findViewById(R.id.main_content),"已复制地址", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        ImageView copy = new ImageView(this);
        copy.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.copy));
        SubActionButton copyButton = itemBulider.setContentView(copy).build();
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getWindow().getDecorView().findViewById(R.id.main_content),"已复制地址", BaseTransientBottomBar.LENGTH_SHORT).show();
                ClipboardManager copy = (ClipboardManager) WebActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText(url);
            }
        });

        ImageView collect = new ImageView(this);
        collect.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.collect));
        SubActionButton collectButton = itemBulider.setContentView(collect).build();
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list = DataCache.instance.getCacheData("fuliang","collect");
                if (list == null){
                    list = new ArrayList<>();
                }
                if (list.contains(model)){
                    Snackbar.make(getWindow().getDecorView().findViewById(R.id.main_content),"已添加到我的收藏", BaseTransientBottomBar.LENGTH_SHORT).show();
                }else {
                    list.add(model);
                    if (DataCache.instance.saveCacheData("fuliang","collect",list)){
                        Snackbar.make(getWindow().getDecorView().findViewById(R.id.main_content),"已添加到我的收藏", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }else {
                        Snackbar.make(getWindow().getDecorView().findViewById(R.id.main_content),"添加收藏夹失败", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(copyButton)
                .addSubActionView(collectButton)
                .addSubActionView(shareButton)
                .attachTo(actionButton)
                .build();
    }


    private class webViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    //Web视图
    private class webChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (getSupportActionBar() != null){
                getSupportActionBar().setTitle(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                myProgressBar.setVisibility(View.INVISIBLE);
            } else {
                if (View.INVISIBLE == myProgressBar.getVisibility()) {
                    myProgressBar.setVisibility(View.VISIBLE);
                }
                myProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
