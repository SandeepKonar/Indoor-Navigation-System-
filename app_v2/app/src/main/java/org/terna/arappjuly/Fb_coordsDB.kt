package org.terna.arappjuly

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Fb_coordsDB(
    context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int
) : SQLiteOpenHelper(context, name, null, 1)
{
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table venues (venue_fb_id TEXT PRIMARY KEY ,venue_name TEXT,created_by TEXT)")
        db.execSQL("create table routes (route_fb_id TEXT PRIMARY KEY ,route_name TEXT,route_parent_id TEXT,created_by TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun newVenue(venue_fb_id : String , venue_name : String , created_by : String)
    {
        var values = ContentValues()

        values.put("venue_fb_id",venue_fb_id)
        values.put("venue_name",venue_name)
        values.put("created_by",created_by)
        val db = writableDatabase

        db.replace("venues",null,values)
    }
    fun newRoute (route_fb_id : String , route_name : String ,route_parent_id : String, created_by : String )
    {
        var values = ContentValues()

        values.put("route_fb_id",route_fb_id)
        values.put("route_name",route_name)
        values.put("created_by",created_by)
        values.put("route_parent_id",route_parent_id)
        val db = writableDatabase
        db.replace("routes",null,values)
    }

    fun newPath (route_fb_id : String,x:Float,y:Float,z:Float)
    {
        val db = readableDatabase
        db.execSQL("create table IF NOT EXISTS ${route_fb_id} (ID INTEGER PRIMARY KEY   AUTOINCREMENT,x REAL ,y REAL,z REAL)")
        var values = ContentValues()

        values.put("x",x)
        values.put("y",y)
        values.put("z",z)
        db.insert(route_fb_id,null,values)
    }

    fun getVenue(venue_fb_id:String) : VenueModel
    {
        val db = readableDatabase
        var venue : VenueModel? = null
        val cursor = db.rawQuery("SELECT * FROM venues WHERE venue_fb_id=\"$venue_fb_id\" ",null )
        if (cursor.moveToFirst()) {
            do {
                Log.e("FbCoordsDB","cursor.getString(cursor.getColumnIndex(\"venue_name\"))")
                var venue_fb_name = cursor.getString(cursor.getColumnIndex("venue_name"))
                var created_by = cursor.getString(cursor.getColumnIndex("created_by"))
                venue = VenueModel(venue_fb_id,venue_fb_name,created_by)
            }while (cursor.moveToNext())
        }

        return  venue!!
    }
    fun getVenueByPosition(position:Int) : VenueModel
    {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM venues WHERE rowid=${position+1} ",null )
//        if (cursor.moveToFirst()) {
        cursor.moveToFirst()
            do {
                Log.e("FbCoordsDB","getvenuebyposition${cursor.getString(cursor.getColumnIndex("venue_fb_id"))}")
                var venue_fb_id = cursor.getString(cursor.getColumnIndex("venue_fb_id"))
                var venue_fb_name = cursor.getString(cursor.getColumnIndex("venue_name"))
                var created_by = cursor.getString(cursor.getColumnIndex("created_by"))
                var venue = VenueModel(venue_fb_id,venue_fb_name,created_by)
                return  venue
            }while (cursor.moveToNext())
        //}


    }
    fun getRouteByPosition(position:Int) : RouteModel
    {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM routes WHERE rowid=${position+1} ",null )
//        if (cursor.moveToFirst()) {
        cursor.moveToFirst()
        do {
            Log.e("FbCoordsDB","getroutebyposition${cursor.getString(cursor.getColumnIndex("route_fb_id"))}")
            var routeId = cursor.getString(cursor.getColumnIndex("route_fb_id"))
            var routeName = cursor.getString(cursor.getColumnIndex("route_name"))
            var created_by = cursor.getString(cursor.getColumnIndex("created_by"))
            var routeParentId =  cursor.getString(cursor.getColumnIndex("route_parent_id"))
            var route = RouteModel(routeId,routeName,routeParentId,created_by)

            return  route
        }while (cursor.moveToNext())
        //}


    }
    fun getRoute(route_fb_id:String): RouteModel
    {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM routes WHERE route_fb_id=\"${route_fb_id}\" ",null )
        cursor.moveToFirst()
        do {
            var route_fb_name = cursor.getString(cursor.getColumnIndex("route_name"))
            var created_by = cursor.getString(cursor.getColumnIndex("created_by"))
            var route_parent_id =  cursor.getString(cursor.getColumnIndex("route_parent_id"))
            var route = RouteModel(route_fb_id,route_fb_name,route_parent_id,created_by)
            return  route
        }while (cursor.moveToNext())

    }

    fun getVenues(): ArrayList<VenueModel> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM venues ",null )
        //var venue_fb_name = cursor.getString(cursor.getColumnIndex("venue_name"))
        val venues = ArrayList<VenueModel>()
        if (cursor.moveToFirst()) {
            do {
                var venue_fb_name = cursor.getString(cursor.getColumnIndex("venue_name"))
                var created_by = cursor.getString(cursor.getColumnIndex("created_by"))
                var venue_fb_id = cursor.getString(cursor.getColumnIndex("venue_fb_id"))
                var venue = VenueModel(venue_fb_id,venue_fb_name,created_by)
                venues.add(venue)
            } while (cursor.moveToNext())

        }
        cursor.close()
        return venues
    }
    fun getVenuesNames(): ArrayList<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM venues ",null )
        //var venue_fb_name = cursor.getString(cursor.getColumnIndex("venue_name"))
        val venues = ArrayList<String>()
        if (cursor.moveToFirst()) {
            do {
                var venue_fb_name = cursor.getString(cursor.getColumnIndex("venue_name"))
                var created_by = cursor.getString(cursor.getColumnIndex("created_by"))
                var venue_fb_id = cursor.getString(cursor.getColumnIndex("venue_fb_id"))
                var venue = VenueModel(venue_fb_id,venue_fb_name,created_by)
                venues.add(venue_fb_name)
            } while (cursor.moveToNext())

        }
        cursor.close()
        return venues
    }
    fun getRoutes(route_parent_id: String): ArrayList<RouteModel> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM routes WHERE route_parent_id=$route_parent_id ",null )
        val routes = ArrayList<RouteModel>()
        if (cursor.moveToFirst()) {
            do {
                var route_fb_id = cursor.getString(cursor.getColumnIndex("route_fb_id"))
                var route_fb_name = cursor.getString(cursor.getColumnIndex("route_name"))
                var created_by = cursor.getString(cursor.getColumnIndex("created_by"))
                var route_parent_id =  cursor.getString(cursor.getColumnIndex("route_parent_id"))
                var route = RouteModel(route_fb_id,route_fb_name,route_parent_id,created_by)
                routes.add(route)
            } while (cursor.moveToNext())

        }
        cursor.close()
        return routes
    }
    fun checkTable(route_fb_id: String) : Boolean
    {
        var  result :Boolean? = false
        val db = readableDatabase
        val cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name=\"$route_fb_id\"",null )
        Log.e("check", "  val cursor = db.rawQuery(\"select DISTINCT tbl_name from sqlite_master where tbl_name=\"$route_fb_id\",null )")
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close()
                result= true
                Log.e("check", "  result = $result")
            }
            cursor.close()
        }else
        {
            Log.e("check", "  result = $result")
            result= false
        }


      return result!!
    }
    fun getRouteNames(route_parent_id: String): ArrayList<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM routes WHERE route_parent_id=\"$route_parent_id\"",null )

        val routeNames = ArrayList<String>()
        if (cursor.moveToFirst()) {
            do {
                var route_name = cursor.getString(cursor.getColumnIndex("route_name"))


                routeNames.add(route_name)
            } while (cursor.moveToNext())

        }
        cursor.close()
        return routeNames
    }
    fun test(query :  String): Cursor? {
        var  db = readableDatabase
        var result = db.rawQuery(query,null )
        return result
    }


    public fun getRouteCoords(routeId : String) : ArrayList<CoordsModel>
    {
        var x : Float? = null
        var y : Float? = null
        var z : Float? = null
        var result = ArrayList<CoordsModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $routeId ",null )
        if (cursor.moveToFirst()) {
            do {
               // var route_name = cursor.getString(cursor.getColumnIndex("route_name"))
                x = cursor.getFloat(cursor.getColumnIndex("x"))
                y = cursor.getFloat(cursor.getColumnIndex("y"))
                z = cursor.getFloat(cursor.getColumnIndex("z"))
                var xyz = CoordsModel(x,y,z)
                result.add(xyz)

            } while (cursor.moveToNext())

        }

        return result

    }

    public fun getPath(routeId : String,i : Int) : Array<Float?>
    {
        var x : Float? = null
        var y : Float? = null
        var z : Float? = null
        var routeId = routeId.replace("\\s".toRegex(), "")
        val db = readableDatabase
        var args = arrayOf("$i")
        if(i<=getCount(routeId))
        {

            val cursor = db.rawQuery("SELECT * FROM $routeId WHERE ID=? ",args )
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
    fun getCount(routeId : String) : Int
    {
        val db = readableDatabase
        var count = DatabaseUtils.queryNumEntries(db, "$routeId")
        //Toast.makeText(context,"${count}", Toast.LENGTH_LONG).show()
        return count.toInt()
    }

}























//class DBOperations(context: Context?) :
//    SQLiteOpenHelper(context, database, null, 1) {
//    var table_name = "RegisterationDetails"
//    var coloum_1 = "ID"
//    var coloum_2 = "NAME"
//    var coloum_3 = "AGE"
//    var coloum_4 = "BREED"
//    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL("create table RegisterationDetails (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,AGE TEXT,BREED TEXT)")
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
//        db.execSQL("DROP TABLE IF EXISTS RegisterationDetails")
//        onCreate(db)
//    }
//
//    companion object {
//        var database = "registeration"
//    }
//}