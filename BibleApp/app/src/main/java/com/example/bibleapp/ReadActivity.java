package com.example.bibleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.os.Handler;

import android.media.AudioManager;


public class ReadActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    TextView label;
    TextView chapterView;
    TextView tvChapter;
    TextToSpeech tts;
    boolean autoMode = false;

    float volume;
    boolean isSpeaking = false;
    float speechRate = 1.0f;
    JSONArray currentVersesArray;
    Handler handler = new Handler();
    Runnable autoTask;

    ImageButton btnNext, btnPrev, btnBack;

    ImageButton btnAuto;

    JSONArray bibleData;

    JSONObject currentBookObj;

    int fontSize;
    String currentBookName = "Genesis";
    int currentChapter = 1;
    private ScrollView scrollView;
    private int verseIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        label = findViewById(R.id.tvBookLabel);
        chapterView = findViewById(R.id.tvVerses);
        tvChapter = findViewById(R.id.tvChapter);

        SharedPreferences prefs =
                getSharedPreferences("BiblePrefs", MODE_PRIVATE);

        fontSize =
                prefs.getInt("pref_font_size", 18);

        chapterView.setTextSize(fontSize);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Bundle params = new Bundle();


        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrevious);
        btnBack = findViewById(R.id.btnBack);




        scrollView = findViewById(R.id.scrollVerses );
        loadBible();

        Intent intent = getIntent();

        String intentBook = null;
        btnAuto = findViewById(R.id.btnAuto);

        volume = prefs.getFloat("pref_audio_volume", 1.0f);
        String voice = prefs.getString("pref_bible_voice", "default");


        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {



                speechRate = prefs.getFloat("pref_speech_rate", 1.0f);

                tts.setSpeechRate(speechRate);
                tts.setLanguage(Locale.ENGLISH);

                tts.setOnUtteranceProgressListener(
                        new UtteranceProgressListener() {

                            @Override
                            public void onStart(String id) {}

                            @Override
                            public void onDone(String id) {

                                runOnUiThread(() -> {

                                    if (!autoMode) return;

                                    verseIndex++;

                                    speakNextVerse();
                                });
                            }

                            @Override
                            public void onError(String id) {}
                        });
            }
        });

        if (voice.equals("deep")) {
            tts.setPitch(0.8f);
        } else if (voice.equals("high")) {
            tts.setPitch(1.2f);
        } else {
            tts.setPitch(1.0f);
        }

        btnAuto.setOnClickListener(v -> {

            autoMode = !autoMode;

            if (autoMode) {

                btnAuto.setImageResource(
                        R.drawable.ic_tts_on
                );

                speakNextVerse();

            } else {

                stopAudio();
            }
        });

        if (intent != null) {

            // supports SearchActivity
            intentBook = intent.getStringExtra("bookName");

            // supports HomeActivity
            if (intentBook == null) {
                intentBook = intent.getStringExtra("book");
            }
        }

        if (intentBook != null && !intentBook.isEmpty()) {

            currentBookName = intentBook;

            // optional: Home/Search can pass chapter
            currentChapter =
                    intent.getIntExtra("chapter", 1);

        } else {

            // no selected book → resume last read
            loadLastRead();
        }

        findBookObject();
        displayChapter();

        btnBack.setOnClickListener(v -> {
            saveLastRead();
            stopAudio();
            Intent newIntent = new Intent(ReadActivity.this, SearchActivity.class);
            startActivity(newIntent);
        });

        btnNext.setOnClickListener(v -> {
            verseIndex = 0;
            int maxChapters = getChapterCount(currentBookName);

            if (currentChapter < maxChapters) {

                currentChapter++;

            } else {

                int bookIndex = getBookIndex(currentBookName);

                if (bookIndex < bibleData.length() - 1) {

                    try {
                        JSONObject nextBook =
                                bibleData.getJSONObject(bookIndex + 1);

                        currentBookName = nextBook.getString("name");
                        currentChapter = 1;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            saveLastRead();
            verseIndex = 0;

            stopAudio();

            displayChapter();
            scrollView.post(() ->
                    scrollView.smoothScrollTo(0, 0)
            );
        });

        btnPrev.setOnClickListener(v -> {
            verseIndex = 0;
            if (currentChapter > 1) {

                currentChapter--;

            } else {

                int bookIndex = getBookIndex(currentBookName);

                if (bookIndex > 0) {

                    try {

                        JSONObject prevBook =
                                bibleData.getJSONObject(bookIndex - 1);

                        currentBookName =
                                prevBook.getString("name");

                        JSONArray chapters =
                                prevBook.getJSONArray("chapters");

                        currentChapter = chapters.length();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            saveLastRead();
            verseIndex = 0;

            stopAudio();
            displayChapter();

            scrollView.post(() ->
                    scrollView.smoothScrollTo(0, 0)
            );

        });

        setupBottomNav();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopAudio();
    }

    private void stopAudio() {

        autoMode = false;

        if (tts != null) {
            tts.stop();
        }

        btnAuto.setImageResource(
                R.drawable.ic_tts_off
        );
    }
    private void speakNextVerse() {

        try {

            if (currentVersesArray == null) return;

            if (verseIndex >= currentVersesArray.length()) {

                verseIndex = 0;

                if (autoMode) {
                    btnNext.performClick();
                }

                return;
            }

            String verse = currentVersesArray.getString(verseIndex);

            String text = "Verse " + (verseIndex + 1) + ". " + verse;

            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "V" + verseIndex);
            params.putFloat(
                    TextToSpeech.Engine.KEY_PARAM_VOLUME,
                    volume
            );

            tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "V" + verseIndex);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD JSON =================
    private void loadBible() {
        try {
            InputStream is = getAssets().open("bible.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer);
            bibleData = new JSONArray(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getBookIndex(String bookName) {

        try {
            for (int i = 0; i < bibleData.length(); i++) {

                JSONObject book = bibleData.getJSONObject(i);

                if (book.getString("name").equalsIgnoreCase(bookName)) {
                    return i;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    private int getChapterCount(String bookName) {

        try {
            int index = getBookIndex(bookName);

            if (index == -1) return 1;

            JSONObject book = bibleData.getJSONObject(index);

            JSONArray chapters = book.getJSONArray("chapters");

            return chapters.length();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }
    // ================= FIND BOOK =================
    private void findBookObject() {
        try {
            for (int i = 0; i < bibleData.length(); i++) {
                JSONObject book = bibleData.getJSONObject(i);

                if (book.getString("name").equalsIgnoreCase(currentBookName)
                        || book.getString("abbrev").equalsIgnoreCase(currentBookName)) {

                    currentBookObj = book;
                    currentBookName = book.getString("name");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DISPLAY =================
    private void displayChapter() {
        verseIndex = 0;
        findBookObject();

        label.setText(currentBookName);

        String ch = "Ch. " + currentChapter;
        tvChapter.setText(ch);
        chapterView.setText("");
        chapterView.setTextSize(fontSize);
        try {

            JSONArray chapters =
                    currentBookObj.getJSONArray("chapters");

            if (currentChapter < 1) {
                currentChapter = 1;
            }

            if (currentChapter > chapters.length()) {
                currentChapter = chapters.length();
            }
            currentVersesArray = chapters.getJSONArray(currentChapter - 1);
            JSONArray verses =
                    chapters.getJSONArray(currentChapter - 1);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < verses.length(); i++) {

                int verseNum = i + 1;
                String verseText = verses.getString(i);

                sb.append("[")
                        .append(verseNum)
                        .append("] ")
                        .append(verseText)
                        .append("\n\n");
            }

            chapterView.setText(sb.toString());

        } catch (Exception e) {

            chapterView.setText("Chapter not found");
            e.printStackTrace();
        }
    }

    // ================= SAVE LAST READ =================
    private void saveLastRead() {
        SharedPreferences prefs =
                getSharedPreferences("BiblePrefs", MODE_PRIVATE);

        prefs.edit()
                .putString("last_book", currentBookName)
                .putInt("last_chapter", currentChapter)
                .apply();
    }

    // ================= LOAD LAST READ =================
    private void loadLastRead() {
        SharedPreferences prefs =
                getSharedPreferences("BiblePrefs", MODE_PRIVATE);

        currentBookName =
                prefs.getString("last_book", "Genesis");

        currentChapter =
                prefs.getInt("last_chapter", 1);
    }

    // ================= BOTTOM NAV =================
    private void setupBottomNav() {

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_read);

        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        int id = item.getItemId();

                        if (id == R.id.nav_home) {
                            stopAudio();
                            startActivity(new Intent(ReadActivity.this, HomeActivity.class));
                            overridePendingTransition(
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            );
                        } else if (id == R.id.nav_search) {
                            stopAudio();
                            startActivity(new Intent(ReadActivity.this, SearchActivity.class));
                            overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                            );
                        } else if (id == R.id.nav_settings) {
                            stopAudio();
                            startActivity(new Intent(ReadActivity.this, SettingsActivity.class));
                        }

                        return true;
                    }
                });
    }

    private void speakChapter() {

        if (currentVersesArray == null) return;

        if (verseIndex >= currentVersesArray.length()) {
            verseIndex = 0;
            return;
        }

        try {

            String verse = currentVersesArray.getString(verseIndex);

            String speechText =
                    "Verse " + (verseIndex + 1) + ". " + verse;

            tts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "V" + verseIndex);

            verseIndex++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}