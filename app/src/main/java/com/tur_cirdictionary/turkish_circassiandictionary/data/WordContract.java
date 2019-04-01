package com.tur_cirdictionary.turkish_circassiandictionary.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class WordContract {

    public static final String CONTENT_AUTHORITY =
            "com.tur_cirdictionary.turkish_circassiandictionary";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WORDS = "words";

    private WordContract() {}

    public static class WordEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_WORDS);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + PATH_WORDS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_WORDS;

        public static final String TABLE_NAME = "words";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME_CIRCASSIAN = "circassian";
        public static final String COLUMN_NAME_TURKISH = "turkish";

    }

}
