package com.example.net;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    private TextView userNameTxt , confirmPasswordTxt , forgetPasswordTxt , qstTxt , choiceTxt, titleTxt , userNameErrorTxt,  emailErrorTxt , passwordErrorTxt , confirmPasswordErrorTxt;
    private TextInputLayout userNameInput , userEmailInput , userPasswordInput ,  confirmPasswordInput ;
    private TextInputEditText userName , userEmail , userPassword , userConfirmPassword;
    private Button signBtn;
    boolean isProblem = false;
    private ProgressDialog progressDialog;
    private Boolean hasAccoount;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        hasAccoount = true;

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userNameTxt = findViewById(R.id.user_name_txt);
        confirmPasswordTxt = findViewById(R.id.confirm_password_txt);
        forgetPasswordTxt = findViewById(R.id.forget_password_txt);
        qstTxt= findViewById(R.id.qst_text);
        choiceTxt = findViewById(R.id.choice_txt);
        titleTxt = findViewById(R.id.title_txt);
        emailErrorTxt = findViewById(R.id.email_error_text);
        passwordErrorTxt = findViewById(R.id.password_error_text);
        confirmPasswordErrorTxt = findViewById(R.id.confirm_password_error_text);
        userNameErrorTxt = findViewById(R.id.user_name_error_text);

        userNameInput = findViewById(R.id.user_name_input);
        userEmailInput = findViewById(R.id.user_email_input);
        userPasswordInput = findViewById(R.id.user_password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        userConfirmPassword = findViewById(R.id.confirm_password);

        signBtn = findViewById(R.id.sign_btn);

        choiceTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!hasAccoount) {

                    choiceTxt.setText("Sign Up");
                    titleTxt.setText("Sign in");
                    qstTxt.setText("Dont you have an account");
                    forgetPasswordTxt.setVisibility(View.VISIBLE);
                    confirmPasswordInput.setVisibility(View.GONE);
                    signBtn.setText("Login");
                    confirmPasswordTxt.setVisibility(View.GONE);
                    userNameTxt.setVisibility(View.GONE);
                    userNameInput.setVisibility(View.GONE);
                    confirmPasswordErrorTxt.setVisibility(View.GONE);
                    passwordErrorTxt.setVisibility(View.GONE);
                    emailErrorTxt.setVisibility(View.GONE);
                    userNameErrorTxt.setVisibility(View.GONE);

                    hasAccoount = true;
                } else {
                    choiceTxt.setText("Login");
                    titleTxt.setText("Register a new account");
                    qstTxt.setText("Already have an account ?");
                    forgetPasswordTxt.setVisibility(View.GONE);
                    confirmPasswordInput.setVisibility(View.VISIBLE);
                    confirmPasswordTxt.setVisibility(View.VISIBLE);
                    userNameInput.setVisibility(View.VISIBLE);
                    userNameTxt.setVisibility(View.VISIBLE);
                    signBtn.setText("Sign Up");
                    confirmPasswordErrorTxt.setVisibility(View.GONE);
                    passwordErrorTxt.setVisibility(View.GONE);
                    emailErrorTxt.setVisibility(View.GONE);
                    userNameErrorTxt.setVisibility(View.GONE);


                    hasAccoount = false;
                }
            }


        });


        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasAccoount){
                    creatNewAccount();
                }else{
                    AllowUserToLogIn();
                }
            }
        });
    }

    private void AllowUserToLogIn() {

        String loginEmail = userEmail.getText().toString();
        String loginPassword = userPassword.getText().toString();

        if (TextUtils.isEmpty(loginEmail)) {
            emailErrorTxt.setVisibility(View.VISIBLE);
        }else{
            emailErrorTxt.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(loginPassword)) {
            passwordErrorTxt.setVisibility(View.VISIBLE);
        }else{
            passwordErrorTxt.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(loginEmail) || TextUtils.isEmpty(loginPassword) ){
            isProblem = true ;
        }else{
            isProblem = false;
        }

        if(!isProblem){
            progressDialog.setTitle("svp attendez ..");
            progressDialog.setMessage("Connexion en cours ...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            firebaseAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(SignInActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        sendUserToHomeActivity();
                    }else {
                        Toast.makeText(SignInActivity.this, "error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });

        }
    }

    private void sendUserToChoosingFieldsActivity() {
        Intent intent = new Intent(this , ChoosingFieldsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void creatNewAccount() {

        String email = userEmail.getText().toString();
        String name = userName.getText().toString().toLowerCase();
        String password = userPassword.getText().toString();
        String confirmPassword = userConfirmPassword.getText().toString();


        if (TextUtils.isEmpty(email)) {
            emailErrorTxt.setVisibility(View.VISIBLE);
        }else{
            emailErrorTxt.setVisibility(View.GONE);
        }



        if (TextUtils.isEmpty(name)) {
            userNameErrorTxt.setVisibility(View.VISIBLE);
        }else{
            userNameErrorTxt.setVisibility(View.GONE);
        }


        if (TextUtils.isEmpty(password)) {
            passwordErrorTxt.setVisibility(View.VISIBLE);
        }else {
            passwordErrorTxt.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordErrorTxt.setText("Please confirm your password");
            confirmPasswordErrorTxt.setVisibility(View.VISIBLE);
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordErrorTxt.setText("Password does not match");
            confirmPasswordErrorTxt.setVisibility(View.VISIBLE);
        }else {
            confirmPasswordErrorTxt.setText("Please confirm your password");
            confirmPasswordErrorTxt.setVisibility(View.GONE);
        }


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            isProblem = true;
        } else {
            isProblem = false;
        }


        if (!isProblem) {

            progressDialog.setTitle("Svp Attendez ...");
            progressDialog.setMessage("Inscription en cours...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        currentUserId = firebaseAuth.getCurrentUser().getUid();
                        HashMap hashMap = new HashMap();
                        hashMap.put("email" , email);
                        hashMap.put("password" , password);
                        hashMap.put("name" , name);
                        userRef.child(currentUserId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(SignInActivity.this, "successfull", Toast.LENGTH_SHORT).show();

                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(SignInActivity.this , ChoosingActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();

                                        }
                                    }
                                });
                            }

                        });

                    } else {
                        Toast.makeText(SignInActivity.this, "erreur : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }

    private void checkUserConnected(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            sendUserToHomeActivity();
        }
    }

    private void sendUserToHomeActivity() {
        Intent intent = new Intent(this , HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserConnected();
    }
}