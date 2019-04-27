package com.tur_cirdictionary.turkish_circassiandictionary;


import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class ArchiveActivity extends AppCompatActivity {


    ListView lw_archiveWordList;
    TextView tv_optionCircassian;
    TextView tv_optionTurkish;
    Switch switch_listingType;
    WordCursorAdapter wordCursorAdapter;

    SearchManager searchManager;
    SearchableInfo searchableInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        lw_archiveWordList = findViewById(R.id.lw_archiveWordList);
        View emptyView = findViewById(R.id.empty_view);
        lw_archiveWordList.setEmptyView(emptyView);
        String[] projectionCircassian = {WordEntry._ID,
                WordEntry.COLUMN_NAME_CIRCASSIAN};

        String[] projectionTurkish = {WordEntry._ID,
                WordEntry.COLUMN_NAME_TURKISH};

        final Cursor cursorCircassian = getContentResolver().query(WordEntry.CONTENT_URI,
                projectionCircassian,
                null,
                null,
                null,
                null);

        final Cursor cursorTurkish = getContentResolver().query(WordEntry.CONTENT_URI,
                projectionTurkish,
                null,
                null,
                WordEntry.COLUMN_NAME_TURKISH + " COLLATE NOCASE ASC",
                null);

        wordCursorAdapter = new WordCursorAdapter(this, cursorCircassian);
        lw_archiveWordList.setAdapter(wordCursorAdapter);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchableInfo = searchManager.getSearchableInfo(getComponentName());
        lw_archiveWordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_clickedItem = view.findViewById(R.id.tv_word);
                String query = tv_clickedItem.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                intent.setComponent(searchableInfo.getSearchActivity());
                startActivity(intent);
            }
        });

        switch_listingType = findViewById(R.id.switch_listingType);
        switch_listingType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wordCursorAdapter = new WordCursorAdapter(buttonView.getContext(), cursorTurkish);
                    lw_archiveWordList.setAdapter(wordCursorAdapter);
                    switch_listingType.setChecked(true);
                } else {
                    wordCursorAdapter = new WordCursorAdapter(buttonView.getContext(), cursorCircassian);
                    lw_archiveWordList.setAdapter(wordCursorAdapter);
                    switch_listingType.setChecked(false);
                }
            }
        });
    }
}
