package com.example.easyexchangetrialfeatures.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangetrialfeatures.Adapters.AdapterChat;
import com.example.easyexchangetrialfeatures.Models.ModelChat;
import com.example.easyexchangetrialfeatures.R;
import com.example.easyexchangetrialfeatures.RegisterAndLogin.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


//public class ChatActivity extends AppCompatActivity{
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//    }
//
//
//}

public class ChatActivity extends AppCompatActivity {

    // views for xml
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageButton sendBtn;
    TextView userid,emailTv;
    EditText messageEnt;


    // add firebase auth
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;




    ArrayList<ModelChat> chatArrayList;

    AdapterChat adapterChat;

    String hisUid;
    String myUid;

//    int count = 0;

    ActionBar actionBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        actionBar = getSupportActionBar();
        actionBar.hide();


        //toolbar = findViewById(R.id.toolbarChatActivity);
        //setSupportActionBar(toolbar);
        //toolbar.setTitle("");
        recyclerView = findViewById(R.id.chat_recyclerView);
        userid = findViewById(R.id.userId);
        emailTv = findViewById(R.id.emailTV);
        messageEnt = findViewById(R.id.messageEnt);
        sendBtn = findViewById(R.id.sendBtn);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");


        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRef = firebaseDatabase.getReference("Users");

        Query userQuery = usersDbRef.orderByChild("uid").equalTo(hisUid);


        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              for (DataSnapshot ds : snapshot.getChildren())
              {
                  String email = ""+ds.child("email").getValue();
                  String uid = ""+ds.child("uid").getValue();
                  emailTv.setText(email);
                  userid.setText(uid);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get Text From editText
                String message = messageEnt.getText().toString().trim();
                // check if text is empty or not
                if (TextUtils.isEmpty(message))
                {
                    userid.setText("Hi there");

                    Toast.makeText(ChatActivity.this,"Can't send empty messages.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //count++;
                    //emailTv.setText("Hi this is test "+count);
                    sendMessage(message);
                }



            }
        });


        readMessages();
        //seenMessage();






    }

    private void sendMessage(String message) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //String timeStamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",message);
        //hashMap.put("timestamp","timeStamp");

        databaseReference.child("Chats").push().setValue(hashMap);
        //databaseReference.push().setValue(hashMap);
        //Toast.makeText(getApplicationContext(),"Clicked "+count,Toast.LENGTH_SHORT).show();
        messageEnt.setText("");



    }


//    private void seenMessage() {
//        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
//        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot ds:snapshot.getChildren())
//                {
//                    ModelChat chat = ds.getValue(ModelChat.class);
//
//                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid))
//                    {
//                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
//                        hasSeenHashMap.put("isSeen",true);
//                        ds.getRef().updateChildren(hasSeenHashMap);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    private void readMessages() {
        chatArrayList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid))
                    {
                        chatArrayList.add(chat);
                    }
                    else if (chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid))
                    {
                        chatArrayList.add(chat);
                    }

                    // adapter
                    adapterChat = new AdapterChat(ChatActivity.this,chatArrayList);
                    adapterChat.notifyDataSetChanged();

                    // set adapter to recyclerview
                    recyclerView.setAdapter(adapterChat);


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







    private void checkUserStatus()
    {
        // get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null)
        {
            // user is signed in so stay here
            //mProfileTv.setText(user.getEmail());
            myUid = user.getUid();
            //userid.setText(myUid);// currently signed in user's uid


        }
        else
        {
            // user not signed in, go to MainActivity
            startActivity(new Intent(ChatActivity.this, RegisterActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}