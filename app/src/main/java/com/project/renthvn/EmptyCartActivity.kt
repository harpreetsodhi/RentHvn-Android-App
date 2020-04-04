package com.project.renthvn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_empty_cart.*

class EmptyCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_cart)

        gotoHomepage.setOnClickListener{
            val intent = Intent(this, Home_Screen::class.java)
            startActivity(intent)
            finish()
        }
    }
}
