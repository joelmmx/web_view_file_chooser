package com.example.myapplicationwebviewjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import okhttp3.Call;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    }

    public void initFields() {
        // TODO Auto-generated method stub

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowFileAccess(true);
    }

    public void setListeners() {
        // TODO Auto-generated method stub

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                webView.loadUrl("about:blank");

                view.clearHistory();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG,"shouldOverrideUrlLoading: request.getUrl(): "+ request.getUrl());
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
                Log.d(TAG,"intercepting req....!!!: "+ url);
                if(url.contains("ine/ajax-loader.gif")){
                    return false;
                }
                return false;
            }

            @NonNull
            private WebResourceResponse handleRequestViaOkHttp(@NonNull final String url)  {
                try {
                    Log.d(TAG, "handleRequestViaOkHttp: url:"+url);
                    // On Android API >= 21 you can get request method and headers
                    // As I said, we need to only display "simple" page with resources
                    // So it's GET without special headers
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
                            Log.d(TAG, "onResponse "+url+": "+response.body().string());
                        }
                    });

//                    final Call call = client.newCall(new Request.Builder()
//                            .url(url)
//                            .build()
//                    );
//                    final Response response = call.execute();
//                    final Call call = client.newCall(request);
                   /* final Response response = call.execute();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = response.body().byteStream().read(buffer)) > -1 ) {
                        for(int i=0;i<buffer.length;i++){
                            Log.d(TAG, "handleRequestViaOkHttp: (char)buffer[i]: "+(char)buffer[i]);
                        }
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();

                    return new WebResourceResponse(
                            response.header("content-type", "text/plain"), // You can set something other as default content-type
                            response.header("content-encoding", "utf-8"),  // Again, you can set another encoding as default
                            new ByteArrayInputStream(baos.toByteArray())
                    );*/
//                   Thread.sleep(10000);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {

            }
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                Log.d(TAG, "onShowFileChooser: filePathCallback: "+filePathCallback.toString());
                mUploadMessage = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                return true;
            }

        });

        webView.loadUrl(url);

//        final MyJavaScriptInterface myJavaScriptInterface
//                = new MyJavaScriptInterface(this);
//        webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        if (webView.canGoBack() == true) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


   /* public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            // webView.loadUrl("javascript:document.getElementById(\"Button3\").innerHTML = \"bye\";");
        }

        @JavascriptInterface
        public void openAndroidDialog() {
            AlertDialog.Builder myDialog
                    = new AlertDialog.Builder(MainActivity.this);
            myDialog.setTitle("DANGER!");
            myDialog.setMessage("You can do what you want!");
            myDialog.setPositiveButton("ON", null);
            myDialog.show();
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            Uri[] uris = new Uri[1];
            uris[0] = result;
            Log.d(TAG, "onActivityResult: result: "+result);
            mUploadMessage.onReceiveValue(uris);
            mUploadMessage = null;
        }
    }
}
