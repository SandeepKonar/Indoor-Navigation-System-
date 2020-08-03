package org.terna.arappjuly

//import com.google.ar.core.Anchor
//import com.google.ar.core.Session
//import android.R.attr.fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import java.io.File
import java.io.FileWriter


var arFragment : ArFragment? = null
var arrowRenderable : ModelRenderable? =null
var ANCHOR_SET= false
//var session : Session? =null
var path : File? = null
var file : File? = null
//var coordinates = coordinates()
var stream : FileWriter? = null
var file2 : File? = null
//var anchor : Anchor? = null
var anchorNode : AnchorNode? =null
var arrowNode : Node? =null
var locationDelta : Vector3? =null
var deltaX : Float? = null
var deltaZ : Float? = null
var local_db : Fb_coordsDB? = null
var coords : ArrayList<CoordsModel>? = null
var routeId : String? = null
var trial = arrayListOf<Array<Float>>()
var arRelLayout : RelativeLayout? = null
var isHit : Boolean? = null
var objectsPlaced: Boolean? = null
var startingAnchor: AnchorNode? = null
var currentNodes : ArrayList<Node> = ArrayList()
var totalNodes : Int? = null
var arrowNode1 : Node? = null
var arrowNode2 : Node? = null
var arrowNode3 : Node? = null
var tempNode : Node? = null
var startButtonPressed : Boolean? =null
private var coordinates: ArrayList<CoordsModel>?  = null
var nodes = arrayOf("arrownode1","arrownode2","arrownode3")


class ArTrial3 : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        routeId = getIntent().getStringExtra("routeId")
        local_db = Fb_coordsDB(this, org.terna.arappjuly.dbName,null,1)
        coordinates = local_db!!.getRouteCoords(routeId!!)
        for (coordinate in coordinates!!)
        {

                log("x ${coordinate.x}")
                log("y ${coordinate.y}")
                log("z ${coordinate.z}")

        }
        var lastRenderable : ViewRenderable? = null
        //private ArFragment fragment;
        var startNavigationButton = findViewById<FloatingActionButton>(R.id.startNavigationButton)
        var stopNavigationButton = findViewById<FloatingActionButton>(R.id.stopNavigationButton)
        startNavigationButton!!.setOnClickListener(this)
        stopNavigationButton!!.setOnClickListener(this)
        arRelLayout = findViewById<RelativeLayout>(R.id.arRelLayout)
        currentNodes
        startButtonPressed=false
        totalNodes = 0
        addCoords()
//        var text = findViewById<TextView>(R.id.currentNodes0)
//        var camtext = findViewById<TextView>(R.id.cameraPos)
//        var diff = findViewById<TextView>(R.id.diff)
        isHit = false
        objectsPlaced = false
        //Toast.makeText(this, "plane detected", Toast.LENGTH_LONG).show()
        ModelRenderable.builder().setSource(this, Uri.parse("model.sfb")).build()
            .thenAccept { renderable -> arrowRenderable = renderable }
        ViewRenderable.builder().setView(this,R.layout.reached).build().thenAccept(){ renderble -> lastRenderable = renderble}

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        arFragment!!.arSceneView.scene.addOnUpdateListener { frameTime ->
            arFragment!!.onUpdate(frameTime);
            //onFrameUpdate()
            if(!isHit!!)
            {

                isHit=checkHit()
            }else
            {   var cam = arFragment!!.getArSceneView().scene.camera.localPosition
                var dx: Float = tempNode!!.localPosition.x - cam.x
                var dy: Float = tempNode!!.localPosition.y - cam.y
                var dz: Float = tempNode!!.localPosition.z - cam.z
//                text.text="${tempNode!!.localPosition}"
//                camtext.text="cam ${cam}"
                var distanceMeters = Math.sqrt(dx * dx + dy * dy + (dz * dz).toDouble()).toFloat()
                //var distanceMeters= distance(tempNode!!,cam)
                if(distanceMeters<0.34 && (totalNodes!!-1)< ((coords!!.size)-1))
                {
                    totalNodes= totalNodes!!+1
                    tempNode!!.localPosition=Vector3(coordinates!![totalNodes!!].x!!,coordinates!![totalNodes!!].y!!,coordinates!![totalNodes!!].z!!)
                    var angle = Vector3.angleBetweenVectors(Vector3(coordinates!![totalNodes!!].x!!,coordinates!![totalNodes!!].y!!,coordinates!![totalNodes!!].z!!),Vector3(coordinates!![totalNodes!!+1].x!!,coordinates!![totalNodes!!+1].y!!,coordinates!![totalNodes!!+1].z!!))

                    tempNode!!.worldRotation= Quaternion(Vector3(1f,0f,0f),angle)
                    Toast.makeText(baseContext, "distanceMeters<0.2", Toast.LENGTH_LONG).show()
                }else
//                    distanceMeters= distance(tempNode!!,cam)
                    distanceMeters = Math.sqrt(dx * dx + dy * dy + (dz * dz).toDouble()).toFloat()
              //  diff.text="dist = $distanceMeters"
                if (distanceMeters<0.34 && (totalNodes!! - 2) == (coords!!.size-1))
                {
                    totalNodes= totalNodes!!+1
                    tempNode!!.localPosition=Vector3(coordinates!![totalNodes!!].x!!,coordinates!![totalNodes!!].y!!,coordinates!![totalNodes!!].z!!)

                    tempNode!!.renderable= lastRenderable
                }

            }
//         if(!objectsPlaced!!){
//              placeObjects()
//         }


        }




    }


    fun distance (arrNode : Node,camera: Camera): Float {
        var cam =camera.localPosition
        var dx: Float = arrNode!!.localPosition.x - cam.x
        var dy: Float = arrNode!!.localPosition.y - cam.y
        var dz: Float = arrNode!!.localPosition.z - cam.z

        val distanceMeters = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz).toDouble()).toFloat()
        return distanceMeters
    }











