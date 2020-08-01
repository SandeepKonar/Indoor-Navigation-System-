package org.terna.arappjuly

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter


class FileHandler() {



    var context : Context? =null
    var dbName : String? = null
    var file: File? = null

    fun initializeFileHandler(context : Context,dbName : String,file: File)
    {
        this.context = context
        this.dbName  = dbName
        this.file = file
    }

    fun FileToDB()
    {
        val db = CoordsDB(context,dbName,1)
        var stream : FileReader? = null
        var bufferedReader = BufferedReader(FileReader(file))

        var line = bufferedReader.readLine()
        Log.e("FileToDB","${line}")
        while (line!=null)
        {
            Toast.makeText(context,"${line}",Toast.LENGTH_LONG).show()
            Log.e("FileToDB","${line}")
            var coords = line.split(" ")
            Toast.makeText(context,"${coords[0].toFloat()} ${coords[1].toFloat()} ${coords[2].toFloat()}", Toast.LENGTH_LONG).show()
            db.insertcoords(coords[0].toFloat(),coords[1].toFloat(),coords[2].toFloat())
            line = bufferedReader.readLine()
        }
    }

    fun DBToFile()
    {

        val db = CoordsDB(context,dbName,1)
        Toast.makeText(context,"${db.getCount()}",Toast.LENGTH_LONG).show()
        var stream : FileWriter? = null
        stream = FileWriter(file)
        db.getCount()
        var i = 1
        while (i<=db.getCount())
        {
            var coords = db.getCoords(i)
            stream.append("${coords[0]} ${coords[1]} ${coords[2]}\n")
            i++
        }
        stream.close()
    }
}