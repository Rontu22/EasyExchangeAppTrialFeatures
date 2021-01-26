package com.example.easyexchangetrialfeatures.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangetrialfeatures.Adapters.AdapterUser;
import com.example.easyexchangetrialfeatures.Models.ModelUser;
import com.example.easyexchangetrialfeatures.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    
    AdapterUser adapterUser;
    ArrayList<ModelUser> userList;
    
    
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        

        actionBar = getSupportActionBar();
        actionBar.setTitle("Users");


        firebaseAuth = FirebaseAuth.getInstance();
        
        recyclerView = findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        
        
        userList = new ArrayList<>();
        
        getAllUsers();
    
    }

    private void getAllUsers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot ds: snapshot.getChildren())
                {
                    ModelUser modelUser = ds.getValue(ModelUser.class);

                    //
                    if (!modelUser.getUid().equals(firebaseUser.getUid()))
                    {
                        userList.add(modelUser);
                    }

                    // adapter
                    adapterUser = new AdapterUser(UsersActivity.this,userList);


                    // set adapter to recyclerview
                    recyclerView.setAdapter(adapterUser);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}