package com.example.bibleapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    TextView tvEmpty, tvResultCount;

    LinearLayout resultsContainer;
    BottomNavigationView bottomNavigationView;

    ArrayList<String> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvResultCount = findViewById(R.id.tvResultCount);
        resultsContainer = findViewById(R.id.resultsContainer);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if(id == R.id.nav_settings){
                    Intent newIntent = new Intent( SearchActivity.this, SettingsActivity.class);
                    startActivity(newIntent);

                }else if(id == R.id.nav_read){
                    Intent newIntent = new Intent(SearchActivity.this, ReadActivity.class);
                    startActivity(newIntent);

                }else if(id == R.id.nav_home){
                    Intent  newIntent= new Intent(SearchActivity.this, HomeActivity.class);
                    startActivity(newIntent);

                }

                return false;


            }
        });
    }






}