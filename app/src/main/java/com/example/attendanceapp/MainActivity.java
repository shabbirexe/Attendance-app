package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
  VideoView vw;
  VideoView vw2;
 TextView tv;
 Button verButton;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
  public static FirebaseUser fUser;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        updateUI(currentUser);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
       public void Student(View view){
        Intent intt=new Intent(this,StudentActivity.class);
        finish();
        startActivity(intt);

       }
       public void Teacher(View view){
           vw.setVisibility(View.INVISIBLE);
           vw2.setVisibility(View.INVISIBLE);
           verButton.setVisibility(View.VISIBLE);
           tv.setVisibility(View.VISIBLE);
           DatabaseReference db= FirebaseDatabase.getInstance().getReference();
           Query que=db.child("KeyStore");
           que.keepSynced(true);
           final String[] str = new String[1];
           ValueEventListener vw11=new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {

                   str[0]=snapshot.getValue(String.class);


               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {
                   Toast.makeText(MainActivity.this, "errorrrrr", Toast.LENGTH_SHORT).show();
               }
           };

           que.addListenerForSingleValueEvent(vw11);
           verButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(tv.getText().toString().equals(str[0])){
                       Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                       startActivityForResult(signInIntent, RC_SIGN_IN);
                       vw.setVisibility(View.VISIBLE);
                       vw2.setVisibility(View.VISIBLE);
                       verButton.setVisibility(View.INVISIBLE);
                       tv.setVisibility(View.INVISIBLE);
                       tv.setText("");
                       vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                           @Override
                           public void onPrepared(MediaPlayer mp) {
                               mp.setLooping(true);
                           }
                       });
                       vw2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                           @Override
                           public void onPrepared(MediaPlayer mp) {
                               mp.setLooping(true);
                           }
                       });
                       vw.start();
                       vw2.start();
                   }else{
                       Toast.makeText(MainActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();
                   }
               }
           });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.verifyCode);
        verButton=findViewById(R.id.verifyButton);
         vw=(VideoView)findViewById(R.id.videoView);
         vw2=(VideoView)findViewById(R.id.videoView2);
        vw.setVideoPath("android.resource://"+ getPackageName() + "/" + R.raw.stud);
        vw2.setVideoPath("android.resource://"+ getPackageName() + "/" + R.raw.teach);
        vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        vw2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        vw.start();
        vw2.start();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();



    }

    private void updateUI(FirebaseUser user) {
   fUser=user;
      Intent in=new Intent(getApplicationContext(),SubjectPage.class);
      finish();
      startActivity(in);

    }
}