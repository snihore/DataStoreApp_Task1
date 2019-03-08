package com.dstore.acer.dstore;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageActivity extends AppCompatActivity {

    private static final int REQ_IMG_CODE = 786;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
    }

    public void uploadImageFromDevice(View view) {
        try{
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, REQ_IMG_CODE);

        }catch(Exception e){
            Log.i("Error",e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_IMG_CODE && resultCode == RESULT_OK && null != data) {

            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                Toast.makeText(this, imageBitmap.toString(), Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong ...", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "you haven't picked image ...", Toast.LENGTH_SHORT).show();
        }
    }
}
