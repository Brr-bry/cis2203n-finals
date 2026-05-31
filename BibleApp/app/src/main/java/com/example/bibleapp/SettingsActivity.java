package com.example.bibleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);


        setupBottomNav();
    }

    private void setupBottomNav() {

        bottomNavigationView =
                findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(
                R.id.nav_settings
        );

        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem item) {

                        int id = item.getItemId();

                        if (id == R.id.nav_home) {
                            startActivity(
                                    new Intent(
                                            SettingsActivity.this,
                                            HomeActivity.class
                                    ));
                            overridePendingTransition(
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            );
                        }

                        else if (id == R.id.nav_search) {
                            startActivity(
                                    new Intent(
                                            SettingsActivity.this,
                                            SearchActivity.class
                                    ));

                            overridePendingTransition(
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            );
                        }

                        else if (id == R.id.nav_read) {
                            startActivity(
                                    new Intent(
                                            SettingsActivity.this,
                                            ReadActivity.class
                                    ));
                            overridePendingTransition(
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            );
                        }

                        return true;
                    }
                });
    }
}