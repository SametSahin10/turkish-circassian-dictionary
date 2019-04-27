package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import static com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class MainActivity extends AppCompatActivity {

    View rootView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    Toolbar toolbar;
    SearchView sw_searchForWord;
    ImageView iv_searchIcon;
    EditText et_queryText;
    SearchableInfo searchableInfo;
    SuggestedWordCursorAdapter suggestedWordCursorAdapter;

    SharedPreferences sharedPreferences;
    InputMethodManager inputMethodManager;

    String extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.root_view);
        rootView.requestFocus();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.nav_search:
                        drawerLayout.closeDrawer(Gravity.START, true);
                        sw_searchForWord.requestFocus();
                        showKeyboard();
                        break;
                    case R.id.nav_archive:
                        intent = new Intent(getApplicationContext(), ArchiveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_info:
                        intent = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_share:
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        String appUrl = "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                        intent.putExtra(Intent.EXTRA_TEXT, appUrl);
                        intent.setType("text/plain");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        break;
                    case R.id.nav_feedback:
                        intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, "a.sahin.ual@gmail.com");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        break;
                    case R.id.nav_rate:
                        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                                | Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        sw_searchForWord = findViewById(R.id.sw_searchForWord);
        int searchIconId = sw_searchForWord.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        iv_searchIcon = sw_searchForWord.findViewById(searchIconId);
        iv_searchIcon.setImageResource(R.drawable.search_icon);

        int queryTextId = sw_searchForWord.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        et_queryText = sw_searchForWord.findViewById(queryTextId);
        et_queryText.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);

        Typeface roboto_regular = ResourcesCompat.getFont(this, R.font.roboto_regular_res);
        et_queryText.setTypeface(roboto_regular);
        et_queryText.setTextColor(getResources().getColor(R.color.mainActivityBackground));
        et_queryText.setHintTextColor(getResources().getColor(R.color.searchViewTextHintColor));

        sw_searchForWord.setSubmitButtonEnabled(true);
        sw_searchForWord.setQueryRefinementEnabled(true);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchableInfo = searchManager
                    .getSearchableInfo(getComponentName());
        }
        sw_searchForWord.setSearchableInfo(searchableInfo);
        sw_searchForWord.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor cursor = sw_searchForWord.getSuggestionsAdapter().getCursor();
                if (cursor != null) {
                    if (newText.length() == 0) {
                        cursor.close();
                    }
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    boolean showSearchSuggestions = sharedPreferences.getBoolean("showSearchSuggestions", getResources().getBoolean(R.bool.showSearchSuggestions));
                    if (newText.length() > 0) {
                        if (showSearchSuggestions) {
                            showSuggestionsForQuery(newText);
                        } else {
                            cursor.close();
                        }
                    }
                }
                return false;
            }
        });

        sw_searchForWord.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                launchQueryFromSuggestion(position);
                hideKeyboard();
                return true;
            }
        });

//        btn_seeArchive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), ArchiveActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btn_clearSearchHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SearchRecentSuggestions recentSuggestions = new SearchRecentSuggestions(getApplicationContext(),
//                        RecentSuggestionsProvider.AUTHORITY,
//                        RecentSuggestionsProvider.MODE);
//
//                recentSuggestions.clearHistory();
//
//                Toast.makeText(MainActivity.this,
//                        "Search history cleared",
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SourceActivity")) {
            extra = intent.getExtras().getString("SourceActivity");
            if (extra.equals("WordDetailActivity")) {
                Log.v("TAG", "Coming from WordDetailActivity");
                sw_searchForWord.requestFocus();
                showKeyboard();
            }
        } else {
            rootView.requestFocus();
            Log.v("TAG", "rootView focused");
        }
        intent.removeExtra("SourceActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideKeyboard();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSuggestionsForQuery(String queryText) {
        String query = queryText;

        String[] projectionCircassian = {WordEntry._ID,
                WordEntry.COLUMN_NAME_CIRCASSIAN};
        String selectionCircassian = WordEntry.COLUMN_NAME_CIRCASSIAN + " LIKE ?";
        String[] selectionArgs = {query + "%"};

        String[] projectionTurkish = {WordEntry._ID,
                WordEntry.COLUMN_NAME_TURKISH};
        String selectionTurkish = WordEntry.COLUMN_NAME_TURKISH + " LIKE ?";

        Cursor cursorCircassian = getContentResolver().query(WordEntry.CONTENT_URI,
                projectionCircassian,
                selectionCircassian,
                selectionArgs,
                WordEntry.COLUMN_NAME_CIRCASSIAN + " ASC",
                null);

        Cursor cursorTurkish = getContentResolver().query(WordEntry.CONTENT_URI,
                projectionTurkish,
                selectionTurkish,
                selectionArgs,
                WordEntry.COLUMN_NAME_TURKISH + " ASC",
                null);

        Cursor[] cursors = {cursorCircassian, cursorTurkish};
        MergeCursor mergedCursor = new MergeCursor(cursors);


        suggestedWordCursorAdapter = new SuggestedWordCursorAdapter(this, mergedCursor);
        sw_searchForWord.setSuggestionsAdapter(suggestedWordCursorAdapter);
    }

    private void launchQueryFromSuggestion(int position) {
        Cursor cursor = sw_searchForWord.getSuggestionsAdapter().getCursor();
        if (cursor != null && cursor.moveToPosition(position)) {
            String[] columnNames = cursor.getColumnNames();
            int columnIndex = 0;
            for (String columnName: columnNames) {
                if (columnName.equals(WordEntry.COLUMN_NAME_CIRCASSIAN)) {
                    columnIndex = cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_CIRCASSIAN);
                } else if (columnName.equals(WordEntry.COLUMN_NAME_TURKISH)) {
                    columnIndex = cursor.getColumnIndexOrThrow(WordEntry.COLUMN_NAME_TURKISH);
                } else if (columnName.equals(SearchManager.SUGGEST_COLUMN_TEXT_1)) {
                    columnIndex = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
                } else {
                    Log.v("TAG", "Unknown column name");
                }
            }
            String suggestion = cursor.getString(columnIndex);
            String action = Intent.ACTION_SEARCH;
            Intent intent = new Intent(action);
            intent.putExtra(SearchManager.QUERY, suggestion);
            if (searchableInfo != null) {
                intent.setComponent(searchableInfo.getSearchActivity());
            }
            startActivity(intent);
        }
    }

    public void showKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
    }

}


