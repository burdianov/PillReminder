package com.crackncrunch.pillreminder.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crackncrunch.pillreminder.utils.ConstansManager;

import static com.crackncrunch.pillreminder.data.db.PillDbContract.PillColumns;
import static com.crackncrunch.pillreminder.data.db.PillDbContract.TABLE_PILLS;

public class PillDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pills.db";
    private static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_TABLE_PILLS = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, " +
                    "%s REAL, %s INTEGER)",
            TABLE_PILLS, PillColumns._ID, PillColumns.COLUMN_PILL_NAME,
            PillColumns.COLUMN_QTY, PillColumns.COLUMN_DATE
    );

    private final Context mContext;

    public PillDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_PILLS);
        loadDemoPill(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PILLS);
        onCreate(sqLiteDatabase);
    }

    private void loadDemoPill(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(PillColumns.COLUMN_PILL_NAME, ConstansManager.PILL_APROVEL);
        cv.put(PillColumns.COLUMN_QTY, 0.5f);
        cv.put(PillColumns.COLUMN_DATE, Long.MAX_VALUE);

        db.insertOrThrow(PillDbContract.TABLE_PILLS, null, cv);
    }
}
