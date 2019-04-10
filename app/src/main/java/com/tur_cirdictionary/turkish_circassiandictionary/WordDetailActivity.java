package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.tur_cirdictionary.turkish_circassiandictionary.data.RecentSuggestionsProvider;
import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class WordDetailActivity extends AppCompatActivity {

    TextView tv_circassian;
    TextView tv_turkish;
    TextView tv_circassianMeaning;
    TextView tv_turkishMeaning;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        tv_circassian = findViewById(R.id.tv_suggestedCircassian);
        tv_turkish = findViewById(R.id.tv_turkish);
        tv_circassianMeaning = findViewById(R.id.tv_circassianMeaning);
        tv_turkishMeaning = findViewById(R.id.tv_turkishMeaning);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query == null) {
                Log.v("TAG", "Query is null");
            } else {
                Log.v("TAG", "Query is not null and it is " + query);
            }

            String projection[] = {WordEntry._ID,
                    WordEntry.COLUMN_NAME_CIRCASSIAN,
                    WordEntry.COLUMN_NAME_TURKISH};

            String selection = WordEntry.COLUMN_NAME_CIRCASSIAN + "=?";
            String[] selectionArgs = {query};

            cursor = getContentResolver().query(WordEntry.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    SearchRecentSuggestions recentSuggestions =
                            new SearchRecentSuggestions(getApplicationContext(),
                                    RecentSuggestionsProvider.AUTHORITY,
                                    RecentSuggestionsProvider.MODE);

                    recentSuggestions.saveRecentQuery(query, null);

                    int columnIndexCircassian =
                            cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_CIRCASSIAN);

                    Log.v("TAG", "Column index is: " + columnIndexCircassian);

                    String circassianMeaning = cursor.getString(columnIndexCircassian);

                    int columnIndexTurkish =
                            cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_TURKISH);
                    String turkishMeaning = cursor.getString(columnIndexTurkish);

                    tv_circassianMeaning.setText(circassianMeaning);
                    tv_turkishMeaning.setText(turkishMeaning);
                }
            } else {
                setContentView(R.layout.activity_word_not_found);
                Log.v("TAG", "Cursor is null");
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
