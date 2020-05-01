package com.essensys.cashsaverz.localDatabases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteDatabaseHandler extends SQLiteOpenHelper {
    public static final String COL_ID = "id";
    public static final String COL_PROD_ID = "product_id";
    public static final String COL_QTY = "qty";
    private static final String COL_USER_ID = "user_id";
    public static final String DATABASE_NAME = "CashSaverz.db";
    public static final String TABLE_NAME = "mycart_table";

    public SqliteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS mycart_table(id INTEGER PRIMARY KEY AUTOINCREMENT,user_id TEXT ,product_id TEXT ,qty INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS mycart_table");
        onCreate(db);
    }

    public boolean insertDataIntoCart(String product_id, String qty) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PROD_ID, product_id);
        contentValues.put(COL_QTY, qty);
        if (db.insert(TABLE_NAME, null, contentValues) == -1) {
            return false;
        }
        return true;
    }

    public Cursor getAllData() {
        return getWritableDatabase().rawQuery("select * from mycart_table", null);
    }
}
