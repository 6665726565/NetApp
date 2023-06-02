package com.example.net;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.net.Fragements.BeginnerProfileResult;
import com.example.net.Fragements.ProfessProfileResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileResult extends AppCompatActivity {

    private ImageButton arrowBack;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_result);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        arrowBack = findViewById(R.id.arrow_back_profile);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String uid = getIntent().getStringExtra("uid");

        Bundle bundle = new Bundle();
        bundle.putString("uid" , uid);

        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isProfess = (Boolean) snapshot.child("isProfess").getValue();
                if (Boolean.TRUE.equals(isProfess)){
                    ProfessProfileResult professProfileResult = new ProfessProfileResult();
                    professProfileResult.setArguments(bundle);
                  replaceFragment(professProfileResult);
                }else {
                    BeginnerProfileResult beginnerProfileResult = new BeginnerProfileResult();
                    beginnerProfileResult.setArguments(bundle);
                    replaceFragment(beginnerProfileResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void replaceFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_result_fragment , fragment);
        transaction.commit();

    }
}