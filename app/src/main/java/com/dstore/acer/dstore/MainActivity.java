package com.dstore.acer.dstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void uploadText(View view) {
        Intent textIntent = new Intent(this, TextDataActivity.class);
        startActivity(textIntent);
//        finish();
    }

    public void uploadImage(View view) {

        Intent imageIntent = new Intent(this, ImageActivity.class);
        startActivity(imageIntent);
//        finish();
    }
}
