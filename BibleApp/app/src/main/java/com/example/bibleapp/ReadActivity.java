package com.example.bibleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class ReadActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    TextView label;
    TextView chapterView;
    TextView tvChapter;


    ImageButton btnNext, btnPrev, btnBack;

    ImageButton btnAuto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        label = findViewById(R.id.tvBookLabel);
        chapterView = findViewById(R.id.tvVerses);
        tvChapter = findViewById(R.id.tvChapter);

        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrevious);
        btnBack = findViewById(R.id.btnBack);
        btnAuto = findViewById(R.id.btnAuto);

        setupBottomNav();

    }
    private void setupBottomNav() {

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_read);

        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        int id = item.getItemId();

                        if (id == R.id.nav_home) {
                            startActivity(new Intent(ReadActivity.this, HomeActivity.class));
                            overridePendingTransition(
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            );
                        } else if (id == R.id.nav_search) {
                            startActivity(new Intent(ReadActivity.this, SearchActivity.class));
                            overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                            );
                        } else if (id == R.id.nav_settings) {
                            startActivity(new Intent(ReadActivity.this, SettingsActivity.class));
                        }

                        return true;
                    }
                });
    }

}