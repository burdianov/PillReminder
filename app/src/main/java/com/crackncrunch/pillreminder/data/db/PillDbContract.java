package com.crackncrunch.pillreminder.data.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class PillDbContract {
    public static final String TABLE_PILLS = "pills";

    public static final class PillColumns implements BaseColumns {
        public static final String COLUMN_PILL_NAME = "pill_name";
        public static final String COLUMN_QTY = "qty";
        public static final String COLUMN_DATE = "date";
    }

    public static final String CONTENT_AUTHORITY = "com.crackncrunch.pillreminder";

    public static final String NAME_SORT = String.format("%s ASC, %s DESC",
            PillColumns.COLUMN_PILL_NAME, PillColumns.COLUMN_DATE);
    public static final String DATE_SORT = String.format("%s DESC, %s ASC",
            PillColumns.COLUMN_DATE, PillColumns.COLUMN_PILL_NAME);

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_PILLS)
            .build();

    public static Uri buildUriWithId(long id) {
        return CONTENT_URI.buildUpon()
                .appendPath(Long.toString(id))
                .build();
    }

    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static float getColumnFloat(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
