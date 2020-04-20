package com.coder24.letsconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    RecyclerView myContactList;
    ImageView findPeopleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findPeopleBtn = findViewById(R.id.find_people_btn);
        myContactList = (RecyclerView) findViewById(R.id.contact_lists);
        myContactList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        findPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FindPeopleActivity.class));
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId()){
                        case R.id.navigation_home:
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            break;

                        case R.id.navigation_setting:
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                            break;

                        case R.id.navigation_notifications:
                            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                            break;

                        case R.id.navigation_logout:
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                            finish();
                            break;
                    }
                    return true;
                }
            };

}
