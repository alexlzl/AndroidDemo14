package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Android WebView 与 Javascript 交互。
 */
public class MainActivity extends ActionBarActivity {
    private WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);

        webView.setVerticalScrollbarOverlay(true);
        //设置WebView支持JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        String url = "file:///android_asset/Index.html";
        webView.loadUrl(url);

        //在js中调用本地java方法
        webView.addJavascriptInterface(new JsInterface(this), "AndroidWebView");

        //添加客户端支持
        webView.setWebChromeClient(new WebChromeClient());
    }

    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void showInfoFromJs(String name) {
            Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
        }
    }

    //在java中调用js代码
    public void sendInfoToJs(View view) {
        String msg = ((EditText) findViewById(R.id.input_et)).getText().toString();
        //调用js中的函数：showInfoFromJava(msg)
        webView.loadUrl("javascript:showInfoFromJava('" + msg + "')");
    }
}

