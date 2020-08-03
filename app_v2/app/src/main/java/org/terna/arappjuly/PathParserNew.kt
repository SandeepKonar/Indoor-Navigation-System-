package org.terna.arappjuly

import android.os.AsyncTask
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import org.terna.arappjulyy.docObject

class PathParserNew : AsyncTask<pathReq, Void, String>() {
    var db = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = true
    }
    fun initFbSettings(){
        db.firestoreSettings = settings
    }
    var fbNew : fireBaseNew? = null
    public interface PathParseStatus
    {
        fun  pathParsed(routeParentId: String?)
        // fun didRecievedError()

    }
    var pathParselistner : PathParseStatus? = null



    override fun doInBackground(vararg params: pathReq?): String {
        var context = params[0]!!.context
        var routeParentId = params[0]!!.route!!.route_parent_id
        var routeName = params[0]!!.route!!.route_name
        var routeId = params[0]!!.route!!.route_fb_id
        fbNew = fireBaseNew(context!!)
        var local_db = Fb_coordsDB(context!!, org.terna.arappjuly.dbName,null,1)
        var route_doc = db.collection("coordinates").document("$routeParentId").collection("routes").document("$routeName").get().addOnSuccessListener { document ->
            Log.e("pathParser", "db.collection(\"coordintes\").document(\"$routeParentId\").collection(\"routes\").document(\"$routeName\")")
            Log.e("pathParser", "${document!!.id} => ${document["coords"]}")
            val docMap = document["coords"] as Map<String, *>
            for (i in 1..docMap.entries.size)
            {
                var x = (docMap.get("$i") as Map<String, *>).get("x").toString().toFloat()
                var y = (docMap.get("$i") as Map<String, *>).get("y").toString().toFloat()
                var z = (docMap.get("$i") as Map<String, *>).get("z").toString().toFloat()
                var id = document.id.replace("\\s".toRegex(), "")
                local_db.newPath(id,x,y,z)

            }
        }

        return routeName!!
    }

    override fun onPostExecute(routeParentId: String?) {
        super.onPostExecute(routeParentId)
        pathParselistner!!.pathParsed(routeParentId)
    }
}