package org.terna.arappjuly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment

class CreatePath_OLD : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_path)
//        var dbCount = applicationContext.databaseList().size
//        //val dbName = getIntent().getStringExtra("dbName")
//        val dbName = (dbCount+1).toString()
//        var Arfragment : ArFragment? = null
//        var arrowRenderable : ModelRenderable? =null
//        var session : Session? =null
//
//        //venueId = Intent
//
//        //var anchor : Anchor? = null
//        Arfragment = supportFragmentManager.findFragmentById(R.id.CreatePathArfragment) as ArFragment?
//
//        var ses = Session(this)
//        session = Arfragment?.arSceneView?.session
//        val pos = floatArrayOf(0f, 0f, -1f)
//        val rotation = floatArrayOf(0f, 0f, 0f, 1f)
//        val anchor = session?.createAnchor(Pose(pos, rotation))
//        var anchorNode = AnchorNode(anchor)
//        anchorNode.setParent(Arfragment?.arSceneView?.scene)
//        var arrowNode = Node()
//        arrowNode.setParent(anchorNode)
//        var camera = Arfragment!!.arSceneView.scene.camera
//        arrowNode.localPosition= Vector3(0f,0f,0f)
//        var pathToDB = PathToDB()
//        pathToDB.initialize(this,dbName = dbName)
//        Arfragment?.arSceneView?.scene?.addOnUpdateListener { frameTime ->
//
//            pathToDB.add(camera.localPosition.x,camera.localPosition.y,camera.localPosition.z)
//
//
//
//            // node2.localPosition=Vector3(0f,1f,-1f)
//            Log.e("updateListner","pathtodb.add ${camera.localPosition}")
//
//        }
    }
    }

