package com.example.dtixador.quoteoftheday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_QUOTES = "quotes";
    public static final String COL_ID = "ID";
    public static final String COL_AUTHOR = "author";
    public static final String COL_QUOTE = "quote";
    public static final String COL_LIKE = "like";
    public static final String COL_DISLIKE = "dislike";
    public static final String COL_FAV = "fav";


    private static final String DATABASE_NAME = "quotes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_QUOTES + " ("
            + COL_ID + " INTEGER , " + COL_AUTHOR + " TEXT NOT NULL, "
            + COL_QUOTE + " TEXT NOT NULL, " + COL_LIKE + " INTEGER, "
            + COL_DISLIKE + " INTEGER ," + COL_FAV + " BOOLEAN);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_QUOTES + ";");
        onCreate(db);
    }
}