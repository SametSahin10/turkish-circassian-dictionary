package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class ArchiveActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int URL_LOADER = 0;

    ListView lw_archiveWordList;

    WordCursorAdapter wordCursorAdapter;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        lw_archiveWordList = findViewById(R.id.lw_archiveWordList);

        View emptyView = findViewById(R.id.empty_view);

        lw_archiveWordList.setEmptyView(emptyView);

        String[] projection = {WordEntry._ID, WordEntry.COLUMN_NAME_CIRCASSIAN};

        cursor = getContentResolver().query(WordEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        wordCursorAdapter = new WordCursorAdapter(this, cursor);

        lw_archiveWordList.setAdapter(wordCursorAdapter);

        getLoaderManager().initLoader(URL_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {
            case URL_LOADER:
                String[] projection = {WordEntry._ID, WordEntry.COLUMN_NAME_CIRCASSIAN};

                return new CursorLoader(this,
                        WordEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);

            default:
                return null;

        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor dataCursor) {

        wordCursorAdapter.swapCursor(dataCursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        wordCursorAdapter.swapCursor(null);

    }

}
