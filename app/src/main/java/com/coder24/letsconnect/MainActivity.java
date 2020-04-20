package com.coder24.letsconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

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
