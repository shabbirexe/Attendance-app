package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.R.color.holo_blue_light;
import static android.R.color.holo_green_light;

public class showattend extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showattend);
        Intent in=getIntent();
       String name= in.getStringExtra("name");
       String roll=in.getStringExtra("roll");
       int pres=in.getIntExtra("present",0);
       int cla=in.getIntExtra("classes",0);
        TextView tv=findViewById(R.id.textView2);
        TextView tv1=findViewById(R.id.textView3);
        TextView tv2=findViewById(R.id.textView5);

        int perc=0;
        try{
            perc=pres*100;
            perc/=cla;

        }catch (ArithmeticException e){
            Toast.makeText(this, "error occured", Toast.LENGTH_SHORT).show();
        }
        tv.setText(name+" ("+roll+")"+" has attended "+pres+" classes "+" out of " +cla+" contucted "+name+"'s attendance is "+perc+"%.");
        if(perc<75){
            tv1.setVisibility(View.VISIBLE);
           Animation obj= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.nav_default_enter_anim);
            tv1.setText("Attendance below 75%");
            tv1.startAnimation(obj);
        }else{
            tv2.setVisibility(View.VISIBLE);
            tv2.setText("Attendance above 75%");

        }
    }
}