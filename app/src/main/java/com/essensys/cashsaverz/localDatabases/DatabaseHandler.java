package com.essensys.cashsaverz.localDatabases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bmb";
    private static final String TABLE_CART = "cart";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_BOOK_NAME = "book_name";
    private static final String COLUMN_AUTHOR_NAME = "author_name";
    private static final String COLUMN_BOOK_IMAGE = "book_image";
    private Context mCtx;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mCtx = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_CART_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CART + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_USER_ID + " TEXT ," +
                COLUMN_PRODUCT_ID + " TEXT ," + COLUMN_BOOK_NAME + " TEXT, " + COLUMN_AUTHOR_NAME +
                " TEXT, " + COLUMN_BOOK_IMAGE + " TEXT)";
        db.execSQL(CREATE_CART_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        onCreate(db);
    }


    public void dropTable() {
        // Drop older table if existed
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        onCreate(db);
    }

    public void insertCartItemToLocalDb(String user_id, String book_id, String book_name, String book_author, String book_image) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursorCheck;
        String checkQuery = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_USER_ID + "= " + user_id + "  AND " + COLUMN_PRODUCT_ID + " = " + book_id + "";
        SQLiteDatabase db1 = this.getReadableDatabase();
        cursorCheck = db1.rawQuery(checkQuery, null);
        if (cursorCheck.moveToFirst()) {
            Toast.makeText(mCtx, "Already added to cart.", Toast.LENGTH_LONG).show();
        } else {

            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, user_id);//index 1
            values.put(COLUMN_PRODUCT_ID, book_id);//index 2
            values.put(COLUMN_BOOK_NAME, book_name);//index 3
            values.put(COLUMN_AUTHOR_NAME, book_author);//index 4
            values.put(COLUMN_BOOK_IMAGE, book_image);//index 5

            // Inserting Row
            long rowid = db.insert(TABLE_CART, null, values);//tableName, nullColumnHack, CotentValues
            if (rowid != -1) {
                Toast.makeText(mCtx, "Item added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mCtx, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        db.close(); // Closing database connection
    }

    public JSONArray getCartItems(String user_id) throws JSONException {
        String selectQuery = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_USER_ID + " = " + user_id + "";
        SQLiteDatabase myDataBase = this.getReadableDatabase();
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME", resultSet.toString());
        return resultSet;
    }

    public void deleteSingleCartItem(String book_id, String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String DELETE_RECORD = "DELETE FROM " + TABLE_CART + " WHERE " + COLUMN_PRODUCT_ID + " = " +
                book_id + " AND " + COLUMN_USER_ID + " = " + user_id + "";
        db.execSQL(DELETE_RECORD);
    }

    public void clearCart() {
        SQLiteDatabase db = this.getReadableDatabase();
        String DELETE_TABLE = "DELETE FROM " + TABLE_CART;
        db.execSQL(DELETE_TABLE);
    }

    public boolean checkDataBase() {
        File dbFile = mCtx.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

/*    public void updateUserTypeToMerchant(String profileimg)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String UPDATE_TABLE = "UPDATE " + TABLE_NAME+" SET "+COLUMN_USER_TYPE+" =3 , "+COLUMN_USER_PROFILE+" = '"+profileimg+"'";
        db.execSQL(UPDATE_TABLE);

    }
    public void updateUserSubscription(String ad, String job)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String UPDATE_TABLE = "UPDATE " + TABLE_NAME+" SET "+COLUMN_USER_ADVERTISE+" = '"+ad+"', "+COLUMN_USER_JOB+" = '"+job+"'";
        db.execSQL(UPDATE_TABLE);

    }*/

}