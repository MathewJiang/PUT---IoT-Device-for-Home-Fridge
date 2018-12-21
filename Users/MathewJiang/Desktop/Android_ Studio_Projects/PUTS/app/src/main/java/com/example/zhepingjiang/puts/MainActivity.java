package com.example.zhepingjiang.puts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /*
     * sendStorageInfo:
     *
     * change from main activity to InfoDisplayAcitivity
     * when the user chooses to do so
     *
     */
    public void sendStorageInfo(View view) {
        EditText storageInfo_text = findViewById(R.id.StorageID_Text);
        String storageInfo_msg = storageInfo_text.getText().toString();

        Intent intent = new Intent(this, InfoDisplayActivity.class);
        intent.putExtra("STORAGEINFO_MSG", storageInfo_msg);

        startActivity(intent);
        return;
    }



}
