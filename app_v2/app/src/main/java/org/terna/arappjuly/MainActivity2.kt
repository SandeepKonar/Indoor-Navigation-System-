package org.terna.arappjuly

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.zxing.integration.android.IntentIntegrator

//var path : File? = null


class MainActivity2 : AppCompatActivity(), fireBaseNew.addVenueListner,fireBaseNew.addRouteListner,
    AdapterView.OnItemClickListener, View.OnClickListener {

    var mIntent : Intent? = null
    var venue_fb_id : String? = null
    var fbNew : fireBaseNew? = null
    var arrayAdapter : ArrayAdapter<String>? = null
    var list : ListView? = null
    var venueNames : ArrayList<String>? = null
    var local_db : Fb_coordsDB? = null
    var createnewVenueName : String? = null
    var user : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        local_db = Fb_coordsDB(this, org.terna.arappjuly.dbName,null,1)
        user = getIntent().getStringExtra("current_user")
        fbNew = fireBaseNew(this)
        fbNew!!.addVenuelistner=this
        fbNew!!.addRoutelistner=this
        fbNew!!.initFbSettings()
        list = findViewById<ListView>(R.id.list)
        venueNames = local_db!!.getVenuesNames()
        arrayAdapter = ArrayAdapter<String>(this,R.layout.list_cell, venueNames!!)
        list!!.adapter=arrayAdapter
        var createPathButton= findViewById<Button>(R.id.CreatePathButton)
        createPathButton.setOnClickListener(this)
        var scanqrButton = findViewById<Button>(R.id.scanqrButton)
        scanqrButton.setOnClickListener(this)
        list!!.setOnItemClickListener(this)
    }

    override fun venueAddedToLocalDb(newVenue: VenueModel) {
        venueNames = local_db?.getVenuesNames()
        recreate()
        fbNew!!.getRoutes(newVenue)
    }
    override fun routeAddedToLocalDb(venueFbId: String?) {
        var mIntent = Intent(this,RouteActivity2::class.java)
        mIntent?.putExtra("venue_id", venueFbId)
        startActivity(mIntent)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var venue = local_db!!.getVenueByPosition(position)
        var mIntent = Intent(this,RouteActivity2::class.java)
        mIntent?.putExtra("venue_id", "${venue.venue_fb_id}")
        startActivity(mIntent)
    }

    override fun onClick(v: View?) {
        if (v?.id==R.id.scanqrButton)
        {
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

            Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()
            fbNew!!.getVenue(result.contents)
            venueNames = local_db?.getVenuesNames()//dont need this

        } else
        {
            super.onActivityResult(requestCode, resultCode, data)

        }
    }


}