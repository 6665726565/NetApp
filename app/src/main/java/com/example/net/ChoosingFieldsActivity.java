package com.example.net;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChoosingFieldsActivity extends AppCompatActivity {

    private MaterialCardView mobileDevLayout , webDevLayout , designLayout;
    private Button choosingFieldBtn;
    private FirebaseAuth firebaseAuth;
    private String currentUserId ;
    private DatabaseReference userRef;
    private String getName , getEmail ,getPassword ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_fields);



        ChoosingActivity.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ChoosingActivity.isProfess = ChoosingActivity.sharedPreferences.getBoolean("isProfess" , true);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        webDevLayout = findViewById(R.id.web_dev_layout);
        mobileDevLayout = findViewById(R.id.mobile_dev_layout);
        designLayout = findViewById(R.id.design_layout);

        choosingFieldBtn = findViewById(R.id.choosing_field_btn);




        mobileDevLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mobileDevLayout.isChecked()){
                    mobileDevLayout.setChecked(false);
                    mobileDevLayout.animate().scaleX(1f).setDuration(150);
                    mobileDevLayout.animate().scaleY(1f).setDuration(150);
                }else {
                    mobileDevLayout.setChecked(true);
                    mobileDevLayout.animate().scaleX(0.95f).setDuration(150);
                    mobileDevLayout.animate().scaleY(0.95f).setDuration(150);
                }
            }
        });

        webDevLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webDevLayout.isChecked()){
                    webDevLayout.setChecked(false);
                    webDevLayout.animate().scaleX(1f).setDuration(150);
                    webDevLayout.animate().scaleY(1f).setDuration(150);
                }else {
                    webDevLayout.setChecked(true);
                    webDevLayout.animate().scaleX(0.95f).setDuration(150);
                    webDevLayout.animate().scaleY(0.95f).setDuration(150);
                }
            }
        });

        designLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (designLayout.isChecked()){
                    designLayout.setChecked(false);
                    designLayout.animate().scaleX(1f).setDuration(150);
                    designLayout.animate().scaleY(1f).setDuration(150);
                }else {
                    designLayout.setChecked(true);
                    designLayout.animate().scaleX(0.95f).setDuration(150);
                    designLayout.animate().scaleY(0.95f).setDuration(150);
                }
            }
        });

        choosingFieldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mobileDevLayout.isChecked() || webDevLayout.isChecked() || designLayout.isChecked())){
                    Toast.makeText(ChoosingFieldsActivity.this, "Please choose one at least", Toast.LENGTH_SHORT).show();
                }else {
                    handlBtnClicked();
                }
            }
        });
    }

    private void handlBtnClicked() {

        String fields = "" ;
        if (mobileDevLayout.isChecked()){
            if (fields == ""){
                fields = fields + "Mobile developer ";
            }else{
                fields = fields + "| Mobile developer ";
            }

        }
        if (webDevLayout.isChecked()){
            if (fields == ""){
                fields = fields + "Web developer ";
            }else{
                fields = fields + "| Web developer ";
            }
        }
        if (designLayout.isChecked()){
            if (fields == ""){
                fields = fields + "Designer ";
            }else{
                fields = fields + "| Designer ";
            }
        }

        HashMap hashMap = new HashMap();
        hashMap.put("uid" , currentUserId);
        hashMap.put("fields" , fields);

        if(ChoosingActivity.isProfess){
            hashMap.put("isProfess" , true);
        }else{
            hashMap.put("isProfess" , false);
        }

        userRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(ChoosingFieldsActivity.this, "successed", Toast.LENGTH_SHORT).show();
                    sendUserToHomeActivity();
                }else {
                    Toast.makeText(ChoosingFieldsActivity.this, "error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserToHomeActivity() {
        Intent intent = new Intent(ChoosingFieldsActivity.this , HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}