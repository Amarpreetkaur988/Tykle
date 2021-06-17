package com.tykle.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tykle.R;

public class TermAndConditionActivity extends AppCompatActivity {


    private WebView webView;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);

        webView = findViewById(R.id.webView);


        pd = new ProgressDialog(TermAndConditionActivity.this);
        pd.setMessage("Please wait Loading...");
        pd.show();
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("http://13.59.137.174/tykle/index.php/api/user/termsAndConditions");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }
}
