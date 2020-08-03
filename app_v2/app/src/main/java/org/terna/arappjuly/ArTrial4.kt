package org.terna.arappjuly

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlin.math.round

var arFragment4  : ArFragment? = null
var textView: TextView? = null
var session: Session? = null
var x: Float? = null
var y: Float? = null
var z: Float? = null
var coords4 : ArrayList<CoordsModel>? = null
var arrRenderable  : ModelRenderable? = null
var lastRenderable  : ViewRenderable? = null



var stop : Boolean? = null
var node  :Node? = null


class ArTrial4 : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_trial4)
        var arFragment4 = supportFragmentManager.findFragmentById(R.id.arFragment4) as ArFragment
        addCoords()
        var i=0
        var firstNodeSet = false
        ModelRenderable.builder().setSource(this, Uri.parse("model.sfb")).build()
            .thenAccept {
                    renderable -> arrRenderable = renderable
            }
        arFragment4.arSceneView.scene.addOnUpdateListener { frameTime ->
            if (firstNodeSet)
            {
                if(distance(node!!,arFragment4!!.arSceneView.scene.camera)<0.2&&i< coords4!!.size-1)
                  {
                      var coord = coords4!![i]
                      node!!.localPosition=Vector3(coord.x!!,coord.y!!,coord.z!!)
                      i++
                  }
            if(distance(node!!,arFragment4!!.arSceneView.scene.camera)<0.2 && i==coords4!!.size-1){

                //lastNode.setParent(anchorNode)
                var coord = coords4!![i]
                node!!.renderable= lastRenderable
                node!!.localPosition=Vector3(coord.x!!,coord.y!!,coord.z!!)
            }
                   }

        }









        arFragment4.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                return@setOnTapArPlaneListener
            }

            if(!firstNodeSet) {
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)

                node = TransformableNode(arFragment4.transformationSystem)
                node!!.renderable = arrRenderable
                node!!.setParent(anchorNode)
                arFragment4.arSceneView.scene.addChild(anchorNode)
                node!!.localPosition= Vector3(coords4!![i].x!!,coords4!![i].y!!,coords4!![i].z!!)
                i++
                firstNodeSet = true
            } else
            {
                arFragment4.arSceneView.getPlaneRenderer().setVisible(false)
                arFragment4.planeDiscoveryController.hide()
                return@setOnTapArPlaneListener
            }
            //node.select()
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}





fun addCoords()
    {
        coords4 = ArrayList()
        coords4!!.add(CoordsModel(0f, 0f, -0.6f))
        coords4!!.add(CoordsModel(0f, 0f, -1.2f))
        coords4!!.add(CoordsModel(0f, 0f, -1.8f))
        coords4!!.add(CoordsModel(0f, 0f, -2.4f))
        coords4!!.add(CoordsModel(0f, 0f, -3.0f))
        coords4!!.add(CoordsModel(0f, 0f, -3.6f))
        coords4!!.add(CoordsModel(0f, 0f, -4.2f))
        coords4!!.add(CoordsModel(0f, 0f, -4.2f))
    }
fun distance (arrNode : Node,camera: Camera): Float {
        var cam =camera.localPosition
        var dx: Float = arrNode!!.localPosition.x - cam.x
        var dy: Float = arrNode!!.localPosition.y - cam.y
        var dz: Float = arrNode!!.localPosition.z - cam.z

        val distanceMeters = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz).toDouble()).toFloat()
        return distanceMeters
    }
