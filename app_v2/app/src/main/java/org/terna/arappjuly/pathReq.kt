package org.terna.arappjuly

import android.content.Context

class pathReq(route: RouteModel, context: Context) {

    var context : Context? = null
    var route : RouteModel? = null

    init {

        this.context=context
        this.route = route
    }
}