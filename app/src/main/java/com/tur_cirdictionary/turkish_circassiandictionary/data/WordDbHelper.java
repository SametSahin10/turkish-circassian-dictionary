package com.tur_cirdictionary.turkish_circassiandictionary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

public class WordDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Cir_Tur.db";

    public WordDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_WORDS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_WORDS);
        onCreate(db);

    }

    private static final String SQL_CREATE_WORDS =
            "CREATE TABLE  " + WordEntry.TABLE_NAME + " ("
                    + WordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + WordEntry.COLUMN_NAME_CIRCASSIAN + " TEXT, "
                    + WordEntry.COLUMN_NAME_TURKISH + " TEXT)";

    private static final String SQL_DELETE_WORDS =
            "DROP TABLE IF EXISTS " + WordContract.WordEntry.TABLE_NAME;

}
