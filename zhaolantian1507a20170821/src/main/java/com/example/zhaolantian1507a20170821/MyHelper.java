package com.example.zhaolantian1507a20170821;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 蜕变~成蝶 on 2017/8/21.
 */

public class MyHelper extends SQLiteOpenHelper{
    public MyHelper(Context context) {
        super(context, "data.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table data(_id text,content text,created_at text,publishedAt text,rand_id text,title text,updated_at text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
