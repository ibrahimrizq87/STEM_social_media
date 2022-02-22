package com.example.fireinstgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fireinstgram.fragmen.HomeFragment;
import com.example.fireinstgram.fragmen.NotificationFragment;
import com.example.fireinstgram.fragmen.ProfileFragment;
import com.example.fireinstgram.fragmen.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainPage extends AppCompatActivity {

    private BottomNavigationView navigationView;

    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

    navigationView = findViewById(R.id.bottom_navigation);

    navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){

                case R.id.nav_home:
                    fragment = new HomeFragment();
                    break;

                case R.id.nav_search:
                    fragment = new SearchFragment();
                    break;

                case R.id.nav_add:
                    startActivity(new Intent(MainPage.this,PostActivity.class));
                    fragment = null;
                    break;

                case R.id.nav_heart:
                    fragment = new NotificationFragment();
                    break;

                case R.id.nav_profile:
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(MainPage.this, "you logged out successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainPage.this, MainActivity.class));
                    break;


            }
 if(fragment != null){
     getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();


 }
 return true;

        }
    });

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();

    }


}
