package com.zf.richtext.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.zf.richtext.RichTextFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RichTextFragment fragment = (RichTextFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        findViewById(R.id.tvSave).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
            intent.putExtra("html", fragment.getHtml());
            startActivity(intent);
        });

        String html = getIntent().getStringExtra("html");
        if (!TextUtils.isEmpty(html)) {
            fragment.setHtml(html);
        }
    }
}
