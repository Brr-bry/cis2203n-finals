package com.example.bibleapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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

        cbReminder.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {

                        if (android.os.Build.VERSION.SDK_INT >= 33 &&
                                checkSelfPermission(
                                        Manifest.permission.POST_NOTIFICATIONS
                                )
                                        != PackageManager.PERMISSION_GRANTED) {

                            cbReminder.setChecked(false);

                            com.google.android.material.snackbar.Snackbar
                                    .make(
                                            buttonView,
                                            "Enable notifications in Settings to use this feature.",
                                            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                                    )
                                    .show();

                            prefs.edit()
                                    .putBoolean(
                                            "pref_reminder_enabled",
                                            false
                                    )
                                    .apply();

                            return;
                        }

                        scheduleReminder();

                    } else {

                        cancelReminder();
                    }

                    prefs.edit()
                            .putBoolean(
                                    "pref_reminder_enabled",
                                    isChecked
                            )
                            .apply();
                });

        btnVoice.setOnClickListener(v -> {

            String[] voices = {
                    "default",
                    "deep",
                    "high"
            };

            new android.app.AlertDialog.Builder(this)
                    .setTitle("Select Voice")
                    .setItems(voices, (dialog, which) -> {

                        String selectedVoice =
                                voices[which];

                        prefs.edit()
                                .putString(
                                        "pref_bible_voice",
                                        selectedVoice
                                )
                                .apply();

                        btnVoice.setText(
                                "Audio Bible Voice › "
                                        + selectedVoice
                        );
                    })
                    .show();
        });

        requestNotificationPermissionIfNeeded();
        setupBottomNav();
    }

    private void loadSettings() {

        fontSize =
                prefs.getInt("pref_font_size", 18);

        tvFontPreview.setTextSize(fontSize);

        seekAudio.setProgress(
                (int)(
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

        seekRate.setProgress((int)(rate * 100));

        cbReminder.setChecked(
                prefs.getBoolean(
                        "pref_reminder_enabled",
                        true
                )
        );

        String voice =
                prefs.getString(
                        "pref_bible_voice",
                        "default"
                );

        btnVoice.setText(
                "Audio Bible Voice › " + voice
        );
    }

    private void scheduleReminder() {

        Calendar now = Calendar.getInstance();

        Calendar next7am = Calendar.getInstance();
        next7am.set(Calendar.HOUR_OF_DAY, 7);
        next7am.set(Calendar.MINUTE, 0);
        next7am.set(Calendar.SECOND, 0);
        next7am.set(Calendar.MILLISECOND, 0);

        // if already past 7AM today → schedule tomorrow
        if (now.after(next7am)) {
            next7am.add(Calendar.DAY_OF_YEAR, 1);
        }

        long delay =
                next7am.getTimeInMillis() - now.getTimeInMillis();

        OneTimeWorkRequest request =
                new OneTimeWorkRequest.Builder(DailyVerseWorker.class)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(this)
                .enqueueUniqueWork(
                        "daily_verse_once",
                        ExistingWorkPolicy.REPLACE,
                        request
                );
    }

    private void cancelReminder() {

        WorkManager.getInstance(this)
                .cancelUniqueWork(
                        "dailyVerseReminder"
                );
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

    private static final int NOTIF_PERMISSION_CODE = 100;

    private void requestNotificationPermissionIfNeeded() {

        if (android.os.Build.VERSION.SDK_INT >= 33) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIF_PERMISSION_CODE
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIF_PERMISSION_CODE) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission granted
            } else {

                // optional: disable reminder toggle
                cbReminder.setChecked(false);
            }
        }
    }
}