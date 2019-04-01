package com.tur_cirdictionary.turkish_circassiandictionary.data;

import android.content.SearchRecentSuggestionsProvider;

public class RecentSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY =
            "com.tur_cirdictionary.turkish_circassiandictionary.data.RecentSuggestionsProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
