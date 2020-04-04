package com.project.renthvn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_men_products.*
import kotlinx.android.synthetic.main.activity_men_products.nav_view

class MenProductsActivity : AppCompatActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_men_products)

        //drawer handling
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )

        toggle.syncState()

        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_men -> {
                    val intent = Intent(this, MenProductsActivity::class.java)
                    intent.putExtra("gender", "Men")
                    startActivity(intent)
                    finish()
                }
                R.id.nav_orders -> {
                    val intent = Intent(this, OrderDetailsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_women -> {
                    val intent = Intent(this, MenProductsActivity::class.java)
                    intent.putExtra("gender", "Women")
                    startActivity(intent)
                    finish()
                }
                R.id.nav_update -> {
                    val intent = Intent(this, UpdatePassword::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        cart_image.setOnClickListener {

            val userId = FirebaseAuth.getInstance().currentUser?.uid!!
            val ref = FirebaseDatabase.getInstance().getReference("Cart")

            val postListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d("cart",dataSnapshot.childrenCount.toString())
                    if(dataSnapshot.value == null){
                        val intent = Intent(this@MenProductsActivity, EmptyCartActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        val intent = Intent(this@MenProductsActivity, Cart::class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
            ref.child(userId).addListenerForSingleValueEvent(postListener)
        }

        val gender = getIntent().getStringExtra("gender")

        val items = createItems(gender)

        val gridLayoutManager = GridLayoutManager(this,2)
        rvMenProducts.layoutManager = gridLayoutManager
        rvMenProducts.adapter = ProductsAdapter(this, items)
    }

    private fun createItems(gender: String): List<ProductsClass> {

        val items = mutableListOf<ProductsClass>()
        val ref = FirebaseDatabase.getInstance().getReference("Products")

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (x in dataSnapshot.children) {
                    items.add(
                        ProductsClass(
                            x.key.toString(),
                            gender,
                            x.child("pname").value.toString(),
                            x.child("pprice").value.toString().toDouble(),
                            x.child("pdesc").value.toString(),
                            x.child("pcolor").value.toString(),
                            x.child("pimage").value.toString()

                        )
                    )
                }
                rvMenProducts.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        }
        ref.child(gender).addListenerForSingleValueEvent(postListener)
        return items
    }

}
