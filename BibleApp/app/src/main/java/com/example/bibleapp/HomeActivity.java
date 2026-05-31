package com.example.bibleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeActivity extends AppCompatActivity {

    TextView tvVerse;
    TextView tvReference;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvVerse = findViewById(R.id.tv_verse);
        tvReference = findViewById(R.id.tv_reference);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if(id == R.id.nav_search){
                    Intent newIntent = new Intent( HomeActivity.this, SearchActivity.class);
                    startActivity(newIntent);

                }else if(id == R.id.nav_read){
                    Intent newIntent = new Intent(HomeActivity.this, ReadActivity.class);
                    startActivity(newIntent);

                }else if(id == R.id.nav_settings){
                    Intent  newIntent= new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(newIntent);

                }

                return false;


            }
        });


        findViewById(R.id.btnGenesis).setOnClickListener(v -> openBook("Genesis"));
        findViewById(R.id.btnPsalms).setOnClickListener(v -> openBook("Psalms"));
        findViewById(R.id.btnProverbs).setOnClickListener(v -> openBook("Proverbs"));
        findViewById(R.id.btnJohn).setOnClickListener(v -> openBook("John"));
        findViewById(R.id.btnMatthew).setOnClickListener(v -> openBook("Matthew"));
        findViewById(R.id.btnActs).setOnClickListener(v -> openBook("Acts"));
        findViewById(R.id.btnRomans).setOnClickListener(v -> openBook("Romans"));

    }

    private void openBook(String bookName) {
        Intent intent = new Intent(HomeActivity.this, ReadActivity.class);
        intent.putExtra("book", bookName);
        startActivity(intent);
    }


}