package com.wakili.smarttailor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ProgressDialog pDialog;


    private FirebaseUser user;

    EditText editTextUserName, editTextPassword;
    Button btnLogin;
    TextView tvsignup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        tvsignup = (TextView)findViewById(R.id.tvSignUp);

        editTextUserName = (EditText) findViewById(R.id.edtPhone);
        editTextPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextUserName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();



                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Pls Enter Your Login Credential ")
                            .setTitle("Error ")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    pDialog = new ProgressDialog(LoginActivity.this);
                    pDialog.setMessage("Please wait... While logging in");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        //user = FirebaseAuth.getInstance().getCurrentUser();
                                        auth = FirebaseAuth.getInstance();

//                                        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//                                        DatabaseReference databaseReference =    mFirebaseDatabase.getReference("users");
//
//                                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users");
//                                        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                                                String userRown = "1";
//                                                int rowsss = Integer.valueOf(userRown);
//
//                                                if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 1){
//                                                    Toast.makeText(Dlogin.this, "You Login as Doctor", Toast.LENGTH_SHORT).show();
//
//                                                    Intent intent = new Intent(Login.this, MainActivity.class);
//                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                    startActivity(intent);
//
//                                                }
//
//                                                else if(Integer.valueOf(dataSnapshot.child("user_row").getValue().toString()) == 0){
//                                                    Toast.makeText(Login.this, "You Login as User", Toast.LENGTH_SHORT).show();
//
//                                                    Intent intent = new Intent(Login.this, PatientDB.class);
//                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                    startActivity(intent);
//
//                                                }
//
//
//                                                //}
//
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);


                                    } else {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage(task.getException().getMessage())
                                                .setTitle("Error ")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                }
            }
        });




        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(in);
            }
        });

    }




}
