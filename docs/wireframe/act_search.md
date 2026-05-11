Activity ID: ACT_SEARCH

Activity Specs

1. SEARCH_1 — Text input field at the top of the screen. Placeholder: "Search a book...". Tapping opens the keyboard. Search fires as the user types; pressing Enter triggers it immediately. Accepts up to 50 characters. Blank input is ignored. Queries under 2 characters show a snackbar: "Enter at least 2 characters to search." Keyboard does not open on screen load.  
2. CLEAR_BTN — The ✕ button inside the search bar. Appears as soon as the user types anything. Tapping clears the field, dismisses the keyboard, and resets the screen to its default empty view.  
3. RESULT_COUNT — A small label below the search bar showing how many books matched (e.g., "4 books found"). Only visible when there are results.  
4. BOOK_ROW — Each matched book name displayed as a tappable row with a chevron (›) on the right. Results appear in Bible order. Tapping opens that book in ACT_BOOK_OVERVIEW.  
5. EMPTY_STATE — The default screen when nothing has been typed yet. Shows a book icon and the text: "Type a book name to get started".  
6. EMPTY_RESULT — Shown when a search returns no matches. Displays: "No books found for "[query]"." The user can edit the search bar or tap CLEAR_BTN to reset.  
7. NAV_BAR — Bottom navigation bar with four tabs: Home, Read, Search, Settings. Search is highlighted as the active tab. Always visible on screen.  
On screen load: Search bar is empty, keyboard is closed, and EMPTY_STATE is shown. No search runs until the user types.


US_04 - This is where the user can search by book name.

