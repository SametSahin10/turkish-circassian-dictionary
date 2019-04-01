package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
    Button btn_dropDatabase;
    Button btn_clearSearchHistory;
    SearchableInfo searchableInfo;
    WordCursorAdapter wordCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sw_searchForWord = findViewById(R.id.sw_searchForWord);
        btn_dropDatabase = findViewById(R.id.btn_dropDatabase);
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

        btn_dropDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projection = WordEntry._ID;
                getContentResolver().delete(WordEntry.CONTENT_URI, projection, null);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_database, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_word:
                insertDummyWord();
                return true;

            case R.id.action_delete_dummy_word:
                deleteDummyWord();
                return true;
        }

        return onOptionsItemSelected(item);
    }

    private Uri insertDummyWord() {
        ContentValues values = new ContentValues();
        values.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "circassian");
        values.put(WordEntry.COLUMN_NAME_TURKISH, "turkish");

        ContentValues values1 = new ContentValues();
        values1.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "abc");
        values1.put(WordEntry.COLUMN_NAME_TURKISH, "edf");

        ContentValues values2 = new ContentValues();
        values2.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "zsw");
        values2.put(WordEntry.COLUMN_NAME_TURKISH, "fdsfsfs");

        Uri uri = getContentResolver().insert(WordEntry.CONTENT_URI, values);
        Uri uri1 = getContentResolver().insert(WordEntry.CONTENT_URI, values1);
        Uri uri2 = getContentResolver().insert(WordEntry.CONTENT_URI, values2);

        if (uri == null || uri1 == null || uri2 == null) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, "Words has been saved", Toast.LENGTH_SHORT)
                    .show();
        }

        return uri;
    }

    private int deleteDummyWord() {
        String selection = WordEntry._ID + "=?";
        String[] selectionArgs = {};

        int numOfRowsDeleted =
                getContentResolver().delete(WordEntry.CONTENT_URI, selection, selectionArgs);

        if (numOfRowsDeleted != 0) {
            Toast.makeText(this, "Deletion successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion failed", Toast.LENGTH_SHORT).show();
        }

        return numOfRowsDeleted;
    }

    private void showSuggestionsForQuery(String queryText) {
        String query = queryText;

        String[] projection = {WordEntry._ID, WordEntry.COLUMN_NAME_CIRCASSIAN};
        String selection = WordEntry.COLUMN_NAME_CIRCASSIAN + " LIKE ?";
        String[] selectionArgs = {query + "%"};

        Cursor cursor = getContentResolver().query(WordEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null,
                null);

        wordCursorAdapter = new WordCursorAdapter(this, cursor);
        sw_searchForWord.setSuggestionsAdapter(wordCursorAdapter);
    }

    private void launchQueryFromSuggestion(int position) {
        Cursor cursor = sw_searchForWord.getSuggestionsAdapter().getCursor();
        if (cursor != null && cursor.moveToPosition(position)) {
            int columnIndex = cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_CIRCASSIAN);
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


