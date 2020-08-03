package org.terna.arappjuly

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class UserManagementActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth
    var email: EditText? = null
    var password:EditText? = null
    var loginBtn: Button? = null
    var signupBtn: Button? = null
    var mIntent : Intent? = null
    public var user = "not signed in"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)
        //bypassAuth()
        //testMode()

        // Initialize Firebase Auth
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser!=null)
        {
            authSuccessful(currentUser.email.toString())
        }
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login)
        signupBtn = findViewById(R.id.signup)
        loginBtn!!.setOnClickListener(this)
        signupBtn!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v?.id==R.id.login)
        {
            auth.signInWithEmailAndPassword(email!!.text.toString(), password!!.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("sign in", "signInWithEmail:success")
                        user = auth.currentUser?.email.toString()
                        authSuccessful(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("sign in", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                        // ...
                    }

                    // ...
                }
        }
        if(v?.id==R.id.signup)
        {
            Toast.makeText(baseContext, "signup button.",
                Toast.LENGTH_SHORT).show()
            auth.createUserWithEmailAndPassword(email!!.text.toString(), password!!.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("sign up", "createUserWithEmail:success")
                        user = auth.currentUser!!.email.toString()
                        authSuccessful(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("sign up", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }

                    // ...
                }
        }
    }

    fun authSuccessful(currentUser : String)
    {
        mIntent = Intent(this,MainActivity::class.java)
        mIntent?.putExtra("current_user", currentUser)
        startActivity(mIntent)
    }
    fun bypassAuth()
    {
        mIntent = Intent(this,MainActivity::class.java)
        //mIntent?.putExtra("current_user", currentUser)
        startActivity(mIntent)
    }
    fun testMode()
    {
        mIntent = Intent(this,FirebaseTestActivity::class.java)
        //mIntent?.putExtra("current_user", currentUser)
        startActivity(mIntent)
    }

}