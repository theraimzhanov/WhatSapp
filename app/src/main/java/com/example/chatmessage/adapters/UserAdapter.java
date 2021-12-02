package com.example.chatmessage.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatmessage.R;
import com.example.chatmessage.models.Users;

import com.example.chatmessage.ui.ChatActivity;
import com.example.chatmessage.ui.DialogActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
Context context;
List<Users> usersList;

    public UserAdapter(Context context) {
        this.context = context;
        this.usersList = new ArrayList<>();
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
Users users =usersList.get(position);
        holder.user_name.setText(users.getName());
        holder.user_status.setText("Hey ,I using Chat message...");
        Picasso.get().load(users.getImageUri()).into(holder.user_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("image",users.getImageUri());
                intent.putExtra("uid",users.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
      CircleImageView user_profile;
      TextView user_name,user_status;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            user_profile = itemView.findViewById(R.id.user_profil);
            user_name  = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);
        }
    }

 }
