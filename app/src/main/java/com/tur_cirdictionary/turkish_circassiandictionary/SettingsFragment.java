package com.tur_cirdictionary.turkish_circassiandictionary;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.tur_cirdictionary.turkish_circassiandictionary.data.RecentSuggestionsProvider;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    SwitchPreference showSearchSuggestions;
    Preference clearRecentSearchHistory;
    ListPreference preferredLanguage;
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

        preferredLanguage = (ListPreference) findPreference("preferredLanguage");
        preferredLanguage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String selectedLanguage = (String) newValue;
                Log.v("TAG", "Seleceted language: " + selectedLanguage);
                String defaultLanguage = sharedPreferences.getString("preferredLanguage", Locale.getDefault().getLanguage());
                if (!(defaultLanguage.equals(selectedLanguage))) {
                    changePreferredLanguage(preference.getContext(), selectedLanguage);
                }
                return true;
            }
        });
    }

    public void changePreferredLanguage(Context context, String selectedLanguage) {
        String selectedLanguageCode = null;
        if (selectedLanguage.equals("Circassian")) {
            selectedLanguageCode = "cau";
        } else if (selectedLanguage.equals("Turkish")) {
            selectedLanguageCode = "tr";
        } else if (selectedLanguage.equals("English")) {
            selectedLanguageCode = "en";
        } else {
            Log.v("TAG", "Unknown language selection");
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("preferredLanguage", selectedLanguageCode);
        editor.apply();

        Locale locale = new Locale(selectedLanguageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        config.locale = locale;
        context.getResources().updateConfiguration(config, displayMetrics);
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().finish();
        startActivity(intent);
    }

}
