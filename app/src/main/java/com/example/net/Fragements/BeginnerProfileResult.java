package com.example.net.Fragements;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.net.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BeginnerProfileResult extends Fragment {

    private CircleImageView profileImage ;
    private TextView userName , userBio , messageTxt;

    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.beginner_profile_result, container ,false);

        String uid = getArguments().getString("uid");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        userName =view.findViewById(R.id.beginner_result_user_name_txt);
        userBio =view.findViewById(R.id.beginner_result_bio_txt);
        profileImage =view.findViewById(R.id.beginner_result_profile_image);
        messageTxt = view.findViewById(R.id.message_txt_beginner);

        messageTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    userName.setText(name);


                    if (snapshot.hasChild("profileImg")){
                        Picasso.get().load(snapshot.child("profileImg").getValue().toString()).error(R.drawable.image_error).placeholder(R.drawable.image_error).into(profileImage);
                    }

                    if(snapshot.child("bio").exists()){
                        String bio = snapshot.child("bio").getValue().toString();
                        if (!TextUtils.isEmpty(bio)) {
                            userBio.setText(bio);
                        }else{
                            userBio.setText("");
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }
}
