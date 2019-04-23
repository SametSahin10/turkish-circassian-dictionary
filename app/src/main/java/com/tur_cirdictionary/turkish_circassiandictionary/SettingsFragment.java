package com.tur_cirdictionary.turkish_circassiandictionary;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.tur_cirdictionary.turkish_circassiandictionary.data.RecentSuggestionsProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    SwitchPreference showSearchSuggestions;
    Preference clearRecentSearchHistory;
    SearchRecentSuggestions recentSearchSuggestions;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        showSearchSuggestions = (SwitchPreference) findPreference("searchSuggestions");
        showSearchSuggestions.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean showSearchSuggestionsChecked = (Boolean) newValue;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (showSearchSuggestionsChecked) {
                    editor.putBoolean("showSearchSuggestions", true);
                } else {
                    editor.putBoolean("showSearchSuggestions", false);
                }
                editor.apply();
                return true;
            }
        });

        clearRecentSearchHistory = findPreference("clearRecentSearchSuggestions");
        clearRecentSearchHistory.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                recentSearchSuggestions = new SearchRecentSuggestions(getContext(),
                                RecentSuggestionsProvider.AUTHORITY,
                                RecentSuggestionsProvider.MODE);
                recentSearchSuggestions.clearHistory();
                Toast.makeText(getContext(),
                        "Recent search history cleared",
                        Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }
}
