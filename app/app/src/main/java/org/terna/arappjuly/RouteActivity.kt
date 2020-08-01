package org.terna.arappjuly

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.qr_layout.view.*
import net.glxn.qrgen.android.QRCode
import java.io.File


class RouteActivity : AppCompatActivity(), fb.fbStatusListner, AdapterView.OnItemClickListener,
    View.OnClickListener {
    var path : File? = null
    var mIntent : Intent? = null
    var venue_fb_id : String? = null
    var fb : fb? = null
    var arrayAdapter : ArrayAdapter<String>? = null
    var list : ListView? = null
    var routeNames : ArrayList<String>? = null
    var local_db : Fb_coordsDB? = null
    var route_parent_id : String? =null
    var pathParser : PathParser? = null
    var createnewRouteName : String? = null
    var venue : VenueModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        route_parent_id = getIntent().getStringExtra("venue_id")
        Log.e("routeActivity","intent $route_parent_id")
        local_db = Fb_coordsDB(this, org.terna.arappjuly.dbName,null,1)
        val user = FirebaseAuth.getInstance().currentUser!!.email.toString()
        venue = local_db!!.getVenue(route_parent_id!!)
        fb = fb(this)
        //fb.getRoutes
        fb!!.listner=this
        fb!!.initFbSettings()
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
        if(venue!!.created_by==user)
        {
            qrButton.visibility=View.VISIBLE
        }
        list!!.setOnItemClickListener(this)
        refeshButton.setOnClickListener(this)
        createPathButton.setOnClickListener(this)
        qrButton.setOnClickListener(this)

    }





    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.e("RouteActivity","position $position")
        var route= local_db!!.getRouteByPosition(position)
        var coords= local_db!!.getRouteCoords(route.route_fb_id!!)
        for (arr in coords)
        {
            Log.e("routeActivty" , "coord: x=${arr[0]} y=${arr[1]} z=${arr[2]}")
        }

        mIntent = Intent(this,RouteActivity::class.java)
        mIntent?.putExtra("route_id", "${route.route_fb_id}")
        mIntent?.putExtra("venue_id", "${route.route_parent_id}")
        startActivity(mIntent)
    }
    override fun gotVenue() {
        TODO("Not yet implemented")
    }

    override fun aftVenueAddToLocalDb(newVenue: VenueModel) {
        TODO("Not yet implemented")
    }

    override fun gotRoutes() {
        TODO("Not yet implemented")
    }

    override fun aftRoutesAddToLocalDb(routeParentId: String) {
        recreate()
    }

    override fun gotPath() {
        TODO("Not yet implemented")
    }

    override fun aftPathAdd() {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        if (v?.id==R.id.refreshButton) {

            fb!!.getRoutes(venue!!, this)
        }

        if (v?.id==R.id.CreatePathButton) {
            var dialoug = AlertDialog.Builder(this)
            var edittext = EditText(this)
            edittext.inputType= InputType.TYPE_CLASS_TEXT
            dialoug.setView(edittext)
            dialoug.setPositiveButton("go", DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                createnewRouteName=edittext.text.toString()
                Toast.makeText(this,"${createnewRouteName}", Toast.LENGTH_SHORT).show()

                mIntent = Intent(this,CreatePath::class.java)
                mIntent!!.putExtra("venueId",route_parent_id)
                mIntent?.putExtra("routeName", createnewRouteName)
                startActivity(mIntent)
            })
            dialoug.show()
        }
        if (v?.id==R.id.qrButton) {
            var dialoug = AlertDialog.Builder(this)
            val myBitmap: Bitmap = QRCode.from("$venue_fb_id").bitmap()
            var imageView = layoutInflater.inflate(R.layout.qr_layout,null)
            imageView.qrImage.setImageBitmap(myBitmap)
            dialoug.setView(imageView)
            dialoug.setPositiveButton("done", DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            })
            dialoug.show()

        }
    }




}