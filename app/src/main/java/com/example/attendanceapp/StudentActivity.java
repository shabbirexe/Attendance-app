package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

  EditText et1;
  ListView lv;
  ArrayList<String> datat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        lv=findViewById(R.id.listview);
         et1=findViewById(R.id.editTextNumber3);
         datat=new ArrayList<>();
         final miscData ddd=new miscData();
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Student");
        final Query query=db;
        final ArrayAdapter<String> adapter =new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datat);
        lv.setAdapter(adapter);
        Button btn=findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StudentActivity.this, "wait while we fetch data", Toast.LENGTH_SHORT).show();
                datat.clear();
                ValueEventListener vw=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ss:snapshot.getChildren()){
                            for(DataSnapshot ss1: ss.child("SubDetails").getChildren()){
                                for(DataSnapshot ss2:ss1.getChildren()){
                                    String name= ss2.getValue(Data.class).getRollNumber();
                                    if(name.equals(et1.getText().toString())){
                                        datat.add(ss1.getKey());
                                        ddd.listy.add(ss2.getValue(Data.class));
                                        break;
                                    }
                                }
                            }
                        }
                        if(datat.isEmpty()){
                            Toast.makeText(StudentActivity.this, "no data for that roll number", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };

                query.addValueEventListener(vw);
                adapter.notifyDataSetChanged();


            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent in =new Intent(getApplicationContext(),showattend.class);
                in.putExtra("index",i);
                in.putExtra("name",ddd.listy.get(i).getName());
                in.putExtra("roll",ddd.listy.get(i).getRollNumber());
                in.putExtra("present",ddd.listy.get(i).getPresent());
                in.putExtra("classes",ddd.listy.get(i).getClasses());
                startActivity(in);
            }
        });
    }
}