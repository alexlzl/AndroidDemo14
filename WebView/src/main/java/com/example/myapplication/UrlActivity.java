package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.KeyEvent.KEYCODE_BACK;

public class UrlActivity extends AppCompatActivity {
    private ScrollWebView mWebView;
    private TextView mTextView;
    private TextView mTitle;
    private LinearLayout mLinearLayout;
    private TextView mScrolly;
    private TextView mWebViewHeight;
    private TextView mWebViewContentHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        /**
         * 如何避免WebView内存泄露？
         *
         * 1 不在xml中定义 Webview ，而是在需要的时候在Activity中创建，并且Context使用 getApplicationgContext()
         *
         * 2 在 Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空。
         */
        mLinearLayout = (LinearLayout) findViewById(R.id.parent);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new ScrollWebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mLinearLayout.addView(mWebView);
//        mWebView = (WebView) findViewById(R.id.webview);
        mTextView = (TextView) findViewById(R.id.textview);
        mTitle = (TextView) findViewById(R.id.title);
        mScrolly = (TextView) findViewById(R.id.scrolly);
        mWebViewHeight = (TextView) findViewById(R.id.webview_height);
        mWebViewContentHeight = (TextView) findViewById(R.id.webview_content_height);
        WebSettings settings = mWebView.getSettings();
        ////如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        //清除网页访问留下的缓存
        //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
        mWebView.clearCache(true);
        //清除当前webview访问的历史记录
        //只会webview访问历史记录里的所有记录除了当前访问记录
        mWebView.clearHistory();
        //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        mWebView.clearFormData();
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        //支持插件
        // settings.setPluginsEnabled(true);
        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        settings.setAllowFileAccess(true); //设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= 24) {
            /**
             * 作用：处理各种通知 & 请求事件
             常见方法：
             常见方法1：shouldOverrideUrlLoading()
             作用：打开网页时不调用系统浏览器， 而是在本WebView中显示；在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
             */
            mWebView.setWebViewClient(new WebViewClient() {
                /**
                 * Describe:作用：打开网页时不调用系统浏览器， 而是在本WebView中显示；在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
                 *
                 * Author:
                 *
                 * Time:2017/10/19 19:55
                 */
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                是否使用第三方浏览器 返回true不调用 返回false调用
                    return true;
                }
                /**
                 * Describe:这个方法中,该方法用于根据请求去获取响应,如果返回null,那么android会根据请求去获取响应并返回,但是如果你重写了该方法并返回了响应,那么WebView就会使用你的响应数据.其中WebResourceRequest封装了请求,WebResourceResponse封装了响应.


                 *
                 * Author:
                 *
                 * Time:2017/10/19 20:12
                 */
//                @Override
//                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                    return getWebRes();
//                }

                /**
                 * Describe:开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
                 *
                 * Author:
                 *
                 * Time:2017/10/19 19:54
                 */
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Toast.makeText(UrlActivity.this, "onPageStarted", Toast.LENGTH_SHORT).show();
                }

