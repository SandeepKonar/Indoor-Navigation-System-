package org.terna.arappjuly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.sceneform.ux.ArFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_firebase_test.*

class FirebaseTestActivity : AppCompatActivity()/*, ARcoreDataProvider.arDataListener, View.OnClickListener*/ {
    var textView : TextView? = null
    var arCoreDataProvider : ARcoreDataProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_test)


//        var db = Firebase.firestore
//        var venue_doc = db.collection("venues")
//            .document("3NKg3zvYtxl8HNgAkkFv")
//            .collection("routes")
//            .document("eyAP4XuUCpMcjpcgm1Fi")
//            .get()
//            .addOnSuccessListener { document ->
////                for (document in documents) {
//                    var pathParser = PathParser()
//                    pathParser.parse(document,this)
//
////                }
//            }


//        var db = Fb_coordsDB(this, org.terna.arappjuly.dbName,null,1)
//        val cursor = db.test("SELECT * FROM venues " )
//        cursor!!.moveToFirst()
//        do {
//            var route_fb_name = cursor?.getString(cursor.getColumnIndex("created_by"))
//            Log.e("testing",route_fb_name)
//        }while (cursor.moveToNext())




//        ARARARARR
//        textView = findViewById(R.id.textView6)
//        var button = findViewById<Button>(R.id.createTestButton)
//        button.setOnClickListener(this)
//        var startButton = findViewById<FloatingActionButton>(R.id.startButtonTest)
//        var stopButton = findViewById<FloatingActionButton>(R.id.stopButtonTest)
//        startButton!!.setOnClickListener(this)
//        stopButton!!.setOnClickListener(this)
//
//    }
//
//    override fun onArData(x: Float, y: Float, z: Float) {
//           textView!!.text="x$x y$y z$z"
//    }
//
//    override fun onClick(v: View?) {
//        if (v?.id==R.id.startButton) {
//        var rootRelatievLayout = findViewById<RelativeLayout>(R.id.RootRelativeLayout)
//        var  arFragment = supportFragmentManager.findFragmentById(R.id.arTestFragment) as ArFragment
//        var arCoreDataProvider = ARcoreDataProvider(this,arFragment,rootRelatievLayout.width,rootRelatievLayout.height)
//        arCoreDataProvider.setArDataListner(this)
//
//        }
//
//        if (v?.id==R.id.startButtonTest) {
//            var rootRelatievLayout = findViewById<RelativeLayout>(R.id.RootRelativeLayout)
//            var  arFragment = supportFragmentManager.findFragmentById(R.id.arTestFragment) as ArFragment
//             arCoreDataProvider = ARcoreDataProvider(this,arFragment,rootRelatievLayout.width,rootRelatievLayout.height)
//            arCoreDataProvider!!.setArDataListner(this)
//            startButtonTest.visibility= View.GONE
//            stopButtonTest.visibility= View.VISIBLE
//        }
//        if (v?.id==R.id.stopButtonTest) {
//
//            //arCoreDataProvider!!.session!!.pause()
//
//            var mIntent = Intent(this,RouteActivity::class.java)
//
//            mIntent!!.putExtra("venue_id","3NKg3zvYtxl8HNgAkkFv")
//
//            startActivity(mIntent)
//            //finish()
//
//        }
        //ararararararr






        var coords = hashMapOf("1" to hashMapOf("x" to "1",
            "y" to "2",
            "z" to "3"))

        coords.put("2", hashMapOf("x" to "4",
            "y" to "5",
            "z" to "6"))
        coords.put("3", hashMapOf("x" to "9",
            "y" to "52",
            "z" to "64"))
        coords.put("4", hashMapOf("x" to "42",
            "y" to "51",
            "z" to "68"))
        coords.put("5", hashMapOf("x" to "45",
            "y" to "45",
            "z" to "26"))
        var data = hashMapOf("venueId" to "terna",
            "routeName" to "a to b",
            "created_by" to "jay",
            "coords" to coords
        )
        println(data)
    }

}