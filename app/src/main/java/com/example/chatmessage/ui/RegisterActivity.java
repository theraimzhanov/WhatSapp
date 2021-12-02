package com.example.chatmessage.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatmessage.R;
import com.example.chatmessage.models.Users;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static int AUTH = 78;
    private static int GET_IMAGE = 77;
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword, editTextName;
    private ImageView imageViewProfil;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private TextView textViewHaveAnAccount;
    private LinearLayout layout;
    private Uri uriImage;
    private ProgressDialog progressDialog;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    public  static     String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initFarebase();
        inirProgresDiolod();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.TwitterBuilder().build());

// Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers).setTheme(R.style.LoginTheme)
                                .build(),
                        AUTH);
            }
        });

        textViewHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        imageViewProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_IMAGE);
            }
        });
    }

    private void inirProgresDiolod() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Ждите...");
        progressDialog.setCancelable(false);
    }


    private void initFarebase() {
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private void initViews() {
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextName = findViewById(R.id.editName);
        layout = findViewById(R.id.registrationUi);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewHaveAnAccount = findViewById(R.id.textViewHaveAnAccount);
        imageViewProfil = findViewById(R.id.imageProfil);
    }

    public void onClickRegister(View view) {
        progressDialog.show();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String conPs = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(conPs)) {
            progressDialog.dismiss();
            showToast("Please enter all");
        } else if (!email.matches(emailPattern)) {
            progressDialog.dismiss();
            showToast("Enter valid email");
        } else if (!password.equals(conPs)) {
            progressDialog.dismiss();
            showToast("Passwords does not equal");
        } else if (password.length() < 6) {
            progressDialog.dismiss();
            showToast("Password must > 5");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        StorageReference storageReference = storage.getReference().child("images").child(email);
                        if (uriImage != null) {
                            storageReference.putFile(uriImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                             imageUri = uri.toString();
                                            Date date = new Date();
                                            Users user = new Users(name,email,imageUri,date.getTime(),mAuth.getUid());
                                            firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                       startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                        else if (uriImage == null){
                            Date date = new Date();
                          imageUri = "https://static.vecteezy.com/system/resources/previews/002/387/693/non_2x/user-profile-icon-free-vector.jpg";
                            Users user = new Users( name, email, imageUri,date.getTime(),mAuth.getUid());
                            firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                               startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                                }
                            });
                        }
                    } else {
                        showToast("Ошибка");
                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE) {
            if (data != null) {
                uriImage = data.getData();
                imageViewProfil.setImageURI(uriImage);
            }
        }
    }

    private void showToast(String toast) {
        Toast.makeText(RegisterActivity.this, toast, Toast.LENGTH_SHORT).show();
    }
}
