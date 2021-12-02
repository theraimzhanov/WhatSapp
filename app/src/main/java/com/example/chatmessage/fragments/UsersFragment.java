package com.example.chatmessage.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chatmessage.R;
import com.example.chatmessage.adapters.UserAdapter;
import com.example.chatmessage.models.Message;
import com.example.chatmessage.models.Users;
import com.example.chatmessage.ui.ChatActivity;
import com.example.chatmessage.ui.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerViewUser;
    private UserAdapter adapter;
    private FirebaseFirestore firestore;
    private List<Users> usersList;
    private FloatingActionButton fav;


    public UsersFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
          firestore =FirebaseFirestore.getInstance();

        recyclerViewUser = getActivity().findViewById(R.id.userRecycler);
        fav = getActivity().findViewById(R.id.gotoGroup);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        usersList = new ArrayList<>();
        adapter = new UserAdapter(getActivity());
        recyclerViewUser.setAdapter(adapter);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        firestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    List<Users> users = queryDocumentSnapshots.toObjects(Users.class);
                    adapter.setUsersList(users);
                    recyclerViewUser.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });
    }

}