                /**
                 * Describe:在页面加载结束时调用。我们可以关闭loading 条，切换程序动作
                 *
                 * Author:
                 *
                 * Time:2017/10/19 19:54
                 */
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Toast.makeText(UrlActivity.this, "onPageFinished", Toast.LENGTH_SHORT).show();
                }

                /**
                 * Describe:在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
                 *
                 * Author:
                 *
                 * Time:2017/10/19 19:56
                 */
                @Override
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
                    Toast.makeText(UrlActivity.this, "onLoadResource", Toast.LENGTH_SHORT).show();
                }

                /**
                 * Describe:作用：加载页面的服务器出现错误时（如404）调用。
                 App里面使用webview控件的时候遇到了诸如404这类的错误的时候，若也显示浏览器里面的那种错误提示页面就显得很丑陋了，那么这个时候我们的app就需要加载一个本地的错误提示页面，即webview如何加载一个本地的页面
                 *
                 * Author:
                 *
                 * Time:2017/10/19 20:01
                 */
                @TargetApi(24)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    switch (error.getErrorCode()) {
                        case 500:
                            Toast.makeText(UrlActivity.this, "onReceivedError", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }

                /**
                 * Describe:作用：处理https请求
                 webView默认是不处理https请求的，页面显示空白，需要进行如下设置：
                 *
                 * Author:
                 *
                 * Time:2017/10/19 20:03
                 */
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    handler.proceed();    //表示等待证书响应
                    // handler.cancel();      //表示挂起连接，为默认方式
                    // handler.handleMessage(null);    //可做其他处理
                }
            });
        } else {
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                是否使用第三方浏览器 返回true不调用 返回false调用
                    mWebView.loadUrl(url);
                    return true;
                }

                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                    return getWebRes();
                }
            });
        }

        /**
         * WebChromeClient类
         作用：辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
         */
        mWebView.setWebChromeClient(new WebChromeClient() {
            /**
             * 作用：获得网页的加载进度并显示
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress <= 100) {
                    String progress = newProgress + "%";
                    mTextView.setText(progress);
                }
            }

            /**
             * 作用：获取Web页中的标题
             每个网页的页面都有一个标题，比如www.baidu.com这个页面的标题即“百度一下，你就知道”，那么如何知道当前webview正在加载的页面的title并进行设置呢？
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle.setText(title);
            }

            /**
             * 支持javascript的警告框
             一般情况下在 Android 中为 Toast，在文本里面加入\n就可以换行
             * @param view
             * @param url
             * @param message
             * @param result
             * @return
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                return super.onJsAlert(view, url, message, result);
                new AlertDialog.Builder(UrlActivity.this)
                        .setTitle("JsAlert")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//                return super.onJsConfirm(view, url, message, result);
                new AlertDialog.Builder(UrlActivity.this)
                        .setTitle("JsConfirm")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
                // 返回布尔值：判断点击时确认还是取消
                // true表示点击了确认；false表示点击了取消；
                return true;


            }

            /**
             * 作用：支持javascript输入框
             点击确认返回输入框中的值，点击取消返回 null。
             * @param view
             * @param url
             * @param message
             * @param defaultValue
             * @param result
             * @return
             */
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
//                return super.onJsPrompt(view, url, message, defaultValue, result);
                final EditText et = new EditText(UrlActivity.this);
                et.setText(defaultValue);
                new AlertDialog.Builder(UrlActivity.this)
                        .setTitle(message)
                        .setView(et)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm(et.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();

                return true;

            }
        });
        mWebView.loadUrl("http://www.jianshu.com/p/08920c2bb128");
        webViewScroolChangeListener();
    }

    /**
     * Describe:判断WebView是否已经滚动到页面底端 或者 顶端:
     * getScrollY() //方法返回的是当前可见区域的顶端距整个页面顶端的距离,也就是当前内容滚动的距离.
     * getHeight()或者getBottom() //方法都返回当前WebView 这个容器的高度
     * getContentHeight() 返回的是整个html 的高度,但并不等同于当前整个页面的高度,因为WebView 有缩放功能, 所以当前整个页面的高度实际上应该是原始html 的高度再乘上缩放比例. 因此,更正后的结果,准确的判断方法应该是：
     * <p>
     * Author:
     * <p>
     * Time:2017/10/21 14:42
     */
    private void webViewScroolChangeListener() {
        mWebView.setOnCustomScroolChangeListener(new ScrollWebView.ScrollInterface() {
            @Override
            public void onSChanged(int l, int t, int oldl, int oldt) {
                //WebView的总高度
                float webViewContentHeight = mWebView.getContentHeight() * mWebView.getScale();
                //WebView的现高度
                float webViewCurrentHeight = (mWebView.getHeight() + mWebView.getScrollY());
                System.out.println("webViewContentHeight=" + webViewContentHeight);
                System.out.println("webViewCurrentHeight=" + webViewCurrentHeight);
                mScrolly.setText("getScrollY===" + mWebView.getScrollY() + "");
                mWebViewHeight.setText("getHeight==" + mWebView.getHeight() + "");
                mWebViewContentHeight.setText("getContentHeight==" + mWebView.getContentHeight() + "");
                if ((webViewContentHeight - webViewCurrentHeight) == 0) {
                    System.out.println("WebView滑动到了底端");
                    Toast.makeText(UrlActivity.this, "WebView滑动到了底端", Toast.LENGTH_SHORT).show();
                }
                if (mWebView.getScrollY() == 0) {
                    Toast.makeText(UrlActivity.this, "WebView滑动到了顶端", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private WebResourceResponse getWebRes() {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
//            URL url = new URL("http://m.gome.com.cn/shop_zdy-80011410_6.html");
            URL url = new URL("https://www.baidu.com");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
//            连接主机超时时间
            httpURLConnection.setConnectTimeout(10 * 1000);
//            设置从主机读取数据超时
            httpURLConnection.setReadTimeout(40 * 1000);
            bufferedReader = new BufferedReader(new
                    InputStreamReader(httpURLConnection
                    .getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        WebResourceResponse webResourceResponse = null;
        webResourceResponse = new WebResourceResponse("text/html",
                "utf-8",
                new ByteArrayInputStream(stringBuilder.toString().getBytes()));
        return webResourceResponse;
    }

    /**
     * Describe:问题：在不做任何处理前提下 ，浏览网页时点击系统的“Back”键,整个 Browser 会调用 finish()而结束自身
     * 目标：点击返回后，是网页回退而不是推出浏览器
     * 解决方案：在当前Activity中处理并消费掉该 Back 事件
     * <p>
     * Author:
     * <p>
     * Time:2017/10/19 17:26
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);


    }

    /**
     * 避免内存泄漏
     * 在 Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空。
     */
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


}
