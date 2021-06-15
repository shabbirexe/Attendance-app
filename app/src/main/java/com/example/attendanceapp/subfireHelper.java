package com.example.attendanceapp;



import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class subfireHelper {
    DatabaseReference db;
    Boolean saved = null;

    public subfireHelper(DatabaseReference db){
        this.db=db;
    }

    public Boolean save(String data){
        if(data==null){
            saved=false;
        }else{
            try{

                db.child("SubList").push().setValue(data);

                saved =true;
            }catch(DatabaseException e){
                e.printStackTrace();
                saved=false;
            }

        }

        return saved;
    }


}
