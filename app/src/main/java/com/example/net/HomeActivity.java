package com.example.net;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.net.Fragements.HomeFragment;
import com.example.net.Fragements.HomePostFragment;
import com.example.net.Fragements.MessageFragment;
import com.example.net.Fragements.ProfileFragment;
import com.example.net.Fragements.QuestionFragment;
import com.example.net.Fragements.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "home";
    private final int PAGE_NUM = 2;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference userRef;
    private BottomNavigationView bottomNavigationView;
    private Menu menu;
    private MenuItem homeItem , searchItem, qstItem , profileItem;
    private Spinner postTxt;
    private ImageButton postBtn , chatBtn;
    private ViewPager2 chatViewPager;
    private FragmentStateAdapter pagerAdapter;
    private Toolbar toolbar;
    public static String value = "Posts";
    private FrameLayout fragmentFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        chacking();
        
        toolbar = findViewById(R.id.tool_bar);
        postBtn = findViewById(R.id.post_btn);
        postTxt = findViewById(R.id.post_txt);
        chatBtn = findViewById(R.id.chat_btn);
        fragmentFrame = findViewById(R.id.container);
        chatViewPager = findViewById(R.id.chat_view_pager);

        pagerAdapter = new ScreenSlidePageAdapter(this);
        chatViewPager.setAdapter(pagerAdapter);

        bottomNavigationView = findViewById(R.id.btm_nav_bar);
        bottomNavigationView.setItemIconTintList(null);


        bottomNavigationView.setSelectedItemId(R.id.item_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavItemSelected(item);
                return true;
            }
        });

        replaceFragment(new HomeFragment());


        chatViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position ==1){
                    bottomNavigationView.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                    fragmentFrame.setElevation(0);
                }else{
                    toolbar.setVisibility(View.VISIBLE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            fragmentFrame.setElevation(3);
                        }
                    }, 100);
                }

                chatBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chatViewPager.setCurrentItem(position +1 , true);
                    }
                });

            }
        });







        menu = bottomNavigationView.getMenu();
        homeItem = menu.findItem(R.id.item_home).setIcon(R.drawable.ic_selected_home);
        searchItem = menu.findItem(R.id.item_search).setIcon(R.drawable.ic_search);
        qstItem = menu.findItem(R.id.item_qst).setIcon(R.drawable.ic_qst);
        profileItem = menu.findItem(R.id.item_profile).setIcon(R.drawable.ic_profile);



        String[] items = {"Posts" , "Articles"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this,R.layout.items_list , items);
        postTxt.setAdapter(itemAdapter);
        postTxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                value = adapterView.getItemAtPosition(i).toString();
                replaceFragment(new HomeFragment());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    protected void onStart() {
        super.onStart();
        chacking();
    }

    private void chacking() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null){
            sendToSignInActivity();
        } else {
            CheckUserExistence();
        }
    }


    private void CheckUserExistence() {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.hasChild(currentUserId))){
                    sendToSignInActivity();
                }else {
                    if (!(snapshot.child(currentUserId).hasChild("isProfess"))){
                        sendUserToChoosingActivity();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendUserToChoosingActivity() {
        Intent intent = new Intent(HomeActivity.this , ChoosingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void sendToSignInActivity() {
        Intent intent = new Intent(HomeActivity.this , SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void handleNavItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.item_home:

                toolbar.setVisibility(View.VISIBLE);
                chatViewPager.setVisibility(View.VISIBLE);
                replaceFragment(new HomeFragment());
                item.setIcon(R.drawable.ic_selected_home);
                searchItem.setIcon(R.drawable.ic_search);
                qstItem.setIcon(R.drawable.ic_qst);
                profileItem.setIcon(R.drawable.ic_profile);
                break;


            case R.id.item_search:
                toolbar.setVisibility(View.GONE);
                chatViewPager.setVisibility(View.GONE);
                item.setIcon(R.drawable.ic_selected_search);
                replaceFragment(new SearchFragment());
                homeItem.setIcon(R.drawable.ic_home);
                qstItem.setIcon(R.drawable.ic_qst);
                profileItem.setIcon(R.drawable.ic_profile);

                break;

            case R.id.item_qst:
                toolbar.setVisibility(View.GONE);
                chatViewPager.setVisibility(View.GONE);
                replaceFragment(new QuestionFragment());
                homeItem.setIcon(R.drawable.ic_home);
                item.setIcon(R.drawable.ic_selected_qst);
                searchItem.setIcon(R.drawable.ic_search);
                profileItem.setIcon(R.drawable.ic_profile);
                break;

            case R.id.item_profile:
                toolbar.setVisibility(View.GONE);
                chatViewPager.setVisibility(View.GONE);
                replaceFragment(new ProfileFragment());
                homeItem.setIcon(R.drawable.ic_home);
                item.setIcon(R.drawable.ic_selected_profile);
                qstItem.setIcon(R.drawable.ic_qst);
                searchItem.setIcon(R.drawable.ic_search);
                break;
        }

    }

    private void replaceFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container , fragment);
        transaction.commit();

    }


    private class ScreenSlidePageAdapter extends FragmentStateAdapter {

        public ScreenSlidePageAdapter(HomeActivity homeActivity) {
            super(homeActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position) {
                case 0 :
                    return new HomeFragment();
                case 1:
                    return new MessageFragment();
            }


            return null;
        }

        @Override
        public int getItemCount() {
            return PAGE_NUM;
        }
    }
}