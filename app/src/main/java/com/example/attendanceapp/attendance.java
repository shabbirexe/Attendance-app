package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class attendance extends AppCompatActivity {
     Button absentButton,presentButton,manualButton;
     TextView tv1;
    int present, classes;
    EditText presentNum,classNum;
    String roll,name,subName;
    DatabaseReference db;
    Query query;
    boolean tr=false;
    public void visib(View view){
        if(tr){
            presentNum.setVisibility(View.INVISIBLE);
            classNum.setVisibility(View.INVISIBLE);
            presentNum.setText("");
            classNum.setText("");
            tr=false;
        }else{
            presentNum.setVisibility(View.VISIBLE);
            classNum.setVisibility(View.VISIBLE);
            tr=true;
    }}
    public void manual(View view){
       if(presentNum.getText().toString().isEmpty() || classNum.getText().toString().isEmpty()){
           return;
        }
        ValueEventListener vw=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    if(roll.equals(snapshot1.getValue(Data.class).getRollNumber()) && name.equals(snapshot1.getValue(Data.class).getName())){
                        db.child(snapshot1.getKey()).child("classes").setValue(Integer.parseInt(classNum.getText().toString()));

                        db.child(snapshot1.getKey()).child("present").setValue(Integer.parseInt(presentNum.getText().toString()));
                        present=Integer.parseInt(presentNum.getText().toString());
                        classes=Integer.parseInt(classNum.getText().toString());
                         presentNum.setText("");
                         classNum.setText("");
                        tv1.setText(name+" ("+roll+") attended "+ present+" classes out of "+classes);

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        query.addListenerForSingleValueEvent(vw);

    }
   public void present(View view){
       ValueEventListener vw=new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot snapshot1 : snapshot.getChildren()){

                   if(roll.equals(snapshot1.getValue(Data.class).getRollNumber()) && name.equals(snapshot1.getValue(Data.class).getName())){
                       db.child(snapshot1.getKey()).child("classes").setValue((Integer.parseInt(snapshot1.child("classes").getValue().toString())) + 1);
                       Toast.makeText(attendance.this, "present", Toast.LENGTH_SHORT).show();
                       db.child(snapshot1.getKey()).child("present").setValue((Integer.parseInt(snapshot1.child("present").getValue().toString())) + 1);
                       present=snapshot1.getValue(Data.class).getPresent();
                       classes=snapshot1.getValue(Data.class).getClasses();
                       classes++;
                       present++;
                       tv1.setText(name+" ("+roll+") attended "+ present+" classes out of "+classes);
                       break;
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       };

     query.addListenerForSingleValueEvent(vw);


   }
   public void absent(View view){
       ValueEventListener vw=new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot snapshot1 : snapshot.getChildren()){

                   if(roll.equals(snapshot1.getValue(Data.class).getRollNumber()) && name.equals(snapshot1.getValue(Data.class).getName())){
                       db.child(snapshot1.getKey()).child("classes").setValue((Integer.parseInt(snapshot1.child("classes").getValue().toString())) + 1);
                       Toast.makeText(attendance.this, "absent", Toast.LENGTH_SHORT).show();
                       present=snapshot1.getValue(Data.class).getPresent();
                       classes=snapshot1.getValue(Data.class).getClasses();
                       classes++;

                       tv1.setText(name+" ("+roll+") attended "+ present+" classes out of "+classes);
                       break;
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       };
       query.addListenerForSingleValueEvent(vw);

   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
       presentNum=findViewById(R.id.editTextNumber);
       classNum=findViewById(R.id.editTextNumber2);
       presentNum.setVisibility(View.INVISIBLE);
       classNum.setVisibility(View.INVISIBLE);
        Intent in=getIntent();
     name= in.getStringExtra("name");
     roll=in.getStringExtra("rollNumber");
      subName=in.getStringExtra("subName");
        Toast.makeText(this, name+" "+roll+" "+subName, Toast.LENGTH_SHORT).show();
       tv1=findViewById(R.id.textView);
       absentButton=findViewById(R.id.button5);
       presentButton=findViewById(R.id.button6);
       manualButton=findViewById(R.id.button7);
      db= FirebaseDatabase.getInstance().getReference("Student/"+ FirebaseAuth.getInstance().getUid()+"/SubDetails/"+subName);
      query= db;
        ValueEventListener vw=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    if(roll.equals(snapshot1.getValue(Data.class).getRollNumber()) && name.equals(snapshot1.getValue(Data.class).getName())){

                        present=snapshot1.getValue(Data.class).getPresent();
                        classes=snapshot1.getValue(Data.class).getClasses();

                        tv1.setText(name+" ("+roll+") attended "+ present+" classes out of "+classes);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        query.addListenerForSingleValueEvent(vw);


       tv1.setText(name+" ("+roll+") attended "+ present+" classes out of "+classes);

    }
}