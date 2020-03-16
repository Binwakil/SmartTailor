package com.wakili.smarttailor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {

    private Uri filePath;
    private ProgressDialog pDialog;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef, mDatabaseRef;

    private Button butSignup, butVerify;
    private EditText edtname, edtphone, edtaddress, edtpassword, edtemail;



    private String phoneNumber, otp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    AlertDialog.Builder alertDialog;

    private String mname, memail, maddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();


        FirebaseApp.initializeApp(this);
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Profile");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Profile").push();

        butSignup = findViewById(R.id.btnSingup);
        edtaddress = findViewById(R.id.edtAddress);
        edtname = findViewById(R.id.edtName);
        edtpassword = findViewById(R.id.etPassword);
        edtphone = findViewById(R.id.edtPhone);

        StartFirebaseLogin();


        butSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OTPVerify();



            }
        });


    }


    private void OTPVerify(){

        edtphone = (EditText) findViewById(R.id.edtPhone);
        edtemail = (EditText)findViewById(R.id.edtEmail);
        phoneNumber = edtphone.getText().toString();
        memail = edtemail.getText().toString();


        auth.fetchSignInMethodsForEmail(memail)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        try {


                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                            if (isNewUser) {

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        phoneNumber,                     // Phone number to verify
                                        120,                           // Timeout duration
                                        TimeUnit.SECONDS,                // Unit of timeout
                                        SignupActivity.this,        // Activity (for callback binding)
                                        mCallback);
                                alertDialog = new AlertDialog.Builder(SignupActivity.this);


                                alertDialog.setTitle("OPT IS SENT UR NO");

                                alertDialog.setMessage("Enter the OTP");
                                final EditText input = new EditText(SignupActivity.this);

                                alertDialog.setIcon(R.drawable.ic_add_white_24dp);
                                alertDialog.setView(input);
                                alertDialog.setCancelable(false);


                                alertDialog.setPositiveButton("Submit",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                otp = input.getText().toString();
                                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                                                signInWithPhoneAuthCredential(credential);

                                            }
                                        });
                                alertDialog.setNegativeButton("Resend OtP",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                OTPVerify();


                                            }
                                        });

                                alertDialog.show();


                            } else {


                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);


                                builder.setTitle("ERROR SIGNUP");

                                builder.setMessage("you have already created account. kinly login with email and phone number");

                                builder.setNegativeButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                builder.show();
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(SignupActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            SavetoFirbase();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(SignupActivity.this, "failure: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SignupActivity.this, "Verification Code Entered is Invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });}

    private void  SavetoFirbase() {

        butSignup = (Button) findViewById(R.id.btnSingup);
        edtaddress = findViewById(R.id.edtAddress);
        edtname = findViewById(R.id.edtName);
        edtpassword = findViewById(R.id.etPassword);
        edtphone = findViewById(R.id.edtPhone);


        FirebaseApp.initializeApp(this);
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Profile");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Profile").push();


        phoneNumber = edtphone.getText().toString();
        memail = edtemail.getText().toString().trim();
        maddress = edtaddress.getText().toString();
        mname = edtname.getText().toString();
        maddress = edtaddress.getText().toString();



        if (phoneNumber.isEmpty() || memail.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("Error")
                    .setTitle("Error ")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (!memail.contains("@")|| !memail.contains(".")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("the email address you entered is not valid You Most entered a valid Email Address!")
                    .setTitle("Invalid Email!")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return;

        }
        else if (phoneNumber.length() < 6) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("Password too short, enter minimum 6 characters!")
                    .setTitle("Invalid Password")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return;

        } else {
            pDialog = new ProgressDialog(SignupActivity.this);
            pDialog.setMessage("Please wait... While Creating your Account");
            pDialog.setCancelable(false);
            pDialog.show();


           final String uname = "Abba";
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



            auth.createUserWithEmailAndPassword(memail, phoneNumber)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final DatabaseReference newComment = databaseRef.push();

                                try {

                                    String key;
                                    key = FirebaseAuth.getInstance().getUid();


                                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            newComment.child("Name").setValue(mname);
                                            newComment.child("Email").setValue(memail);
                                            newComment.child("Phone").setValue(phoneNumber);
                                            newComment.child("Address").setValue(maddress);
                                            newComment.child("uid").setValue("")
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {

                                                            if (pDialog.isShowing())
                                                            {
                                                                pDialog.dismiss();
                                                            }
                                                            Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_LONG).show();

                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(SignupActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                } catch (Exception e) {
                                    Log.e("ERROR", e.getMessage(), e);
                                    Toast.makeText(SignupActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                                }


                                Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                intent.putExtra("name1", uname);
                                startActivity(intent);



                            } else {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage(task.getException().getMessage())
                                        .setTitle("Error")
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
        }


    }

    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(SignupActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
                SavetoFirbase();
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(SignupActivity.this,"verification fialed "+e,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(SignupActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
            }
        };
    }


}
