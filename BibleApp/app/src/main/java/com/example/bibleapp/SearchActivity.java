package com.example.bibleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class SearchActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if(id == R.id.nav_settings){
                    Intent newIntent = new Intent( SearchActivity.this, SettingsActivity.class);
                    startActivity(newIntent);
                    overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                    );
                }else if(id == R.id.nav_read){
                    Intent newIntent = new Intent(SearchActivity.this, ReadActivity.class);
                    startActivity(newIntent);
                    overridePendingTransition(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    );
                }else if(id == R.id.nav_home){
                    Intent  newIntent= new Intent(SearchActivity.this, HomeActivity.class);
                    startActivity(newIntent);
                    overridePendingTransition(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    );
                }

                return false;


            }
        });
    }






}