//class ArTrial4 : AppCompatActivity(), View.OnClickListener {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_ar_trial4)
//        arFragment4 = supportFragmentManager.findFragmentById(R.id.arFragment4) as ArFragment
//        addCoords()
//        var startNavigationButton = findViewById<FloatingActionButton>(R.id.startNavigationButton)
//        var stopNavigationButton = findViewById<FloatingActionButton>(R.id.stopNavigationButton)
//        startNavigationButton!!.setOnClickListener(this)
//        stopNavigationButton!!.setOnClickListener(this)
//        var ses = Session(this)
//        session = arFragment4?.arSceneView?.session
//        val pos = floatArrayOf(0f, 0f, -1f)
//        val rotation = floatArrayOf(0f, 0f, 0f, 1f)
//        val anchor = session?.createAnchor(Pose(pos, rotation))
//        var anchorNode = AnchorNode(anchor)
//        anchorNode.setParent(arFragment4?.arSceneView?.scene)
//        ModelRenderable.builder().setSource(this, Uri.parse("model.sfb")).build()
//            .thenAccept {
//                    renderable -> arrRenderable = renderable
//            }
//        ViewRenderable.builder().setView(this,R.layout.reached).build().thenAccept(){ renderble -> lastRenderable = renderble}
//        var arrNode = Node()
//        var lastNode = Node()
//        arrNode.setParent(anchorNode)
//        arrNode.localPosition = Vector3(0f, 0f, 0f)
//        arrNode.renderable= arrRenderable
//        var camera = arFragment4!!.arSceneView.scene.camera
//        arrNode.localPosition = Vector3(0f, 0f, 0f)
//        stop=false
//        var i=0
//        arFragment4?.arSceneView?.scene?.addOnUpdateListener { frameTime ->
//            // textView!!.setText("${camera.localPosition.x},${camera.localPosition.y},${camera.localPosition.z}")
//            Log.e("ARcoreDataProvider", "x ${camera.localPosition.x} , z ${camera.localPosition.z}")
//            if(distance(arrNode,arFragment4!!.arSceneView.scene.camera)<0.2&&i< coords4!!.size-1)
//            {
//                var coord = coords4!![i]
//                arrNode.localPosition=Vector3(coord.x!!,coord.y!!,coord.z!!)
//                i++
//            }
//            if(distance(arrNode,arFragment4!!.arSceneView.scene.camera)<0.2 && i==coords4!!.size-1){
//
//                //lastNode.setParent(anchorNode)
//                var coord = coords4!![i]
//                arrNode.renderable= lastRenderable
//                arrNode.localPosition=Vector3(coord.x!!,coord.y!!,coord.z!!)
//            }
//
////                    round(camera?.localPosition!!.x * 100.0f) / 100f,
////                    round(camera!!.localPosition.y * 100.0f) / 100f,
////                    round(camera!!.localPosition.z * 100.0f) / 100f
//
//
//        }
//
//
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    fun startAr(arFragment: ArFragment) {
//        var ses = Session(this)
//        session = arFragment4?.arSceneView?.session
//        val pos = floatArrayOf(0f, 0f, -1f)
//        val rotation = floatArrayOf(0f, 0f, 0f, 1f)
//        val anchor = session?.createAnchor(Pose(pos, rotation))
//        var anchorNode = AnchorNode(anchor)
//        anchorNode.setParent(arFragment4?.arSceneView?.scene)
//        ModelRenderable.builder().setSource(this, Uri.parse("model.sfb")).build()
//            .thenAccept {
//                    renderable -> arrRenderable = renderable
//            }
//        ViewRenderable.builder().setView(this,R.layout.reached).build().thenAccept(){ renderble -> lastRenderable = renderble}
//        var arrNode = Node()
//        var lastNode = Node()
//        arrNode.setParent(anchorNode)
//        arrNode.localPosition = Vector3(0f, 0f, 0f)
//        arrNode.renderable= arrRenderable
//        var camera = arFragment4!!.arSceneView.scene.camera
//        arrNode.localPosition = Vector3(0f, 0f, 0f)
//        stop=false
//        var i=0
//        arFragment4?.arSceneView?.scene?.addOnUpdateListener { frameTime ->
//            // textView!!.setText("${camera.localPosition.x},${camera.localPosition.y},${camera.localPosition.z}")
//            Log.e("ARcoreDataProvider", "x ${camera.localPosition.x} , z ${camera.localPosition.z}")
//                      if(distance(arrNode,arFragment4!!.arSceneView.scene.camera)<0.2&&i< coords4!!.size-1)
//                      {
//                            var coord = coords4!![i]
//                            arrNode.localPosition=Vector3(coord.x!!,coord.y!!,coord.z!!)
//                            i++
//                      }
//            if(distance(arrNode,arFragment4!!.arSceneView.scene.camera)<0.2 && i==coords4!!.size-1){
//
//                //lastNode.setParent(anchorNode)
//                var coord = coords4!![i]
//                arrNode.renderable= lastRenderable
//                arrNode.localPosition=Vector3(coord.x!!,coord.y!!,coord.z!!)
//            }
//
////                    round(camera?.localPosition!!.x * 100.0f) / 100f,
////                    round(camera!!.localPosition.y * 100.0f) / 100f,
////                    round(camera!!.localPosition.z * 100.0f) / 100f
//
//
//            }
//        }
//
//    fun addCoords()
//    {
//        coords4 = ArrayList()
//        coords4!!.add(CoordsModel(0f, 0f, -0.6f))
//        coords4!!.add(CoordsModel(0f, 0f, -1.2f))
//        coords4!!.add(CoordsModel(0f, 0f, -1.8f))
//        coords4!!.add(CoordsModel(0f, 0f, -2.4f))
//        coords4!!.add(CoordsModel(0f, 0f, -3.0f))
//        coords4!!.add(CoordsModel(0f, 0f, -3.6f))
//        coords4!!.add(CoordsModel(0f, 0f, -4.2f))
//        coords4!!.add(CoordsModel(0f, 0f, -4.2f))
//    }
//
//    fun distance (arrNode : Node,camera: Camera): Float {
//        var cam =camera.localPosition
//        var dx: Float = arrNode!!.localPosition.x - cam.x
//        var dy: Float = arrNode!!.localPosition.y - cam.y
//        var dz: Float = arrNode!!.localPosition.z - cam.z
//
//        val distanceMeters = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz).toDouble()).toFloat()
//        return distanceMeters
//    }
//
//    override fun onClick(v: View?) {
//        if(v?.id==R.id.startNavigationButton)
//        {
//            startAr(arFragment4!!)
//            //startButtonPressed=true
//        }
//        if(v?.id==R.id.stopNavigationButton)
//        {
//            var intent = Intent(this, RouteActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//
//}


