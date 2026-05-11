# ACT_READ — ReadActivity

## Activity

- **ACT_READ** — ReadActivity.kt — Displays the full chapter text of the selected Bible passage with the target verse highlighted.
  - Layout: activity_read.xml
  - Entry: Tap a result card in ACT_SEARCH
  - Exit: Back button → ACT_SEARCH

## User Stories

- **US_01** — As a user, I want to read the Bible by book, chapter, and verse, so that I can easily navigate and understand specific passages.
- **US_02** — As a user, I want to scroll smoothly through chapters, so that reading feels comfortable and uninterrupted.

## Components

- **BACKBTN_1** — ImageButton — Navigates back to ACT_SEARCH
- **BOOKLABEL_1** — TextView — Displays book name and chapter number
- **US_01** — RecyclerView — List of verses; target verse is highlighted
  - **VERSE_1** — TextView — Verse number and text
  - **VERSE_2** — TextView — Verse number and text (highlighted as target verse)
  - **VERSE_3** — TextView — Verse number and text
  - **VERSE_N** — TextView — Repeats for each verse in the chapter
- **US_02** — ScrollBar — Scroll indicator for navigating long chapters
