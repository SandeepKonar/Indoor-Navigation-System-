package org.terna.arappjuly

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import kotlin.math.round


class ARcoreDataProvider(context: Context,arFragment : ArFragment,width : Int,height : Int) {
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
        var camera = arFragment!!.arSceneView.scene.camera
        arrowNode.localPosition = Vector3(0f, 0f, 0f)
        stop=false

        arFragment?.arSceneView?.scene?.addOnUpdateListener { frameTime ->
            // textView!!.setText("${camera.localPosition.x},${camera.localPosition.y},${camera.localPosition.z}")
            Log.e("ARcoreDataProvider", "x ${camera.localPosition.x} , z ${camera.localPosition.z}")
                if (listner==null)
                {
                    ses!!.close()
                }else {
                    listner!!.onArData(
                        round(camera?.localPosition!!.x * 100.0f) / 100f,
                        round(camera!!.localPosition.y * 100.0f) / 100f,
                        round(camera!!.localPosition.z * 100.0f) / 100f

                    )
                }
            }



        }

    }
