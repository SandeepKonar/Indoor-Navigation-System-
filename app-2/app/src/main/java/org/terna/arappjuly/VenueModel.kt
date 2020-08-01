package org.terna.arappjuly

class VenueModel (venue_fb_id : String,venue_name : String,created_by : String ){
    var venue_fb_id : String? = null
    var venue_name : String? = null
    var created_by : String? = null

    init {
        this.venue_fb_id=venue_fb_id
        this.venue_name=venue_name
        this.created_by=venue_name
    }
}