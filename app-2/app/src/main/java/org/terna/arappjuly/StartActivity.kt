package org.terna.arappjuly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView

class StartActivity : AppCompatActivity() {
    var button : Button? = null
    var image: ImageView? = null
    var bottom : Animation? = null
    var top : Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        button = findViewById(R.id.splashScreenButton)
        image = findViewById(R.id.splashImageView)

        top = AnimationUtils.loadAnimation(this, R.anim.fromtop)
        bottom = AnimationUtils.loadAnimation(this, R.anim.frombottom)
        button!!.startAnimation(bottom)
        image!!.startAnimation(top)
        button!!.setOnClickListener {
            val intent = Intent(this, UserManagementActivity::class.java)
            startActivity(intent)
            this.finish()
        }


    }
}