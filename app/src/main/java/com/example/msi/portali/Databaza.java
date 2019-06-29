package com.example.msi.portali;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Databaza extends SQLiteOpenHelper {
    public Databaza(Context context) {
        super(context,"news", null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE LAJMET (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE VARCHAR(1000),DESCRIPTION TEXT,AUTHOR TEXT,PUBLISHEDAT TEXT,SOURCE TEXT,TIME TEXT);";
        String query1 = "CREATE TABLE KOHA (BOTATIME INTEGER,TEKNOLOGJIATIME INTEGER,BITCOINTIME INTEGER,TOPTIME INTEGER,DAY TEXT);";
        String query2 = "CREATE TABLE SIGNUP(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(15),EMAIL TEXT,PASSWORD TEXT)";
        String query3 = "CREATE TABLE SAVE(SESSION INTEGER)";
        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
