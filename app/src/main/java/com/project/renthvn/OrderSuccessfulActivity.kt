package com.project.renthvn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_order_successful.*

class OrderSuccessfulActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_successful)

        goToOrders.setOnClickListener{
            val intent = Intent(this, OrderDetailsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
