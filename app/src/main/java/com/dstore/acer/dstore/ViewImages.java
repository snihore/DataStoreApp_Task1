package com.dstore.acer.dstore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewImages extends AppCompatActivity {

    private StorageReference storage_ref;
    private DatabaseReference db_ref;
    private ListView imgShowList;
    private ArrayList<byte[]> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images);

        storage_ref = FirebaseStorage.getInstance().getReference().child("Images");
        db_ref = FirebaseDatabase.getInstance().getReference("Images");
        imgShowList = (ListView)findViewById(R.id.img_show_list);
        images = new ArrayList();

        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    final String name = snapshot.getValue(String.class);

                    final long ONE_MEGABYTE = 1024 * 1024;
                    storage_ref.child(name)
                            .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Data for "images/island.jpg" is returns, use this as needed
                            images.add(bytes);
                            Log.i("BYTES###"+name, images.toString());
                            imgShowList.setAdapter(new CustomListView());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Log.i("ERROR", exception.getMessage());
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class CustomListView extends BaseAdapter{

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_list_layout, viewGroup, false);
            ImageView img = (ImageView) view.findViewById(R.id.img_set);
            Bitmap b = BitmapFactory.decodeByteArray(images.get(i), 0, images.get(i).length);
            img.setImageBitmap(b);
            return view;
        }
    }
}
