package com.example.bibleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class SettingsActivity extends AppCompatActivity {

    Button btnIncrease, btnDecrease;

    TextView btnVoice;
    TextView tvFontPreview;

    SeekBar seekAudio;
    SeekBar seekRate;

    CheckBox cbReminder;

    BottomNavigationView bottomNavigationView;

    SharedPreferences prefs;

    int fontSize = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("BiblePrefs", MODE_PRIVATE);

        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);

        btnVoice = findViewById(R.id.btnVoice);

        tvFontPreview = findViewById(R.id.tvFontPreview);

        seekAudio = findViewById(R.id.seekAudio);
        seekRate = findViewById(R.id.seekRate);

        cbReminder = findViewById(R.id.cbReminder);

        loadSettings();

        btnIncrease.setOnClickListener(v -> {

            if (fontSize < 32) {
                fontSize++;

                tvFontPreview.setTextSize(fontSize);

                prefs.edit()
                        .putInt("pref_font_size", fontSize)
                        .apply();
            }
        });

        btnDecrease.setOnClickListener(v -> {

            if (fontSize > 12) {
                fontSize--;

                tvFontPreview.setTextSize(fontSize);

                prefs.edit()
                        .putInt("pref_font_size", fontSize)
                        .apply();
            }
        });

        seekAudio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser) {}

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar) {

                        prefs.edit()
                                .putFloat(
                                        "pref_audio_volume",
                                        seekBar.getProgress() / 100f
                                )
                                .apply();
                    }
                });

        seekRate.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser) {}

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar) {

                        float rate = seekBar.getProgress() / 100f;

                        if (rate < 0.5f) rate = 0.5f;

                        prefs.edit()
                                .putFloat("pref_speech_rate", rate)
                                .apply();
                    }
                });

        setupBottomNav();
    }

    private void loadSettings() {

        fontSize =
                prefs.getInt("pref_font_size", 18);

        tvFontPreview.setTextSize(fontSize);

        seekAudio.setProgress(
                (int) (
                        prefs.getFloat(
                                "pref_audio_volume",
                                1f
                        ) * 100
                )
        );

        float rate =
                prefs.getFloat(
                        "pref_speech_rate",
                        1f
                );

        seekRate.setProgress((int) (rate * 100));

        cbReminder.setChecked(
                prefs.getBoolean(
                        "pref_reminder_enabled",
                        true
                )
        );
    }

    private void setupBottomNav(){

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