package com.crackncrunch.pillreminder.data.dto;

import android.database.Cursor;

import static com.crackncrunch.pillreminder.data.db.PillDbContract.PillColumns;
import static com.crackncrunch.pillreminder.data.db.PillDbContract.getColumnFloat;
import static com.crackncrunch.pillreminder.data.db.PillDbContract.getColumnLong;
import static com.crackncrunch.pillreminder.data.db.PillDbContract.getColumnString;

public class Pill {
    private String name;
    private float qty;
    private long date;

    public Pill(Cursor cursor) {
        this.name = getColumnString(cursor, PillColumns.COLUMN_PILL_NAME);
        this.qty = getColumnFloat(cursor, PillColumns.COLUMN_QTY);
        this.date = getColumnLong(cursor, PillColumns.COLUMN_DATE);
    }

    public String getName() {
        return name;
    }

    public float getQty() {
        return qty;
    }

    public long getDate() {
        return date;
    }
}
