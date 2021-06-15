package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TeacherPage extends AppCompatActivity {

    EditText et1,et2;
    TextView tv;
    Button btn1,btn2;
    FirebaseUser user;
    ListView lv;
    ArrayAdapter<String> adapter;
    DatabaseReference mDatabase;
     FirebaseHelper helper;

   public void addStudent(View view){
       lv.setVisibility(View.INVISIBLE);
       btn1.setVisibility(View.INVISIBLE);
       et1.setVisibility(View.VISIBLE);et2.setVisibility(View.VISIBLE); btn2.setVisibility(View.VISIBLE);
       btn2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()){
                   Toast.makeText(TeacherPage.this, "don't leave the fields empty", Toast.LENGTH_SHORT).show();
                   lv.setVisibility(View.VISIBLE);
                   btn1.setVisibility(View.VISIBLE);
                   et1.setVisibility(View.INVISIBLE);et2.setVisibility(View.INVISIBLE); btn2.setVisibility(View.INVISIBLE);
               }else{
                   lv.setVisibility(View.VISIBLE);
                   btn1.setVisibility(View.VISIBLE);
                   et1.setVisibility(View.INVISIBLE);et2.setVisibility(View.INVISIBLE); btn2.setVisibility(View.INVISIBLE);
                   Data data=new Data();
                   data.setName(et1.getText().toString());
                   data.setRollNumber(et2.getText().toString());
                   data.setClasses(0);
                   data.setPresent(0);
                   et1.setText("");
                   et2.setText("");
                 if(helper.save(data)){
                     Toast.makeText(TeacherPage.this, "Details saved", Toast.LENGTH_SHORT).show();
                 }else{
                     Toast.makeText(TeacherPage.this, "failed!", Toast.LENGTH_SHORT).show();
                 }

               }
             adapter.notifyDataSetChanged();
            Intent in=getIntent();
              finish();
              startActivity(in);
           }
       });
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_page);
        helper=new FirebaseHelper(mDatabase);
       user=FirebaseAuth.getInstance().getCurrentUser();
        Intent in=getIntent();
         helper.name=in.getStringExtra("value");
         tv=findViewById(R.id.textView7);
         tv.setText(helper.name);
        mDatabase=FirebaseDatabase.getInstance().getReference("Student/"+FirebaseAuth.getInstance().getUid());

        final Query query=mDatabase.child("SubDetails").child(helper.name);
        lv=findViewById(R.id.listview);

        final ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                helper.students.clear();
                if(snapshot.exists()){

                    for(DataSnapshot ds: snapshot.getChildren()){
                        String nam= Objects.requireNonNull(ds.getValue(Data.class)).getName();
                        helper.students.add(nam);
                        helper.rollNumber.add(Objects.requireNonNull(ds.getValue(Data.class)).getRollNumber());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(valueEventListener);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,helper.students);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in=new Intent(TeacherPage.this,attendance.class);
                in.putExtra("name",adapterView.getItemAtPosition(i).toString());
                in.putExtra("subName",helper.name);
                in.putExtra("rollNumber",helper.rollNumber.get(i));
                startActivity(in);
            }
        });

      et1=findViewById(R.id.subName);
      et2=findViewById(R.id.subCode);
      btn1=findViewById(R.id.button);
      btn2=findViewById(R.id.button2);
        et1.setVisibility(View.INVISIBLE);et2.setVisibility(View.INVISIBLE); btn2.setVisibility(View.INVISIBLE);

    }
}