package com.gxuc.runfast.shop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gxuc.runfast.shop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Devon on 2018/1/3.
 */

public class WebActivity extends ToolBarActivity {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        setWebViewSetting();
        initView();
        initData();
    }

    private void initView() {
        String url = getIntent().getStringExtra("url");
//        WebSettings settings = webview.getSettings();
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setJavaScriptEnabled(true);

        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initData() {

    }

    private void setWebViewSetting() {
        WebSettings settings = webview.getSettings();
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptEnabled(true);
    }

}
