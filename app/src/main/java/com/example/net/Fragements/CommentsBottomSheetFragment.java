package com.example.net.Fragements;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Adapters.CommentsAdapter;
import com.example.net.Models.Comments;
import com.example.net.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsBottomSheetFragment extends BottomSheetDialogFragment {


    private BottomSheetDialog dialog;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private View rootView;
    private CircleImageView profileImg;
    private EditText addEdtTxt;
    private TextView publishTxt;
    private DatabaseReference userRef , commentsRef;
    private FirebaseAuth mAuth ;
    private String currentUserId;
    private RecyclerView commentsRecycler;
    private ArrayList<Comments> comments = new ArrayList();
    private String name , img ;
    private CommentsAdapter commentsAdapter ;
    private FragmentActivity fragmentActivity;
    private String key;

    public CommentsBottomSheetFragment(FragmentActivity activity , String key) {
       this.fragmentActivity = activity;
       this.key = key;

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.comments_bottom_sheet , container , false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        ImageButton backBtn = dialog.findViewById(R.id.cmnts_dialog_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        CoordinatorLayout layout = dialog.findViewById(R.id.btm_sheet_layout);
        assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        profileImg = dialog.findViewById(R.id.comments_img);
        addEdtTxt = dialog.findViewById(R.id.comments_edt_txt);
        publishTxt = dialog.findViewById(R.id.publish_txt);

        commentsRecycler = dialog.findViewById(R.id.comments_recycler);

        publishTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("profileImg")){
                    img = snapshot.child("profileImg").getValue().toString();
                    Picasso.get().load(img).error(R.color.white).placeholder(R.color.white).into(profileImg);
                }

                name = snapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        commentsRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                if (snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        comments.add(dataSnapshot.getValue(Comments.class));
                    }

                    commentsAdapter = new CommentsAdapter(fragmentActivity , comments);
                    commentsRecycler.setLayoutManager(new LinearLayoutManager(fragmentActivity));
                    commentsRecycler.setAdapter(commentsAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addComment() {
        String content = addEdtTxt.getText().toString();
        if (!TextUtils.isEmpty(content)){

            String id = commentsRef.push().getKey();
            HashMap hashMap = new HashMap();
            hashMap.put("profileImg" , img);
            hashMap.put("userName" , name);
            hashMap.put("comment" , content );

            commentsRef.child(key).child(id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    addEdtTxt.setText("");
                }
            });
        }
    }


}
