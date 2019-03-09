package com.dstore.acer.dstore;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageActivity extends AppCompatActivity {

    private static final int REQ_IMG_CODE = 786;

    private StorageReference storage_ref;
    private DatabaseReference db_ref;
    private ImageView uploadedImageshow;
    private TextView imgLoading;
    private Button imgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        storage_ref = FirebaseStorage.getInstance().getReference().child("Images");
        db_ref = FirebaseDatabase.getInstance().getReference("Images");

        uploadedImageshow = (ImageView)findViewById(R.id.uploaded_image_show);
        imgLoading = (TextView)findViewById(R.id.img_loading);
        imgBtn = (Button)findViewById(R.id.img_btn);
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

            Uri selectedImage = data.getData();

            final String name = db_ref.push().getKey();
            final BitmapStore bitmapStore = new BitmapStore(name, selectedImage);

            imgLoading.setVisibility(View.VISIBLE);
            imgBtn.setClickable(false);

            storage_ref.child(bitmapStore.getName())
                    .putFile(bitmapStore.getUri())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            final Uri uri = taskSnapshot.getDownloadUrl();

                            db_ref.child(db_ref.push().getKey()).setValue(name, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError == null){
                                        Toast.makeText(ImageActivity.this, "Uploaded ...", Toast.LENGTH_SHORT).show();
                                        imgLoading.setVisibility(View.INVISIBLE);
                                        imgBtn.setClickable(true);

                                        Glide.with(getApplicationContext())
                                                .load(uri)
                                                .into(uploadedImageshow);
                                    }
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImageActivity.this, "Image not uploaded ...", Toast.LENGTH_SHORT).show();
                            imgLoading.setVisibility(View.INVISIBLE);
                            imgBtn.setClickable(true);

                        }
                    });

        }else{
            Toast.makeText(this, "you haven't picked image ...", Toast.LENGTH_SHORT).show();
        }
    }

    public void getImageFromFirebase(View view) {

        Intent viewImgIntent = new Intent(this, ViewImages.class);
        startActivity(viewImgIntent);

    }
}


