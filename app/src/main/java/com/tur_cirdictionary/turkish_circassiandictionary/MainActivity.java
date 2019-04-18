package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import static com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class MainActivity extends AppCompatActivity {

    View rootView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    Toolbar toolbar;
    SearchView sw_searchForWord;
    ImageView iv_searchIcon;
    EditText et_queryText;
    Button btn_seeArchive;
    Button btn_clearSearchHistory;
    SearchableInfo searchableInfo;
    WordCursorAdapter wordCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.root_view);
        rootView.requestFocus();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.nav_search:
                        //Focus on the search bar.
                        drawerLayout.closeDrawer(Gravity.START, true);
                        sw_searchForWord.requestFocus();
                        break;
                    case R.id.nav_archive:
                        intent = new Intent(getApplicationContext(), ArchiveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_info:
                        //Launch AboutActivity.
                        break;
                    case R.id.nav_rate:
                        //Start rating action.
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        sw_searchForWord = findViewById(R.id.sw_searchForWord);
//        btn_seeArchive = findViewById(R.id.btn_seeArchive);
//        btn_clearSearchHistory = findViewById(R.id.btn_clearSearchHistory);
        int searchIconId = sw_searchForWord.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        iv_searchIcon = sw_searchForWord.findViewById(searchIconId);
        iv_searchIcon.setImageResource(R.drawable.search_icon);

        int queryTextId = sw_searchForWord.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        et_queryText = sw_searchForWord.findViewById(queryTextId);
        et_queryText.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);

        sw_searchForWord.setSubmitButtonEnabled(true);
        sw_searchForWord.setQueryRefinementEnabled(true);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchableInfo = searchManager
                .getSearchableInfo(getComponentName());
        if (searchableInfo == null) {
            Log.v("TAG", "searchableInfo is null");
        }

        sw_searchForWord.setSearchableInfo(searchableInfo);
        sw_searchForWord.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    showSuggestionsForQuery(newText);
                }
                return false;
            }
        });

        sw_searchForWord.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                launchQueryFromSuggestion(position);
                return true;
            }
        });

//        btn_seeArchive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), ArchiveActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btn_clearSearchHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SearchRecentSuggestions recentSuggestions = new SearchRecentSuggestions(getApplicationContext(),
//                        RecentSuggestionsProvider.AUTHORITY,
//                        RecentSuggestionsProvider.MODE);
//
//                recentSuggestions.clearHistory();
//
//                Toast.makeText(MainActivity.this,
//                        "Search history cleared",
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        rootView.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSuggestionsForQuery(String queryText) {
        String query = queryText;

        String[] projectionCircassian = {WordEntry._ID,
                WordEntry.COLUMN_NAME_CIRCASSIAN};
        String selectionCircassian = WordEntry.COLUMN_NAME_CIRCASSIAN + " LIKE ?";
        String[] selectionArgs = {query + "%"};

        String[] projectionTurkish = {WordEntry._ID,
                WordEntry.COLUMN_NAME_TURKISH};
        String selectionTurkish = WordEntry.COLUMN_NAME_TURKISH + " LIKE ?";

        Cursor cursorCircassian = getContentResolver().query(WordEntry.CONTENT_URI,
                projectionCircassian,
                selectionCircassian,
                selectionArgs,
                WordEntry.COLUMN_NAME_CIRCASSIAN + " ASC",
                null);

        Cursor cursorTurkish = getContentResolver().query(WordEntry.CONTENT_URI,
                projectionTurkish,
                selectionTurkish,
                selectionArgs,
                WordEntry.COLUMN_NAME_TURKISH + " ASC",
                null);

        Cursor[] cursors = {cursorCircassian, cursorTurkish};
        MergeCursor mergedCursor = new MergeCursor(cursors);

        wordCursorAdapter = new WordCursorAdapter(this, mergedCursor);
        sw_searchForWord.setSuggestionsAdapter(wordCursorAdapter);
    }

    private void launchQueryFromSuggestion(int position) {
        Cursor cursor = sw_searchForWord.getSuggestionsAdapter().getCursor();
        if (cursor != null && cursor.moveToPosition(position)) {
            String[] columnNames = cursor.getColumnNames();
            int columnIndex = 0;
            for (String columnName: columnNames) {
                if (columnName.equals(WordEntry.COLUMN_NAME_CIRCASSIAN)) {
                    columnIndex = cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_CIRCASSIAN);
                } else if (columnName.equals(WordEntry.COLUMN_NAME_TURKISH)) {
                    columnIndex = cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_TURKISH);
                } else if (columnName.equals(SearchManager.SUGGEST_COLUMN_TEXT_1)) {
                    columnIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
                } else {
                    Log.v("TAG", "Unknown column index");
                }
            }
            String suggestion = cursor.getString(columnIndex);
            String action = Intent.ACTION_SEARCH;
            Intent intent = new Intent(action);
            intent.putExtra(SearchManager.QUERY, suggestion);
            if (searchableInfo != null) {
                intent.setComponent(searchableInfo.getSearchActivity());
            }
            startActivity(intent);
        }
    }
}


