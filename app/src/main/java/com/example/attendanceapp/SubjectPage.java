package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SubjectPage extends AppCompatActivity {
     EditText et1,et2;
     Button btn1,btn2,btn3;
     ListView lv;
     subfireHelper helper;
    DatabaseReference db;
     ArrayAdapter<String> adapter;
    ArrayList<String> subject=new ArrayList<>();;
    public void signOut(View view){
        AlertDialog.Builder abd=new AlertDialog.Builder(this);

        abd.setMessage("are you sure you want to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog=abd.create();
        alertDialog.show();

    }

    public void addStudent(View view){
        lv.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.VISIBLE); et1.setVisibility(View.VISIBLE);et2.setVisibility(View.VISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        et1.setVisibility(View.VISIBLE);et2.setVisibility(View.VISIBLE); btn2.setVisibility(View.VISIBLE);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()){
                    Toast.makeText(SubjectPage.this, "don't leave the fields empty", Toast.LENGTH_SHORT).show();
                    lv.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.INVISIBLE);
                    et1.setVisibility(View.INVISIBLE);
                    et2.setVisibility(View.INVISIBLE);
                }else{
                    lv.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.INVISIBLE);
                    et1.setVisibility(View.INVISIBLE);
                    et2.setVisibility(View.INVISIBLE);


                   boolean tr= helper.save(et1.getText().toString()+ " ("+et2.getText().toString()+ ")");

                   if( tr){
                       Toast.makeText(SubjectPage.this, "Data saved", Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(SubjectPage.this, "failed!", Toast.LENGTH_SHORT).show();
                   }
                    et1.setText("");
                    et2.setText("");
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_page);
        ImageView iv=findViewById(R.id.imageView);
       btn3=findViewById(R.id.buttonn3);
        TextView tv232=findViewById(R.id.textView4);
        tv232.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
         Uri uri=FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

                 Glide.with(this)
                 .load(uri)// the uri you got from Firebase

                 .into(iv); //Your imageView variable
         db= FirebaseDatabase.getInstance().getReference("Student/"+FirebaseAuth.getInstance().getUid());
        final Query query=db.child("SubList");
         helper=new subfireHelper(db);
        et1=findViewById(R.id.subName);et2=findViewById(R.id.subCode);
        lv=(ListView) findViewById(R.id.listview);
        et1.setVisibility(View.INVISIBLE);
        et2.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.VISIBLE);
        btn1=findViewById(R.id.button);
        btn2=findViewById(R.id.button2);
         et1.setVisibility(View.INVISIBLE);et2.setVisibility(View.INVISIBLE); btn2.setVisibility(View.INVISIBLE);

         final ValueEventListener valueEventListener=new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 subject.clear();
                 if(snapshot.exists()){
                     for(DataSnapshot ds: snapshot.getChildren()){
                         String name=ds.getValue(String.class);

                         subject.add(name);
                     }
                     adapter.notifyDataSetChanged();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         };
         query.addValueEventListener(valueEventListener);
        adapter=new ArrayAdapter<>(SubjectPage.this,android.R.layout.simple_list_item_1,subject);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                Intent in=new Intent(getApplicationContext(),TeacherPage.class);

                in.putExtra("value", adapterView.getItemAtPosition(i).toString());
                    startActivity(in);
            }
        });
    }
}