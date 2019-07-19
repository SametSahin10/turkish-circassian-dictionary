package com.tur_cirdictionary.turkish_circassiandictionary;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;


public class AlphabetActivity extends AppCompatActivity {

    ListView lw_alphabet;
    Cursor cursor;
    LetterCursorAdapter letterCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet);

        lw_alphabet = findViewById(R.id.lw_alphabet);

        String[] projection = {WordEntry._ID, WordEntry.COLUMN_NAME_CIRCASSIAN,
                WordEntry.COLUMN_NAME_TURKISH,
                };
        String selection = WordEntry.COLUMN_NAME_CATEGORY + "=?";
        String[] selectionArgs = {"alfabe"};
        cursor = getContentResolver().query(WordEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null,
                null);
        letterCursorAdapter = new LetterCursorAdapter(getApplicationContext(), cursor);
        lw_alphabet.setAdapter(letterCursorAdapter);
    }
}
