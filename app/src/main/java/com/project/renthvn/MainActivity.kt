package com.project.renthvn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var logoutBtn: Button
    private lateinit var updatePass: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val user = FirebaseAuth.getInstance().currentUser
//        if (user == null) {
//            Toast.makeText(this, "User not logged in!", Toast.LENGTH_LONG).show()
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//            // User is signed in
//        } else {
//            Toast.makeText(this, "User is logged in!", Toast.LENGTH_LONG).show()
//            // No user is signed in
//        }


//        if (auth.currentUser == null) {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        } else {
//            Toast.makeText(this, "Already logged in!", Toast.LENGTH_LONG).show()
//        }

        val intent = Intent(this, Home_Screen::class.java)
        startActivity(intent)

        setContentView(R.layout.activity_main)

        logoutBtn = findViewById(R.id.main_logoutBtn)
        updatePass = findViewById(R.id.main_updatePassBtn)

        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        updatePass.setOnClickListener {
            val intent = Intent(this, UpdatePassword::class.java)
            startActivity(intent)
        }

        main_goToCart.setOnClickListener{
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

//        main_goToCart.setOnClickListener(
//
////            val intent = Intent(this, Cart::class.java)
//            startActivity(intent)
//        }

    }
}
