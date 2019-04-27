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

public class SuggestedWordCursorAdapter extends CursorAdapter {
    public SuggestedWordCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_word_suggestion, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_suggestedWord = view.findViewById(R.id.tv_word_suggestion);
        String columnName = cursor.getColumnName(1);
        if (columnName.equals(WordContract.WordEntry.COLUMN_NAME_CIRCASSIAN)) {
            String circassianMeaning = cursor.getString(cursor.
                    getColumnIndexOrThrow(WordContract.WordEntry.COLUMN_NAME_CIRCASSIAN));
            tv_suggestedWord.setText(circassianMeaning);
        } else if (columnName.equals(WordContract.WordEntry.COLUMN_NAME_TURKISH)) {
            String turkishMeaning = cursor.getString(cursor.
                    getColumnIndexOrThrow(WordContract.WordEntry.COLUMN_NAME_TURKISH));
            tv_suggestedWord.setText(turkishMeaning);
        } else {
            Log.v("TAG", "Unknown column name");
        }
    }

}
