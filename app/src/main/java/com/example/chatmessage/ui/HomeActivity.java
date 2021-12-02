package com.example.chatmessage.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.chatmessage.R;
import com.example.chatmessage.adapters.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    TabLayout tableLayout;
    ViewPager2 viewPager2;
    FragmentAdapter adapter;
    private FirebaseAuth auth;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemSignOut) {
 showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tableLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.viewPager2);
        auth = FirebaseAuth.getInstance();
        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm,getLifecycle());
        viewPager2.setAdapter(adapter);

        tableLayout.addTab(tableLayout.newTab().setText("Users"));
        tableLayout.addTab(tableLayout.newTab().setText("Home"));
        tableLayout.addTab(tableLayout.newTab().setText("Friends"));

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tableLayout.selectTab(tableLayout.getTabAt(position));

            }
        });
    }
    private void showDialog(){
        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.diolog_layout);
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.show();
        TextView textViewNo = dialog.findViewById(R.id.btnNo);
        TextView textViewYes = dialog.findViewById(R.id.btnYes);
        textViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        textViewYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
    }
}