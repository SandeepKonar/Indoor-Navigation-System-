package org.terna.arappjuly

class RouteModel(route_fb_id : String,route_name : String,route_parent_id:String,created_by : String) {
    var route_fb_id : String? = null
    var route_name : String? = null
    var route_parent_id : String? = null
    var created_by : String? = null

    init {
        this.route_fb_id=route_fb_id
        this.route_name=route_name
        this.route_parent_id=route_parent_id
        this.created_by=created_by
    }
}
//in FB_coordsDB keep tables venue, route and each_downloaded_route might be useful for offline access