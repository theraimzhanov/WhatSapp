package com.example.chatmessage.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chatmessage.fragments.CallFragment;
import com.example.chatmessage.fragments.StatusFragment;
import com.example.chatmessage.fragments.UsersFragment;

public class FragmentAdapter extends FragmentStateAdapter {


public FragmentAdapter(@NonNull FragmentManager fragmentManager,@NonNull Lifecycle lifecycle){
    super(fragmentManager,lifecycle);

}


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new StatusFragment();
            case 2:
                return new CallFragment();
        }
        return new UsersFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
