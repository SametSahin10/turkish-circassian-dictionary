package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tur_cirdictionary.turkish_circassiandictionary.data.RecentSuggestionsProvider;
import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class WordDetailActivity extends AppCompatActivity {

    TextView tv_queriedWord;
    TextView tv_meaning;
    Button btn_searchAnotherWord;
    Button btn_empty_view_searchAnotherWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        tv_queriedWord = findViewById(R.id.tv_queriedWord);
        tv_meaning = findViewById(R.id.tv_meaning);


        btn_searchAnotherWord = findViewById(R.id.btn_searchAnotherWord);
        btn_searchAnotherWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("SourceActivity", "WordDetailActivity");
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String[] projectionAll = {WordEntry._ID,
                    WordEntry.COLUMN_NAME_CIRCASSIAN,
                    WordEntry.COLUMN_NAME_TURKISH};

            String[] projectionCircassian = {WordEntry._ID,
                    WordEntry.COLUMN_NAME_CIRCASSIAN};
            String selectionCircassian = WordEntry.COLUMN_NAME_CIRCASSIAN + "=?";
            String[] selectionArgs = {query};

            String[] projectionTurkish = {WordEntry._ID,
                    WordEntry.COLUMN_NAME_TURKISH};
            String selectionTurkish = WordEntry.COLUMN_NAME_TURKISH + "=?";

            Cursor cursorCircassian = getContentResolver().query(WordEntry.CONTENT_URI,
                    projectionCircassian,
                    selectionCircassian,
                    selectionArgs,
                    null,
                    null);

            Cursor cursorTurkish = getContentResolver().query(WordEntry.CONTENT_URI,
                    projectionTurkish,
                    selectionTurkish,
                    selectionArgs,
                    null,
                    null);

            Cursor[] cursors = {cursorCircassian, cursorTurkish};

            MergeCursor mergedCursor = new MergeCursor(cursors);
            if (mergedCursor.moveToFirst()) {
                SearchRecentSuggestions recentSuggestions =
                        new SearchRecentSuggestions(getApplicationContext(),
                                RecentSuggestionsProvider.AUTHORITY,
                                RecentSuggestionsProvider.MODE);

                recentSuggestions.saveRecentQuery(query, null);

                String[] columnNames = mergedCursor.getColumnNames();
                int columnIndexID;
                int id;
                int columnIndexWord;
                String meaning = null;
                for (String columnName: columnNames) {
                    if (columnName.equals(WordEntry.COLUMN_NAME_CIRCASSIAN)) {
                        columnIndexID = mergedCursor.getColumnIndexOrThrow(WordEntry._ID);
                        id = mergedCursor.getInt(columnIndexID);
                        String selectionAll = WordEntry._ID + "=?";
                        String[] selectionArgsAll = {String.valueOf(id)};
                        Cursor cursorAll = getContentResolver().query(WordEntry.CONTENT_URI,
                                projectionAll,
                                selectionAll,
                                selectionArgsAll,
                                null);
                        if (cursorAll != null && cursorAll.moveToFirst()) {
                            columnIndexWord = cursorAll.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_TURKISH);
                            meaning  = cursorAll.getString(columnIndexWord);
                        }
                    } else if (columnName.equals(WordEntry.COLUMN_NAME_TURKISH)) {
                        columnIndexID = mergedCursor.getColumnIndexOrThrow(WordEntry._ID);
                        id = mergedCursor.getInt(columnIndexID);
                        String selectionAll = WordEntry._ID + "=?";
                        String[] selectionArgsAll = {String.valueOf(id)};
                        Cursor cursorAll = getContentResolver().query(WordEntry.CONTENT_URI,
                                projectionAll,
                                selectionAll,
                                selectionArgsAll,
                                null);
                        if (cursorAll != null && cursorAll.moveToFirst()) {
                            columnIndexWord = cursorAll.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_CIRCASSIAN);
                            meaning  = cursorAll.getString(columnIndexWord);
                        }
                    } else {
                        Log.v("TAG100", "Unknown column name");
                    }
                }
                tv_queriedWord.setText(query);
                tv_meaning.setText(meaning);
            } else {
                setContentView(R.layout.activity_word_not_found);
                btn_empty_view_searchAnotherWord = findViewById(R.id.btn_emptyView_searchAnotherWord);
                btn_empty_view_searchAnotherWord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.putExtra("SourceActivity", "WordDetailActivity");
                        startActivity(intent);
                    }
                });
            }
        }

        //Below is left as commented for future implementation.
//        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
//            Log.v("TAG", "This is inside the ACTION VIEW");
//            Uri dataUri = intent.getData();
//            if (dataUri != null) {
//                String lastPath = dataUri.getLastPathSegment();
//                Log.v("TAG", "Last path segment is: " + lastPath);
//            } else {
//                Log.v("TAG", "dataUri is null");
//            }
//        }
    }
}
