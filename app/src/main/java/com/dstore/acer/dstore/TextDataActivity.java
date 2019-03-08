package com.dstore.acer.dstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TextDataActivity extends AppCompatActivity {

    private EditText inputText;
    private DatabaseReference db_ref;
    private ImageButton send_btn;
    private ArrayList<String> textStores;
    private TextView loadedText;
    private ListView textListView;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_data);

        inputText = (EditText)findViewById(R.id.input_text);
        send_btn = (ImageButton)findViewById(R.id.send_btn);
        loadedText = (TextView)findViewById(R.id.loaded_text);
        textListView = (ListView) findViewById(R.id.text_list_view);
        db_ref = FirebaseDatabase.getInstance().getReference("TextStore");
        textStores = new ArrayList<>();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, textStores);


    }

    @Override
    protected void onStart() {
        super.onStart();

        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textStores.clear();
                loadedText.setVisibility(View.INVISIBLE);
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    TextStore textStore = snapshot.getValue(TextStore.class);
                    textStores.add(textStore.getText());
                    textListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendText(View view) {

        if(!inputText.getText().toString().equals("")){
            String text = inputText.getText().toString();
            inputText.setText("");
            inputText.setHint("processing ...");
            send_btn.setClickable(false);

            TextStore textStore = new TextStore(text);

            db_ref.child(db_ref.push().getKey()).setValue(textStore, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError == null){
                        Toast.makeText(TextDataActivity.this, "Saved ...", Toast.LENGTH_SHORT).show();
                        inputText.setHint("Enter Text");
                        send_btn.setClickable(true);
                    }else {
                        Toast.makeText(TextDataActivity.this, "Not saved ...", Toast.LENGTH_SHORT).show();
                        inputText.setHint("Enter Text");
                        send_btn.setClickable(true);
                    }
                }
            });


        }else {
            Toast.makeText(this, "Please enter some text ...", Toast.LENGTH_SHORT).show();
        }

    }
}
