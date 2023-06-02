package com.example.net;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChoosingActivity extends AppCompatActivity {
    private CardView skillsCard , experienceCard;
    private ImageView skillsImg , experienceImg;

    public static boolean isProfess;
    public static SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private String currentUserId;
    private String getName , getEmail ,getPassword ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);

        skillsCard = findViewById(R.id.skillsCard);
        experienceCard = findViewById(R.id.experienceCard);
        skillsImg = findViewById(R.id.newSkillImg);
        experienceImg = findViewById(R.id.shareExperImg);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        isProfess = sharedPreferences.getBoolean("isProfess",true);


        skillsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean("isProfess" , false).apply();
                sendUserToChoosingFieldsActivity();
            }
        });

        experienceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean("isProfess" , true).apply();
                sendUserToChoosingFieldsActivity();
            }
        });


    }

    private void sendUserToChoosingFieldsActivity() {
        Intent intent = new Intent(this , ChoosingFieldsActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("Email1" , getEmail);
        startActivity(intent);
        finish();
    }

    private void sendUserToSignInActivity() {
        Intent intent = new Intent(ChoosingActivity.this , SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void sendUserToHomeActivity() {
        Intent intent = new Intent(this , HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}