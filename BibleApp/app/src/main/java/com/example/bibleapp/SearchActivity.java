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
        loadBooksFromJson();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchBooks(newText);
                return true;
            }
        });
    }

    private void searchBooks(String query) {

        resultsContainer.removeAllViews();

        if (query.trim().isEmpty()) {
            resetUI();
            return;
        }

        ArrayList<String> matches = new ArrayList<>();

        for (String book : books) {
            if (book.toLowerCase().contains(query.toLowerCase())) {
                matches.add(book);
            }
        }

        if (matches.isEmpty()) {

            tvResultCount.setVisibility(View.GONE);

            tvEmpty.setText("No books found for \"" + query + "\".");
            tvEmpty.setVisibility(View.VISIBLE);

            return;
        }

        // SHOW RESULTS
        tvEmpty.setVisibility(View.GONE);
        tvResultCount.setVisibility(View.VISIBLE);

        tvResultCount.setText(matches.size() + " books found");

        for (String book : matches) {

            Button btn = new Button(this);
            btn.setText(book);
            btn.setAllCaps(false);

            btn.setBackgroundColor(
                    getResources().getColor(R.color.button_bg)
            );

            btn.setTextColor(Color.BLACK);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            params.setMargins(0, 0, 0, 16);
            btn.setLayoutParams(params);

            btn.setOnClickListener(v -> {

                Intent intent =
                        new Intent(SearchActivity.this, ReadActivity.class);

                intent.putExtra("bookName", book);
                startActivity(intent);
                overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );
            });

            resultsContainer.addView(btn);
        }
    }

    private void resetUI() {

        resultsContainer.removeAllViews();

        tvResultCount.setVisibility(View.GONE);


        tvEmpty.setText("Results will show here.");
        tvEmpty.setVisibility(View.VISIBLE);
    }

    private void loadBooksFromJson() {

        try {

            InputStream is = getAssets().open("bible.json");

            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer);

            JSONArray bibleArray = new JSONArray(json);

            books.clear();

            for (int i = 0; i < bibleArray.length(); i++) {

                JSONObject bookObj =
                        bibleArray.getJSONObject(i);

                String bookName =
                        bookObj.getString("name");

                books.add(bookName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}