//    fun onFrameUpdate() {
//        if(arrowNode3!=null)
//        {
//            var textView = findViewById<TextView>(R.id.arrow1)
//            textView.setText("a1 ${arrowNode1!!.localPosition}")
//            var textView2 = findViewById<TextView>(R.id.arrow2)
//            textView2.setText("a2 ${arrowNode2!!.localPosition}")
//            var textView3 = findViewById<TextView>(R.id.arrow3)
//            textView3.setText("a3${arrowNode3!!.localPosition}")
//            var textView4 = findViewById<TextView>(R.id.currentNode0)
//            textView4.setText("curr0${currentNodes[0]!!.localPosition}")
//            currentNodes[0].localPosition
//
//        }
//        if (!isHit!!) {
//            isHit = checkHit()
//        } else
//            if (!objectsPlaced!!) {
//                log("placing Objects")
//                placeObjectsFixed() // place 5 objects at start then recycle //2 at home
//            }
//        log("total nodes ${totalNodes}")
//        if (objectsPlaced!!) {
//            recycleNodesFixed()
//        }
////        Toast.makeText(baseContext, "$totalNodes", Toast.LENGTH_SHORT).show()
////        log("totalNodes $totalNodes")
//        /*
//         hit obtained and objectsplaced
//         now recycle
//         now
//         */
//    }
//
//
//    fun placeObjectsFixed(): Boolean {
//        var noOfStartingObjects = 3
//        arrowNode1 = Node()
//        arrowNode1!!.setParent(startingAnchor)
//        arrowNode1!!.localPosition = Vector3(coords!![totalNodes!!].x!!,coords!![totalNodes!!].y!!,coords!![totalNodes!!].z!!)
//        arrowNode1!!.renderable = arrowRenderable
//        arrowNode1!!.name="arrowNode1"
//        log("arrowNode1 created at ${arrowNode1!!.localPosition}")
//        totalNodes= totalNodes!!+1
//
//        arrowNode2 = Node()
//        arrowNode2!!.setParent(startingAnchor)
//        arrowNode2!!.localPosition = Vector3(coords!![totalNodes!!].x!!,coords!![totalNodes!!].y!!,coords!![totalNodes!!].z!!)
//        arrowNode2!!.renderable = arrowRenderable
//        arrowNode2!!.name="arrowNode2"
//        log("arrowNode2 created at ${arrowNode2!!.localPosition}")
//        totalNodes= totalNodes!!+1
//
//        arrowNode3 = Node()
//        arrowNode3!!.setParent(startingAnchor)
//        arrowNode3!!.localPosition = Vector3(coords!![totalNodes!!].x!!,coords!![totalNodes!!].y!!,coords!![totalNodes!!].z!!)
//        arrowNode3!!.renderable = arrowRenderable
//        arrowNode3!!.name="arrowNode3"
//        log("arrowNode3 created at ${arrowNode3!!.localPosition}")
//        totalNodes= totalNodes!!+1
//
//        currentNodes.add(arrowNode1!!)
//        currentNodes.add(arrowNode1!!)
//        currentNodes.add(arrowNode1!!)
//
//
//        objectsPlaced = true
//        log("objects placed")
//        return objectsPlaced!!
//
//      }
//
//    fun recycleNodesFixed() {
//
//            var currentNode = currentNodes[0]
//        Toast.makeText(this, "currentNode ${currentNode.name}", Toast.LENGTH_SHORT).show()
//
////            var frame= arFragment!!.getArSceneView().getArFrame()
////            var camera = frame!!.getCamera()  check this again and work with worldposition instead of local position
//            var scene = arFragment!!.getArSceneView().scene
//            var camera = scene!!.getCamera()
//            var nodeLocation = currentNode!!.localPosition
//            var userLocation = camera.localPosition
//        var textView = findViewById<TextView>(R.id.cameraPos)
//        textView.text = "cameraPos= z${userLocation.z} x${userLocation.x}"
//        var textView2 = findViewById<TextView>(R.id.currentNodes0)
//        textView2.text = "currentNode[0]Pos= z${nodeLocation.z} x${nodeLocation.x}"
//        var textView3 = findViewById<TextView>(R.id.diff)
//
//            //node location-cam location . if resx neg & resy neg remove add new
//           // var result = Vector3.subtract(nodeLocation, userLocation)
//        //textView3.text = "diff= z${result.z} x${result.x}"
//        var dx: Float = nodeLocation.x - userLocation.x
//        var dy: Float = nodeLocation.y - userLocation.y
//        var dz: Float = nodeLocation.z - userLocation.z
//
//        ///Compute the straight-line distance.
//
//        ///Compute the straight-line distance.
//        val distanceMeters = Math.sqrt(dx * dx + dy * dy + (dz * dz).toDouble()).toFloat()
//        textView3.text = "Distance z${currentNode.localPosition.z} = $distanceMeters"
//        if(distanceMeters<0.2)
//        {
//                if (currentNodes[0].name=="arrowNode2")
//                {
//                    arrowNode2!!.localPosition=Vector3(coords!![totalNodes!!].x!!,coords!![totalNodes!!].y!!,coords!![totalNodes!!].z!!)
//                    log("removed  arrowNode2")
//                    currentNodes.removeAt(0)
//                    currentNodes.add(arrowNode2!!)
//                    totalNodes=totalNodes!!+1
//                }
//            if (currentNodes[0].name=="arrowNode1")
//            {
//                arrowNode1!!.localPosition=Vector3(coords!![totalNodes!!].x!!,coords!![totalNodes!!].y!!,coords!![totalNodes!!].z!!)
//                log("removed  arrowNode2")
//                currentNodes.removeAt(0)
//                currentNodes.add(arrowNode1!!)
//                totalNodes=totalNodes!!+1
//            }
//            if (currentNodes[0].name=="arrowNode3")
//            {
//                arrowNode3!!.localPosition=Vector3(coords!![totalNodes!!].x!!,coords!![totalNodes!!].y!!,coords!![totalNodes!!].z!!)
//                log("removed  arrowNode3")
//                currentNodes.removeAt(0)
//                currentNodes.add(arrowNode3!!)
//                totalNodes=totalNodes!!+1
//            }
//
////                currentNodes[0].localPosition=Vector3(coords!![totalNodes!!].x!!,coords!![totalNodes!!].y!!,coords!![totalNodes!!].z!!)
////                log("removed at z ${currentNodes[0]}")
////                currentNodes.removeAt(0)
////                currentNodes.add(currentNode)
////                totalNodes=totalNodes!!+1
//
//                log("node recycled")
//        }
////
////            if ((result.x) < 0 && -(result.z) < 0 && buttonPressed!!) {
////                currentNodes[0].localPosition=Vector3(coords!![totalNodes!!].x!!,coords!![totalNodes!!].y!!,coords!![totalNodes!!].z!!)
////                log("removed at z ${currentNodes[0]}")
////                currentNodes.removeAt(0)
////                currentNodes.add(currentNode)
////                totalNodes=totalNodes!!+1
////
////                log("node recycled")
////            }
//        }
//
//
//    fun addNodeFixed() {
//        currentNodes.add(Node())
//        currentNodes[currentNodes.size!! - 1].setParent(startingAnchor)
//        currentNodes[currentNodes.size!! - 1].localPosition = Vector3(
//            coords!![totalNodes!! - 1].x!!,
//            coords!![totalNodes!! - 1].x!!,
//            coords!![totalNodes!! - 1].x!!
//        )
//        currentNodes[currentNodes.size!! - 1].renderable = arrowRenderable
//        log("arrowNode${currentNodes.size!! - 1} created at ${currentNodes[currentNodes.size!! - 1].localPosition}")
//        totalNodes = totalNodes!! + 1
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
//        fun placeObjects(): Boolean {
//            var noOfStartingObjects = 3
//
//            if (coords!!.size < noOfStartingObjects) {
//                for (i in 0..coords!!.size - 1) {
//                    currentNodes.add(Node())
//                    currentNodes[i].setParent(startingAnchor)
//                    currentNodes[i].localPosition =
//                        Vector3(coords!![i].x!!, coords!![i].x!!, coords!![i].x!!)
//                    currentNodes[i].renderable = arrowRenderable
//                    log("arrowNode$i created at ${currentNodes[i].localPosition}")
//                    totalNodes = totalNodes!! + 1
//                }
//            }
//            for (i in 0..noOfStartingObjects - 1) {
//
//                currentNodes.add(Node())
//                currentNodes[i].setParent(startingAnchor)
//                currentNodes[i].localPosition =
//                    Vector3(coords!![i].x!!, coords!![i].x!!, coords!![i].x!!)
//                currentNodes[i].renderable = arrowRenderable
//                log("arrowNode$i created at ${currentNodes[i].localPosition}")
//                totalNodes = totalNodes!! + 1
////                var arrowNode1 = Node()
////                arrowNode1.setParent(startingAnchor)
////                arrowNode1.localPosition = Vector3(0f,0f,-0.5f)
////                arrowNode1.renderable = arrowRenderable
//                //log("arrowNode1 created at ${arrowNode1.localPosition}")
//            }
//            objectsPlaced = true
//            log("objects placed")
//            return objectsPlaced!!
//        }

    fun checkHit(): Boolean {
        var hitCheck = false
        var frame = arFragment!!.getArSceneView().getArFrame();
        var tracking = frame!!.getCamera().getTrackingState()

        var hits = frame.hitTest(
            ((arRelLayout!!.width) / 2).toFloat(),
            ((arRelLayout!!.height) / 2).toFloat()
        )
        for (hit in hits)
        {
            if (hit.trackable.trackingState == TrackingState.TRACKING)
            {
                hitCheck = true
                arFragment!!.planeDiscoveryController.hide()
                var anchor = hit.createAnchor() //ask user to point the camera staright down
                startingAnchor = AnchorNode(anchor)
                //startingAnchor!!.renderable = arrowRenderable
                startingAnchor!!.setParent(arFragment!!.getArSceneView().scene)
                startingAnchor!!.worldPosition=Vector3(0f,0f,0f)
                tempNode = Node()
                tempNode!!.setParent(startingAnchor)
                tempNode!!.localPosition = Vector3(coords!![totalNodes!!].x!!,coords!![totalNodes!!].y!!,coords!![totalNodes!!].z!!)
                //tempNode!!.setLookDirection(Vector3(0f,0f,-1f),Vector3(0f,0f,-1f))
//                    totalNodes= totalNodes!!+1
                tempNode!!.renderable = arrowRenderable
                tempNode!!.name="tempNode"
            }
//            var textView = findViewById<TextView>(R.id.hit)
//            textView.text = "hit= ${hit.trackable.trackingState.name}"
            //Log.e("ARtrial2", "hit= ${hit.trackable.trackingState.name}")
        }
        //Log.e("ARtrial2", "tracking= $tracking")
        if (tracking == TrackingState.TRACKING) {
            var textView = findViewById<TextView>(R.id.tracking)
            textView.text = "TrackingState.TRACKING"
        }
        if (tracking == TrackingState.PAUSED) {
            var textView = findViewById<TextView>(R.id.tracking)
            textView.text = "TrackingState.PAUSED"
        }
        if (tracking == TrackingState.STOPPED) {
            var textView = findViewById<TextView>(R.id.tracking)
            textView.text = "TrackingState.STOPPED"
        }
        return hitCheck
    }

    override fun onClick(v: View?) {
        if(v?.id==R.id.startNavigationButton)
        {
            startButtonPressed=true
        }
        if(v?.id==R.id.stopNavigationButton)
        {
            var intent = Intent(this,MainActivity2::class.java)

            startActivity(intent)
        }

    }

