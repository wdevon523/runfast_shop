package com.gxuc.runfast.shop.activity.usercenter;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gxuc.runfast.shop.activity.ToolBarActivity;
import com.gxuc.runfast.shop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帮助中心
 */
public class HelpCenterActivity extends ToolBarActivity {

    @BindView(R.id.view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        ButterKnife.bind(this);
        initWebSetting();
        webView.loadUrl("http://120.77.70.27/iwapb/home/user/help.html");
    }

    private void initWebSetting() {
        WebSettings settings = webView.getSettings();
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
//        settings.setBuiltInZoomControls(false);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.setTag(null);
            webView.clearHistory();
            webView.destroy();
            webView = null;
        }
    }
}
