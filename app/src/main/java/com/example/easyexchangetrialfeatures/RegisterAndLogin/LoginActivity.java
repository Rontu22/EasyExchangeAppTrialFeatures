package com.example.easyexchangetrialfeatures.RegisterAndLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyexchangetrialfeatures.ActivityDashboard;
import com.example.easyexchangetrialfeatures.R;
import com.example.easyexchangetrialfeatures.Chat.UsersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    ActionBar actionBar;
    EditText mEmailEnt,mPasswordEnt;
    Button mLoginBtn;
    TextView toRegister;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Login");


        mEmailEnt = findViewById(R.id.emailEnt_Login);
        mPasswordEnt = findViewById(R.id.passwordEnt_Login);
        mLoginBtn = findViewById(R.id.login_button);
        toRegister = findViewById(R.id.not_have_account);

        mAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEnt.getText().toString();
                String password = mPasswordEnt.getText().toString();


                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    mEmailEnt.setError("Invalid Email");
                    mEmailEnt.setFocusable(true);
                }
                else if (password.length() < 6)
                {
                    mPasswordEnt.setError("Password length must be 6 characters long ");
                    mPasswordEnt.setFocusable(true);
                }
                else {
                    loginUser(email,password);
                }


            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser(String email, String password) {
        progressDialog.setMessage("Logging in user ...");
        progressDialog.show();



        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();


//                            String email = user.getEmail().toString();
//                            String uid = user.getUid();
//
//                            HashMap<Object, String> hashMap = new HashMap<>();
//
//                            hashMap.put("email",email);
//                            hashMap.put("uid",uid);
//
//                            FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            DatabaseReference reference = database.getReference("Users");
//
//                            reference.child(uid).setValue(hashMap);
//
//                            Toast.makeText(Try.this,"Registering user ...",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(Try.this,UsersActivity.class));




                            //if user is signing in first time then get and show info from
                            // google account
                            if (task.getResult().getAdditionalUserInfo().isNewUser())
                            {
                                //get-user email and uid from auth
                                String email = user.getEmail().toString();
                                String uid = user.getUid();

                                //When user is registered store user info in the
                                //firebase realtime database too using HashMap
                                HashMap<Object,String> hashMap = new HashMap<>();
                                //put these info in HashMap
                                hashMap.put("email",email);
                                hashMap.put("uid",uid);
//                                hashMap.put("name",""); // will add later (e.g. edit profile)
//                                hashMap.put("phone","");// will add later (e.g. edit profile)
//                                hashMap.put("image","");// will add later (e.g. edit profile)
//                                hashMap.put("cover","");

                                // firebase database instance
                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                // path to store user data names "Users"
                                DatabaseReference reference = database.getReference("Users");

                                // put data within hashMap in database
                                reference.child(uid).setValue(hashMap);
                            }



                            Intent intent = new Intent(LoginActivity.this, ActivityDashboard.class);
                            intent.putExtra("email",mAuth.getCurrentUser().getEmail());
                            intent.putExtra("uid", mAuth.getCurrentUser().getUid());
                            startActivity(intent);



                            //startActivity(new Intent(LoginActivity.this, UsersActivity.class));
//                            finish();




                            //updateUI(user);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}