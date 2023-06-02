package com.example.net.Fragements;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.net.Adapters.SearchAdapter;
import com.example.net.Models.SearchResult;
import com.example.net.Models.Users;
import com.example.net.ProfileResult;
import com.example.net.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private ImageButton arrowBack;
    private EditText searchEdt ;
    private TextView recentTxt;
    private RecyclerView recyclerView;
    private ArrayList<Users> results = new ArrayList<>() ;
    private ArrayList<Users> recents = new ArrayList<>() ;
    private SearchAdapter searchAdapter ;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef , recentRef ;
    private String currentUserId ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container ,false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        recentRef = FirebaseDatabase.getInstance().getReference().child("Recents").child(currentUserId);


        searchEdt = view.findViewById(R.id.search_edt);
        recentTxt = view.findViewById(R.id.recent_search);

        recyclerView = view.findViewById(R.id.search_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!(TextUtils.isEmpty(charSequence))){
                    searchUsers(charSequence.toString().toLowerCase().trim());
                    recentTxt.setVisibility(View.GONE);
                }else {
                   results.clear();
                   getRecents();
                   searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        getRecents();


        return view;
    }

    private void searchUsers(String toLowerCase) {

        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("name").startAt(toLowerCase).endAt(toLowerCase + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                results.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!TextUtils.equals(dataSnapshot.getKey(), currentUserId)){
                        results.add(dataSnapshot.getValue(Users.class));

                    }
                }

                searchAdapter = new SearchAdapter(results, getContext(), new SearchAdapter.MyClickInterface() {
                    @Override
                    public void onItemClicked(View view, int position) {
                        recentRef.child(results.get(position).getUid()).setValue(results.get(position));
                        Intent intent = new Intent(getActivity() , ProfileResult.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("uid" ,results.get(position).getUid());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClicked(int notePosition, View view) {

                    }
                });

                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getRecents() {
        recents.clear();
        recentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (searchEdt.getText().toString().equals("")) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            recents.add(dataSnapshot.getValue(Users.class));
                        }
                    }
                    if (recents.size()==0){
                        recentTxt.setVisibility(View.GONE);
                    }else {
                        recentTxt.setVisibility(View.VISIBLE);
                    }
                    searchAdapter = new SearchAdapter(recents, getContext(), new SearchAdapter.MyClickInterface() {
                        @Override
                        public void onItemClicked(View view, int position) {

                            Intent intent = new Intent(getActivity() , ProfileResult.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("uid" ,recents.get(position).getUid());
                            startActivity(intent);


                        }

                        @Override
                        public void onLongItemClicked(int notePosition, View view) {

                        }
                    });
                    recyclerView.setAdapter(searchAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
