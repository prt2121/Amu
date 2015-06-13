package com.prt2121.amu.ui;

import com.prt2121.amu.R;
import com.prt2121.amu.util.FlowUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    public static final String TAG = PrivacyPolicyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        WebView webView = (WebView) findViewById(R.id.privacyPolicyWebview);
        webView.loadUrl("file:///android_asset/html/privacy-policy.html");
        findViewById(R.id.acceptButton).setOnClickListener(
                v -> {
                    FlowUtil.accecptPrivacyPolicy(PrivacyPolicyActivity.this, TAG);
                    Intent intent = new Intent(PrivacyPolicyActivity.this, MaterialTypeFilterActivity.class);
                    PrivacyPolicyActivity.this.startActivity(intent);
                    PrivacyPolicyActivity.this.finish();
                });
    }

}
