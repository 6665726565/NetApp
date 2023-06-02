package com.example.net.Adding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.net.HomeActivity;
import com.example.net.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

public class AddPostActivity extends AppCompatActivity {


    private static final int Gallery_Pick =1;
    private Uri imageUri;
    private ImageButton backBtn ;
    private TextView publishTxt ,userName , userField , selectSinglePhoto , selectMultiplePhotos;
    private CircleImageView profilePicture;
    private EditText postDescription ;

    private FirebaseAuth mAuth ;
    private String currentUserId , downloadUrl;
    private DatabaseReference userRef , postRef;
    private StorageReference postImageRef;
    private CardView addPostCardView;
    private ImageView postImage;

    private ProgressDialog progressDialog;

    private CheckBox mobileDev , webDev , designer;
    private String description ,name , uField , userProfileImg;
    private String fields = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postImageRef = FirebaseStorage.getInstance().getReference().child("Images");

        mobileDev = findViewById(R.id.add_post_mobile_dev);
        webDev = findViewById(R.id.add_post_web_developement);
        designer = findViewById(R.id.add_post_design);

        backBtn = findViewById(R.id.add_post_back_btn);
        publishTxt = findViewById(R.id.add_post_btn);
        userName = findViewById(R.id.add_post_user_name);
        userField = findViewById(R.id.add_post_user_field);
        postDescription = findViewById(R.id.add_post_description);
        profilePicture = findViewById(R.id.add_post_profile_picture_post);
        addPostCardView = findViewById(R.id.add_post_card_view);
        postImage = findViewById(R.id.add_post_img);

        addPostCardView.setVisibility(View.GONE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToHomeActivity();
            }
        });


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 name = snapshot.child("name").getValue().toString();
                 uField = snapshot.child("fields").getValue().toString();

                if (snapshot.hasChild("profileImg")){
                    userProfileImg = snapshot.child("profileImg").getValue().toString();
                    Picasso.get().load(userProfileImg).error(R.drawable.image_error).placeholder(R.drawable.image_error).into(profilePicture);
                }

                userName.setText(name);
                userField.setText(uField);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        publishTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlPublishClicked();
            }
        });

        showDialog();

    }

    private void handlPublishClicked() {



        if (mobileDev.isChecked()) {
            if (fields == "") {
                fields = fields + "Mobile developement ";
            } else {
                fields = fields + "| Mobile developement ";
            }

        }
        if (webDev.isChecked()) {
            if (fields == "") {
                fields = fields + "Web developement ";
            } else {
                fields = fields + "| Web developement ";
            }
        }
        if (designer.isChecked()) {
            if (fields == "") {
                fields = fields + "Design ";
            } else {
                fields = fields + "| Design ";
            }
        }



        if (!(mobileDev.isChecked() || webDev.isChecked() || designer.isChecked())) {
            Toast.makeText(AddPostActivity.this, "Please select the post's field", Toast.LENGTH_SHORT).show();
        } else {

            if (imageUri!=null){
                ValidateImageInfo();
            }else {
                description = postDescription.getText().toString();
                if (!TextUtils.isEmpty(description)){
                    HashMap hashMap = new HashMap();
                    String key = postRef.push().getKey();


                    hashMap.put("postDescription" , description);
                    hashMap.put("fields" , fields);
                    hashMap.put("uid" , currentUserId);
                    hashMap.put("userName" , name);
                    hashMap.put("uField" , uField);
                    hashMap.put("key" , key);
                    hashMap.put("likesNumber" , "0");
                    hashMap.put("commentsNumber" , "0");

                    if (!TextUtils.isEmpty(userProfileImg)){
                        hashMap.put("userProfileImg" , userProfileImg);
                    }

                    postRef.child( key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){

                                sendUserToHomeActivity();


                            }else{
                                Toast.makeText(AddPostActivity.this, "Problme occured : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();

                            }
                        }

                    });
                }else {
                    Toast.makeText(this, "please add something !", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


    private void sendUserToHomeActivity() {
        Intent intent = new Intent(AddPostActivity.this , HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(AddPostActivity.this, R.style.DialogSheetBottomTheme);
        dialog.setContentView(R.layout.add_post_sheet);

        selectSinglePhoto = dialog.findViewById(R.id.single_photo);
        selectMultiplePhotos = dialog.findViewById(R.id.multiple_photos);

        selectSinglePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                OppenGallery();
            }
        });

        selectMultiplePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private void OppenGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , Gallery_Pick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null){
            addPostCardView.setVisibility(View.VISIBLE);
            imageUri = data.getData();
            postImage.setImageURI(imageUri);
        }
    }

    private void ValidateImageInfo() {
        description = postDescription.getText().toString();

       progressDialog.setTitle("Please wait ...");
       progressDialog.setMessage("we are uploading your post ...");
       progressDialog.show();
       progressDialog.setCanceledOnTouchOutside(false);


       StoringImageToFireBaseStorage();


    }

    private void StoringImageToFireBaseStorage() {

        StorageReference filPath = postImageRef.child(imageUri.getLastPathSegment() + ".jpg");
        filPath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){


                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                downloadUrl = task.getResult().toString();
                                SavingImageInformationToDataBase();


                            }else{
                                Toast.makeText(AddPostActivity.this, "Problem Occured : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                        Toast.makeText(AddPostActivity.this, "eurror occured  : " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void SavingImageInformationToDataBase() {
        HashMap hashMap = new HashMap();
        String key = postRef.push().getKey();

        hashMap.put("postImg" , downloadUrl);
        hashMap.put("postDescription" , description);
        hashMap.put("fields" , fields);
        hashMap.put("uid" , currentUserId);
        hashMap.put("userName" , name);
        hashMap.put("uField" , uField);
        hashMap.put("key" , key);
        hashMap.put("likesNumber" , "0");
        hashMap.put("commentsNumber" , "0");


        if (!TextUtils.isEmpty(userProfileImg)){
            hashMap.put("userProfileImg" , userProfileImg);
        }

        postRef.child( key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    sendUserToHomeActivity();


                }else{
                    Toast.makeText(AddPostActivity.this, "Problme occured : " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();

                }
            }

        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendUserToHomeActivity();
    }
}