//        fun recycleNodes() {
//
//            for (currentNode in currentNodes) {
////            var frame= arFragment!!.getArSceneView().getArFrame()
////            var camera = frame!!.getCamera()  check this again and work with worldposition instead of local position
//                var scene = arFragment!!.getArSceneView().scene
//                var camera = scene!!.getCamera()
//                var nodeLocation = currentNode.localPosition
//                var userLocation = camera.localPosition
//                //node location-cam location . if resx neg & resy neg remove add new
//                var result = Vector3.subtract(nodeLocation, userLocation)
//                if (result.x < 0 && result.z < 0) {
//                    anchorNode!!.removeChild(currentNode)
//                    addNode()
//                    currentNodes.remove(currentNode)
//                    log("node recycled")
//                }
//            }
//        }

    fun addCoords() {
        /* +x right
           -z front

        static Vector3	right()
        Gets a Vector3 set to (1, 0, 0)
        static Vector3	up()
        Gets a Vector3 set to (0, 1, 0)
        static Vector3	forward()
        Gets a Vector3 set to (0, 0, -1)
        static Vector3 left()
        Gets a Vector3 set to (-1, 0, 0)
         */
        coords = ArrayList()
        //Log.e("addCoords Angle", "${Vector3.angleBetweenVectors(Vector3(0f, 0f, -0.6f),Vector3(0f, 0f, -0.6f))}")
        coords!!.add(CoordsModel(0f, 0f, -0.6f))
        coords!!.add(CoordsModel(0f, 0f, -1.2f))
        coords!!.add(CoordsModel(0f, 0f, -1.8f))
        coords!!.add(CoordsModel(0f, 0f, -2.4f))
        coords!!.add(CoordsModel(0f, 0f, -3.0f))
        coords!!.add(CoordsModel(0.3f, 0f, -3.6f))
//            coords!!.add(CoordsModel(0f, 0f, -4.2f))
//            coords!!.add(CoordsModel(0f, 0f, -4.2f))
    }

