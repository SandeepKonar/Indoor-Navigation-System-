package org.terna.arappjuly

import android.content.Context

class PathToDB {
    var context : Context? =null
    var dbName : String? = null
    var db : CoordsDB? = null
    fun initialize(context : Context, dbName : String)
    {
        this.context = context
        this.dbName  = dbName
        db = CoordsDB(context,dbName,1)

    }

    fun add(x : Float,y : Float,z : Float)
    {

        db?.insertcoords(x.toFloat(),y.toFloat(),z.toFloat())
    }
}
