**Activity Specs**

**ACT_SETTINGS** - The Activity ID
**User Story:** US_05 — As a user, I want to change the font size of the text, so that I can read comfortably based on my preference.

1. **TEXTSIZE_1** — Increases app-wide text size by one step per tap. Size steps: Small (12sp) → Medium (14sp) → Large (18sp) → Extra Large (22sp). Button is **disabled and grayed out** when already at Extra Large. Change is applied immediately app-wide. Value saved to `SharedPreferences` key: `pref_text_size` (String). Default: `"Medium"`.

2. **TEXTSIZE_2** — Decreases app-wide text size by one step per tap. Button is **disabled and grayed out** at Small. Same storage as TEXTSIZE_1.

3. **REMINDER_1** — Enables or disables the Daily Verse push notification. Default: ON (checked). When toggled ON, app schedules a daily notification via `WorkManager` at 7:00 AM local time. If notification permission is **denied by the OS**, toggle reverts to OFF and shows a snackbar: *"Enable notifications in Settings to use this feature."* Saved to `SharedPreferences` key: `pref_reminder_enabled` (Boolean).

4. **AUDIO_1** — Volume slider for Audio Bible playback. Range: 0.0 (mute) to 1.0 (maximum), step: 0.05. Value is applied in real-time as the user drags. On finger lift, value is saved to `SharedPreferences` key: `pref_audio_volume` (Float). Default: 0.7. If audio service is unavailable, slider is **disabled** with label: *"Audio unavailable."*

5. **AUDIO_2** — Tappable row labeled "Audio Bible Voice" with chevron (›). Navigates to `ACT_VOICE_SELECT`. Current selected voice name is displayed as a subtitle (e.g., "James"). If no voice is selected yet, subtitle shows *"Not set."* Voice selection is stored in `SharedPreferences` key: `pref_bible_voice` (String).

**On screen load:** All controls read their saved values from `SharedPreferences` and reflect the current state before the user interacts.
