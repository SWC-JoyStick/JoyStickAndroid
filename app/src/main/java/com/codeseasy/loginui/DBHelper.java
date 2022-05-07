package com.codeseasy.loginui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String db_name = "User-db";
    private static final int version = 1;
    public DBHelper(Context context){
        super(context,db_name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String init_table = "create table user(id integer primary key autoincrement,username varchar(20),password varchar(20),age integer)";
        db.execSQL(init_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

}
