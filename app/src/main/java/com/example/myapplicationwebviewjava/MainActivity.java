package com.example.myapplicationwebviewjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import okhttp3.Call;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    private WebView webView;
    private String url = "http://189.240.30.58:810/ine/";

    private ValueCallback<Uri[]> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
        setListeners();
        changeActionBarColor();
    }

    public void changeActionBarColor() {
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF69B4"));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    public void initFields() {
        // TODO Auto-generated method stub

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowFileAccess(true);
    }

    public void setListeners() {
        // TODO Auto-generated method stub

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                webView.loadUrl("about:blank");

                view.clearHistory();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "shouldOverrideUrlLoading: request.getUrl(): " + request.getUrl());
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (urlShouldBeHandledByWebView(url)) {
                    return super.shouldInterceptRequest(view, request);
                }

                return handleRequestViaOkHttp(url);
            }

            private boolean urlShouldBeHandledByWebView(String url) {
                Log.d(TAG, "intercepting req....!!!: " + url);
                if (url.contains("vision.googleapis.com")) {
                    return false;
                }
                return true;
            }

            @NonNull
            private WebResourceResponse handleRequestViaOkHttp(@NonNull final String url) {
                try {
                    Log.d(TAG, "handleRequestViaOkHttp: url:" + url);
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    final Call call = client.newCall(request);
                    call.enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            // failure case
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // success case
                            Log.d(TAG, "onResponse " + url + ": " + response.body().string());
                            if (url.contains("vision.googleapis.com")) {
                                webView.post(() -> {
                                    Log.d(TAG, "run: ");
                                    webView.loadUrl("javascript:console.log(document.getElementById('demo').textContent);");

                                });
                            }
                        }
                    });

                    return null;
                } catch (Exception e) {
                    Log.e(TAG, "ERROR: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                Log.d(TAG, "onShowFileChooser: filePathCallback: " + filePathCallback.toString());
                mUploadMessage = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                return true;
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                Log.d(TAG, "onConsoleMessage: ");
                if (cmsg.message().contains("Cargando")) {
                    webView.post(() -> webView.loadUrl("javascript:console.log(document.getElementById('demo').textContent);"));
                } else {
                    Log.d(TAG, "onConsoleMessage: msg: " + cmsg.message());
                    if (cmsg.message().contains("Texto obtenido de la imagen")) {
                        startActivity(UbicacionesActivity.newIntent(getApplicationContext(), cmsg.message(), "9"));
                    }

                }
                return false;
            }

        });

        webView.loadUrl(url);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], intent = [" + intent + "]");
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            Uri[] uris = new Uri[1];
            uris[0] = result;
            mUploadMessage.onReceiveValue(uris);
            mUploadMessage = null;
        }
    }
}
