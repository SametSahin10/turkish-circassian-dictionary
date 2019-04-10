package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.tur_cirdictionary.turkish_circassiandictionary.data.RecentSuggestionsProvider;

import static com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class MainActivity extends AppCompatActivity {

    SearchView sw_searchForWord;
    Button btn_seeArchive;
    Button btn_clearSearchHistory;
    SearchableInfo searchableInfo;
    WordCursorAdapter wordCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sw_searchForWord = findViewById(R.id.sw_searchForWord);
        btn_seeArchive = findViewById(R.id.btn_seeArchive);
        btn_clearSearchHistory = findViewById(R.id.btn_clearSearchHistory);

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

        btn_seeArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ArchiveActivity.class);
                startActivity(intent);
            }
        });

        btn_clearSearchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchRecentSuggestions recentSuggestions = new SearchRecentSuggestions(getApplicationContext(),
                        RecentSuggestionsProvider.AUTHORITY,
                        RecentSuggestionsProvider.MODE);

                recentSuggestions.clearHistory();

                Toast.makeText(MainActivity.this,
                        "Search history cleared",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });


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
                }
                if (columnName.equals(SearchManager.SUGGEST_COLUMN_TEXT_1)) {
                    columnIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
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


