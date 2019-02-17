package com.tur_cirdictionary.turkish_circassiandictionary;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class ArchiveActivity extends AppCompatActivity {

    ListView lw_archiveWordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        lw_archiveWordList = findViewById(R.id.lw_archiveWordList);

        View emptyView = findViewById(R.id.empty_view);

        lw_archiveWordList.setEmptyView(emptyView);

        String[] projection = {WordEntry._ID, WordEntry.COLUMN_NAME_CIRCASSIAN};

        //WordDbHelper wordDbHelper = new WordDbHelper(this);

        //SQLiteDatabase database = wordDbHelper.getWritableDatabase();

        Cursor cursor = getContentResolver().query(WordEntry.CONTENT_URI, projection,
                null,
                null,
                null);

        if (cursor == null) {

            Log.v("TAG", "Cursor is null");

        } else {

            Log.v("TAG", "Cursor is not null");

        }

        WordCursorAdapter wordCursorAdapter = new WordCursorAdapter(this, cursor);

        lw_archiveWordList.setAdapter(wordCursorAdapter);

    }
}
