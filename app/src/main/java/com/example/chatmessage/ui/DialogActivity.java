package com.example.chatmessage.ui;

import static com.example.chatmessage.ui.RegisterActivity.imageUri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatmessage.R;
import com.example.chatmessage.adapters.DialogAdapter;
import com.example.chatmessage.models.Message;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogActivity extends AppCompatActivity {
private CircleImageView imageViewProfile;
private EditText editTextMessage;
private TextView textViewName;
private CardView sendMessage;
private ImageView imageViewImage,iconBack;
RecyclerView recyclerViewDialog;
    private FirebaseStorage storage;
    String my_collection ;
    String other_collection;
    private FirebaseDatabase database;
    String id;
    private StorageReference reference;
    private FirebaseAuth auth;
    private SharedPreferences preferences;
private static int RC_GET_IMAGE = 12;
DialogAdapter adapter;
List<Message>list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
          database = FirebaseDatabase.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        storage = FirebaseStorage.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        textViewName = findViewById(R.id.chatNameDiolog);
        imageViewProfile = findViewById(R.id.dialogImageViewInChat);
        editTextMessage = findViewById(R.id.ettextMessageDiolog);
        sendMessage = findViewById(R.id.btnSendDiolog);
        imageViewImage = findViewById(R.id.imageViewAddImageDialog);
        iconBack = findViewById(R.id.btBackDialog);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DialogActivity.this,HomeActivity.class));
                finish();
            }
        });
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String image = intent.getStringExtra("image");
         id = intent.getStringExtra("uid");
        Picasso.get().load(image).into(imageViewProfile);
        textViewName.setText(name);
        recyclerViewDialog=  findViewById(R.id.recyclerChatDiolog);
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DialogAdapter(this);
        list = new ArrayList<>();

          imageViewImage.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                  intent.setType("image/jpeg");
                  intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                  startActivityForResult(intent, RC_GET_IMAGE);
              }
          });


          sendMessage.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  sendMessage(editTextMessage.getText().toString().trim(),null);
              }
          });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                                Toast.makeText(DialogActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
    }
    private void sendMessage(String textOfMessage, String urlToImage){
        Message message = null;
        FirebaseUser user = auth.getCurrentUser();
        String my_collection = user.getUid()+id;
        String other_collection = id+user.getUid();
        String author = preferences.getString("author", "Anonim");
        Date date = new Date();
        if (textOfMessage != null && !textOfMessage.isEmpty()) {
            message = new Message(author, textOfMessage, System.currentTimeMillis(), null,imageUri);
        } else if (urlToImage != null && !urlToImage.isEmpty()) {
            message = new Message(author, null, System.currentTimeMillis(), urlToImage,imageUri);
        }
        else if (textOfMessage.isEmpty() && urlToImage == null){
            Toast.makeText(DialogActivity.this, "Жаз дейм", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}

