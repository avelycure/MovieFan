package com.avelycure.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.avelycure.data.local.SQLiteConstants.DB_NAME
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_MOVIE_INFO

class MovieInfoDbHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(sqLiteDb: SQLiteDatabase) {
        sqLiteDb.execSQL("CREATE TABLE $TABLE_NAME_MOVIE_INFO (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "adult NUMERIC," +
                "budget INTEGER," +
                "imdb_id INTEGER, " +
                "original_lang TEXT," +
                "original_title TEXT," +
                "overview TEXT," +
                "popularity REAL," +
                "genres TEXT," +
                "p_companies TEXT," +
                "p_countries TEXT," +
                "release_date NUMERIC," +
                "status TEXT," +
                "revenue INTEGER," +
                "tagline TEXT," +
                "title TEXT," +
                "vote_average REAL," +
                "movie_id INTEGER," +
                "images_backdrop TEXT," +
                "images_posters TEXT);"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}