package com.avelycure.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieDbHelper(context: Context): SQLiteOpenHelper(context, SQLiteConstants.DB_NAME, null, 1) {
    override fun onCreate(sqLiteDb: SQLiteDatabase) {
        sqLiteDb.execSQL("CREATE TABLE ${SQLiteConstants.TABLE_NAME_MOVIES} (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "original_title TEXT," +
                "poster_path TEXT," +
                "genres TEXT," +
                "popularity REAL," +
                "vote_average REAL," +
                "release_date NUMERIC," +
                "movie_id INTEGER," +
                "vote_count INTEGER);"
        )
    }

    override fun onUpgrade(sqLiteDb: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}