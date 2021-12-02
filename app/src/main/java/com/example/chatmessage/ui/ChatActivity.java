package com.example.chatmessage.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatmessage.models.Message;
import com.example.chatmessage.adapters.MessagesAdapter;
import com.example.chatmessage.R;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import javax.annotation.Nullable;

public class ChatActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private static final int RC_GET_IMAGE = 101;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference reference;

    private RecyclerView recyclerViewMessages;
    private MessagesAdapter adapter;

    private EditText editTextMessage;
    private CardView imageViewSendMessage;
    private ImageView imageViewAddImage;
    String imageUri= "https://static.vecteezy.com/system/resources/previews/002/387/693/non_2x/user-profile-icon-free-vector.jpg";
    private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        // Create a storage reference from our app
        reference = storage.getReference();
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        imageViewAddImage = findViewById(R.id.imageViewAddImage);
        adapter = new MessagesAdapter(this);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(adapter);

        if (editTextMessage.length()<0){
            imageViewSendMessage.setVisibility(View.GONE);
        }









        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(editTextMessage.getText().toString().trim(), null);
            }
        });
        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent, RC_GET_IMAGE);
            }
        });
        if (mAuth.getCurrentUser() != null) {
            preferences.edit().putString("author", mAuth.getCurrentUser().getEmail()).apply();
        } else {
            mAuth.signOut();
        }
    }

    private void sendMessage(String textOfMessage, String urlToImage) {
        Message message = null;
       String author = preferences.getString("author", "Raimzhanov");

        if (textOfMessage != null && !textOfMessage.isEmpty()) {
            message = new Message(author, textOfMessage, System.currentTimeMillis(), null,imageUri);
        } else if (urlToImage != null && !urlToImage.isEmpty()) {
            message = new Message(author, null, System.currentTimeMillis(), urlToImage,imageUri);
        }
        else if (textOfMessage.isEmpty() && urlToImage == null){
            Toast.makeText(ChatActivity.this, "Жаз дейм", Toast.LENGTH_SHORT).show();
            return;
        }
        firestore.collection("messages").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                editTextMessage.setText("");
                recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
            firestore.collection("messages").orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null) {
                        List<Message> messages = queryDocumentSnapshots.toObjects(Message.class);
                        adapter.setMessages(messages);
                        recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);
                    }
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GET_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();
                if (uri != null) {
                    final StorageReference referenceToImage = reference.child("images/" + uri.getLastPathSegment());
                    referenceToImage.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }

                            // Continue with the task to get the download URL
                            return referenceToImage.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                if (downloadUri != null) {
                                    sendMessage(null, downloadUri.toString());
                                }
                            } else {
                            }
                        }
                    });
                }
            }
        }

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
                    preferences.edit().putString("author", user.getEmail()).apply();
                }
                // ...
            } else {
                if (response != null) {
                    Toast.makeText(this, "Error: " + response.getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}