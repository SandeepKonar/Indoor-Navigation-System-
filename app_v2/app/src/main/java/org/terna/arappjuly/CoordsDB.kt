package org.terna.arappjuly

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CoordsDB (
    context: Context?,
    dbName: String?,
    version: Int
): SQLiteOpenHelper(context, dbName, null, version) {
    var dbName = dbName
    var context = context
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE coords(ID INTEGER PRIMARY KEY   AUTOINCREMENT,x REAL ,y REAL,z REAL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertcoords(x:Float,y:Float,z:Float)
    {
        var values = ContentValues()

        values.put("x",x)
        values.put("y",y)
        values.put("z",z)
        val db = writableDatabase
        db.insert("coords",null,values)
    }
    fun getCount() : Int
    {
        val db = readableDatabase
        var count = DatabaseUtils.queryNumEntries(db, "coords")
        //Toast.makeText(context,"${count}", Toast.LENGTH_LONG).show()
        return count.toInt()
    }
    public fun getCoords(i : Int) : Array<Float?>
    {
        var x : Float? = null
        var y : Float? = null
        var z : Float? = null

        val db = readableDatabase
        var args = arrayOf("$i")
        if(i<=getCount())
        {

            val cursor = db.rawQuery("SELECT * FROM coords WHERE ID=? ",args )
            cursor.moveToFirst()
            //Log.e("getcoords","i=$i ${cursor.getFloat(cursor.getColumnIndex("x"))},${cursor.getFloat(cursor.getColumnIndex("z"))}")
            //Toast.makeText(context,"${cursor.getColumnIndex("x")},${cursor.getColumnIndex("z")}", Toast.LENGTH_LONG).show()
            x = cursor.getFloat(cursor.getColumnIndex("x"))
            y = cursor.getFloat(cursor.getColumnIndex("y"))
            z = cursor.getFloat(cursor.getColumnIndex("z"))

        }
        var xyz = arrayOf(x,y,z)
        //Log.e("getcoords","${xyz[0]}${xyz[2]}")
        return xyz

    }


}