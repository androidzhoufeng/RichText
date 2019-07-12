package com.zf.richtext.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        String html_content = getIntent().getStringExtra("html");
        webView.loadDataWithBaseURL("", html_content, "text/html", "UTF-8", null);

        findViewById(R.id.tvSave).setOnClickListener(v -> {
            Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
            intent.putExtra("html", html_content);
            startActivity(intent);
        });
    }
}
