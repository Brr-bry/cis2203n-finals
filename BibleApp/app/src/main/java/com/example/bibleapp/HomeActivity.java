package com.example.bibleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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
                    overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                    );
                }else if(id == R.id.nav_read){
                    Intent newIntent = new Intent(HomeActivity.this, ReadActivity.class);
                    startActivity(newIntent);
                    overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                    );
                }else if(id == R.id.nav_settings){
                    Intent  newIntent= new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(newIntent);
                    overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                    );
                }

                return false;


            }
        });

        loadVerse();

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
        overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
        );
    }
    private void loadVerse() {

        new Thread(() -> {

            HttpURLConnection connection = null;

            try {
                URL url = new URL("https://beta.ourmanna.com/api/v1/get?format=json");

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();

                JSONObject obj = new JSONObject(sb.toString());

                String verse ="\"" +
                        obj.getJSONObject("verse")
                                .getJSONObject("details")
                                .optString("text", "No verse found") + "\"";
                String ref =
                        obj.getJSONObject("verse")
                                .getJSONObject("details")
                                .optString("reference", "No passage found")
                        ;

                runOnUiThread(() ->{

                            tvVerse.setText(verse);
                            tvReference.setText(ref);
                        }
                );

            } catch (Exception e) {

                runOnUiThread(() ->
                        tvVerse.setText("Unable to load verse.")
                );

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

}