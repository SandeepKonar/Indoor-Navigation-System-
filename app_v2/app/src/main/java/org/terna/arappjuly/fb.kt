package org.terna.arappjuly

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import org.terna.arappjuly.PathParser.PathParseStatus
import org.terna.arappjulyy.docObject
import java.io.Serializable


class fb(context: Context) : PathParseStatus{
    public interface fbStatusListner
    {
        fun  gotVenue()
        fun aftVenueAddToLocalDb(newVenue: VenueModel)
        fun gotRoutes()
        fun aftRoutesAddToLocalDb(routeParentId:String)
        fun gotPath()
        fun aftPathAdd()

    }

        var listner : fbStatusListner? = null

        var local_db = Fb_coordsDB(context, dbName,null,1)
        var db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        fun initFbSettings(){
            db.firestoreSettings = settings
        }


    fun getVenue(venue_fb_id:String,addNewVenue : Boolean,arrayAdapter: ArrayAdapter<String>){
        var venue : VenueModel? = null
        //var venue_doc = db.collection("venues")
        var venue_doc = db.collection("documentID")
            .document("${venue_fb_id}")
            .get()
            .addOnSuccessListener { document ->
                run {
                    Log.e("success", "${document.id} => ${document["created_by"]}")
                    var newVenue = VenueModel(venue_fb_id, document["name"] as String,document["created_by"] as String)
                    Log.e("success", "var newVenue = VenueModel(venue_fb_id, document[\"name\"] as String,document[\"created_by\"] as String)")
                    Log.e("success", "$venue_fb_id ${document["name"]} ,${document["created_by"]}")
                    if (addNewVenue)
                    {
                        addNewVenueToLocalDb(newVenue,arrayAdapter)
                    }
                    venue=newVenue
                }
            }
            .addOnFailureListener { exception ->
                Log.e("failed", "Error getting documents.", exception)
            }

    }

    fun addNewVenueToLocalDb(newVenue : VenueModel,arrayAdapter: ArrayAdapter<String>)
    {

        local_db.newVenue(newVenue.venue_fb_id!!,newVenue.venue_name!!,newVenue.created_by!!)
        listner!!.aftVenueAddToLocalDb(newVenue)
        arrayAdapter.notifyDataSetChanged()
    }

    fun getAllVenueInfo(Venue_fb_id:String){
       // initFbSettings()
        db.collection("venues")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.e("success", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("failed", "Error getting documents.", exception)
            }
    }

    fun getRoutes(Venue: VenueModel,context: Context)
    {   var routes = ArrayList<RouteModel>()

        var pathParser = PathParser()
        pathParser.listner=this
        //var venue_doc = db.collection("venues")
        var venue_doc = db.collection("coordinates")
            .document("${Venue.venue_fb_id}")
            .collection("routes")
            .get()
            .addOnSuccessListener { documents ->

                pathParser.execute(docObject(documents,Venue.venue_fb_id!!,context))
                for (document in documents) {
                    Log.e("fb", "${document.id} => ${document.data}")

                    var newRoute = RouteModel(document.id, document["name"] as String,Venue.venue_fb_id!!,document["created_by"] as String)
                    routes!!.add(newRoute)

                }
                addNewRouteToLocalDb(routes!!)


//                run {
//                    Log.e("success", "${document.id} => ${document["created_by"]}")
//                    var newVenue = VenueModel(venue_fb_id, document["name"] as String,document["created_by"] as String)
//                    if (addNewVenue)
//                    {
//                        addNewVenue(newVenue,arrayAdapter)
//                    }
//                    venue=newVenue
//                }
            }
            .addOnFailureListener { exception ->
                Log.e("failed", "Error getting documents.", exception)
            }
    }
    fun addNewRouteToLocalDb(newRoutes : ArrayList<RouteModel>)
    {
        for(newRoute in newRoutes)
        {
            local_db.newRoute(newRoute.route_fb_id!!,newRoute.route_name!!,newRoute.route_parent_id!!,newRoute.created_by!!)
        }


    }

    fun addNewVenue(
        data: HashMap<String, String>,
        context: Context
    )
    {
        db.collection("venues").add(data)
            .addOnSuccessListener { documentReference ->
                Log.e("fb", "DocumentSnapshot written with ID: ${documentReference.id}")
                local_db.newVenue(documentReference.id.toString(),data["name"]!!,data["created_by"]!!)

                var mIntent = Intent(context,RouteActivity::class.java)
                mIntent?.putExtra("venue_id", data["name"])
                context.startActivity(mIntent)

            }
    }

    fun addNewRoute(venueId: String, data: HashMap<String, Serializable?>)
    {

     var routeName = data["routeName"]
     var created_by =  data["created_by"]
     var coords = data["coords"]
     var routeDoc = db.collection("venues").document("$venueId").collection("routes").document()
        local_db.newRoute(routeDoc.id.toString(),routeName.toString(),venueId.toString(),created_by.toString())
     routeDoc.set(data).addOnSuccessListener { RouteActivity().recreate() }
    }




    override fun pathParsed(routeParentId: String?) {
        listner!!.aftRoutesAddToLocalDb(routeParentId!!)
    }


}