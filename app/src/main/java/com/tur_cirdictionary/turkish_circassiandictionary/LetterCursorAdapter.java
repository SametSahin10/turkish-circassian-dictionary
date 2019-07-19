package com.tur_cirdictionary.turkish_circassiandictionary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.tur_cirdictionary.turkish_circassiandictionary.data.WordContract;

public class LetterCursorAdapter extends CursorAdapter {
    public LetterCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_letter, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_letter = view.findViewById(R.id.tv_letter);
        TextView tv_letter_description = view.findViewById(R.id.tv_letter_description);

        int circassianColumnIndex = cursor.getColumnIndex(WordContract.WordEntry.COLUMN_NAME_CIRCASSIAN);
        int turkishColumnIndex = cursor.getColumnIndex(WordContract.WordEntry.COLUMN_NAME_TURKISH);

        String circassianMeaning = cursor.getString(circassianColumnIndex);
        String turkishMeaning = cursor.getString(turkishColumnIndex);

        tv_letter.setText(circassianMeaning);
        tv_letter_description.setText(turkishMeaning);
    }
}
