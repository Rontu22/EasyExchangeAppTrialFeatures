package com.example.easyexchangetrialfeatures.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangetrialfeatures.Chat.ChatActivity;
import com.example.easyexchangetrialfeatures.Models.ModelUser;
import com.example.easyexchangetrialfeatures.R;

import java.util.ArrayList;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyHolder> {

    Context context;
    ArrayList<ModelUser> userList;

    public AdapterUser(Context context, ArrayList<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
         View view = LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);

         return new MyHolder(view);




    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        final String hisUID = userList.get(position).getUid();
        final String userEmail = userList.get(position).getEmail();

        holder.mUidTV.setText(hisUID);
        holder.mEmailTV.setText(userEmail);


//         handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid",hisUID);
                context.startActivity(intent);

//                context.startActivity(new Intent(context, TrialActivity1.class));
            }
        });

//        holder.mUidTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                context.startActivity(new Intent(context, Try.class));
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView mUidTV,mEmailTV;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //context = itemView.getContext();

            mEmailTV = itemView.findViewById(R.id.emailTV1);
            mUidTV = itemView.findViewById(R.id.uidTV1);


        }

//        @Override
//        public void onClick(View view) {
//            Toast.makeText(context,"The item Clicked is : "+getAdapterPosition(),Toast.LENGTH_SHORT).show();
//            Log.d("Position",getAdapterPosition()+"");
//            Intent intent;
//            intent = new Intent(context,ChatActivity.class);
//            context.startActivity(intent);
//
//        }
    }
}
