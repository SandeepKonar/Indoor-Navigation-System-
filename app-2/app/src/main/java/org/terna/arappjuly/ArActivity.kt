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
import java.io.File
import java.io.FileWriter
import kotlin.math.abs
//import

class ArActivity : AppCompatActivity(), View.OnClickListener {
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
        //var dbCount = applicationContext.databaseList().size
        val routeId = getIntent().getStringExtra("routeId")
       // val dbName = (dbCount).toString()
        local_db = Fb_coordsDB(this, org.terna.arappjuly.dbName,null,1)
        var startButton = findViewById<FloatingActionButton>(R.id.startNavigationButton)
        var stopButton = findViewById<FloatingActionButton>(R.id.stopNavigationButton)
        startButton!!.setOnClickListener(this)
        stopButton!!.setOnClickListener(this)
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment?
        var arSceneView = arFragment!!.arSceneView

        ModelRenderable.builder().setSource(this, Uri.parse("model.sfb")).build().thenAccept { renderable -> arrowRenderable = renderable}

        setupSession(this)
        var frame = arFragment!!.arSceneView.arFrame
        var camera = arFragment!!.arSceneView.arFrame?.camera
        var count = local_db!!.getCount(routeId)
        var i =1
        var arCoreCamera = arFragment!!.arSceneView.scene.camera
        var previousPosition = arCoreCamera.localPosition
        var currentLocation = Vector3(0f,0f,0f)

        arFragment!!.arSceneView.scene.addOnUpdateListener {
            frame = arFragment!!.arSceneView.arFrame
            camera = frame?.camera
            currentLocation = arFragment!!.arSceneView.scene.camera.localPosition
            if (camera?.trackingState != TrackingState.TRACKING)
            {
                return@addOnUpdateListener
            }
            if(ANCHOR_SET==false)
            {
                anchorNode = createAnchorNode(frame = frame!!)
                arrowNode = createArrowNode(anchorNode = anchorNode!! ,frame = frame!!)
            }

            locationDelta= Vector3.subtract(currentLocation,previousPosition)
            deltaX= abs(locationDelta!!.x)
            deltaZ= abs(locationDelta!!.z)
            if (i<=count&&(deltaX!!>=0.0027f||deltaZ!!>=0.0027f))
            {

//                var xyz =db.getCoords(i)
//                arrowNode!!.localPosition = Vector3(xyz[0]!!, xyz[1]!!, xyz[2]!!)
                i++
            }
            previousPosition=currentLocation
            
        }
    }




    fun createAnchorNode (frame: Frame) : AnchorNode?
    {
        val pos = floatArrayOf(0f, 0f, -1f)
        val rotation = floatArrayOf(0f, 0f, 0f, 1f)
        val cameraPose = frame.camera.pose
        cameraPose.rotateVector(floatArrayOf(0f, 0f, 0f, 1f))
//   anchor = arFragment?.arSceneView?.session!!.createAnchor(frame.camera.pose)
        anchor = arFragment?.arSceneView?.session!!.createAnchor(Pose(pos, rotation))
        anchorNode = AnchorNode(anchor)
        anchorNode?.setParent(arFragment!!.arSceneView.scene)
        ANCHOR_SET=true
        log("anchor created")
        return anchorNode!!
    }
    fun createArrowNode(anchorNode: AnchorNode, frame: Frame) : Node
    {
        var arrowNode = Node()
        arrowNode.setParent(anchorNode)
        arrowNode.localPosition = Vector3.add(anchorNode.localPosition, Vector3(0f,-0.8f,0f))
        arrowNode.renderable = arrowRenderable
        log("arrowNode created at ${arrowNode.localPosition}")
        return arrowNode
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
    fun log(string: String)
    {
        Log.e("debug",string)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}