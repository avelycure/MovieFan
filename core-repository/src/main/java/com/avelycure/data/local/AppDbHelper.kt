package com.avelycure.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.avelycure.data.local.SQLiteConstants.DB_NAME
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_MOVIES
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_MOVIE_INFO
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_PERSONS

class AppDbHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(sqLiteDb: SQLiteDatabase) {
        sqLiteDb.execSQL("CREATE TABLE $TABLE_NAME_MOVIES (" +
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

        sqLiteDb.execSQL("CREATE TABLE $TABLE_NAME_PERSONS (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "person_id INTEGER," +
                "profile_path TEXT," +
                "adult INTEGER," +
                "name TEXT," +
                "popularity REAL," +
                "known_for_department TEXT," +
                "known_for_movie TEXT," +
                "known_for_tv TEXT," +
                "expanded NUMERIC," +
                "birthday TEXT," +
                "death_day TEXT," +
                "also_known_as TEXT," +
                "gender INTEGER," +
                "biography TEXT," +
                "place_of_birth TEXT," +
                "imdb_id INTEGER," +
                "homepage TEXT," +
                "profile_images TEXT);"
        )

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

    override fun onUpgrade(sqLiteDb: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}