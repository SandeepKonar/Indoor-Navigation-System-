package org.terna.arappjuly

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import org.terna.arappjuly.PathParser.PathParseStatus

class fireBaseNew (context: Context) : PathParseStatus {

    public interface addVenueListner {
        fun venueAddedToLocalDb(newVenue: VenueModel)
    }
    public interface addRouteListner {
        fun routeAddedToLocalDb(venueFbId: String?)
    }

    var addVenuelistner: addVenueListner? = null
    var addRoutelistner: addRouteListner? = null
    var local_db = Fb_coordsDB(context, dbName, null, 1)
    var db = Firebase.firestore
    val settings = firestoreSettings {
        isPersistenceEnabled = true
    }

    fun initFbSettings() {
        db.firestoreSettings = settings
    }


    fun getVenue(venue_fb_id: String) {
        var venue_doc = db.collection("documentID")
            .document("${venue_fb_id}")
            .get()
            .addOnSuccessListener { document ->
                var newVenue = VenueModel(venue_fb_id.replace("\\s".toRegex(), ""), venue_fb_id as String, "none" as String)
                local_db.newVenue(
                    newVenue.venue_fb_id!!,
                    newVenue.venue_name!!,
                    newVenue.created_by!!
                )
                addVenuelistner!!.venueAddedToLocalDb(newVenue)
            }



    }

    fun getRoutes(newVenue : VenueModel) {
        var routes = ArrayList<RouteModel>()
        var venue_doc = db.collection("documentID")
            .document("${newVenue!!.venue_fb_id}")
            .collection("routes")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.e("fb", "${document.id} => ${document.data}")

                    var newRoute = RouteModel(document.id.replace("\\s".toRegex(), ""), document.id as String,newVenue.venue_fb_id!!,"none" as String)
                    local_db.newRoute(newRoute.route_fb_id!!,newRoute.route_name!!,newRoute.route_parent_id!!,newRoute.created_by!!)
                    routes!!.add(newRoute)

                }
                addRoutelistner!!.routeAddedToLocalDb(newVenue!!.venue_fb_id)
            }
    }
    override fun pathParsed(routeParentId: String?) {
        TODO("Not yet implemented")
    }
}