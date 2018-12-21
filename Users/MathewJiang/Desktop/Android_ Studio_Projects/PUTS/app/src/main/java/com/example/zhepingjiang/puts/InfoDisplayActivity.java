package com.example.zhepingjiang.puts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_display);

        Intent intent = getIntent();
        String storageinfo_msg = intent.getStringExtra("STORAGEINFO_MSG");

        TextView textView = findViewById(R.id.DisplayTextView1);
        textView.setText(storageinfo_msg);


    }
}
