package org.terna.arappjuly


import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.*
//import com.google.ar.core.R
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_create_path.*
import java.io.File
import java.io.FileWriter
import kotlin.math.abs

class ARactivityTrial : AppCompatActivity(), View.OnClickListener {
    var arFragment : ArFragment? = null
    var arrowRenderable : ModelRenderable? =null
    var ANCHOR_SET= false
    var session : Session? =null
    var path : File? = null
    var file : File? = null
    //var coordinates = coordinates()
    var stream : FileWriter? = null
    var file2 : File? = null
    var anchor : Anchor? = null
    var anchorNode : AnchorNode? =null
    var arrowNode : Node? =null
    var locationDelta : Vector3? =null
    var deltaX : Float? = null
    var deltaZ : Float? = null
    var local_db : Fb_coordsDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        val routeId = getIntent().getStringExtra("routeId")
        ModelRenderable.builder().setSource(this, Uri.parse("model.sfb")).build().thenAccept { renderable -> arrowRenderable = renderable}
        local_db = Fb_coordsDB(this, org.terna.arappjuly.dbName,null,1)
        var startButton = findViewById<FloatingActionButton>(R.id.startNavigationButton)
        var stopButton = findViewById<FloatingActionButton>(R.id.stopNavigationButton)
        startButton!!.setOnClickListener(this)
        stopButton!!.setOnClickListener(this)



    }

    override fun onClick(v: View?) {
        startButton.visibility= View.GONE
        stopButton.visibility= View.VISIBLE
        startAR()
    }

    fun startAR()
    {
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment?
        var arSceneView = arFragment!!.arSceneView
        setupSession(this)
    }


    fun setupSession(context: Context)
    {
        arFragment!!.getPlaneDiscoveryController().hide();
        arFragment!!.getPlaneDiscoveryController().setInstructionView(null);
        session = Session(context)
        var config = Config(session)
        config.setPlaneFindingMode(Config.PlaneFindingMode.DISABLED)
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)
        config.focusMode= Config.FocusMode.AUTO
        session?.configure(config)
        arFragment!!.arSceneView?.setupSession(session)
    }
}
