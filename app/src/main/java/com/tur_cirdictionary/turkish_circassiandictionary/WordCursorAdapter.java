package com.tur_cirdictionary.turkish_circassiandictionary;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract;

public class WordCursorAdapter extends CursorAdapter {

    public WordCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_word, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

//        TextView tv_suggestedCircassian = view.findViewById(R.id.tv_suggestedCircassian);
//        TextView tv_suggestedTurkish = view.findViewById(R.id.tv_suggestedTurkish);

        TextView tv_suggestedWord = view.findViewById(R.id.tv_suggestedWord);
        String columnName = cursor.getColumnName(1);
        if (columnName.equals(WordContract.WordEntry.COLUMN_NAME_CIRCASSIAN)) {
            String circassian = cursor.getString(cursor.
                    getColumnIndexOrThrow(WordContract.WordEntry.COLUMN_NAME_CIRCASSIAN));
            tv_suggestedWord.setText(circassian);
        } else if (columnName.equals(WordContract.WordEntry.COLUMN_NAME_TURKISH)) {
            String turkish = cursor.getString(cursor.
                    getColumnIndexOrThrow(WordContract.WordEntry.COLUMN_NAME_TURKISH));
            tv_suggestedWord.setText(turkish);
        } else {
            Log.v("TAG", "Unknown column name");
        }

    }
}
