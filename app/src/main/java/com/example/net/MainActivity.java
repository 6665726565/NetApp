package com.example.net;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.net.OnBoarding.OnBoardingItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton backBtn ;
    private Button onBoardBtn , getStartedBtn ;
    private ViewPager2 onBoardingPager;
    private DotsIndicator dotsIndicator ;
    private OnBoardingAdapter onBoardingIemAdapter;
    private TextView skipTxt;
    private Animation animation;
    private FirebaseAuth firebaseAuth;
    private SaveState saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backBtn = findViewById(R.id.backBtn);
        onBoardBtn = findViewById(R.id.onBoardingBtn);
        getStartedBtn = findViewById(R.id.getStartedBtn);
        onBoardingPager = findViewById(R.id.viewPager);
        dotsIndicator = findViewById(R.id.dots_indicator);
        skipTxt = findViewById(R.id.skipTxt);


        firebaseAuth = FirebaseAuth.getInstance();


        saveState = new SaveState("ob" , this);

        setupOnBoardingItem();

        if (saveState.getState() == 1){
            sendUserToSignInActivity();
        }

        setupOnBoardingItem();

        onBoardingPager.setAdapter(onBoardingIemAdapter);
        dotsIndicator.attachTo(onBoardingPager);

        onBoardingPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position>=1){
                    backBtn.setVisibility(View.VISIBLE);
                }else {
                    backBtn.setVisibility(View.GONE);

                }

                if (position == onBoardingIemAdapter.getItemCount()-1 ){
                    animation = AnimationUtils.loadAnimation(MainActivity.this , R.anim.bottom_anim);
                    skipTxt.setVisibility(View.GONE);
                    dotsIndicator.setVisibility(View.GONE);
                    onBoardBtn.setVisibility(View.GONE);
                    getStartedBtn.setAnimation(animation);
                    getStartedBtn.setVisibility(View.VISIBLE);
                }else {
                    skipTxt.setVisibility(View.VISIBLE);
                    dotsIndicator.setVisibility(View.VISIBLE);
                    onBoardBtn.setVisibility(View.VISIBLE);
                    getStartedBtn.setVisibility(View.GONE);
                }

                getStartedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveState.setState(1);
                        sendUserToSignInActivity();
                    }
                });

                skipTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBoardingPager.setCurrentItem(onBoardingIemAdapter.getItemCount()-1 , true);
                    }
                });

                onBoardBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position < onBoardingIemAdapter.getItemCount()-1){
                            onBoardingPager.setCurrentItem(position +1 , true);
                        }else {

                        }

                    }
                });
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBoardingPager.setCurrentItem(position-1  , true);

                    }
                });


            }
        });

    }

    private void sendUserToChooseActivity() {
        Intent intent =new Intent(MainActivity.this , ChoosingActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupOnBoardingItem(){
        List<OnBoardingItem> onBoardingItems = new ArrayList<>();

        OnBoardingItem welcome = new OnBoardingItem();
        welcome.setTitle("Welcome to the App");
        welcome.setDescription("name , is an app  that connect professionels and beginners together for a better future ");
        welcome.setImage(R.drawable.ic_undraw_hello_re_3evm);

        OnBoardingItem beginner = new OnBoardingItem();
        beginner.setTitle("Intrested in specific field ?");
        beginner.setDescription(" as a beginner ! now you can communicate with experts in the same field you are intrested in and learn from there experiences.");
        beginner.setImage(R.drawable.ic_design);

        OnBoardingItem network = new OnBoardingItem();
        network.setTitle("Expand your network !");
        network.setDescription(" as a professional ! this is your chance to expand your network with other seniors in diverse fields.");
        network.setImage(R.drawable.ic_network);



        onBoardingItems.add(welcome);
        onBoardingItems.add(beginner);
        onBoardingItems.add(network);


        onBoardingIemAdapter = new OnBoardingAdapter(onBoardingItems , this);


    }



    void sendUserToSignInActivity(){
        Intent signInActivity = new Intent(this , SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signInActivity);
        finish();
    }


}