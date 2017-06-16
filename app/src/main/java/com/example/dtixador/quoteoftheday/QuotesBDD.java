package com.example.dtixador.quoteoftheday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class QuotesBDD {

    private static final String TABLE_QUOTES = "quotes";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_AUTHOR = "author";
    private static final int NUM_COL_AUTHOR = 1;
    private static final String COL_QUOTE = "quote";
    private static final int NUM_COL_QUOTE = 2;
    private static final String COL_LIKE = "like";
    private static final int NUM_COL_LIKE = 3;
    private static final String COL_DISLIKE = "dislike";
    private static final int NUM_COL_DISLIKE = 4;
    private static final String COL_FAV = "fav";
    private static final int NUM_COL_FAV = 5;

    private SQLiteDatabase bdd;

    private MySQLiteHelper maBaseSQLite;

    public QuotesBDD(Context context){
        maBaseSQLite = new MySQLiteHelper(context);
    }

    public void open(){
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertQuote(int id, Quote quote){
        ContentValues values = new ContentValues();
        values.put(COL_AUTHOR, quote.getmAuthor());
        values.put(COL_QUOTE, quote.getmQuote());
        values.put(COL_LIKE, quote.getLike());
        values.put(COL_DISLIKE, quote.getDislike());
        values.put(COL_FAV, quote.isFav());
        values.put(COL_ID, id);
        return bdd.insert(TABLE_QUOTES, null, values);
    }

    public int updateQuote(int id, Quote quote){
        ContentValues values = new ContentValues();
        values.put(COL_AUTHOR, quote.getmAuthor());
        values.put(COL_QUOTE, quote.getmQuote());
        values.put(COL_LIKE, quote.getLike());
        values.put(COL_DISLIKE, quote.getDislike());
        values.put(COL_FAV, quote.isFav());
        quote.setId(id);
        values.put(COL_ID, quote.getId());
        return bdd.update(TABLE_QUOTES, values, COL_ID + " = " + id, null);
    }

    public int removeQuoteWithID(int id){
        return bdd.delete(TABLE_QUOTES, COL_ID + " = " +id, null);
    }

    public Quote getQuoteWithAuthor(String titre){
        Cursor c = bdd.query(TABLE_QUOTES, new String[] {COL_ID, COL_AUTHOR, COL_QUOTE, COL_LIKE, COL_DISLIKE, COL_FAV}, COL_AUTHOR + " LIKE \"" + titre +"\"", null, null, null, null);
        return cursorToQuote(c);
    }

    public void deleteAllQuotes() {
        bdd.delete("quotes", null, null);
    }

    public ArrayList<Quote> getAllQuotes() {
        Cursor cursor = bdd.rawQuery("select * from quotes",null);
        ArrayList<Quote> listOfQuotes = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Quote quote = new Quote();
                quote.setmAuthor(cursor.getString(NUM_COL_AUTHOR));
                quote.setmQuote(cursor.getString(NUM_COL_QUOTE));
                quote.setId(cursor.getInt(NUM_COL_ID));
                if (cursor.getString(NUM_COL_FAV).equals("1")) {
                    quote.setFav(true);
                }
                else {
                    quote.setFav(false);
                }
                quote.setLike(cursor.getInt(NUM_COL_LIKE));
                quote.setDislike(cursor.getInt(NUM_COL_DISLIKE));

                listOfQuotes.add(quote);
                cursor.moveToNext();
            }
        }
        return listOfQuotes;
    }

    private Quote cursorToQuote(Cursor c){
        if (c.getCount() == 0)
            return null;
        c.moveToFirst();
        Quote quote = new Quote();
        quote.setmAuthor(c.getString(NUM_COL_AUTHOR));
        quote.setmQuote(c.getString(NUM_COL_QUOTE));
        quote.setId(c.getInt(NUM_COL_ID));
        if (c.getString(NUM_COL_FAV).equals("1")) {
            quote.setFav(true);
        }
        else {
            quote.setFav(false);
        }
        quote.setLike(c.getInt(NUM_COL_LIKE));
        quote.setDislike(c.getInt(NUM_COL_DISLIKE));
        c.close();
        return quote;
    }
}