package org.terna.arappjuly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.squareup.picasso.Picasso
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata


class PathActivity : AppCompatActivity(), View.OnClickListener {

    var storage =Firebase.storage
    var routeName : String? = null
    var routeId : String? = null
    var imgView : ImageView? = null
    var imgLink = "https://firebasestorage.googleapis.com/v0/b/arapp-ec262.appspot.com/o/paths%2Froom%202.jpg?alt=media&token=47ecbdb5-ebda-4d81-8ae3-1ecfbdc5eefc"
    var startPathArButton : Button? = null
    var viewPathImage : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path)
        storage = Firebase.storage
        val storageRef = storage.reference


        routeName = getIntent().getStringExtra("routeName")
        routeId = routeName!!.replace("\\s".toRegex(), "")
        val pathReference = storageRef.child("paths/${routeName}.jpg")
        pathReference.downloadUrl.addOnSuccessListener { url ->
            imgLink= url.toString()
        }

        imgView=findViewById(R.id.pathImage)
        var txt = findViewById<TextView>(R.id.pathTitle)
        txt.text= routeName
        startPathArButton= findViewById<Button>(R.id.startPathAr)
        startPathArButton!!.setOnClickListener(this)
        viewPathImage = findViewById<Button>(R.id.viewPathImage)
        viewPathImage!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id==R.id.startPathAr)
        {

            var mIntent = Intent(this,ArTrial3::class.java)
            mIntent?.putExtra("routeId", "${routeId}")
            startActivity(mIntent)
        }
        if (v?.id==R.id.viewPathImage)
        {
            Picasso.with(this).load("$imgLink").into(imgView)
            imgView!!.visibility= View.VISIBLE
            startPathArButton!!.visibility= View.GONE
            viewPathImage!!.visibility= View.GONE

        }
    }
}