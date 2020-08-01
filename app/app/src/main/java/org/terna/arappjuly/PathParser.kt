package org.terna.arappjuly

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import org.terna.arappjulyy.docObject

class PathParser : AsyncTask<docObject,Void,String>() {
    fun parse(document : DocumentSnapshot,context: Context)
    {


    }
    public interface PathParseStatus
    {
        fun  pathParsed(routeParentId: String?)
       // fun didRecievedError()

    }

    var listner : PathParseStatus? = null
    override fun doInBackground(vararg params: docObject?): String? {
        var documents = params[0]!!.documents
        var context = params[0]!!.context
        var routeParentId = params[0]!!.routeParentId
        for (document in documents!!)
        {
            var local_db = Fb_coordsDB(context!!, org.terna.arappjuly.dbName,null,1)
            Log.e("fb", "${document!!.id} => ${document["coords"]}")
            val docMap = document.get("coords") as Map<String, *>


            Log.e("fb", "${docMap.entries.size}")
            for (i in 1..docMap.entries.size)
            {
                var x = (docMap.get("$i") as Map<String, *>).get("x").toString().toFloat()
                var y = (docMap.get("$i") as Map<String, *>).get("y").toString().toFloat()
                var z = (docMap.get("$i") as Map<String, *>).get("z").toString().toFloat()
                var id = document.id
                local_db.newPath(id,x,y,z)

            }
        }


//        var x = (docMap.get("1") as Map<String, *>).get("x")
//        var y = (docMap.get("1") as Map<String, *>).get("y")
//        var z = (docMap.get("1") as Map<String, *>).get("z")
//        Log.e("fb", "${document.id} => x$x y$y z$z}")
        return routeParentId
    }

    override fun onPostExecute(routeParentId: String?) {
        super.onPostExecute(routeParentId)
        listner!!.pathParsed(routeParentId)
    }

}