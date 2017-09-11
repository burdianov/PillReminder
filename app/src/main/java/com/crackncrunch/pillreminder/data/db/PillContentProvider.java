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
    public static final int PILL_WITH_ID = 101;

    private PillDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.crackncrunch.pillreminder/tasks
        sUriMatcher.addURI(PillDbContract.CONTENT_AUTHORITY,
                PillDbContract.TABLE_PILLS, PILLS);

        // content://com.crackncrunch.pillreminder/tasks/id
        sUriMatcher.addURI(PillDbContract.CONTENT_AUTHORITY,
                PillDbContract.TABLE_PILLS + "/#",
                PILL_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PillDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

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
            case PILL_WITH_ID:
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
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case PILLS:
                long id = db.insert(PillDbContract.TABLE_PILLS, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(
                            PillDbContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable
            String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case PILLS:
                //Rows aren't counted with null selection
                selection = (selection == null) ? "1" : selection;
                break;
            case PILL_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", PillDbContract.PillColumns._ID);
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Illegal delete URI");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = db.delete(PillDbContract.TABLE_PILLS, selection, selectionArgs);

        if (count > 0) {
            //Notify observers of the change
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues
            contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case PILL_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", PillDbContract.PillColumns._ID);
                selectionArgs = new String[]{String.valueOf(id)};

                int count = db.update(PillDbContract.TABLE_PILLS,
                        contentValues, selection, selectionArgs);
                if (count > 0) {
                    //Notify observers of the change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
