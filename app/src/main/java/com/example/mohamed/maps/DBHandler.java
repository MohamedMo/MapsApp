package com.example.mohamed.maps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Mohamed on 26/04/2016.
 */
public class DBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favourites.db";
    public static final String TABLE_FAVOURITES = "favourites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VICINITY = "vicinity";
    public static final String COLUMN_RATINGS = "ratings";
    public static final String COLUMN_URL = "url";
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> listDB = new ArrayList<String>();
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_FAVOURITES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_VICINITY + " TEXT, " +
                COLUMN_RATINGS + " INTEGER, " +
                COLUMN_URL + " TEXT " +
                ");";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP_TABLE IF EXIST " + TABLE_FAVOURITES);
        onCreate(db);
    }


    public void addToDatabase (String name , String vicinity , Double ratings , String url){

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_VICINITY,vicinity);
        values.put(COLUMN_RATINGS,ratings);
        values.put(COLUMN_URL,url);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_FAVOURITES, null, values);
        db.close();
    }


    public void deleteItem (String name){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FAVOURITES + " WHERE " + COLUMN_NAME + "='" + name + "'");

    }


    public ArrayList<String> databaseToString (){

        String text = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_FAVOURITES + " WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("name")) != null){

                text = c.getString(c.getColumnIndex("name"));
                list.add(text);

            }
            c.moveToNext();
        }

        db.close();
        return list;
    }

    public ArrayList<String> getList (String name){

        String namedb ="";
        String vicinity ="";
        Double ratings;
        String url ="";

        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES + " WHERE "
                + COLUMN_NAME + "='" + name + "'";

        Cursor c = db.rawQuery(selectQuery, null);


        if (c != null) {
            c.moveToFirst();


            namedb = c.getString(c.getColumnIndex(COLUMN_NAME));
            vicinity = c.getString(c.getColumnIndex(COLUMN_VICINITY));
            url = c.getString(c.getColumnIndex(COLUMN_URL));




        }

        listDB.add(namedb);
        listDB.add(vicinity);
        listDB.add(url);

        System.out.println(listDB);



        return listDB;
    }

    public Double getRating (String name){


        Double ratings = null;


        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES + " WHERE "
                + COLUMN_NAME + "='" + name + "'";

        Cursor c = db.rawQuery(selectQuery, null);


        if (c != null) {
            c.moveToFirst();


            ratings = c.getDouble(c.getColumnIndex(COLUMN_RATINGS));





        }


        System.out.println(listDB);



        return ratings;
    }




    public boolean check (String name) {
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES + " WHERE "
                + COLUMN_NAME + "='" + name + "'";

        Cursor c = db.rawQuery(selectQuery, null);
        if(c.getCount() <= 0){

            return false;
        }

        return true;
    }


}