//        private fun placeObjects_Old(): Boolean {
//            var arrowNode1 = Node()
//            arrowNode1.setParent(startingAnchor)
//            arrowNode1.localPosition = Vector3(0f, 0f, -0.5f)
//            arrowNode1.renderable = arrowRenderable
//            log("arrowNode1 created at ${arrowNode1.localPosition}")
//
//            var arrowNode2 = Node()
//            arrowNode2.setParent(startingAnchor)
//            arrowNode2.localPosition = Vector3(0f, 0f, -1.0f)
//            arrowNode2.renderable = arrowRenderable
//            log("arrowNode2 created at ${arrowNode2.localPosition}")
//
//            var arrowNode3 = Node()
//            arrowNode3.setParent(startingAnchor)
//            arrowNode3.localPosition = Vector3(0f, 0f, -1.5f)
//            arrowNode3.renderable = arrowRenderable
//            log("arrowNode3 created at ${arrowNode3.localPosition}")
//
//            var arrowNode4 = Node()
//            arrowNode4.setParent(startingAnchor)
//            arrowNode4.localPosition = Vector3(0f, 0f, -2f)
//            arrowNode4.renderable = arrowRenderable
//            log("arrowNode4 created at ${arrowNode4.localPosition}")
//
//            objectsPlaced = true
//            return objectsPlaced!!
//        }

//        fun addNode() {
//            currentNodes.add(Node())
//            currentNodes[currentNodes.size!! - 1].setParent(startingAnchor)
//            currentNodes[currentNodes.size!! - 1].localPosition = Vector3(
//                coords!![totalNodes!! - 1].x!!,
//                coords!![totalNodes!! - 1].x!!,
//                coords!![totalNodes!! - 1].x!!
//            )
//            currentNodes[currentNodes.size!! - 1].renderable = arrowRenderable
//            log("arrowNode${currentNodes.size!! - 1} created at ${currentNodes[currentNodes.size!! - 1].localPosition}")
//            totalNodes = totalNodes!! + 1
//
//        }

    fun log(string: String) {
        Log.e("ARtrial2", string)
    }
}
