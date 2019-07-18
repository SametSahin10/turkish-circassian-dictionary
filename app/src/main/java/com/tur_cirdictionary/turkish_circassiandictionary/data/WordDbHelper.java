package com.tur_cirdictionary.turkish_circassiandictionary.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract.WordEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WordDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH
            = "/data/data/com.tur_cirdictionary.turkish_circassiandictionary/databases/";
    private static final String DATABASE_NAME = "Cir_Tur_1.sqlite";
    private SQLiteDatabase database;
    private final Context context;

    public WordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void createDatabase() {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.v("Database", "Database exists");
            //Cool. Don't do anything.
        } else {
            try {
                Log.v("Database", "Database does not exists");
                Log.v("Database", "Copying the database");
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        String path = DATABASE_PATH + DATABASE_NAME;
        File db = new File(path);
        if (db.exists()) {
            return true;
        }
        File dir = new File(db.getParent());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return false;
    }

    private void copyDataBase() throws IOException {
        InputStream inputStream = context.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream outputStream = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public SQLiteDatabase openDatabase() {
        String path = DATABASE_PATH + DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static final String SQL_CREATE_WORDS =
            "CREATE TABLE  " + WordEntry.TABLE_NAME + " ("
                    + WordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + WordEntry.COLUMN_NAME_CIRCASSIAN + " TEXT, "
                    + WordEntry.COLUMN_NAME_TURKISH + " TEXT)";

    private static final String SQL_DELETE_WORDS =
            "DROP TABLE IF EXISTS " + WordContract.WordEntry.TABLE_NAME;

}
