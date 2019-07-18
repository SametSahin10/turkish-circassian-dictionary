package com.tur_cirdictionary.turkish_circassiandictionary;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import java.util.Locale;

import static com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class MainActivity extends AppCompatActivity implements CharacterRecyclerAdapter.OnCharacterListener {

    View rootView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    Toolbar toolbar;
    SearchView sw_searchForWord;
    ImageView iv_searchIcon;
    EditText et_queryText;
    Button btn_showSpecialCharacters;
    Button btn_closePanel;
    SearchableInfo searchableInfo;
    SuggestedWordCursorAdapter suggestedWordCursorAdapter;

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;

    SharedPreferences sharedPreferences;
    InputMethodManager inputMethodManager;

    String extra;
    String[] specialCharacters = {"á", "ć", "é", "ǵ", "ḣ", "j´", "ķ", "ḱ", "ľ", "ṕ", "š", "ś", "š", "ṫ", "ĺ", "ź"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLanguage(this);
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
                    case R.id.nav_word_list:
                        intent = new Intent(getApplicationContext(), ArchiveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_alphabet:
                        intent = new Intent(getApplicationContext(), AlphabetActivity.class);
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
                        intent.setData(Uri.parse("mailto:a.sahin.ual@gmail.com"));
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

        recyclerView = findViewById(R.id.rw_specialCharacters);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new CharacterRecyclerAdapter(specialCharacters, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        Drawable closePanelDrawable = getResources().getDrawable(R.drawable.close_special_character_panel);
        closePanelDrawable.setBounds(0,
                                    0,
                                    (int) (closePanelDrawable.getIntrinsicWidth() * 0.04f),
                                    (int) (closePanelDrawable.getIntrinsicHeight() * 0.04f));

        btn_closePanel = findViewById(R.id.btn_closePanel);
        btn_closePanel.setCompoundDrawables(closePanelDrawable, null, null, null);
        btn_closePanel.setCompoundDrawablePadding((int) (8 * getResources().getDisplayMetrics().density));
        btn_closePanel.setVisibility(View.INVISIBLE);

        Drawable specialCharactersDrawable = getResources().getDrawable(R.drawable.special_characters);
        specialCharactersDrawable.setBounds(0,
                            0,
                            (int) (specialCharactersDrawable.getIntrinsicWidth() * 0.08f),
                            (int) (specialCharactersDrawable.getIntrinsicHeight() * 0.08f));
        btn_showSpecialCharacters = findViewById(R.id.btn_showSpecialCharacters);
        btn_showSpecialCharacters.setCompoundDrawablePadding((int) (15 * getResources().getDisplayMetrics().density));
        btn_showSpecialCharacters.setCompoundDrawables(specialCharactersDrawable, null, null, null);

        btn_showSpecialCharacters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeOut(btn_showSpecialCharacters);
                slideInFromRight(btn_closePanel);
                slideInFromBottom(recyclerView);
            }
        });

        btn_closePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideOutToRight(btn_closePanel);
                slideOutToBottom(recyclerView);
                fadeIn(btn_showSpecialCharacters);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SourceActivity")) {
            extra = intent.getExtras().getString("SourceActivity");
            if (extra.equals("WordDetailActivity")) {
                sw_searchForWord.requestFocus();
                showKeyboard();
            }
        } else {
            rootView.requestFocus();
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

    public void checkLanguage(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferredLanguage = sharedPreferences
                .getString("preferredLanguage", Locale.getDefault().getLanguage());
        Log.v("TAG", "preferredLanguage: " + preferredLanguage);
        String preferredLanguageCode = null;
        if (preferredLanguage.equals("Circassian")) {
            preferredLanguageCode = "cau";
        } else if (preferredLanguage.equals("Turkish")) {
            preferredLanguageCode = "tr";
        } else if (preferredLanguage.equals("English")) {
            preferredLanguageCode = "en";
        } else {
            Log.v("TAG", "Unknown language selection");
        }
        if (preferredLanguageCode != null) {
            Locale locale = new Locale(preferredLanguageCode);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            config.locale = locale;
            context.getResources().updateConfiguration(config, displayMetrics);
        }
    }

    @Override
    public void onCharacterClick(int position) {
        String characterClicked = specialCharacters[position];
        et_queryText.append(characterClicked);
    }

    public void slideInFromBottom(View view) {
        TranslateAnimation animation =
                new TranslateAnimation(0, 0, view.getHeight() + 60, 0);
        animation.setDuration(400);
        animation.setFillAfter(true);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    public void slideInFromRight(View view) {
        TranslateAnimation animation =
                new TranslateAnimation(view.getWidth() + 60, 0, 0, 0);
        animation.setDuration(400);
        animation.setFillAfter(true);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    public void slideOutToBottom(View view) {
        TranslateAnimation animation =
                new TranslateAnimation(0, 0, 0, view.getHeight() + 60);
        animation.setDuration(400);
        animation.setFillAfter(true);
        view.startAnimation(animation);
        view.setVisibility(View.INVISIBLE);
    }

    public void slideOutToRight(View view) {
        TranslateAnimation animation =
                new TranslateAnimation(0, view.getWidth() + 60, 0, 0);
        animation.setDuration(400);
        animation.setFillAfter(true);
        view.startAnimation(animation);
        view.setVisibility(View.INVISIBLE);
    }

    public void fadeIn(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in));
        view.setVisibility(View.VISIBLE);
    }

    public void fadeOut(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out));
        view.setVisibility(View.INVISIBLE);
    }
}


