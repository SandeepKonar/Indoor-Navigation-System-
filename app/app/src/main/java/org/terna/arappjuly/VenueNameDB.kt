package org.terna.arappjuly/*package layout

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VenueNameDB(
    context: Context?,
    name: String?,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {
    var dbName = name
    var context = context
    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}*/