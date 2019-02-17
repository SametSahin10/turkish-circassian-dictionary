package com.tur_cirdictionary.turkish_circassiandictionary;

import android.content.Context;
import android.database.Cursor;
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

        TextView tv_circassian = view.findViewById(R.id.tv_circassian);

        String circassian = cursor.getString(cursor.
                getColumnIndexOrThrow(WordContract.WordEntry.COLUMN_NAME_CIRCASSIAN));

        tv_circassian.setText(circassian);

    }
}
