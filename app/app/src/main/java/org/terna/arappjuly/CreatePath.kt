package org.terna.arappjuly

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.sceneform.ux.ArFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_path.*
import java.io.Serializable


class CreatePath  : AppCompatActivity(), View.OnClickListener, ARcoreDataProvider.arDataListener {
    var venueId : String? = null
    var routeName : String? = null
//    var startButton : FloatingActionButton? = null
//    var stopButton : FloatingActionButton? = null
    var coords = HashMap<String,Serializable>()
    var count = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_path)
        venueId = getIntent().getStringExtra("venueId")
        routeName = getIntent().getStringExtra("routeName")
       var startButton = findViewById<FloatingActionButton>(R.id.startButton)
        var stopButton = findViewById<FloatingActionButton>(R.id.stopButton)
        startButton!!.setOnClickListener(this)
        stopButton!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id==R.id.startButton) {
            var rootRelatievLayout = findViewById<RelativeLayout>(R.id.createPathRootRelativeLayout)
            var  arFragment = supportFragmentManager.findFragmentById(R.id.createPathArfragment) as ArFragment
            var arCoreDataProvider = ARcoreDataProvider(this,arFragment,rootRelatievLayout.width,rootRelatievLayout.height)
            arCoreDataProvider.setArDataListner(this)
            startButton.visibility= View.GONE
            stopButton.visibility= View.VISIBLE
        }

        if (v?.id==R.id.stopButton) {
            var data = hashMapOf(
                "name" to routeName,
                "created_by" to FirebaseAuth.getInstance().currentUser?.email.toString(),
                "coords" to coords
            )
            var fb = fb(this)
            fb.addNewRoute(venueId!!, data = data)
            val intent = Intent(this, RouteActivity::class.java)
            intent!!.putExtra("venue_id",venueId)
            startActivity(intent)


        }
    }

    override fun onArData(x: Float, y: Float, z: Float) {
        coords.put("$count", hashMapOf("x" to "$x",
                                      "y" to "$y",
                                      "z" to "$z"))
        count+=1
    }


}