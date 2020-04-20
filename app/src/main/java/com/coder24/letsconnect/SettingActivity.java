package com.coder24.letsconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    private Button saveBtn;
    private EditText userName, about;
    private ImageView profileImageView;

    private static int galleryPick = 1;
    private Uri imageUri;
    private StorageReference userProfileImageRef;
    private String downloadUrl;
    private DatabaseReference userRef;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Instantiate progressDialogbar
        loadingBar = new ProgressDialog(this);

        //reference for image is Firebase storage with name "Profile Images"
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        //reference for storing user data to Firebase Database
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        saveBtn = findViewById(R.id.save_setting_btn);
        userName = findViewById(R.id.username_setting);
        about = findViewById(R.id.username_about);
        profileImageView = findViewById(R.id.setting_profile_image);


       //get image from  gallery of phone
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,galleryPick);
            }
        });
        //
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        //retrieve user data and show to setting page initailly
        retrieveUserInfo();
    }

    //set that image to profileImageView element
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPick && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void saveUserData() {
        final String getUserName = userName.getText().toString();
        final String getUserAbout = about.getText().toString();

        //while saving the data profile image upload is not mandatory so below cond. is used
        if(imageUri == null){

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //if image already exist than save only name and about
                    if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image")){
                        saveInfoOnlyWithoutImage();
                    }
                    else{
                        Toast.makeText(SettingActivity.this, "Please upload image...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if(getUserName.equals("")){
            Toast.makeText(this, "Name is mandatory..", Toast.LENGTH_SHORT).show();
        }
        else if(getUserAbout.equals("")){
            Toast.makeText(this, "About is mandatory..", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Account Setting");
            loadingBar.setMessage("Please wait...");
            loadingBar.show();
            //Here is profile is also uploaded than we have to saved that too.
            final StorageReference filePath = userProfileImageRef.
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final UploadTask uploadTask = filePath.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    downloadUrl = filePath.getDownloadUrl().toString();
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        downloadUrl = task.getResult().toString();

                        HashMap<String, Object> profileMap = new HashMap<>();
                        profileMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        profileMap.put("name", getUserName);
                        profileMap.put("about", getUserAbout);
                        profileMap.put("image", downloadUrl);

                        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(SettingActivity.this, MainActivity.class));
                                    finish();
                                    loadingBar.dismiss();

                                    Toast.makeText(SettingActivity.this, "Updated Successfull!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    private void saveInfoOnlyWithoutImage() {
        final String getUserName = userName.getText().toString();
        final String getUserAbout = about.getText().toString();

        if(getUserName.equals("")){
            Toast.makeText(this, "Name is mandatory..", Toast.LENGTH_SHORT).show();
        }
        else if(getUserAbout.equals("")){
            Toast.makeText(this, "About is mandatory..", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Account Setting");
            loadingBar.setMessage("Please wait...");
            loadingBar.show();

            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("name", getUserName);
            profileMap.put("about", getUserAbout);
            profileMap.put("image", downloadUrl);
            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                        finish();
                        loadingBar.dismiss();

                        Toast.makeText(SettingActivity.this, "Updated Successfull!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void retrieveUserInfo(){
        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String imageDb = dataSnapshot.child("image").getValue().toString();
                            String nameDb = dataSnapshot.child("name").getValue().toString();
                            String aboutDb = dataSnapshot.child("about").getValue().toString();

                            userName.setText(nameDb);
                            about.setText(aboutDb);
                            Picasso.get().load(imageDb).placeholder(R.drawable.profile_image)
                                    .into(profileImageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
