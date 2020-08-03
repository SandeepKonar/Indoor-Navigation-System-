package org.terna.arappjuly

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.ux.ArFragment
import kotlin.math.round

class ARcoreMinimalDataProvider (context: Context,arFragment : ArFragment,width : Int,height : Int){
    var textView: TextView? = null
    var session: Session? = null
    var x: Float? = null
    var y: Float? = null
    var z: Float? = null
    var parentContext = context
    var arFragment = arFragment
    var stop : Boolean? = null

    interface arDataListener {
        fun onArData(x: Float, y: Float, z: Float)
    }

    var listner: arDataListener? = null
    fun setArDataListner(listener: arDataListener) {
        this.listner = listener
        arData(arFragment)
    }

    fun arData(arFragment: ArFragment) {
        var ses = Session(parentContext)
        session = arFragment?.arSceneView?.session
        val pos = floatArrayOf(0f, 0f, -0.1f)
        val rotation = floatArrayOf(0f, 0f, 0f, 1f)

        val anchor = session?.createAnchor(Pose(pos, rotation))
        var anchorNode = AnchorNode(anchor)
        anchorNode.setParent(arFragment?.arSceneView?.scene)
        var arrowNode = Node()
        arrowNode.setParent(anchorNode)
        var cam= arFragment!!.arSceneView.scene.camera
        var currNodeLocation = cam.localPosition
        arrowNode.localPosition = Vector3(0f, 0f, 0f)
        stop=false
        var first = true

        arFragment?.arSceneView?.scene?.addOnUpdateListener { frameTime ->
            // textView!!.setText("${camera.localPosition.x},${camera.localPosition.y},${camera.localPosition.z}")
            Log.e("ARcoreDataProvider", "x ${cam.localPosition.x} , z ${cam.localPosition.z}")
            cam = arFragment!!.arSceneView.scene.camera
            var currentCamLocation = cam.localPosition
            if(first)
            {
                listner!!.onArData(
                    round(currentCamLocation.x * 100.0f) / 100f,
                    round(currentCamLocation.y * 100.0f) / 100f,
                    round(currentCamLocation.z * 100.0f) / 100f
                )
                first=false
                currNodeLocation=currentCamLocation
            }

            var distance = distance(currNodeLocation,currentCamLocation)
            if(distance<=0.7&&distance>=0.65)
            {
                if (listner==null)
                {
                    ses!!.close()
                }else {
                    listner!!.onArData(
                        round(currentCamLocation.x * 100.0f) / 100f,
                        round(currentCamLocation.y * 100.0f) / 100f,
                        round(currentCamLocation.z * 100.0f) / 100f

                    )
                    currNodeLocation=currentCamLocation
                }
            }




        }



    }
    fun distance (arrNode: Vector3, camera: Vector3): Float {
        var cam =camera
        var dx: Float = arrNode!!.x - cam.x
        var dy: Float = arrNode!!.y - cam.y
        var dz: Float = arrNode!!.z - cam.z

        val distanceMeters = Math.sqrt(dx * dx + dy * dy + (dz * dz).toDouble()).toFloat()
        return distanceMeters
    }
}