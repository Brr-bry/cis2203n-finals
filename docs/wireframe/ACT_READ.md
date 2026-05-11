# ACT_READ — ReadActivity

**Activity name:** ReadActivity.kt  
**Layout file:** activity_read.xml  
**Description:** Displays the full chapter text of the selected Bible passage, with the target verse highlighted for context.  
**Entry Point:** Tap a result card in ACT_SEARCH  
**Exit Point:** Back button → ACT_SEARCH (back stack)

---

## Activity Details

- **Activity name:** ReadActivity.kt
- **Layout file:** activity_read.xml
- **Entry point:** Tap a result card in ACT_SEARCH
- **Exit point:** Back button → ACT_SEARCH (back stack)
- **Intent extras received:** book: String, chapter: Int, targetVerse: Int
- **Scroll behavior:** RecyclerView auto-scrolls to target verse on load

---

## Components

- **STATUSBAR_1**
  - Type: System
  - Widget: System UI
  - Description: Displays time, signal, and battery. Managed by the OS; no custom implementation needed.

- **TOOLBAR_1**
  - Type: Navigation
  - Widget: Toolbar
  - Description: Top app bar containing app name and logo. Follows Material Design AppBar pattern.

- **BACKBTN_1**
  - Type: Navigation
  - Widget: ImageButton
  - Description: Returns the user to ACT_SEARCH via the back stack. Placed inside the Toolbar as a navigation icon.

- **BOOKLABEL_1**
  - Type: String
  - Widget: TextView (×2)
  - Description: Two stacked TextViews showing book name (e.g. "Psalms") and chapter number (e.g. "Chapter 1"). Values passed as intent extras from ACT_SEARCH.

- **US_01**
  - Type: List
  - Widget: RecyclerView + ViewHolder
  - User Story: US_01 — As a user, I want to read the Bible by book, chapter, and verse.
  - Description: Scrollable list of verse items for the selected chapter. Each item displays a verse number and verse text. The target verse is visually highlighted with a distinct background.

- **US_02**
  - Type: Visual
  - Widget: ScrollBar
  - User Story: US_02 — As a user, I want to scroll smoothly through chapters.
  - Description: Vertical scroll indicator on the right edge of the verse list. Allows smooth, uninterrupted scrolling through long chapters.
