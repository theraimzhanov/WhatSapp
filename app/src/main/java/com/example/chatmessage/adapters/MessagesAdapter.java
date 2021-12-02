package com.example.chatmessage.adapters;



import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatmessage.models.Message;
import com.example.chatmessage.R;
import com.example.chatmessage.models.Users;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private static final int TYPE_MY_MESSAGE = 0;
    private static final int TYPE_OTHER_MESSAGE = 1;

    private List<Message> messages;
    private Context context;

    public MessagesAdapter(Context context) {
        messages = new ArrayList<>();
        this.context = context;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == TYPE_MY_MESSAGE) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_my_message, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_other_message, viewGroup, false);
        }
        return new MessagesViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        String author = message.getAuthor();
        if (author != null && author.equals(PreferenceManager.getDefaultSharedPreferences(context).getString("author", "Anonim"))) {
            return TYPE_MY_MESSAGE;
        } else {
            return TYPE_OTHER_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder messagesViewHolder, int i) {
        Message message = messages.get(i);
        String author = message.getAuthor();
        String textOfMessage = message.getTextOfMessage();
        String urlToMessage = message.getImageUrl();
        Picasso.get().load(message.getProfilUri()).into(messagesViewHolder.imageViewProfile);
        if (urlToMessage == null || urlToMessage.isEmpty()) {
            messagesViewHolder.imageViewImage.setVisibility(View.GONE);
        } else {
            messagesViewHolder.imageViewImage.setVisibility(View.VISIBLE);
        }
        messagesViewHolder.textViewAuthor.setText(author);
        if (textOfMessage != null && !textOfMessage.isEmpty()) {
            messagesViewHolder.textViewTextOfMessage.setVisibility(View.VISIBLE);
            messagesViewHolder.textViewTextOfMessage.setText(textOfMessage);
        } else {
            messagesViewHolder.textViewTextOfMessage.setVisibility(View.GONE);
        }
        if (urlToMessage != null && !urlToMessage.isEmpty()) {
            Picasso.get().load(urlToMessage).into(messagesViewHolder.imageViewImage);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAuthor;
        private TextView textViewTextOfMessage;
        private ImageView imageViewImage;
        private CircleImageView imageViewProfile;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewTextOfMessage = itemView.findViewById(R.id.textViewTextOfMessage);
            imageViewImage = itemView.findViewById(R.id.imageViewImage);
            imageViewProfile = itemView.findViewById(R.id.image_profile);
        }
    }
}




