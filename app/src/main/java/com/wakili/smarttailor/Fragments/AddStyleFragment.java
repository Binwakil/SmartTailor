package com.wakili.smarttailor.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wakili.smarttailor.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;


public class AddStyleFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    private ImageView styleImg;
    private CircleImageView addImg;
    private MaterialEditText edtTitle, edtdetail;
    AlertDialog ourdialog;
    ProgressDialog pDialog;
    private EditText seachEdt;

    private DatabaseReference databaseRef, mDatabaseRef, mDatabaseUsers;
    private StorageReference storage;
    private ArrayAdapter<CharSequence> spinner_adapter;



    private RecyclerView recycler_siwes;
    private FirebaseDatabase mFirebaseInstance;

    private Uri uri = null;
    private Bitmap bitmap, bitmp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_style, container, false);

        Button btnadd = view.findViewById(R.id.btnSave);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               save();

            }
        });


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Change photo");
                builder.setMessage("Choose a method to change photo");
                builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickPhotoFromGallery();
                    }
                });
                builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchTakePictureIntent();
                    }
                });
                builder.create().show();


            }
        });


        styleImg= view.findViewById(R.id.styleimg);
        edtdetail = view.findViewById(R.id.edtDetail);
        edtTitle = view.findViewById(R.id.edtTitle);




        FirebaseApp.initializeApp(getContext());
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Visitors");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Visitors").push();

        return view;

    }


    public  void savetoFB() {
        final String vphone = edtTitle.getText().toString();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("MEE", dataSnapshot.getChildrenCount() + "");
                boolean isFound = false;
                for(DataSnapshot rec : dataSnapshot.getChildren()){
                    if (rec.child("phone").getValue().toString().equals(vphone)) {
                        isFound = true;
                        break;
                    }
                }
                databaseRef.removeEventListener(this);
                if(isFound) {
                    Toast.makeText(getContext(), "The User Exist", Toast.LENGTH_SHORT).show();
                }else{
                    save();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void save(){

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait... Updating Your Profile");
        pDialog.setCancelable(false);
        pDialog.show();
        final String imagepath = styleImg.getDrawable().toString();
        final String vtitle = edtTitle.getText().toString();
        final String vdetail = edtdetail.getText().toString();

        if (!TextUtils.isEmpty(vdetail) && !TextUtils.isEmpty(vtitle)){
            StorageReference filepath = storage.child("profile_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    //getting the post image download url
                    final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = databaseRef;
                    Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();




                    final DatabaseReference newComment = databaseRef.push();

        try {
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newComment.child("ImagePath").setValue(imagepath);
                    newComment.child("Title").setValue(vtitle);
                    newComment.child("Detail").setValue(vdetail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (ourdialog != null && ourdialog.isShowing()){
                                        ourdialog.dismiss();
                                    }

                                    mDatabaseUsers.removeEventListener(this);
                                }
                            });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                }
            });
            GalleryFragment galleryFragment  = new GalleryFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    //setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
                    .replace(R.id.nav_host_fragment,galleryFragment)
                    .addToBackStack(null)
                    .commit();

        } catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }







        btnsave.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(DupdateProfile.this, "Uploading…", Toast.LENGTH_LONG).show();
            pDialog = new ProgressDialog(DupdateProfile.this);
            pDialog.setMessage("Please wait... Updating Your Profile");
            pDialog.setCancelable(false);
            pDialog.show();
            userId = user.getUid();
            tname = etName.getText().toString();
            temail = etEmail.getText().toString();
            tphone = etPhone.getText().toString();
            tqual = etQual.getText().toString();
            tspec = etSpec.getText().toString();
            tabout = etAbut.getText().toString();
            toffice = etOffice.getText().toString();

            // do a check for empty fields
            if (!TextUtils.isEmpty(tname) && !TextUtils.isEmpty(temail)){
                StorageReference filepath = storage.child("profile_images").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        //getting the post image download url
                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        final DatabaseReference newPost = databaseRef;
                        Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();






                        //adding post contents to database reference
                        databaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                newPost.child("Name").setValue(tname);
                                newPost.child("Email").setValue(temail);
                                newPost.child("Phone").setValue(tphone);
                                newPost.child("Office").setValue(toffice);
                                newPost.child("Qualification").setValue(tqual);
                                newPost.child("Specialization").setValue(tspec);
                                newPost.child("About Us").setValue(tabout);
                                newPost.child("imageUrl").setValue(downloadUrl.toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                //add user details in real time database
                                                Firebase  reference = new Firebase(FIRE_BASE_USER_URL);
                                                reference.child(user.getUid()).child("user_id").setValue(user.getUid());
                                                reference.child(user.getUid()).child("name").setValue(tname);
                                                reference.child(user.getUid()).child("email").setValue(temail);
                                                reference.child(user.getUid()).child("phone").setValue(tphone);
                                                reference.child(user.getUid()).child("office").setValue(toffice);
                                                reference.child(user.getUid()).child("qualification").setValue(tqual);
                                                reference.child(user.getUid()).child("specialization").setValue(tspec);
                                                reference.child(user.getUid()).child("about").setValue(tabout);
                                                reference.child(user.getUid()).child("imageUrl").setValue(downloadUrl.toString());
                                                reference.child(user.getUid()).child("medicallicienceURL").setValue(downloadUrl.toString());

                                                if (task.isSuccessful()){
                                                    if (pDialog.isShowing())
                                                        pDialog.dismiss();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(DupdateProfile.this);
                                                    builder.setMessage("Recorder Saved Successfully")
                                                            .setTitle(tname);
                                                    builder.show();

                                                    Intent intent = new Intent(DupdateProfile.this, Home.class);
                                                    startActivity(intent);
                                                }}});}
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            } }); } }); }}}); }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                    styleImg.setImageBitmap(bitmap);
                    styleImg.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if(data != null && data.getExtras() != null && data.hasExtra("data")) {
                bitmap = (Bitmap) data.getExtras().get("data");
                styleImg.setImageBitmap(bitmap);
                styleImg.setVisibility(View.VISIBLE);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickPhotoFromGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

}
