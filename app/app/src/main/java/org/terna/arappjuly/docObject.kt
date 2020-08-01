package org.terna.arappjulyy

import android.content.Context
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class docObject(documents : QuerySnapshot,routeParentId:String, context: Context) {
    var documents : QuerySnapshot? = null
    var context : Context? = null
    var routeParentId : String? = null

    init {
        this.documents=documents
        this.context=context
        this.routeParentId = routeParentId
    }
}