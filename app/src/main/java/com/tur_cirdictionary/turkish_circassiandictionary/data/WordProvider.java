package com.tur_cirdictionary.turkish_circassiandictionary.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import static com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.BASE_CONTENT_URI;
import static com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class WordProvider extends ContentProvider {

    public static final String LOG_TAG = WordProvider.class.getSimpleName();

    private static final int WORDS = 100;
    private static final int WORD_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(WordContract.CONTENT_AUTHORITY, WordContract.PATH_WORDS, WORDS);
        sUriMatcher.addURI(WordContract.CONTENT_AUTHORITY, WordContract.PATH_WORDS
                + "/#", WORD_ID);

    }

    private WordDbHelper wordDbHelper;

    @Override
    public boolean onCreate() {
        wordDbHelper = new WordDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        wordDbHelper.createDatabase();
        SQLiteDatabase database = wordDbHelper.openDatabase();
        if (database == null) {
            Log.v("TAG", "Database is null");
        }
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case WORDS:
                cursor = database.query(WordEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            case WORD_ID:
                selection = WordEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(WordEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = sUriMatcher.match(uri);

        switch (match) {

            case WORDS:
                return WordEntry.CONTENT_LIST_TYPE;

            case WORD_ID:
                return WordEntry.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match" + match);

        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = sUriMatcher.match(uri);

        switch (match) {

            case WORDS:
                return insertWord(uri, values);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        int numOfRowsDeleted;

        int match = sUriMatcher.match(uri);

        SQLiteDatabase database = wordDbHelper.openDatabase();

        switch (match) {

            case WORDS:
                numOfRowsDeleted = database.delete(WordEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case WORD_ID:
                long wordId = ContentUris.parseId(uri);

                selection = WordEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(wordId)};

                numOfRowsDeleted =
                        database.delete(WordEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);

        }

        if (numOfRowsDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);

        }

        return numOfRowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = sUriMatcher.match(uri);

        switch (match) {

            case WORDS:
                return updateWord(uri, values, selection, selectionArgs);

            case WORD_ID:
                long wordId = ContentUris.parseId(uri);

                selection = WordEntry._ID + "=?";
                selectionArgs  = new String[] {String.valueOf(wordId)};

                return updateWord(uri, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);

        }

    }

    private Uri insertWord(Uri uri, ContentValues values) {

        if (values.containsKey(WordEntry.COLUMN_NAME_CIRCASSIAN)) {

            String circassian = values.getAsString(WordEntry.COLUMN_NAME_CIRCASSIAN);

            if (circassian == null) {

                throw new IllegalArgumentException("Word requires circassian translation");

            }

        }

        if (values.containsKey(WordEntry.COLUMN_NAME_TURKISH)) {

            String turkish = values.getAsString(WordEntry.COLUMN_NAME_TURKISH);

            if (turkish == null) {

                throw new IllegalArgumentException("Word requires turkish translation");

            }

        }

        if (values.size() < 0) {

            return null;

        }

        SQLiteDatabase database = wordDbHelper.openDatabase();

        long idOfNewlyInserted = database.insert(WordEntry.TABLE_NAME, null, values);

        if (idOfNewlyInserted == -1) {

            return null;

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(WordEntry.CONTENT_URI, idOfNewlyInserted);

    }

    private int updateWord(Uri uri, ContentValues values,
                           String selection, String[] selectionArgs) {

        if (values.containsKey(WordEntry.COLUMN_NAME_CIRCASSIAN)) {

            String circassian = values.getAsString(WordEntry.COLUMN_NAME_CIRCASSIAN);

            if (circassian == null) {

                throw new IllegalArgumentException("Circassian translation required ");

            }

        }

        if (values.containsKey(WordEntry.COLUMN_NAME_TURKISH)) {

            String turkish = values.getAsString(WordEntry.COLUMN_NAME_TURKISH);

            if (turkish == null) {

                throw new IllegalArgumentException("Turkish translation required");

            }

        }

        if (values.size() < 0) {

            return 0;

        }

        SQLiteDatabase database = wordDbHelper.openDatabase();

        int numOfRowsUpdated =
                database.update(WordEntry.TABLE_NAME, values, selection, selectionArgs);

        if (numOfRowsUpdated != 0) {

            getContext().getContentResolver().notifyChange(uri, null);

        }

        return numOfRowsUpdated;

    }

}
