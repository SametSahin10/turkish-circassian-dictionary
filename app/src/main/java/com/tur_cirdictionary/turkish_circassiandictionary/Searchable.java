package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;
import com.tur_cirdictionary.turkish_circassiandictionary.data.WordDbHelper;

public class Searchable extends Activity {

    ListView wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        wordList = findViewById(R.id.list);

        WordDbHelper wordDbHelper = new WordDbHelper(this);
        SQLiteDatabase db = wordDbHelper.getReadableDatabase();

        String[] results;

        Intent intent = getIntent();

        if (intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);

            String[] projection = {WordEntry.COLUMN_NAME_CIRCASSIAN};

            Cursor cursor = db.query(WordEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null);

            //Call search method here.
            results = searchAWord(cursor, query);

            int lengthOfResults = results.length;

            Log.v("TAG", String.valueOf(lengthOfResults));

            for (int m = 0; m < results.length; m++) {

                Log.v("TAG", String.valueOf(results[m]));

            }

            ArrayAdapter<String> wordAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,
                    results);

            wordList.setAdapter(wordAdapter);

            cursor.close();

        }

    }

    //String[] circassianWords = {"awey", "awge", "awox", "bılım", "bleq", "cenet", "denef", "fo"};

    public String[] searchAWord(Cursor cursor, String query) {

        int sizeOfResults = findSizeOfResults(cursor, query);
        String[] results = new String[sizeOfResults];
        int indexOfResult = 0;

        while (cursor.moveToNext()) {

            int columnIndex = cursor.getColumnIndex(WordEntry.COLUMN_NAME_CIRCASSIAN);
            String currentString = cursor.getString(columnIndex);

            for (int i = 0; i < currentString.length(); i++) {

                for (int j = 0; j < currentString.length(); j++) {

                    for (int k = j + 1; k < currentString.length() + 1; k++) {

                        if (currentString.substring(j, k).equalsIgnoreCase(query)) {

                            results[indexOfResult] = currentString;
                            indexOfResult++;

                        }

                    }

                }

            }

        }

        return results;

    }

    public int findSizeOfResults(Cursor cursor, String query) {

        int sizeOfResults = 0;

        while (cursor.moveToNext()) {

            int columnIndex = cursor.getColumnIndex(WordEntry.COLUMN_NAME_CIRCASSIAN);
            String currentString = cursor.getString(columnIndex);

            for (int j = 0; j < currentString.length(); j++) {

                for (int k = j + 1; k < currentString.length() + 1; k++) {

                    if (currentString.substring(j, k).equalsIgnoreCase(query)) {

                        sizeOfResults++;
                        break;

                    }

                }

            }

        }

        return sizeOfResults;

    }
}
