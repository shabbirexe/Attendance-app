package com.example.attendanceapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {
    DatabaseReference db;
    Boolean saved = null;
    String name;
     ArrayList<String> rollNumber=new ArrayList<>();
    ArrayList<String> students=new ArrayList<>();
  public FirebaseHelper(DatabaseReference db){
      this.db=db;
  }
  public Boolean save(Data data){
      if(data==null){
        saved=false;
      }else{
          try{

              db= FirebaseDatabase.getInstance().getReference("Student/"+FirebaseAuth.getInstance().getUid());
              db.child("SubDetails").child(this.name).push().setValue(data);
              saved =true;
          }catch(DatabaseException e){
              e.printStackTrace();
              saved=false;
          }

      }
     return saved;
  }



}
