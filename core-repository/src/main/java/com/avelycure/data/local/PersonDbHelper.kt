package com.avelycure.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PersonDbHelper(context: Context): SQLiteOpenHelper(context, SQLiteConstants.DB_NAME, null, 1) {
    override fun onCreate(sqLiteDb: SQLiteDatabase) {
        sqLiteDb.execSQL("CREATE TABLE ${SQLiteConstants.TABLE_NAME_PERSONS} (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "person_id INTEGER," +
                "profile_path TEXT," +
                "adult NUMERIC," +
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
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}