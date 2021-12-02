package com.example.chatmessage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatmessage.R;
import com.example.chatmessage.models.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.DiologViewHolder> {
    private static final int TYPE_MY_MESSAGE = 0;
    private static final int TYPE_OTHER_MESSAGE = 1;
    private List<Message> messageList;
    private Context context;

    public DialogAdapter( Context context) {
        messageList = new ArrayList<>();
        this.context = context;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiologViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_MY_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_my_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_other_message, parent, false);
        }
        return new DiologViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiologViewHolder holder, int position) {
        Message message = messageList.get(position);
        String author = message.getAuthor();
        String textOfMessage = message.getTextOfMessage();
        String urlToMessage = message.getImageUrl();
        Picasso.get().load(message.getProfilUri()).into(holder.imageViewProfile);
        if (urlToMessage == null || urlToMessage.isEmpty()) {
            holder.imageViewImage.setVisibility(View.GONE);
        } else {
            holder.imageViewImage.setVisibility(View.VISIBLE);
        }
        holder.textViewAuthor.setText(author);
        if (textOfMessage != null && !textOfMessage.isEmpty()) {

            holder.textViewTextOfMessage.setVisibility(View.VISIBLE);

            holder.textViewTextOfMessage.setText(textOfMessage);
        } else {

            holder.textViewTextOfMessage.setVisibility(View.GONE);
        }
        if (urlToMessage != null && !urlToMessage.isEmpty()) {
            Picasso.get().load(urlToMessage).into(holder.imageViewProfile);
    }
    }
    @Override
    public int getItemCount() {
        return messageList.size();
    }


    class DiologViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewAuthor;
        private TextView textViewTextOfMessage;
        private ImageView imageViewImage;
        private CircleImageView imageViewProfile;
      public DiologViewHolder(@NonNull View itemView) {
          super(itemView);
          textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
          textViewTextOfMessage = itemView.findViewById(R.id.textViewTextOfMessage);
          imageViewImage = itemView.findViewById(R.id.imageViewImage);
          imageViewProfile = itemView.findViewById(R.id.image_profile);
      }

    }
}
