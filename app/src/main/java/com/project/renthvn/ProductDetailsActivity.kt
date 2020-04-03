package com.project.renthvn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_product_details.*
import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_product_details.cart_image
import kotlinx.android.synthetic.main.activity_product_details.nav_view
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


private val DAY_VALUES = listOf(1, 3, 5)
private val SIZE_VALUES = listOf("Small", "Medium", "Large", "XL", "XXL")

class ProductDetailsActivity : AppCompatActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
//        title = ""

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
                    if(dataSnapshot.value == null){
                        val intent = Intent(this@ProductDetailsActivity, EmptyCartActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        val intent = Intent(this@ProductDetailsActivity, Cart::class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
            ref.child(userId).addListenerForSingleValueEvent(postListener)
        }

        val pid = intent.getStringExtra("pid")
        val gender = intent.getStringExtra("gender");

        // adapter for the size spinner
        ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SIZE_VALUES).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sizeSpinner.adapter = this
        }

        // adapter for the period spinner
        ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item, DAY_VALUES).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            daysSpinner.adapter = this
        }

        // connect to FireBase
        val ref = FirebaseDatabase.getInstance().getReference("Products")

        var imageURL:String = ""
        var productName:String = ""
        var productColor:String = ""
        var productDesc:String = ""
        var productPrice:Double = 0.00

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                imageURL = dataSnapshot.child("pimage").value.toString()

                Glide.with(this@ProductDetailsActivity)
                    .load(imageURL)
                    .into(product_details_image)

                productName = dataSnapshot.child("pname").value.toString()
                productColor = dataSnapshot.child("pcolor").value.toString()
                productDesc = dataSnapshot.child("pdesc").value.toString()
                productPrice = dataSnapshot.child("pprice").value.toString().toDouble()

                product_details_name.text = productName
                color_value.text = productColor
                desc_value.text = productDesc
                priceValue.text = "CA$ "+productPrice.toString()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        ref.child(gender!!).child(pid!!).addListenerForSingleValueEvent(postListener)

        // add the item to cart
        addToCartButton.setOnClickListener{

            if (delivery_date_value.text.isEmpty()) {
                Toast.makeText(this, "Please select the Event Date", Toast.LENGTH_SHORT).show()
            }
            else{

            val ref = FirebaseDatabase.getInstance().getReference("Cart")
            val cid = ref.push().key

            val data = CartClass(cid!!, pid, imageURL, productName,
                sizeSpinner.selectedItem.toString(), daysSpinner.selectedItem.toString().toInt(),
                date_picker_label.text.toString(), delivery_date_value.text.toString(),
                pickup_date_value.text.toString(), productColor, productDesc,
                productPrice*daysSpinner.selectedItem.toString().toInt(), gender
                )

            val userId = FirebaseAuth.getInstance().currentUser?.uid!!

            ref.child(userId).child(cid.toString()).setValue(data).addOnCompleteListener{
//                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Cart::class.java)
                startActivity(intent)
                finish()
            }
            }
        }

        // listener for the days spinner
        daysSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                date_picker_label.text = "Select Date ▼"
                delivery_date_value.text = ""
                pickup_date_value.text = ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        date_picker_label.setOnClickListener {
            val c = Calendar.getInstance()
            var mYear = c.get(Calendar.YEAR)
            var mMonth = c.get(Calendar.MONTH)
            var mDay = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(this, R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var date = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                    var delivery_date = date.minusDays(1)
                    var pickup_date = date.plusDays(daysSpinner.selectedItem.toString().toLong())

                    var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

                    var formattedCurrentDate = date.format(formatter)
                    var formattedDeliveryDate = delivery_date.format(formatter)
                    var formattedPickupDate = pickup_date.format(formatter)

                    date_picker_label.text = formattedCurrentDate.toString()
                    delivery_date_value.text = formattedDeliveryDate.toString()
                    pickup_date_value.text = formattedPickupDate.toString()

                }, mYear, mMonth, mDay
            )
            // setting event date minimum to the next day
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() + 86400000
            datePickerDialog.show()
        }
    }
}
