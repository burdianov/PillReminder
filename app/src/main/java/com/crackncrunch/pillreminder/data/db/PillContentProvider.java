package com.crackncrunch.pillreminder.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PillContentProvider extends ContentProvider {
    public static final String TAG = PillContentProvider.class.getSimpleName();

    public static final int PILLS = 100;
    public static final int PILLS_WITH_ID = 101;

    private PillDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.crackncrunch.pillreminder/tasks
        sUriMatcher.addURI(PillDbContract.CONTENT_AUTHORITY,
                PillDbContract.TABLE_PILLS, PILLS);

        // content://com.crackncrunch.pillreminder/tasks/id
        sUriMatcher.addURI(PillDbContract.CONTENT_AUTHORITY,
                PillDbContract.TABLE_PILLS + "/#",
                PILLS_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            //Query all tasks
            case PILLS:
                retCursor = db.query(PillDbContract.TABLE_PILLS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            //Query one task
            case PILLS_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", PillDbContract.PillColumns
                        ._ID);
                selectionArgs = new String[]{String.valueOf(id)};

                retCursor = db.query(PillDbContract.TABLE_PILLS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
