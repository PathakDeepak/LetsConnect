package com.coder24.letsconnect;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FindPeopleActivity extends AppCompatActivity {

    private RecyclerView findFriends;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);

        searchText = findViewById(R.id.search_user);
        findFriends = findViewById(R.id.find_friends_lists);
        findFriends.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    //Since layout is imported from other layout and not notificaiton layout
    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTxt;
        Button videoCallBtn;
        ImageView profileImageView;
        RelativeLayout cardView;

        public FindFriendsViewHolder(@NonNull View view) {
            super(view);

            userNameTxt = view.findViewById(R.id.name_contact);
            videoCallBtn= view.findViewById(R.id.call_btn);
            profileImageView = view.findViewById(R.id.image_contact);
            cardView = view.findViewById(R.id.card_view1);


        }
    }

}
