package com.example.easyexchangetrialfeatures.RegisterAndLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyexchangetrialfeatures.ActivityDashboard;
import com.example.easyexchangetrialfeatures.R;
import com.example.easyexchangetrialfeatures.Chat.UsersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmailEnt,mPasswordEnt;
    Button mRegisterBtn;
    TextView login1;


    //
    ProgressDialog progressDialog;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        mEmailEnt = findViewById(R.id.emailEnt);
        mPasswordEnt = findViewById(R.id.passwordEnt);
        mRegisterBtn = findViewById(R.id.register_button);
        login1 = findViewById(R.id.have_account_TV);



        mAuth = FirebaseAuth.getInstance();




        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering user....");

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEnt.getText().toString().trim();
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
                    retgisteruser(email,password);
                }



            }
        });

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });






    }

    private void retgisteruser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();


                            String email = user.getEmail().toString();
                            String uid = user.getUid();

                            HashMap<Object,String> hashMap = new HashMap<>();

                            hashMap.put("email",email);
                            hashMap.put("uid",uid);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");

                            reference.child(uid).setValue(hashMap);



                            Toast.makeText(RegisterActivity.this,"Registering user ...",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, ActivityDashboard.class);
                            intent.putExtra("email",mAuth.getCurrentUser().getEmail());
                            intent.putExtra("uid", mAuth.getCurrentUser().getUid());
                            startActivity(intent);


                            //startActivity(new Intent(RegisterActivity.this, UsersActivity.class));
                            //updateUI(user);
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });


    }
}