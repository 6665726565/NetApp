package com.example.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private ImageButton arrowBack , logOut;
    private static final int Gallery_Pick =1;
    private Uri ImageUri;
    private StorageReference postImageRef;
    private TextInputEditText userName , userEmail , userPassword , userBio;
    private Button saveChangesBtn ;
    private CircleImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private String currentUserId , downloadUrl;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        postImageRef = FirebaseStorage.getInstance().getReference().child("Profile_Picture").child(currentUserId);

        progressDialog = new ProgressDialog(this);


        arrowBack = findViewById(R.id.arrow_back);
        logOut = findViewById(R.id.log_out);
        saveChangesBtn = findViewById(R.id.edit_profile_btn);

        userName = findViewById(R.id.edt_profile_user_name);
        userEmail = findViewById(R.id.edt_profile_user_email);
        userPassword = findViewById(R.id.edt_profile_user_password);
        userBio = findViewById(R.id.edt_profile_bio);
        profileImage = findViewById(R.id.profile_image);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OppenGallery();
            }
        });

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToHomeActivity();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                sendUserToSignInActivity();
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String password = snapshot.child("password").getValue().toString();


                    userName.setText(name);
                    userEmail.setText(email);
                    userPassword.setText(password);

                    if (snapshot.hasChild("profileImg")){
                        downloadUrl = snapshot.child("profileImg").getValue().toString();
                        Picasso.get().load(downloadUrl).error(R.drawable.image_error).placeholder(R.drawable.image_error).into(profileImage);
                    }

                    if (snapshot.child("bio").exists()){
                        String bio = snapshot.child("bio").getValue().toString();
                        userBio.setText(bio);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveChanges();
            }
        });


    }

    private void sendUserToSignInActivity() {
        Intent intent = new Intent(EditProfileActivity.this , SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void SaveChanges() {

        String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String bio = userBio.getText().toString();


        HashMap hashMap = new HashMap();
        hashMap.put("name" , name);
        hashMap.put("email" , email);
        hashMap.put("password" , password);
        hashMap.put("bio" , bio);
        hashMap.put("profileImg" , downloadUrl);

        userRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "information saved !", Toast.LENGTH_SHORT).show();
                    sendUserToHomeActivity();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendUserToHomeActivity();
    }

    private void sendUserToHomeActivity() {
        Intent intent = new Intent(EditProfileActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void OppenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , Gallery_Pick);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            profileImage.setImageURI(ImageUri);
            ValidateImageInfo();
        }
    }


    private void ValidateImageInfo() {
        if (profileImage == null){
            Toast.makeText(EditProfileActivity.this, "you have to choose a pic", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("wait...");
            progressDialog.setMessage("uploading image ..");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);


            StoringImageToFireBaseStorage();

        }
    }

    private void StoringImageToFireBaseStorage() {

        StorageReference filPath = postImageRef.child(ImageUri.getLastPathSegment() + ".jpg");
        filPath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){


                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                downloadUrl = task.getResult().toString();
                                Toast.makeText(EditProfileActivity.this, "successful!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();


                            }else{
                                Toast.makeText(EditProfileActivity.this, "Problem Occured : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(EditProfileActivity.this, "eurror occured  : " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}