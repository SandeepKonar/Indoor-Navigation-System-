package org.terna.arappjuly

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import java.io.File

internal val dbName = "offline_fb_coordsDB"
class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemClickListener,
    fb.fbStatusListner {
    var path : File? = null
    var mIntent : Intent? = null
    var venue_fb_id : String? = null
    var fb : fb? = null
    var arrayAdapter : ArrayAdapter<String>? = null
    var list : ListView? = null
    var venueNames : ArrayList<String>? = null
    var local_db : Fb_coordsDB? = null
    var createnewVenueName : String? = null
    var user : String? = null
    internal val dbName = "offline_fb_coordsDB"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        local_db = Fb_coordsDB(this, org.terna.arappjuly.dbName,null,1)
        //var dbName = Calendar.getInstance().getTime().toString().replace("\\s".toRegex(), "")
        user = getIntent().getStringExtra("current_user")
//        Toast.makeText(baseContext, user,
//            Toast.LENGTH_LONG).show()
         fb = fb(this)
        fb!!.listner=this
        fb!!.initFbSettings()
        //fb!!.getVenue("3NKg3zvYtxl8HNgAkkFv",false)
        //Toast.makeText(baseContext, user, Toast.LENGTH_LONG).show()
        var dbList = applicationContext.databaseList()
        list = findViewById<ListView>(R.id.list)
        venueNames = local_db!!.getVenuesNames()
        arrayAdapter = ArrayAdapter<String>(this,R.layout.list_cell, venueNames!!)
        list!!.adapter=arrayAdapter
        var createPathButton= findViewById<Button>(R.id.CreatePathButton)
        createPathButton.setOnClickListener(this)
        var scanqrButton = findViewById<Button>(R.id.scanqrButton)
        scanqrButton.setOnClickListener(this)
        list!!.setOnItemClickListener(this)
//        mIntent = Intent(this,CreatePath::class.java)
//        mIntent?.putExtra("dbName", "${dbName}")
//        startActivity(mIntent)
    }

    override fun onClick(v: View?) {
        if (v?.id==R.id.CreatePathButton)
        {
            //var dbName = Calendar.getInstance().getTime().toString().replace("\\s".toRegex(), "")
            inputPopUp()
            Toast.makeText(this,"next", Toast.LENGTH_SHORT).show()
//            mIntent = Intent(this,CreatePath::class.java)
//            //mIntent?.putExtra("dbName", "${dbName}")
//            startActivity(mIntent)


        }else
            if (v?.id==R.id.scanqrButton)
            {
                val scanner = IntentIntegrator(this)
                scanner.initiateScan()
                }


    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
       // Toast.makeText(this,"${view!!.findViewById<TextView>(R.id.textView).text}", Toast.LENGTH_LONG).show()
        var venue = local_db!!.getVenueByPosition(position)
        mIntent = Intent(this,RouteActivity::class.java)
        mIntent?.putExtra("venue_id", "${venue.venue_fb_id}")
        startActivity(mIntent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

            Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG)
                        .show()
            fb!!.getVenue(result.contents,true,arrayAdapter!!)
            venueNames = local_db?.getVenuesNames()//dont need this

            } else
        {
                super.onActivityResult(requestCode, resultCode, data)

        }
    }




    override fun gotVenue() {
        TODO("Not yet implemented")
    }

    override fun aftVenueAddToLocalDb(newVenue: VenueModel) {
        venueNames = local_db?.getVenuesNames()
        for (venueName in venueNames!!)
        {
            Log.e("mainActivity","$venueName")
        }
        //arrayAdapter!!.notifyDataSetChanged()
        recreate()
        fb!!.getRoutes(newVenue,this)

    }


    override fun gotRoutes() {
        TODO("Not yet implemented")
    }

    override fun aftRoutesAddToLocalDb(routeParentId:String) {

        mIntent = Intent(this,RouteActivity::class.java)
        mIntent?.putExtra("venue_id", routeParentId)
        startActivity(mIntent)
    }

    override fun gotPath() {
        TODO("Not yet implemented")
    }

    override fun aftPathAdd() {
        TODO("Not yet implemented")
    }

    fun inputPopUp()
    {
                var dialoug = AlertDialog.Builder(this)
                var edittext = EditText(this)
                edittext.inputType= InputType.TYPE_CLASS_TEXT
                dialoug.setView(edittext)
                dialoug.setPositiveButton("go", DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                    createnewVenueName=edittext.text.toString()
                    Toast.makeText(this,"${createnewVenueName}", Toast.LENGTH_SHORT).show()
                    val data = hashMapOf(
                        "name" to "$createnewVenueName",
                        "created_by" to "$user"
                    )
                    fb!!.addNewVenue(data,this)

//                    mIntent = Intent(this,RouteActivity::class.java)
//                    mIntent?.putExtra("venue_id", createnewVenueName)
//                    startActivity(mIntent)
                })
                dialoug.show()
    }

}
