package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.tur_cirdictionary.turkish_circassiandictionary.data.WordDbHelper;

import static com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class MainActivity extends AppCompatActivity {

    SearchView sw_searchForWord;
    Button btn_seeArchive;
    Button btn_insertAWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sw_searchForWord = findViewById(R.id.sw_searchForWord);

        btn_seeArchive = findViewById(R.id.btn_seeArchive);

        btn_insertAWord = findViewById(R.id.btn_insertAWord);

        sw_searchForWord.setSubmitButtonEnabled(true);
        sw_searchForWord.setQueryRefinementEnabled(true);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());

        sw_searchForWord.setSearchableInfo(searchableInfo);

        btn_seeArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), ArchiveActivity.class);
                startActivity(i);

            }
        });

        btn_insertAWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "xasdvzxcsd");

                Uri uri = getContentResolver().insert(WordEntry.CONTENT_URI, values);

                Log.v("TAG", "Uri of newly inserted row: " + uri);

            }
        });

        WordDbHelper wordDbHelper = new WordDbHelper(this);

        SQLiteDatabase database = wordDbHelper.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();
        ContentValues values4 = new ContentValues();
        ContentValues values6 = new ContentValues();
        ContentValues values7 = new ContentValues();
        ContentValues values8 = new ContentValues();
        ContentValues values9 = new ContentValues();
        ContentValues values10 = new ContentValues();
        ContentValues values11 = new ContentValues();
        ContentValues values12 = new ContentValues();
        ContentValues values13 = new ContentValues();
        ContentValues values14 = new ContentValues();
        ContentValues values15 = new ContentValues();
        ContentValues values16 = new ContentValues();
        ContentValues values17 = new ContentValues();

        values1.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values2.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values3.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values4.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values6.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values7.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values8.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values9.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values10.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values11.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values12.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values13.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values14.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values15.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values16.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");
        values17.put(WordEntry.COLUMN_NAME_CIRCASSIAN, "aaa");

        database.insert(WordEntry.TABLE_NAME, null, values1);
        database.insert(WordEntry.TABLE_NAME, null, values2);
        database.insert(WordEntry.TABLE_NAME, null, values3);
        database.insert(WordEntry.TABLE_NAME, null, values4);
        database.insert(WordEntry.TABLE_NAME, null, values6);
        database.insert(WordEntry.TABLE_NAME, null, values7);
        database.insert(WordEntry.TABLE_NAME, null, values8);
        database.insert(WordEntry.TABLE_NAME, null, values9);
        database.insert(WordEntry.TABLE_NAME, null, values10);
        database.insert(WordEntry.TABLE_NAME, null, values11);
        database.insert(WordEntry.TABLE_NAME, null, values12);
        database.insert(WordEntry.TABLE_NAME, null, values13);
        database.insert(WordEntry.TABLE_NAME, null, values14);
        database.insert(WordEntry.TABLE_NAME, null, values15);
        database.insert(WordEntry.TABLE_NAME, null, values16);
        database.insert(WordEntry.TABLE_NAME, null, values17);

    }

}


