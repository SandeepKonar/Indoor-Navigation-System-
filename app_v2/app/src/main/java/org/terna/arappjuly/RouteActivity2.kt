package org.terna.arappjuly

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File



class RouteActivity2 : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemClickListener,
    PathParserNew.PathParseStatus {
    var path : File? = null
    var mIntent : Intent? = null
    var venue_fb_id : String? = null
    var fbNew : fireBaseNew? = null
    var arrayAdapter : ArrayAdapter<String>? = null
    var list : ListView? = null
    var routeNames : ArrayList<String>? = null
    var local_db : Fb_coordsDB? = null
    var route_parent_id : String? =null
    //var pathParser : PathParser? = null
    var createnewRouteName : String? = null
    var venue : VenueModel? = null
    var pathParser = PathParserNew()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        route_parent_id = getIntent().getStringExtra("venue_id")
        Log.e("routeActivity","intent $route_parent_id")
        local_db = Fb_coordsDB(this, org.terna.arappjuly.dbName,null,1)
        //val user = FirebaseAuth.getInstance().currentUser!!.email.toString()
        venue = local_db!!.getVenue(route_parent_id!!)
        fbNew = fireBaseNew(this)
        pathParser!!.pathParselistner=this
        //fb.getRoutes
        //fbNew!!.listner=this
        fbNew!!.initFbSettings()
        list = findViewById<ListView>(R.id.list)
        var refeshButton =findViewById<ImageButton>(R.id.refreshButton)
        routeNames = local_db!!.getRouteNames(route_parent_id!!)
        if (routeNames!!.isEmpty())
        {
            Toast.makeText(this,"no routes", Toast.LENGTH_SHORT).show()
        }

        arrayAdapter = ArrayAdapter<String>(this,R.layout.list_cell, routeNames!!)
        list!!.adapter=arrayAdapter
        var createPathButton= findViewById<Button>(R.id.CreatePathButton)
        var qrButton = findViewById<ImageButton>(R.id.qrButton)
        list!!.setOnItemClickListener(this)
        refeshButton.setOnClickListener(this)
        createPathButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var route= local_db!!.getRouteByPosition(position)
        Log.e("itemClick","id ${route.route_fb_id},name${route.route_name}")
        Log.e("itemClick","${local_db!!.checkTable(route!!.route_fb_id!!)}")
        if (local_db!!.checkTable(route!!.route_fb_id!!))
        {

            var mIntent = Intent(this,PathActivity::class.java)
            mIntent?.putExtra("routeName", "${route!!.route_name}")
            startActivity(mIntent)
        }else
        {
            pathParser.execute(pathReq(route,this))
        }

    }

    override fun pathParsed(routeName: String?) {
        Log.e("pathParser","parsed")
        //routeNames = local_db!!.getRouteNames(route_parent_id!!)

       // var venue = local_db!!.getVenueByPosition(position)
        var mIntent = Intent(this,PathActivity::class.java)
        mIntent?.putExtra("routeName", "${routeName}")
        startActivity(mIntent)
    }
}