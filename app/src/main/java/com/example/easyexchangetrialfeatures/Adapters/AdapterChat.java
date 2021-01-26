package com.example.easyexchangetrialfeatures.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyexchangetrialfeatures.Models.ModelChat;
import com.example.easyexchangetrialfeatures.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{


    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context context;
    ArrayList<ModelChat>  chatArrayList;

    FirebaseUser firebaseUser;

    public AdapterChat(Context context, ArrayList<ModelChat> chatArrayList) {
        this.context = context;
        this.chatArrayList = chatArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
            return new MyHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String message = chatArrayList.get(position).getMessage();
        //String timeStamp = chatArrayList.get(position).getTimestamp();

//        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//        cal.setTimeInMillis(Long.parseLong(timeStamp));
//        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();


        holder.messageTv.setText(message);
        //holder.timeTv.setText("dateTime");

    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatArrayList.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView messageTv,timeTv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);




            messageTv = itemView.findViewById(R.id.messageTv);
            //timeTv = itemView.findViewById(R.id.timeTv);

        }
    }
}
